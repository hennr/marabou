package com.github.marabou.events;

import com.github.marabou.model.AudioFile;

import java.util.Set;

public class FilesSelectedEvent {
    public final Set<AudioFile> selectedAudioFiles;

    public FilesSelectedEvent(Set<AudioFile> selectedAudioFiles) {
        this.selectedAudioFiles = selectedAudioFiles;
    }
}
