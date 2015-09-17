/**
 * Marabou - Audio Tagger
 * Copyright (C) 2012 - 2015 Jan-Hendrik Peters
 * https://github.com/hennr/marabou
 * Marabou is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package com.github.marabou.audio.store;

import com.github.marabou.audio.AudioFile;
import com.github.marabou.audio.AudioFileFactory;
import com.github.marabou.audio.AudioFileProperty;
import com.github.marabou.audio.save.SaveService;
import com.github.marabou.ui.events.ErrorEvent;
import com.github.marabou.ui.events.FilesSelectedEvent;
import com.github.marabou.ui.events.OpenFileEvent;
import com.github.marabou.ui.events.SaveSelectedFilesEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.Mp3File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.github.marabou.helper.Constants.IGNORE_THIS_WHEN_SAVING;

public class AudioFileStore {

    Logger log = LoggerFactory.getLogger(AudioFileStore.class);

    private final EventBus bus;
    private final SaveService saveService;
    AudioFileFactory audioFileFactory;
    protected Map<String, AudioFile> audioFiles = new HashMap<>();
    protected Set<AudioFile> currentlySelectedFiles = new HashSet<>();
    protected AudioFile sidePanelEntries = new AudioFile("");

    public AudioFileStore(EventBus bus, AudioFileFactory audioFileFactory, SaveService saveService) {
        this.bus = bus;
        bus.register(this);
        this.audioFileFactory = audioFileFactory;
        this.saveService = saveService;
    }

    protected void addFile(final File inputFile) {

        if (isFileAlreadyInStore(inputFile)) {
            return;
        }

        try {
            AudioFile audioFile = audioFileFactory.createAudioFile(inputFile);
            storeAudioFile(audioFile);
            bus.post(new AudioFileAddedEvent(audioFile));
        } catch (Exception e) {
            log.error("error during file add", e);
            bus.post(new ErrorEvent(e.getMessage()));
        }
    }

    private boolean isFileAlreadyInStore(File inputFile) {
        try {
            if (audioFiles.containsKey(inputFile.getCanonicalPath())) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void storeAudioFile(AudioFile newFile) {
        audioFiles.put(newFile.getFilePath(), newFile);
    }

    public void removeAudioFile(String filePath) {
        audioFiles.remove(filePath);
    }

    public AudioFile getAudioFileByFilePath(String filePath) {
        return audioFiles.get(filePath);
    }

    @Subscribe
    public void onOpenFile(OpenFileEvent openFileEvent) {
        try {
            addFile(openFileEvent.getFile());
        } catch (RuntimeException e) {
            bus.post(new ErrorEvent("Couldn't open file: " + openFileEvent.getFile()));
        }
    }

    @Subscribe
    public void updateSidePanelModel(FilesSelectedEvent event) {
        currentlySelectedFiles = event.selectedAudioFiles;
    }

    @Subscribe
    public void onAudioFilePropertyUpdate(SidePanelModifiedEvent update) {
        AudioFileProperty property = update.getAudioFileProperty();
        String newValue = update.getNewValue();

        switch (property) {
            case Album:
                sidePanelEntries = sidePanelEntries.withAlbum(newValue);
                break;
            case Artist:
                sidePanelEntries = sidePanelEntries.withArtist(newValue);
                break;
            case Comments:
                sidePanelEntries = sidePanelEntries.withComment(newValue);
                break;
            case Composer:
                sidePanelEntries = sidePanelEntries.withComposer(newValue);
                break;
            case Disc_number:
                sidePanelEntries = sidePanelEntries.withDiscNumber(newValue);
                break;
            case Genre:
                sidePanelEntries = sidePanelEntries.withGenre(newValue);
                break;
            case Title:
                sidePanelEntries = sidePanelEntries.withTitle(newValue);
                break;
            case Track:
                sidePanelEntries = sidePanelEntries.withTrack(newValue);
                break;
            case Year:
                sidePanelEntries = sidePanelEntries.withYear(newValue);
                break;
            default:
                throw new RuntimeException("Found an untreated audio file property: " + property + " Please file a bug report at the project website.");
        }
    }

    @Subscribe
    public void onSaveSelectedFiles(SaveSelectedFilesEvent saveSelectedFilesEvent) {
        for (AudioFile audioFile : currentlySelectedFiles) {
            Mp3File mp3File = audioFileFactory.createMp3File(new File(audioFile.getFilePath()));
            mp3File.setId3v2Tag(createTagForAudioFile(audioFile));

            try {
                String newFilePath = saveService.saveMp3File(mp3File);
                removeAudioFile(mp3File.getFilename());
                storeAudioFile(audioFileFactory.createAudioFile(mp3File));

                AudioFileSavedEvent savedEvent = new AudioFileSavedEvent(audioFile.getFilePath(), newFilePath);
                bus.post(savedEvent);
            } catch (RuntimeException e) {
                bus.post(new ErrorEvent("Error saving file: " + mp3File.getFilename()));
            }
        }
    }

    private ID3v24Tag createTagForAudioFile(AudioFile currentAudioFile) {

        ID3v24Tag id3Tag = new ID3v24Tag();
        id3Tag.setAlbum(determineSaveValue(sidePanelEntries.getAlbum(), currentAudioFile.getAlbum()));
        id3Tag.setArtist(determineSaveValue(sidePanelEntries.getArtist(), currentAudioFile.getArtist()));
        id3Tag.setComment(determineSaveValue(sidePanelEntries.getComment(), currentAudioFile.getComment()));
        id3Tag.setComposer(determineSaveValue(sidePanelEntries.getComposer(), currentAudioFile.getComposer()));
        id3Tag.setPartOfSet(determineSaveValue(sidePanelEntries.getDiscNumber(), currentAudioFile.getDiscNumber()));
        id3Tag.setGenreDescription(determineSaveValue(sidePanelEntries.getGenre(), currentAudioFile.getGenre()));
        id3Tag.setTitle(determineSaveValue(sidePanelEntries.getTitle(), currentAudioFile.getTitle()));
        id3Tag.setTrack(determineSaveValue(sidePanelEntries.getTrack(), currentAudioFile.getTrack()));
        id3Tag.setYear(determineSaveValue(sidePanelEntries.getYear(), currentAudioFile.getYear()));
        return id3Tag;
    }

    private String determineSaveValue(String sidePanelValue, String oldValue) {
        return sidePanelValue.equals(IGNORE_THIS_WHEN_SAVING) ? oldValue : sidePanelValue;
    }

    public Set<AudioFile> getSelectedAudioFiles() {
        return currentlySelectedFiles;
    }

    public List<AudioFile> getStoredAudioFiles() {
        return new LinkedList<>(audioFiles.values());
    }
}
