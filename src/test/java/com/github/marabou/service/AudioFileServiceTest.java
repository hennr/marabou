package com.github.marabou.service;

import com.github.marabou.audio.AudioFileFilter;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class AudioFileServiceTest {

    @Test
    public void anEmptyFolderStructureReturnsAnEmptyList() {

        // given
        AudioFileFilter audioFileFilter = new AudioFileFilter();
        AudioFileService service = new AudioFileService(audioFileFilter);
        File dirToScan = mock(File.class);
        File[] files = new File[] {};
        when(dirToScan.listFiles()).thenReturn(files);

        // when
        List<File> result = service.findAcceptableFilesRecursively(dirToScan);

        // then
        assertEquals(0, result.size());
    }

    @Test
    public void aSingleFileIsFound() throws IOException {

        // given
        AudioFileFilter audioFileFilter = new AudioFileFilter();
        AudioFileService service = new AudioFileService(audioFileFilter);
        File dirToScan = mock(File.class);
        File mockedMp3File = aValidMp3File();
        File[] files = new File[] {mockedMp3File};
        when(dirToScan.listFiles()).thenReturn(files);

        // when
        List<File> result = service.findAcceptableFilesRecursively(dirToScan);

        // then
        assertEquals(1, result.size());
        result.contains(mockedMp3File);
    }

    @Test
    public void makesUseOfTheAudioFileFilter() throws IOException {

        // given
        AudioFileFilter audioFileFilter = spy(new AudioFileFilter());
        AudioFileService service = new AudioFileService(audioFileFilter);
        File mockedMp3File = aValidMp3File();
        File dirToScan = mock(File.class);
        when(dirToScan.listFiles()).thenReturn(new File[] {mockedMp3File});

        // when
        List<File> result = service.findAcceptableFilesRecursively(dirToScan);

        // then
        verify(audioFileFilter).accept(any(File.class));
        assertEquals(1, result.size());
        result.contains(mockedMp3File);
    }

    @Test
    public void detectsFilesystemLinkLoopsAndAvoidsThem() throws IOException {

        // given
        File mockedMp3File = aValidMp3File();

        final String SLASH_FOO_CANONICAL_PATH_NAME = "/foo";

        AudioFileFilter audioFileFilter = new AudioFileFilter();
        AudioFileService service = new AudioFileService(audioFileFilter);

        File slashFoo = mock(File.class);
        when(slashFoo.isDirectory()).thenReturn(true);
        when(slashFoo.getName()).thenReturn(SLASH_FOO_CANONICAL_PATH_NAME);
        when(slashFoo.getCanonicalPath()).thenReturn(SLASH_FOO_CANONICAL_PATH_NAME);

        File slashFooSlashXSlashY = mock(File.class);
        when(slashFooSlashXSlashY.isDirectory()).thenReturn(true);
        when(slashFooSlashXSlashY.listFiles()).thenReturn(new File[] {});
        when(slashFooSlashXSlashY.getCanonicalPath()).thenReturn(SLASH_FOO_CANONICAL_PATH_NAME);

        File slashFooSlashX = mock(File.class);
        when(slashFooSlashX.isDirectory()).thenReturn(true);
        when(slashFooSlashX.listFiles()).thenReturn(new File[] {slashFooSlashXSlashY});
        when(slashFooSlashX.getCanonicalPath()).thenReturn(SLASH_FOO_CANONICAL_PATH_NAME);

        when(slashFoo.listFiles()).thenReturn(new File[] {slashFooSlashX, slashFooSlashXSlashY, mockedMp3File});

        // when
        List<File> result = service.findAcceptableFilesRecursively(slashFoo);

        // then
        assertEquals(1, result.size());
    }

    private File aValidMp3File() throws IOException {
        File mockedMp3File = mock(File.class);
        when(mockedMp3File.getName()).thenReturn("foo.mp3");
        when(mockedMp3File.canRead()).thenReturn(true);
        when(mockedMp3File.isFile()).thenReturn(true);
        when(mockedMp3File.exists()).thenReturn(true);
        when(mockedMp3File.getCanonicalPath()).thenReturn("foo.mp3");
        return mockedMp3File;
    }

    // TODO
    // test mit orndername = foo.mp3
    // test dateien werden in rekursiven strukturen gefunden
}
