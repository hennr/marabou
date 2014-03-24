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
        AudioFileFilter audioFileFilter = mock(AudioFileFilter.class);
        when(audioFileFilter.accept(any(File.class))).thenReturn(true);

        TableController controllerUnderTest = new TableController(audioFileFilter);

        HSQLDBController hsqldbControllerMock = mock(HSQLDBController.class);
        controllerUnderTest.hsqldbController = hsqldbControllerMock;

        File file = aValidMockedFile();

        // when
        controllerUnderTest.openFile(file);

        // then
        verify(audioFileFilter).accept(file);
        verify(hsqldbControllerMock).insertFile(file);
        verify(hsqldbControllerMock).addAllTableItems();
    }

    @Test
    public void testOpenFiles() throws Exception {

        // given
        AudioFileFilter audioFileFilter = new AudioFileFilter();
        when(audioFileFilter.accept(any(File.class))).thenReturn(true);

        TableController controllerUnderTest = new TableController(audioFileFilter);

        HSQLDBController hsqldbControllerMock = mock(HSQLDBController.class);
        controllerUnderTest.hsqldbController = hsqldbControllerMock;

        List<File> files = new ArrayList<>(3);
        files.add(aValidMockedFile());
        files.add(aValidMockedFile());

        // when
        controllerUnderTest.openFiles(files);

        // then
        verify(hsqldbControllerMock).insertFile(files.get(0));
        verify(hsqldbControllerMock).insertFile(files.get(1));
        verify(hsqldbControllerMock, times(2)).addAllTableItems();
    }

    private File aValidMockedFile() {
        File file = mock(File.class);
        when(file.getName()).thenReturn("foo.mp3");
        when(file.canRead()).thenReturn(true);
        when(file.isDirectory()).thenReturn(false);
        return file;
    }
}
