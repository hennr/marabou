/**
 * Marabou - Audio Tagger
 * <p>
 * Copyright (C) 2012 - 2015 Jan-Hendrik Peters
 * <p>
 * https://github.com/hennr/marabou
 * <p>
 * Marabou is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package com.github.marabou.audio.store;

import com.github.marabou.audio.AudioFile;
import com.github.marabou.audio.AudioFileFactory;
import com.github.marabou.audio.Genres;
import com.github.marabou.audio.UnknownGenreException;
import com.github.marabou.audio.save.SaveService;
import com.github.marabou.ui.events.FilesSelectedEvent;
import com.github.marabou.ui.events.SaveSelectedFilesEvent;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.Mp3File;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.util.Set;

import static com.github.marabou.helper.Constants.IGNORE_THIS_WHEN_SAVING;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static testdata.builder.TestAudioFileBuilder.aValidCompleteAudioFile;
import static testdata.builder.TestAudioFileBuilder.anotherValidCompleteAudioFile;

public class AudioFileStoreTest {

    private String storedAudioFileFilePath = "/path";

    @Test
    public void getsTheSameAudioFileByFilePathAfterStoring() throws Exception {
        // given
        AudioFileFactory fileFactoryMock = mock(AudioFileFactory.class);
        AudioFileStore audioFileStore = new AudioFileStore(new EventBus(), fileFactoryMock, new SaveService());

        File dummyFile = mock(File.class);
        AudioFile audioFile = new AudioFile(storedAudioFileFilePath);
        when(fileFactoryMock.createAudioFile(any(File.class))).thenReturn(audioFile);

        // when
        audioFileStore.addFile(dummyFile);
        AudioFile result = audioFileStore.getAudioFileByFilePath(storedAudioFileFilePath);

        // then
        assertEquals(audioFile, result);
    }

    @Test
    public void getStoredFilesReturnsCorrectResults() {
        // given
        AudioFileFactory fileFactoryMock = mock(AudioFileFactory.class);
        AudioFileStore audioFileStore = new AudioFileStore(new EventBus(), fileFactoryMock, new SaveService());

        File dummyFile = mock(File.class);
        AudioFile audioFile = new AudioFile(storedAudioFileFilePath);
        when(fileFactoryMock.createAudioFile(any(File.class))).thenReturn(audioFile);

        // when
        audioFileStore.addFile(dummyFile);

        // then
        assertEquals(1, audioFileStore.getStoredAudioFiles().size());
        assertEquals(storedAudioFileFilePath, audioFileStore.getStoredAudioFiles().get(0).getFilePath());
    }

    @Test
    public void canRemoveAudioFile() throws Exception {
        // given
        AudioFileFactory fileFactoryMock = mock(AudioFileFactory.class);
        AudioFileStore audioFileStore = new AudioFileStore(new EventBus(), fileFactoryMock, new SaveService());

        File dummyFile = mock(File.class);
        AudioFile audioFile = new AudioFile(storedAudioFileFilePath);
        when(fileFactoryMock.createAudioFile(any(File.class))).thenReturn(audioFile);

        // when
        audioFileStore.addFile(dummyFile);
        audioFileStore.removeAudioFile(storedAudioFileFilePath);

        // then
        assertNull(audioFileStore.getAudioFileByFilePath(storedAudioFileFilePath));
        assertEquals(0, audioFileStore.audioFiles.size());
    }

    @Test
    public void doesNotStoreTheSameFileMoreThanOnce() {
        // given
        AudioFileFactory fileFactoryMock = mock(AudioFileFactory.class);
        AudioFileStore audioFileStore = new AudioFileStore(new EventBus(), fileFactoryMock, new SaveService());

        File dummyFile = mock(File.class);
        AudioFile audioFile = new AudioFile("/path");
        when(fileFactoryMock.createAudioFile(any(File.class))).thenReturn(audioFile);

        // when
        audioFileStore.addFile(dummyFile);
        audioFileStore.addFile(dummyFile);

        // then
        assertEquals(1, audioFileStore.audioFiles.size());
    }

    @Test
    public void remembersSelectedFilesOnFilesSelectedEvent() {
        // given
        EventBus bus = new EventBus();
        AudioFileFactory fileFactoryMock = mock(AudioFileFactory.class);
        AudioFileStore audioFileStore = new AudioFileStore(bus, fileFactoryMock, new SaveService());

        AudioFile audioFile = new AudioFile("/path");
        when(fileFactoryMock.createAudioFile(any(File.class))).thenReturn(audioFile);

        Set<AudioFile> selectedFiles = Sets.newHashSet(audioFile);

        // when
        bus.post(new FilesSelectedEvent(selectedFiles));

        // then
        Set<AudioFile> result = audioFileStore.getSelectedAudioFiles();
        assertEquals(1, result.size());
        assertTrue(result.contains(audioFile));
    }

    @Test
    public void forgetsAboutPreviouslySelectedFilesOnNewFilesSelectedEvent() {
        // given
        EventBus bus = new EventBus();
        AudioFileFactory fileFactoryMock = mock(AudioFileFactory.class);
        AudioFileStore audioFileStore = new AudioFileStore(bus, fileFactoryMock, new SaveService());

        AudioFile audioFileOne = new AudioFile("/one");
        when(fileFactoryMock.createAudioFile(any(File.class))).thenReturn(audioFileOne);

        Set<AudioFile> selectedFiles = Sets.newHashSet(audioFileOne);

        // when
        bus.post(new FilesSelectedEvent(selectedFiles));

        // next event
        AudioFile audioFileTwo = new AudioFile("/two");
        selectedFiles.clear();
        selectedFiles.add(audioFileTwo);
        bus.post(new FilesSelectedEvent(selectedFiles));

        // then
        Set<AudioFile> result = audioFileStore.getSelectedAudioFiles();
        assertTrue(result.contains(audioFileTwo));
        assertFalse(result.contains(audioFileOne));
    }

    @Test
    public void aSaveSelectedFilesEventProvokesNewAudioFileSavedEvent() throws Exception {
        // given
        AudioFile audioFileMock = mock(AudioFile.class);

        AudioFileFactory audioFileFactoryMock = mock(AudioFileFactory.class);
        when(audioFileFactoryMock.createAudioFile(any(Mp3File.class))).thenReturn(audioFileMock);
        when(audioFileFactoryMock.createMp3File(any())).thenReturn(mock(Mp3File.class));

        EventBus eventBusMock = mock(EventBus.class);

        SaveService saveServiceMock = mock(SaveService.class);
        when(saveServiceMock.saveMp3File(any(Mp3File.class))).thenReturn("/newPath");

        AudioFileStore audioFileStore = new AudioFileStore(eventBusMock, audioFileFactoryMock, saveServiceMock);

        AudioFile audioFile = new AudioFile("/path").withAlbum("album");

        audioFileStore.currentlySelectedFiles = Sets.newHashSet(audioFile);

        // when
        audioFileStore.onSaveSelectedFiles(new SaveSelectedFilesEvent());

        // then
        ArgumentCaptor<AudioFileSavedEvent> argument = ArgumentCaptor.forClass(AudioFileSavedEvent.class);
        verify(eventBusMock).post(argument.capture());
        assertEquals("/path", argument.getValue().oldFilePath);
        assertEquals("/newPath", argument.getValue().newFilePath);
    }

    @Test
    public void savesSidePanelEntriesToSelectedFiles() throws UnknownGenreException {
        // given
        Mp3File mp3FileMock = mock(Mp3File.class);

        AudioFileFactory audioFileFactoryMock = mock(AudioFileFactory.class);
        when(audioFileFactoryMock.createAudioFile(any(Mp3File.class))).thenReturn(
                new AudioFile("will be used to hold new entries"));
        when(audioFileFactoryMock.createMp3File(any())).thenReturn(mp3FileMock);

        EventBus bus = new EventBus();

        SaveService saveServiceMock = mock(SaveService.class);

        AudioFileStore audioFileStore = new AudioFileStore(bus, audioFileFactoryMock, saveServiceMock);

        AudioFile sidePanelEntriesAudioFile = aValidCompleteAudioFile();

        audioFileStore.sidePanelEntries = sidePanelEntriesAudioFile;

        AudioFile audioFile = new AudioFile("we need at least one selected file");
        audioFileStore.currentlySelectedFiles = Sets.newHashSet(audioFile);

        // when
        bus.post(new SaveSelectedFilesEvent());

        // then
        ArgumentCaptor<ID3v24Tag> tagToBeSavedCaptor = ArgumentCaptor.forClass(ID3v24Tag.class);
        verify(mp3FileMock).setId3v2Tag(tagToBeSavedCaptor.capture());
        ID3v24Tag tag = tagToBeSavedCaptor.getValue();

        assertEquals(sidePanelEntriesAudioFile.getArtist(), tag.getArtist());
        assertEquals(sidePanelEntriesAudioFile.getAlbum(), tag.getAlbum());
        assertEquals(sidePanelEntriesAudioFile.getComment(), tag.getComment());
        assertEquals(sidePanelEntriesAudioFile.getComposer(), tag.getComposer());
        assertEquals(sidePanelEntriesAudioFile.getDiscNumber(), tag.getPartOfSet());
        assertEquals(sidePanelEntriesAudioFile.getGenre(), Genres.getGenreById(tag.getGenre()));
        assertEquals(sidePanelEntriesAudioFile.getTitle(), tag.getTitle());
        assertEquals(sidePanelEntriesAudioFile.getTrack(), tag.getTrack());
        assertEquals(sidePanelEntriesAudioFile.getYear(), tag.getYear());
    }

    @Test
    public void doesNotOverwriteValuesIfFieldIsSetToIgnore() throws UnknownGenreException {
        // given
        Mp3File mp3FileMock = mock(Mp3File.class);

        AudioFileFactory audioFileFactoryMock = mock(AudioFileFactory.class);
        when(audioFileFactoryMock.createAudioFile(any(Mp3File.class))).thenReturn(
                new AudioFile("will be used to hold new entries"));
        when(audioFileFactoryMock.createMp3File(any())).thenReturn(mp3FileMock);

        EventBus bus = new EventBus();

        SaveService saveServiceMock = mock(SaveService.class);

        AudioFileStore audioFileStore = new AudioFileStore(bus, audioFileFactoryMock, saveServiceMock);

        AudioFile sidePanelEntriesAudioFile = aValidCompleteAudioFile()
                .withTitle(IGNORE_THIS_WHEN_SAVING)
                .withTrack(IGNORE_THIS_WHEN_SAVING)
                .withDiscNumber(IGNORE_THIS_WHEN_SAVING);

        audioFileStore.sidePanelEntries = sidePanelEntriesAudioFile;

        AudioFile selectedAudioFile = anotherValidCompleteAudioFile();

        audioFileStore.currentlySelectedFiles = Sets.newHashSet(selectedAudioFile);

        // when
        bus.post(new SaveSelectedFilesEvent());

        // then
        ArgumentCaptor<ID3v24Tag> tagToBeSavedCaptor = ArgumentCaptor.forClass(ID3v24Tag.class);
        verify(mp3FileMock).setId3v2Tag(tagToBeSavedCaptor.capture());
        ID3v24Tag tag = tagToBeSavedCaptor.getValue();

        // these should contain the side panel values
        assertEquals(sidePanelEntriesAudioFile.getArtist(), tag.getArtist());
        assertEquals(sidePanelEntriesAudioFile.getAlbum(), tag.getAlbum());
        assertEquals(sidePanelEntriesAudioFile.getComment(), tag.getComment());
        assertEquals(sidePanelEntriesAudioFile.getComposer(), tag.getComposer());
        assertEquals(sidePanelEntriesAudioFile.getGenre(), tag.getGenreDescription());
        assertEquals(sidePanelEntriesAudioFile.getYear(), tag.getYear());

        // these should contain the old values
        assertEquals(selectedAudioFile.getTitle(), tag.getTitle());
        assertEquals(selectedAudioFile.getTrack(), tag.getTrack());
        assertEquals(selectedAudioFile.getDiscNumber(), tag.getPartOfSet());
    }
}
