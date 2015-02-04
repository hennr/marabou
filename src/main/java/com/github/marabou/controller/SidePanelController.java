package com.github.marabou.controller;

import com.github.marabou.events.FilesSelectedEvent;
import com.github.marabou.events.SidePanelUpdatableEvent;
import com.github.marabou.model.AudioFile;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.util.Set;

public class SidePanelController {


    private EventBus bus;
    private Set<AudioFile> audioFiles;

    public SidePanelController(EventBus bus) {
        this.bus = bus;
        bus.register(this);
    }

    @Subscribe
    public void updateSidePanelModel(FilesSelectedEvent event) {
        audioFiles = event.selectedAudioFiles;
        bus.post(new SidePanelUpdatableEvent(audioFiles));
    }
}
