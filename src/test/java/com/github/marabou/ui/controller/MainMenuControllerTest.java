/**
 * Marabou - Audio Tagger
 * Copyright (C) 2012 - 2015 Jan-Hendrik Peters
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
import com.github.marabou.ui.events.OpenFileEvent;
import com.github.marabou.ui.events.SaveSelectedFilesEvent;
import com.github.marabou.ui.view.AboutWindow;
import com.google.common.eventbus.EventBus;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class MainMenuControllerTest {
    EventBus bus = mock(EventBus.class);

    @Test
    public void opensValidFile() throws Exception {

        // given
        MainMenuController controllerUnderTest = givenAMainMenuControllerWithMocks();

        controllerUnderTest.audioFileStore = mock(AudioFileStore.class);

        File file = aValidMockedFile();

        // when
        controllerUnderTest.openFile(file);

        ArgumentCaptor<OpenFileEvent> argument = ArgumentCaptor.forClass(OpenFileEvent.class);
        verify(bus).post(argument.capture());

        // then
        assertEquals(file, argument.getValue().getFile());
    }

    @Test
    public void openingAFileProvokesAnOpenFileEvent() throws Exception {

        // given
        MainMenuController controllerUnderTest = givenAMainMenuControllerWithMocks();

        controllerUnderTest.audioFileStore = mock(AudioFileStore.class);

        List<File> files = new ArrayList<>(1);
        File file = aValidMockedFile();
        files.add(file);

        // when
        controllerUnderTest.openFiles(files);

        ArgumentCaptor<OpenFileEvent> argumentCaptor = ArgumentCaptor.forClass(OpenFileEvent.class);
        verify(bus).post(argumentCaptor.capture());

        // then
        assertEquals(file, argumentCaptor.getValue().getFile());
    }


    @Test
    public void opensMultipleFiles() throws Exception {

        // given
        MainMenuController controllerUnderTest = givenAMainMenuControllerWithMocks();

        controllerUnderTest.audioFileStore = mock(AudioFileStore.class);

        List<File> files = new ArrayList<>(2);
        files.add(aValidMockedFile());
        files.add(aValidMockedFile());

        // when
        controllerUnderTest.openFiles(files);

        ArgumentCaptor argumentCaptor = ArgumentCaptor.forClass(OpenFileEvent.class);
        verify(bus, times(2)).post(argumentCaptor.capture());

        // then
        assertTrue(argumentCaptor.getValue() instanceof OpenFileEvent);
    }

    @Test
    public void saveSelectedFilesFiresEvent() throws Exception {

        // given
        MainMenuController controller = givenAMainMenuControllerWithMocks();

        // when
        controller.handleSaveSelectedFilesEvent();

        // then
        verify(controller.bus).post(any(SaveSelectedFilesEvent.class));
    }

    private MainMenuController givenAMainMenuControllerWithMocks() {
        UserProperties userPropertiesMock = mock(UserProperties.class);
        AudioFileLoader audioFileLoaderMock = mock(AudioFileLoader.class);
        AboutWindow aboutWindowMock = mock(AboutWindow.class);
        AudioFileStore audioFileStore = mock(AudioFileStore.class);

        return new MainMenuController(bus, audioFileStore, userPropertiesMock, audioFileLoaderMock, aboutWindowMock);
    }

    private File aValidMockedFile() {
        File file = mock(File.class);
        when(file.getName()).thenReturn("foo.mp3");
        when(file.canRead()).thenReturn(true);
        when(file.isDirectory()).thenReturn(false);
        return file;
    }
}
