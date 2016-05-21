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

public class AudioFile {

    String artist = "";
    String title = "";
    String album = "";
    String duration = "";
    String track = "";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AudioFile audioFile = (AudioFile) o;

        if (artist != null ? !artist.equals(audioFile.artist) : audioFile.artist != null) return false;
        if (title != null ? !title.equals(audioFile.title) : audioFile.title != null) return false;
        if (album != null ? !album.equals(audioFile.album) : audioFile.album != null) return false;
        if (duration != null ? !duration.equals(audioFile.duration) : audioFile.duration != null) return false;
        if (track != null ? !track.equals(audioFile.track) : audioFile.track != null) return false;
        if (bitRate != null ? !bitRate.equals(audioFile.bitRate) : audioFile.bitRate != null) return false;
        if (samplerate != null ? !samplerate.equals(audioFile.samplerate) : audioFile.samplerate != null) return false;
        if (channels != null ? !channels.equals(audioFile.channels) : audioFile.channels != null) return false;
        if (year != null ? !year.equals(audioFile.year) : audioFile.year != null) return false;
        if (genre != null ? !genre.equals(audioFile.genre) : audioFile.genre != null) return false;
        if (comment != null ? !comment.equals(audioFile.comment) : audioFile.comment != null) return false;
        if (discNumber != null ? !discNumber.equals(audioFile.discNumber) : audioFile.discNumber != null) return false;
        if (composer != null ? !composer.equals(audioFile.composer) : audioFile.composer != null) return false;
        if (encoding != null ? !encoding.equals(audioFile.encoding) : audioFile.encoding != null) return false;
        return !(filePath != null ? !filePath.equals(audioFile.filePath) : audioFile.filePath != null);

    }

    @Override
    public int hashCode() {
        int result = artist != null ? artist.hashCode() : 0;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (album != null ? album.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        result = 31 * result + (track != null ? track.hashCode() : 0);
        result = 31 * result + (bitRate != null ? bitRate.hashCode() : 0);
        result = 31 * result + (samplerate != null ? samplerate.hashCode() : 0);
        result = 31 * result + (channels != null ? channels.hashCode() : 0);
        result = 31 * result + (year != null ? year.hashCode() : 0);
        result = 31 * result + (genre != null ? genre.hashCode() : 0);
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        result = 31 * result + (discNumber != null ? discNumber.hashCode() : 0);
        result = 31 * result + (composer != null ? composer.hashCode() : 0);
        result = 31 * result + (encoding != null ? encoding.hashCode() : 0);
        result = 31 * result + (filePath != null ? filePath.hashCode() : 0);
        return result;
    }

    String bitRate = "";
    String samplerate = "";
    String channels = "";
    String year = "";
    String genre = "";
    String comment = "";
    String discNumber = "";
    String composer = "";
    String encoding = "";
    String filePath = "";

    /**
     * The given file path is the ID for this audio file.
     */
    public AudioFile(String canonicalFilePath) {
        this.filePath = canonicalFilePath;
    }

    public String getArtist() {
        return artist;
    }

    public AudioFile withArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public AudioFile withTitle(String title) {
        this.title = title;
        return this;
    }

    public String getAlbum() {
        return album;
    }

    public AudioFile withAlbum(String album) {
        this.album = album;
        return this;
    }

    public String getDuration() {
        return duration;
    }

    public AudioFile withDuration(String duration) {
        this.duration = duration;
        return this;
    }

    public String getTrack() {
        return track;
    }

    public AudioFile withTrack(String track) {
        this.track = track;
        return this;
    }

    public String getBitRate() {
        return bitRate;
    }

    public AudioFile withBitRate(String bitRate) {
        this.bitRate = bitRate;
        return this;
    }

    public String getSamplerate() {
        return samplerate;
    }

    public AudioFile withSamplerate(String samplerate) {
        this.samplerate = samplerate;
        return this;
    }

    public String getChannels() {
        return channels;
    }

    public AudioFile withChannels(String channels) {
        this.channels = channels;
        return this;
    }

    public String getYear() {
        return year;
    }

    public AudioFile withYear(String year) {
        this.year = year;
        return this;
    }

    public String getGenre() {
        return genre;
    }

    public AudioFile withGenre(String genre) {
        this.genre = genre;
        return this;
    }

    public String getComment() {
        return comment;
    }

    public AudioFile withComment(String comment) {
        this.comment = comment;
        return this;
    }

    public String getDiscNumber() {
        return discNumber;
    }

    public AudioFile withDiscNumber(String discNumber) {
        this.discNumber = discNumber;
        return this;
    }

    public String getComposer() {
        return composer;
    }

    public AudioFile withComposer(String composer) {
        this.composer = composer;
        return this;
    }

    public String getEncoding() {
        return encoding;
    }

    public AudioFile withEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public String getFilePath() {
        return filePath;
    }
}
