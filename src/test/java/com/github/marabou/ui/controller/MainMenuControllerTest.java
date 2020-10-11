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

import com.github.marabou.audio.loader.AudioFileLoader;
import com.github.marabou.audio.store.AudioFileStore;
import com.github.marabou.properties.UserProperties;
import com.github.marabou.ui.events.SaveSelectedFilesEvent;
import com.github.marabou.ui.view.AboutWindow;
import com.github.marabou.ui.view.OpenDirectoryDialog;
import com.google.common.eventbus.EventBus;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import java.io.File;

public class MainMenuControllerTest {

    EventBus bus = mock(EventBus.class);
    UserProperties userPropertiesMock = mock(UserProperties.class);
    AudioFileLoader audioFileLoaderMock = mock(AudioFileLoader.class);
    AboutWindow aboutWindowMock = mock(AboutWindow.class);
    AudioFileStore audioFileStore = mock(AudioFileStore.class);
    OpenDirectoryDialog openDirectoryDialog = mock(OpenDirectoryDialog.class);
    MainMenuController controller = new MainMenuController(bus, audioFileStore, userPropertiesMock, audioFileLoaderMock, aboutWindowMock, openDirectoryDialog);

    @Test
    public void handleOpenDirectoryEventCallsAudioFileLoader() {
        // given
        when(openDirectoryDialog.getDirectoryToOpen(any())).thenReturn("openMe");
        // when
        controller.handleOpenDirectoryEvent();
        // then
        verify(audioFileLoaderMock).openDirectory(eq(new File("openMe")));
    }

    @Test
    public void handleOpenDirectoryEventSavesLastPathIfEnabled() {
        // given
        when(openDirectoryDialog.getDirectoryToOpen(any())).thenReturn("openMe");
        when(userPropertiesMock.rememberLastPath()).thenReturn(true);
        // when
        controller.handleOpenDirectoryEvent();
        // then
        verify(userPropertiesMock).setLastPath(eq("openMe"));
    }

    @Test
    public void handleOpenDirectoryEventDoesNotSaveLastPathIfDisabled() {
        // given
        when(openDirectoryDialog.getDirectoryToOpen(any())).thenReturn("openMe");
        when(userPropertiesMock.rememberLastPath()).thenReturn(false);
        // when
        controller.handleOpenDirectoryEvent();
        // then
        verify(userPropertiesMock, never()).setLastPath(any());
    }

    @Test
    public void saveSelectedFilesFiresEvent() throws Exception {
        // when
        controller.handleSaveSelectedFilesEvent();
        // then
        verify(controller.bus).post(any(SaveSelectedFilesEvent.class));
    }
}
