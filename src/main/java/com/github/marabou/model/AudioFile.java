/**
 * Copyright (C) 2012 - 2015 Jan-Hendrik Peters
 *
 * This file is part of marabou.
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

public class AudioFile {
    String id;
    String artist;
    String title;
    String album;
    String duration;
    String track;
    String bitrate;
    String samplerate;
    String channels;
    String year;
    String genre;
    String comments;
    String discNumber;
    String composer;
    String encoding;
    String filePath;

    public AudioFile(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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

    public String getBitrate() {
        return bitrate;
    }

    public AudioFile withBitrate(String bitrate) {
        this.bitrate = bitrate;
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

    public String getComments() {
        return comments;
    }

    public AudioFile withComments(String comments) {
        this.comments = comments;
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

    public AudioFile withFilePath(String filePath) {
        this.filePath = filePath;
        return this;
    }
}
