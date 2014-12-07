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

    /**
     * Insert audio file into DB.
     *
     * @throws InvalidDataException if file is not a supported file type or if it's corrupt
     */
    public void insertFile(final File audioFile) throws InvalidDataException, IOException, UnsupportedTagException {
        ID3v1 id31Tag = null;
        ID3v2 id32Tag = null;
        boolean id31 = false;
        boolean id32 = false;

        Mp3File mp3File = new Mp3File(audioFile.getAbsolutePath());
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

        // if the file got an id31 tag
        if (id31) {
            // artist
            artist = id31Tag.getArtist();
            // title
            title = id31Tag.getTitle();
            // album
            album = id31Tag.getAlbum();
            // track number
            trackNumber = id31Tag.getTrack();
            // year
            year = id31Tag.getYear();
            // genre
            int genreId = id31Tag.getGenre();
            try {
                genre = Genres.getGenreById(genreId);
            } catch (UnknownGenreException e) {
                genre = "";
            }

            // comments
            comment = id31Tag.getComment();
            // disc number
            discNo = "Not supported yet";
            // composer
            composer = "Not supported in idv31";
        }

        // if the file got an id32 tag
        if (id32) {
            // artist
            artist = id32Tag.getArtist();
            // title
            title = id32Tag.getTitle();
            // album
            album = id32Tag.getAlbum();
            // track number
            trackNumber = id32Tag.getTrack();
            // year
            year = id32Tag.getYear();
            // genre
            int genreId = id32Tag.getGenre();
            try {
                genre = Genres.getGenreById(genreId);
            } catch (UnknownGenreException e) {
                genre = "";
            }
            // comments
            comment = id32Tag.getComment();
            // disc number
            discNo = "Not supported yet";
            // composer
            composer = id32Tag.getComposer();
        }

        // values not tag version specific

        // duration
        String duration;
        try {
            duration = calculateTrackLength((int) mp3File.getLengthInSeconds());
        } catch (IllegalArgumentException e) {
            duration = "0";
        }
        // bit rate
        String bitRate = Integer.toString(mp3File.getBitrate());
        // sample rate
        String sampleRate = Integer.toString(mp3File.getSampleRate());
        // channels
        String channels = mp3File.getChannelMode();
        // encoding
        String encoding = "mp3";
        // full path to file
        String fullPath = audioFile.getAbsolutePath();

        AudioFile newFile = new AudioFile(fullPath)
                .withArtist(artist)
                .withTitle(title)
                .withAlbum(album)
                .withTrack(trackNumber)
                .withYear(year)
                .withGenre(genre)
                .withComments(comment)
                .withDiscNumber(discNo)
                .withComposer(composer)
                .withBitrate(bitRate)
                .withSamplerate(sampleRate)
                .withChannels(channels)
                .withEncoding(encoding)
                .withFilePath(fullPath)
                .withDuration(duration);
        
        storeFile(newFile);

        bus.post(new NewFileEvent(newFile));
    }

    private void storeFile(AudioFile newFile) {
        files = new HashMap<>();
        files.put(newFile.getId(), newFile);
    }

    /**
     * converts seconds to the following format: min:secs
     * @return min:secs as a string
     */
    private String calculateTrackLength(int secs) {

        if (secs < 0) {
            return Integer.toString(secs);
        }

        int mins = secs / 60;

        if (mins == 0) {
            if (secs < 10) {
                return "0:0" + secs;
            } else {
                return "0:" + secs;
            }
        } else {
            String seconds = String.valueOf(secs - (60 * mins));
            if (seconds.length() < 2) {
                seconds = "0" + seconds;
            }
            return mins + ":" + seconds;
        }
    }

}
