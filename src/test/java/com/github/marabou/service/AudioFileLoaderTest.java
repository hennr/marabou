/**
 * Marabou - Audio Tagger
 *
 * Copyright (C) 2012 - 2015 Jan-Hendrik Peters
 *
 * https://github.com/hennr/marabou
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
package com.github.marabou.service;

import com.github.marabou.audio.loader.AudioFileFilter;

import com.github.marabou.audio.loader.AudioFileLoader;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.mockito.Mockito.*;

public class AudioFileLoaderTest {

    @Test
    public void anEmptyFolderStructureReturnsAnEmptyList() {

        // given
        AudioFileFilter audioFileFilter = new AudioFileFilter();
        AudioFileLoader service = new AudioFileLoader(audioFileFilter);
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
        AudioFileLoader service = new AudioFileLoader(audioFileFilter);
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
        AudioFileLoader service = new AudioFileLoader(audioFileFilter);
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
        AudioFileLoader service = new AudioFileLoader(audioFileFilter);

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

    @Test
    public void detectsAllFilesInRecursiveFolderStructure() throws IOException {

        // given
        File mockedMp3File = aValidMp3File();

        AudioFileFilter audioFileFilter = new AudioFileFilter();
        AudioFileLoader service = new AudioFileLoader(audioFileFilter);

        File slashFoo = mock(File.class);
        when(slashFoo.isDirectory()).thenReturn(true);
        when(slashFoo.getName()).thenReturn("/foo");
        when(slashFoo.getCanonicalPath()).thenReturn("/foo");

        File slashFooSlashXSlashY = mock(File.class);
        when(slashFooSlashXSlashY.isDirectory()).thenReturn(true);
        when(slashFooSlashXSlashY.listFiles()).thenReturn(new File[] {mockedMp3File});
        when(slashFooSlashXSlashY.getCanonicalPath()).thenReturn("/foo/x/y");

        File slashFooSlashX = mock(File.class);
        when(slashFooSlashX.isDirectory()).thenReturn(true);
        when(slashFooSlashX.listFiles()).thenReturn(new File[] {slashFooSlashXSlashY, mockedMp3File});
        when(slashFooSlashX.getCanonicalPath()).thenReturn("/foo/x");

        when(slashFoo.listFiles()).thenReturn(new File[] {slashFooSlashX, slashFooSlashXSlashY, mockedMp3File});

        // when
        List<File> result = service.findAcceptableFilesRecursively(slashFoo);

        // then
        assertEquals(3, result.size());
    }

    @Test
    public void doesNotDetectBadlyNamedFolderAsFile() throws IOException {

        // given
        AudioFileFilter audioFileFilter = spy(new AudioFileFilter());
        AudioFileLoader service = new AudioFileLoader(audioFileFilter);
        File validFile = aValidMp3File();

        File badlyNamedFolder = mock(File.class);
        when(badlyNamedFolder.isDirectory()).thenReturn(true);
        when(badlyNamedFolder.getName()).thenReturn("bar.mp3");
        when(badlyNamedFolder.getCanonicalPath()).thenReturn("bar.mp3");
        when(badlyNamedFolder.listFiles()).thenReturn(new File[] {validFile});

        // when
        List<File> results = service.findAcceptableFilesRecursively(badlyNamedFolder);

        // then
      assertEquals(1, results.size());
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

}
