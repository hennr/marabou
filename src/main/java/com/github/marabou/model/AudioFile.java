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
