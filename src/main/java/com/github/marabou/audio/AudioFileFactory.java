/**
 * Marabou - Audio Tagger
 * Copyright (C) 2012 - 2016 Jan-Hendrik Peters
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
package com.github.marabou.audio;

import com.github.marabou.ui.events.ErrorEvent;
import com.google.common.eventbus.EventBus;
import com.mpatric.mp3agic.*;

import java.io.File;
import java.io.IOException;

public class AudioFileFactory {

    private final EventBus bus;

    public AudioFileFactory(EventBus bus) {
        this.bus = bus;
    }

    public AudioFile createAudioFile(File inputFile) {

        Mp3File mp3File = createMp3File(inputFile);
        String canonicalFilePath = getCanonicalFilePath(inputFile);

        AudioFile audioFile = new AudioFile(canonicalFilePath);

        audioFile = withId3VersionAgnosticValues(mp3File, audioFile);

        if (mp3File.hasId3v2Tag()) {
            audioFile = withPropertiesAvailableInID3V1(mp3File.getId3v2Tag(), audioFile);
            audioFile = withPropertiesAvailableInID3V2(mp3File.getId3v2Tag(), audioFile);
            return audioFile;

        } else if (mp3File.hasId3v1Tag()) {
            return withPropertiesAvailableInID3V1(mp3File.getId3v1Tag(), audioFile);
        }
        return audioFile;
    }

    public AudioFile createAudioFile(Mp3File mp3File) {

        AudioFile audioFile = new AudioFile(mp3File.getFilename());

        audioFile = withId3VersionAgnosticValues(mp3File, audioFile);

        if (mp3File.hasId3v2Tag()) {
            audioFile = withPropertiesAvailableInID3V1(mp3File.getId3v2Tag(), audioFile);
            audioFile = withPropertiesAvailableInID3V2(mp3File.getId3v2Tag(), audioFile);
            return audioFile;
        }

        if (mp3File.hasId3v1Tag()) {
            return withPropertiesAvailableInID3V1(mp3File.getId3v1Tag(), audioFile);
        }

        return audioFile;
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

    private AudioFile withId3VersionAgnosticValues(Mp3File mp3File, AudioFile audioFile) {

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


    private AudioFile withPropertiesAvailableInID3V1(ID3v1 id31TagOrBetter, AudioFile audioFile) {

        if (id31TagOrBetter.getArtist() != null) {
            audioFile.withArtist(id31TagOrBetter.getArtist());
        }

        if (id31TagOrBetter.getTitle() != null) {
            audioFile.withTitle(id31TagOrBetter.getTitle());
        }

        if (id31TagOrBetter.getAlbum() != null) {
            audioFile.withAlbum(id31TagOrBetter.getAlbum());
        }

        if (id31TagOrBetter.getTrack() != null) {
            audioFile.withTrack(id31TagOrBetter.getTrack());
        }

        if (id31TagOrBetter.getYear() != null) {
            audioFile.withYear(id31TagOrBetter.getYear());
        }

        if (id31TagOrBetter.getGenreDescription() != null) {
            audioFile.withGenre(id31TagOrBetter.getGenreDescription());
        } else {
            getGenreDescriptionByGenreId(id31TagOrBetter);
        }

        if (id31TagOrBetter.getComment() != null) {
            audioFile.withComment(id31TagOrBetter.getComment());
        }

        return audioFile;
    }

    private String getGenreDescriptionByGenreId(ID3v1 id31TagOrBetter) {
        int genreId = id31TagOrBetter.getGenre();
        try {
            return ID3v1Genres.GENRES[genreId];
        } catch (ArrayIndexOutOfBoundsException e) {
            return "";
        }
    }

    private AudioFile withPropertiesAvailableInID3V2(ID3v2 id32Tag, AudioFile audioFile) {

        if (id32Tag.getPartOfSet() != null) {
            audioFile.withDiscNumber(id32Tag.getPartOfSet());
        }

        if (id32Tag.getComposer() != null) {
            audioFile.withComposer(id32Tag.getComposer());
        }

        return audioFile;
    }

    private String getCanonicalFilePath(File inputFile) {

        String fullFilePath;
        try {
            fullFilePath = inputFile.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException("Could not add given file.");
        }
        return fullFilePath;
    }

    public Mp3File createMp3File(File audioFile) {
        try {
            return new Mp3File(audioFile.getCanonicalPath());
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            String errorMessage = "Couldn't open given file: " + audioFile.getAbsolutePath();
            bus.post(new ErrorEvent(errorMessage));
            throw new RuntimeException(errorMessage);
        }
    }
}
