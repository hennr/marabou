package com.github.marabou.service;

import com.github.marabou.audio.AudioFileFilter;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AudioFileServiceTest {

    @Test
    public void anEmptyFolderStructureReturnsAnEmptyList() {

        // given
        AudioFileFilter audioFileFilter = new AudioFileFilter();
        AudioFileService service = new AudioFileService(audioFileFilter);
        File dirToScan = mock(File.class);
        File[] files = new File[]{};
        when(dirToScan.listFiles()).thenReturn(files);

        // when
        List<File> result = service.findFiles(dirToScan);

        // then
        assertEquals(0, result.size());
    }

    @Test
    public void aSingleFileIsFound() throws IOException {

        // given
        AudioFileFilter audioFileFilterMock = new AudioFileFilter();
        AudioFileService service = new AudioFileService(audioFileFilterMock);
        File dirToScan = mock(File.class);
        File mockedMp3File = mock(File.class);
        when(mockedMp3File.getName()).thenReturn("foo.mp3");
        when(mockedMp3File.canRead()).thenReturn(true);
        when(mockedMp3File.exists()).thenReturn(true);
        when(mockedMp3File.isFile()).thenReturn(true);
        when(mockedMp3File.getCanonicalPath()).thenReturn("foo.mp3");
        File[] files = new File[] {mockedMp3File};
        when(dirToScan.listFiles()).thenReturn(files);

        // when
        List<File> result = service.findFiles(dirToScan);

        // then
        assertEquals(1, result.size());
        result.contains(mockedMp3File);
    }

    @Test
    public void makesUseOfTheAudioFileFilter() throws IOException {

        // given
        AudioFileFilter audioFileFilterMock = mock(AudioFileFilter.class);
        when(audioFileFilterMock.accept(any(File.class))).thenReturn(true);
        AudioFileService service = new AudioFileService(audioFileFilterMock);
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
        List<File> result = service.findFiles(dirToScan);

        // then
        verify(audioFileFilterMock).accept(any(File.class));
        assertEquals(1, result.size());
        result.contains(mockedMp3File);
    }

    // TODO
    // test mit orndername = foo.mp3

    // test keine links werden akzeptiert

    // test dateien werden in rekursiven strukturen gefunden

}