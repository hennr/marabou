package com.github.marabou.gui;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MainMenuTest {

    @Test
    public void anEmptyFolderStructureReturnsAnEmptyList() {

        // given
        File dirToScan = mock(File.class);
        File[] files = new File[]{};
        when(dirToScan.listFiles()).thenReturn(files);

        // when
        List<File> result = MainMenu.findFiles(dirToScan);

        // then
        assertThat(result).hasSize(0);
    }

    @Test
    public void aSingleFileIsFound() throws IOException {

        // given
        File dirToScan = mock(File.class);
        File mockedMp3File = mock(File.class);
        when(mockedMp3File.getName()).thenReturn("foo.mp3");
        when(mockedMp3File.canRead()).thenReturn(true);
        when(mockedMp3File.exists()).thenReturn(true);
        when(mockedMp3File.getCanonicalPath()).thenReturn("foo.mp3");
        when(mockedMp3File.getAbsolutePath()).thenReturn("foo.mp3");
        File[] files = new File[] {mockedMp3File};
        when(dirToScan.listFiles()).thenReturn(files);

        // when
        List<File> result = MainMenu.findFiles(dirToScan);

        // then
        assertThat(result).
                hasSize(1).
                contains(mockedMp3File);
    }


    // test mit orndername = foo.mp3

    // test keine links werden akzeptiert

    // test dateien werden in rekursiven strukturen gefunden
}
