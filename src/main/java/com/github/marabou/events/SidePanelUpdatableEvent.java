package com.github.marabou.events;

import com.github.marabou.model.AudioFile;

import java.util.Set;

public class SidePanelUpdatableEvent {
    public Set<AudioFile> audioFiles;

    public SidePanelUpdatableEvent(Set<AudioFile> audioFiles) {
        this.audioFiles = audioFiles;
    }
}
