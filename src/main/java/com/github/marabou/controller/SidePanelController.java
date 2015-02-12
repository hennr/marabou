package com.github.marabou.controller;

import com.github.marabou.events.FilesSelectedEvent;
import com.github.marabou.model.AudioFile;
import com.github.marabou.view.SidePanel;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.util.Set;

public class SidePanelController {

    private SidePanel sidePanel;

    public SidePanelController(EventBus bus, SidePanel sidePanel) {
        bus.register(this);
        this.sidePanel = sidePanel;
    }

    @Subscribe
    public void updateSidePanelModel(FilesSelectedEvent event) {
        Set<AudioFile> audioFiles = event.selectedAudioFiles;
        sidePanel.updateLists(audioFiles);
    }
}
