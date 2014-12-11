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
