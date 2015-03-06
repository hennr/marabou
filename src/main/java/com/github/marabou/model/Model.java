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
package com.github.marabou.model;

import com.github.marabou.events.NewFileEvent;
import com.github.marabou.audio.Genres;
import com.github.marabou.helper.UnknownGenreException;
import com.mpatric.mp3agic.*;
import com.google.common.eventbus.EventBus;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Model {

    private final EventBus bus;
    private Map<String, AudioFile> files = new HashMap<>();

    public Model(EventBus bus) {
        this.bus = bus;
    }

    public void addFile(final File inputFile) {

        Mp3File mp3File;
        try {
            mp3File = getMp3File(inputFile);
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            throw new RuntimeException("Couldn't open given file");
        }

        String fullFilePath;
        try {
            fullFilePath = inputFile.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException("Could not add given file.");
        }

        AudioFile audioFile = new AudioFile(fullFilePath).withFilePath(fullFilePath);

        audioFile = getId3VersionAgnosticValues(audioFile, mp3File);

        if (mp3File.hasId3v2Tag()) {
            audioFile = getId3v2Tags(mp3File, audioFile);
        } else if (mp3File.hasId3v1Tag()) {
            audioFile = getId3v1Tags(mp3File, audioFile);
        }

        storeFile(audioFile);
        bus.post(new NewFileEvent(audioFile));
    }

    protected Mp3File getMp3File(File audioFile) throws IOException, UnsupportedTagException, InvalidDataException {
        return new Mp3File(audioFile.getCanonicalPath());
    }

    protected AudioFile getId3VersionAgnosticValues(AudioFile audioFile, Mp3File mp3File) {

        String duration = calculateTrackLength(mp3File.getLengthInSeconds());
        String bitRate = Integer.toString(mp3File.getBitrate());
        String sampleRate = Integer.toString(mp3File.getSampleRate());
        String channels = "";
        if (mp3File.getChannelMode() != null) {
            channels = mp3File.getChannelMode();
        }

        audioFile
                .withDuration(duration)
                .withBitRate(bitRate)
                .withSamplerate(sampleRate)
                .withChannels(channels)
                .withEncoding("mp3");

        return audioFile;
    }

    public AudioFile getId3v1Tags(Mp3File mp3File, AudioFile audioFile) {

        ID3v1 id31Tag = mp3File.getId3v1Tag();

        if (id31Tag.getArtist() != null ) {
            audioFile.withArtist(id31Tag.getArtist());
        }

        if (id31Tag.getTitle() != null) {
            audioFile.withTitle(id31Tag.getTitle());
        }

        if (id31Tag.getAlbum() != null) {
            audioFile.withAlbum(id31Tag.getAlbum());
        }

        if (id31Tag.getTrack() != null) {
            audioFile.withTrack(id31Tag.getTrack());
        }

        if (id31Tag.getYear() != null) {
            audioFile.withYear(id31Tag.getYear());
        }

        int genreId = id31Tag.getGenre();
        try {
            String genre = Genres.getGenreById(genreId);
            audioFile.withGenre(genre);
        } catch (UnknownGenreException e) {
            // leave empty if id is unknown
        }

        if (id31Tag.getComment() != null) {
            audioFile.withComment(id31Tag.getComment());
        }

        return audioFile;
    }

    private AudioFile getId3v2Tags(Mp3File mp3File, AudioFile audioFile) {

        ID3v2 id32Tag = mp3File.getId3v2Tag();

        if (id32Tag.getArtist() != null) {
            audioFile.withArtist(id32Tag.getArtist());
        }

        if (id32Tag.getTitle() != null) {
            audioFile.withTitle(id32Tag.getTitle());
        }

        if (id32Tag.getAlbum() != null) {
            audioFile.withAlbum(id32Tag.getAlbum());
        }

        if (id32Tag.getTrack() != null) {
            audioFile.withTrack(id32Tag.getTrack());
        }

        if (id32Tag.getYear() != null) {
            audioFile.withYear(id32Tag.getYear());
        }

        int genreId = id32Tag.getGenre();
        try {
            String genre = Genres.getGenreById(genreId);
            audioFile.withGenre(genre);
        } catch (UnknownGenreException e) {
            // leave empty if id is unknown
        }

        if (id32Tag.getComment() != null) {
            audioFile.withComment(id32Tag.getComment());
        }

        if (id32Tag.getPartOfSet() != null) {
            audioFile.withDiscNumber(id32Tag.getPartOfSet());
        }

        if (id32Tag.getComposer() != null) {
            audioFile.withComposer(id32Tag.getComposer());
        }

        return audioFile;
    }

    private void storeFile(AudioFile newFile) {
        files.put(newFile.getId(), newFile);
    }

    /**
     * converts seconds to the following format: min:secs
     * @return min:secs as a string
     */
    protected String calculateTrackLength(long secs) {

        if (secs < 0) {
            return "0:00";
        }

        long minutes = secs / 60;

        if (minutes == 0) {
            if (secs < 10) {
                return "0:0" + secs;
            } else {
                return "0:" + secs;
            }
        } else {
            String seconds = String.valueOf(secs - (60 * minutes));
            if (seconds.length() < 2) {
                seconds = "0" + seconds;
            }
            return minutes + ":" + seconds;
        }
    }

    public AudioFile getAudioFileByFilePath(String filePath) {
        return files.get(filePath);
    }
}
