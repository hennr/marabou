/**
 * Marabou - Audio Tagger
 *
 * Copyright (C) 2012 - 2015 Jan-Hendrik Peters
 *
 * https://github.com/hennr/marabou
 *
 * Marabou is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package com.github.marabou.audio.store;

import com.github.marabou.audio.AudioFile;
import com.github.marabou.audio.AudioFileFactory;
import com.github.marabou.audio.AudioFileProperty;
import com.github.marabou.audio.Genres;
import com.github.marabou.helper.UnknownGenreException;
import com.github.marabou.ui.events.ErrorEvent;
import com.github.marabou.ui.events.FilesSelectedEvent;
import com.github.marabou.ui.events.OpenFileEvent;
import com.github.marabou.ui.events.SaveSelectedFilesEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.common.io.Files;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.ID3v24Tag;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class AudioFileStore {

    Logger log = LoggerFactory.getLogger(AudioFileStore.class);

    private final EventBus bus;
    AudioFileFactory audioFileFactory;
    protected Map<String, AudioFile> audioFiles = new HashMap<>();
    protected Set<AudioFile> currentlySelectedFiles = new HashSet<>();
    private AudioFile audioFile = new AudioFile("");

    public AudioFileStore(EventBus bus, AudioFileFactory audioFileFactory) {
        this.bus = bus;
        bus.register(this);
        this.audioFileFactory = audioFileFactory;
    }

    protected void addFile(final File inputFile) {
        try {
          AudioFile audioFile = audioFileFactory.createAudioFile(inputFile);
          storeAudioFile(audioFile);
          bus.post(new AudioFileAddedEvent(audioFile));
        } catch (Exception e) {
          log.error("error during file add", e);
          bus.post(new ErrorEvent(e.getMessage()));
        }
    }

    private void storeAudioFile(AudioFile newFile) {
        audioFiles.put(newFile.getFilePath(), newFile);
    }

    protected void removeAudioFile(String filePath) {
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
    public void onAudioFilePropertyUpdate(AudioFilePropertyChangeEvent update) {
        AudioFileProperty property = update.getAudioFileProperty();
        String newValue = update.getNewValue();

        switch (property) {
            case Album:
                audioFile =  audioFile.withAlbum(newValue);
                break;
            case Artist:
                audioFile =  audioFile.withArtist(newValue);
                break;
            case Comments:
                audioFile =  audioFile.withComment(newValue);
                break;
            case Composer:
                audioFile =  audioFile.withComposer(newValue);
                break;
            case Disc_number:
                audioFile =  audioFile.withDiscNumber(newValue);
                break;
            case Genre:
                audioFile =  audioFile.withGenre(newValue);
                break;
            case Title:
                audioFile =  audioFile.withTitle(newValue);
                break;
            case Track:
                audioFile =  audioFile.withTrack(newValue);
                break;
            case Year:
                audioFile =  audioFile.withYear(newValue);
                break;
            default:
                throw new RuntimeException("Found an untreated audio file property: " + property +  " Please file a bug report at the project website.");
        }
    }

    @Subscribe
    public void onSaveSelectedFiles(SaveSelectedFilesEvent saveSelectedFilesEvent) {
        for (AudioFile audioFile : currentlySelectedFiles) {
            Mp3File mp3File = audioFileFactory.createMp3File(new File(audioFile.getFilePath()));
            mp3File.setId3v2Tag(createTagForAudioFile());

            try {
                String newFilePath = saveMp3File(mp3File);
                removeAudioFile(mp3File.getFilename());
                storeAudioFile(audioFileFactory.createAudioFile(mp3File));

                AudioFileSavedEvent savedEvent = new AudioFileSavedEvent(audioFile.getFilePath(), newFilePath);
                bus.post(savedEvent);
            } catch (RuntimeException e) {
                bus.post(new ErrorEvent("Error saving file: " + mp3File.getFilename()));
            }
        }
    }

    protected String saveMp3File(Mp3File mp3File) {
      try {
          File tempFile = getTempFile();
        mp3File.save(tempFile.getAbsolutePath());

        Files.copy(tempFile, new File(mp3File.getFilename()));
        tempFile.delete();
      } catch (IOException | NotSupportedException saveException) {
        throw new RuntimeException(saveException);
      }
        return mp3File.getFilename();
    }

    private File getTempFile() {
      return new File(System.getProperty("java.io.tmpdir") + File.separatorChar + UUID.randomUUID().toString());
    }

    private ID3v2 createTagForAudioFile() {
      ID3v24Tag id3Tag = new ID3v24Tag();
      id3Tag.setAlbum(audioFile.getAlbum());
      id3Tag.setArtist(audioFile.getArtist());
      id3Tag.setComment(audioFile.getComment());
      id3Tag.setComposer(audioFile.getComposer());
      id3Tag.setPartOfSet(audioFile.getDiscNumber());
      try {
          id3Tag.setGenre(Genres.getIndexForName(audioFile.getGenre()));
      } catch (UnknownGenreException e) {
          id3Tag.setGenreDescription(audioFile.getGenre());
      }
        id3Tag.setTitle(audioFile.getTitle());
      id3Tag.setTrack(audioFile.getTrack());
      id3Tag.setYear(audioFile.getYear());
      return id3Tag;
    }
}
