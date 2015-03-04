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

    public void addFile(final File audioFile) {

        ID3v1 id31Tag = null;
        ID3v2 id32Tag = null;
        boolean id31 = false;
        boolean id32 = false;

        Mp3File mp3File;
        try {
            mp3File = getMp3File(audioFile);
        } catch (IOException | UnsupportedTagException | InvalidDataException e) {
            throw new RuntimeException("Couldn't open given file");
        }

        if (mp3File.hasId3v1Tag()) {
            id31 = true;
            id31Tag = mp3File.getId3v1Tag();
        }
        if (mp3File.hasId3v2Tag()) {
            id32 = true;
            id32Tag = mp3File.getId3v2Tag();
        }

        String artist = "";
        String title = "";
        String album = "";
        String trackNumber = "";
        String year = "";
        String genre = "";
        String comment = "";
        String discNo = "";
        String composer = "";

        if (id31) {

            if (id31Tag.getArtist() != null ) {
                artist = id31Tag.getArtist();
            }

            if (id31Tag.getTitle() != null) {
                title = id31Tag.getTitle();
            }

            if (id31Tag.getAlbum() != null) {
                album = id31Tag.getAlbum();
            }

            if (id31Tag.getTrack() != null) {
                trackNumber = id31Tag.getTrack();
            }

            if (id31Tag.getYear() != null) {
                year = id31Tag.getYear();
            }

            int genreId = id31Tag.getGenre();
            try {
                genre = Genres.getGenreById(genreId);
            } catch (UnknownGenreException e) {
                genre = "";
            }

            if (id31Tag.getComment() != null) {
                comment = id31Tag.getComment();
            }

            discNo = "Not supported in idv31";

            composer = "Not supported in idv31";
        }

        if (id32) {

            if(id32Tag.getArtist() != null) {
                artist = id32Tag.getArtist();
            }

            if (id32Tag.getTitle() != null) {
                title = id32Tag.getTitle();
            }

            if (id32Tag.getAlbum() != null) {
                album = id32Tag.getAlbum();
            }

            if (id32Tag.getTrack() != null) {
                trackNumber = id32Tag.getTrack();
            }

            if (id32Tag.getYear() != null) {
                year = id32Tag.getYear();
            }

            int genreId = id32Tag.getGenre();
            try {
                genre = Genres.getGenreById(genreId);
            } catch (UnknownGenreException e) {
                genre = "";
            }

            if (id32Tag.getComment() != null) {
                comment = id32Tag.getComment();
            }

            if (id32Tag.getPartOfSet() != null) {
                discNo = id32Tag.getPartOfSet();
            }

            if (id32Tag.getComposer() != null) {
                composer = id32Tag.getComposer();
            }
        }

        // values not tag version specific

        String duration = calculateTrackLength(mp3File.getLengthInSeconds());

        String bitRate = Integer.toString(mp3File.getBitrate());

        String sampleRate = Integer.toString(mp3File.getSampleRate());

        String channels = mp3File.getChannelMode();

        String encoding = "mp3";

        String fullFilePath;
        try {
            fullFilePath = audioFile.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException("Could not add given file.");
        }

        AudioFile newFile = new AudioFile(fullFilePath)
                .withArtist(artist)
                .withTitle(title)
                .withAlbum(album)
                .withTrack(trackNumber)
                .withYear(year)
                .withGenre(genre)
                .withComments(comment)
                .withDiscNumber(discNo)
                .withComposer(composer)
                .withBitRate(bitRate)
                .withSamplerate(sampleRate)
                .withChannels(channels)
                .withEncoding(encoding)
                .withFilePath(fullFilePath)
                .withDuration(duration);
        
        storeFile(newFile);
        bus.post(new NewFileEvent(newFile));
    }

    protected Mp3File getMp3File(File audioFile) throws IOException, UnsupportedTagException, InvalidDataException {
        return new Mp3File(audioFile.getCanonicalPath());
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
