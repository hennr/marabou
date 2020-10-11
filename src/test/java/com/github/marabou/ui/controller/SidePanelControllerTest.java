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

import com.github.marabou.audio.AudioFile;
import com.github.marabou.ui.events.FilesSelectedEvent;
import com.github.marabou.ui.view.SidePanel;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static testdata.builder.TestAudioFileBuilder.aValidCompleteAudioFile;

public class SidePanelControllerTest {

    @Test
    public void updatesSidePanelOnFilesSelectedEvent() throws Exception {

        // given
        EventBus bus = new EventBus();
        SidePanel sidePanelMock = mock(SidePanel.class);
        new SidePanelController(bus, sidePanelMock);
        AudioFile audioFile = aValidCompleteAudioFile();
        Set<AudioFile> selectedAudioFiles = Sets.newHashSet();
        selectedAudioFiles.add(audioFile);
        FilesSelectedEvent filesSelectedEvent = new FilesSelectedEvent(selectedAudioFiles);

        // when
        bus.post(filesSelectedEvent);

        // then
        verify(sidePanelMock).updateComboBoxes(selectedAudioFiles);
    }
}
