package com.github.marabou.controller;

import com.github.marabou.audio.AudioFileFilter;
import com.github.marabou.db.HSQLDBController;
import org.junit.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

public class TableControllerTest {

    @Test
    public void opensValidFile() throws Exception {

        // given
        AudioFileFilter audioFileFilter = new AudioFileFilter();
        TableController controllerUnderTest = new TableController(audioFileFilter);
        HSQLDBController hsqldbControllerMock = mock(HSQLDBController.class);
        controllerUnderTest.hsqldbController = hsqldbControllerMock;
        File file = aValidMockedFile("foo.mp3");

        // when
        controllerUnderTest.openFile(file);

        // then
        verify(hsqldbControllerMock).insertFile(file);
        verify(hsqldbControllerMock).addAllTableItems();
    }

    @Test
    public void testOpenFiles() throws Exception {

        // given
        AudioFileFilter audioFileFilter = new AudioFileFilter();
        TableController controllerUnderTest = new TableController(audioFileFilter);
        HSQLDBController hsqldbControllerMock = mock(HSQLDBController.class);
        controllerUnderTest.hsqldbController = hsqldbControllerMock;
        List<File> files = new ArrayList<>(3);
        files.add(aValidMockedFile("foo.mp3"));
        files.add(aValidMockedFile("foo.mP3"));
        files.add(aValidMockedFile("foo.MP3"));

        // when
        controllerUnderTest.openFiles(files);

        // then
        verify(hsqldbControllerMock).insertFile(files.get(0));
        verify(hsqldbControllerMock).insertFile(files.get(1));
        verify(hsqldbControllerMock).insertFile(files.get(2));
        verify(hsqldbControllerMock, times(3)).addAllTableItems();
    }

    @Test
    public void ignoresFilesWithNonSupportedFileName() throws Exception{

        // given
        AudioFileFilter audioFileFilter = new AudioFileFilter();
        TableController controllerUnderTest = new TableController(audioFileFilter);
        HSQLDBController hsqldbControllerMock = mock(HSQLDBController.class);
        controllerUnderTest.hsqldbController = hsqldbControllerMock;
        File file = aValidMockedFile("foo.ogg");

        // when
        controllerUnderTest.openFile(file);

        // then
        verifyZeroInteractions(hsqldbControllerMock);
    }

    private File aValidMockedFile(String fileName) {
        File file = mock(File.class);
        when(file.getName()).thenReturn(fileName);
        when(file.canRead()).thenReturn(true);
        when(file.isDirectory()).thenReturn(false);
        return file;
    }
}
