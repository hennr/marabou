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
package com.github.marabou.ui.controller;

import com.github.marabou.audio.store.SidePanelModifiedEvent;
import com.github.marabou.ui.events.FilesSelectedEvent;
import com.github.marabou.ui.events.ComboPropertyChange;
import com.github.marabou.audio.AudioFile;
import com.github.marabou.ui.view.SidePanel;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.util.Set;

public class SidePanelController {

    private final EventBus bus;
    private SidePanel sidePanel;


    public SidePanelController(EventBus bus, SidePanel sidePanel) {
        bus.register(this);
        this.sidePanel = sidePanel;
        this.bus = bus;
    }

    @Subscribe
    public void updateSidePanelModel(FilesSelectedEvent event) {
        Set<AudioFile> audioFiles = event.selectedAudioFiles;
        sidePanel.updateComboBoxes(audioFiles);
    }

    public void onPropertyChange(ComboPropertyChange propertyChange) {
        bus.post(new SidePanelModifiedEvent(propertyChange.getProperty(), propertyChange.getNewValue()));
    }
}
