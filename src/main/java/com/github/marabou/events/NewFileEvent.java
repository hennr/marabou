package com.github.marabou.events;

import com.github.marabou.model.AudioFile;

public class NewFileEvent {
    AudioFile audioFile;

    public NewFileEvent(AudioFile newFile) {
        this.audioFile = newFile;
    }

    public AudioFile getAudioFile() {
        return audioFile;
    }
}
