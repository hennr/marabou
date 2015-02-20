/**
 * Copyright (C) 2012 - 2015 Jan-Hendrik Peters
 *
 * This file is part of marabou.
 *
 * Marabou is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package com.github.marabou.controller;

import com.github.marabou.audio.AudioFileFilter;
import com.github.marabou.events.SaveSelectedFilesEvent;
import com.github.marabou.model.Model;
import com.github.marabou.view.AboutWindow;
import com.github.marabou.properties.UserProperties;
import com.github.marabou.service.AudioFileService;
import com.google.common.eventbus.EventBus;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class MainMenuControllerTest {

    @Test
    public void opensValidFile() throws Exception {

        // given
        MainMenuController controllerUnderTest = givenAMainMenuControllerWithMocks();

        Model modelMock = mock(Model.class);
        controllerUnderTest.model = modelMock;

        File file = aValidMockedFile();

        // when
        controllerUnderTest.openFile(file);

        // then
        verify(modelMock).addFile(file);
    }

    @Test
    public void testOpenFiles() throws Exception {

        // given
        MainMenuController controllerUnderTest = givenAMainMenuControllerWithMocks();

        Model modelMock = mock(Model.class);
        controllerUnderTest.model = modelMock;

        List<File> files = new ArrayList<>(3);
        files.add(aValidMockedFile());
        files.add(aValidMockedFile());

        // when
        controllerUnderTest.openFiles(files);

        // then
        verify(modelMock).addFile(files.get(0));
        verify(modelMock).addFile(files.get(1));
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
        AudioFileFilter audioFileFilterMock = mock(AudioFileFilter.class);
        when(audioFileFilterMock.accept(any(File.class))).thenReturn(true);
        UserProperties userPropertiesMock = mock(UserProperties.class);
        AudioFileService audioFileServiceMock = mock(AudioFileService.class);
        AboutWindow aboutWindowMock = mock(AboutWindow.class);
        Model model = mock(Model.class);
        EventBus bus = mock(EventBus.class);

        return new MainMenuController(bus, model, audioFileFilterMock, userPropertiesMock, audioFileServiceMock, aboutWindowMock);
    }

    private File aValidMockedFile() {
        File file = mock(File.class);
        when(file.getName()).thenReturn("foo.mp3");
        when(file.canRead()).thenReturn(true);
        when(file.isDirectory()).thenReturn(false);
        return file;
    }
}
