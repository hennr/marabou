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
import com.github.marabou.ui.events.ErrorEvent;
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
    private Map<String, AudioFile> audioFiles = new HashMap<>();
    private Set<AudioFile> filesWithPendingChanges = new HashSet<>();

    public AudioFileStore(EventBus bus, AudioFileFactory audioFileFactory) {
        this.bus = bus;
        bus.register(this);
        this.audioFileFactory = audioFileFactory;
    }

    public void addFile(final File inputFile) {
        try {
          AudioFile audioFile = audioFileFactory.createAudioFile(inputFile);
          storeFile(audioFile);
          bus.post(new AudioFileAddedEvent(audioFile));
        } catch (Exception e) {
          log.error("error during file add", e);
          bus.post(new ErrorEvent(e.getMessage()));
        }
    }

    private void storeFile(AudioFile newFile) {
        audioFiles.put(newFile.getFilePath(), newFile);
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
    public void onPropertyUpdate(AudioFilePropertyChangeEvent update) {
      filesWithPendingChanges.addAll(
        updateAudioFiles(update.getAffectedFiles(), update.getAudioFileProperty(), update.getNewValue())
      );
      bus.post(new AudioFilesUpdated(update.getAffectedFiles()));
    }

    @Subscribe
    public void onSaveSelectedFiles(SaveSelectedFilesEvent saveSelectedFilesEvent) {
      savePendingChanges();
      bus.post(new AudioFilesSaved());
    }

    private Set<AudioFile> updateAudioFiles(Set<AudioFile> affectedFiles, AudioFileProperty audioFileProperty, String newValue) {
      for (AudioFile audioFile : affectedFiles) {
        updateAudioFile(audioFile, audioFileProperty, newValue);
      }

      return affectedFiles;
    }

    private void savePendingChanges() {
      for (AudioFile audioFileWithChanges : filesWithPendingChanges) {
        Mp3File mp3File = audioFileFactory.createMp3File(new File(audioFileWithChanges.getFilePath()));
        mp3File.setId3v2Tag(createTagForAudioFile(audioFileWithChanges));
        saveMp3File(mp3File);
      }
    }

    private void saveMp3File(Mp3File mp3File) {
      try {
        File tempFile = getTempFile();
        mp3File.save(tempFile.getAbsolutePath());
        Files.copy(tempFile, new File(mp3File.getFilename()));
        tempFile.delete();
      } catch (IOException | NotSupportedException saveException) {
        throw new RuntimeException(saveException);
      }
    }

    private File getTempFile() {
      return new File(System.getProperty("java.io.tmpdir") + File.separatorChar + UUID.randomUUID().toString());
    }

    private AudioFile updateAudioFile(AudioFile audioFile, AudioFileProperty property, String newValue) {
      switch (property) {
        case Album:
          return audioFile.withAlbum(newValue);
        case Artist:
          return audioFile.withArtist(newValue);
        case Comments:
          return audioFile.withComment(newValue);
        case Composer:
          return audioFile.withComposer(newValue);
        case Disc_number:
          return audioFile.withDiscNumber(newValue);
        case Genre:
          return audioFile.withGenre(newValue);
        case Title:
          return audioFile.withTitle(newValue);
        case Track:
          return audioFile.withTrack(newValue);
        case Year:
          return audioFile.withYear(newValue);
        default:
          return audioFile;
      }
    }

    private ID3v2 createTagForAudioFile(AudioFile audioFile) {
      ID3v24Tag id3Tag = new ID3v24Tag();
      id3Tag.setAlbum(audioFile.getAlbum());
      id3Tag.setArtist(audioFile.getArtist());
      id3Tag.setComment(audioFile.getComment());
      id3Tag.setComposer(audioFile.getComposer());
      id3Tag.setPartOfSet(audioFile.getDiscNumber());
      id3Tag.setGenre(Genres.getIndexForName(audioFile.getGenre()));
      id3Tag.setTitle(audioFile.getTitle());
      id3Tag.setTrack(audioFile.getTrack());
      id3Tag.setYear(audioFile.getYear());
      return id3Tag;
    }

}
