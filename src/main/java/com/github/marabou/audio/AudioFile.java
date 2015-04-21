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
package com.github.marabou.audio;

public class AudioFile {
    String artist ="";
    String title ="";
    String album ="";
    String duration ="";
    String track ="";
    String bitRate ="";
    String samplerate ="";
    String channels ="";
    String year ="";
    String genre ="";
    String comment ="";
    String discNumber ="";
    String composer ="";
    String encoding ="";
    String filePath ="";

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
