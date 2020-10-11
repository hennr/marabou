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
package com.github.marabou.service;

import com.github.marabou.audio.loader.AudioFileFilter;

import com.github.marabou.audio.loader.AudioFileLoader;
import com.github.marabou.ui.events.OpenFileEvent;
import com.google.common.eventbus.EventBus;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.*;

public class AudioFileLoaderTest {

    AudioFileFilter audioFileFilter = spy(new AudioFileFilter());
    EventBus bus = mock(EventBus.class);
    AudioFileLoader audioFileLoader = new AudioFileLoader(audioFileFilter, bus);

    @Test
    public void openingFileFiresEvent() throws Exception {

        // given
        File file = aValidMp3File();

        // when
        audioFileLoader.openFile(file);

        ArgumentCaptor<OpenFileEvent> argument = ArgumentCaptor.forClass(OpenFileEvent.class);
        verify(bus).post(argument.capture());

        // then
        assertEquals(file, argument.getValue().getFile());
    }

    @Test
    public void openingAnEmptyFolderDoesNotFireAnEvent() {

        // given
        File dirToScan = mock(File.class);
        File[] files = new File[]{};
        when(dirToScan.listFiles()).thenReturn(files);

        // when
        audioFileLoader.openDirectory(dirToScan);

        // then
        verify(bus, never()).post(isA(OpenFileEvent.class));
    }

    @Test
    public void aSingleFileIsFound() throws IOException {

        // given
        File dirToScan = mock(File.class);
        File mockedMp3File = aValidMp3File();
        File[] files = new File[]{mockedMp3File};
        when(dirToScan.listFiles()).thenReturn(files);

        // when
        audioFileLoader.openDirectory(dirToScan);

        // then
        verify(bus, times(1)).post(isA(OpenFileEvent.class));
    }

    @Test
    public void makesUseOfTheAudioFileFilter() throws IOException {

        // given
        File mockedMp3File = aValidMp3File();
        File dirToScan = mock(File.class);
        when(dirToScan.listFiles()).thenReturn(new File[]{mockedMp3File});

        // when
        audioFileLoader.openDirectory(dirToScan);

        // then
        verify(audioFileFilter).accept(isA(File.class));
    }

    @Test
    public void detectsFilesystemLinkLoopsAndAvoidsThem() throws IOException {

        // given
        File mockedMp3File = aValidMp3File();

        final String SLASH_FOO_CANONICAL_PATH_NAME = "/foo";

        File slashFoo = mock(File.class);
        when(slashFoo.isDirectory()).thenReturn(true);
        when(slashFoo.getName()).thenReturn(SLASH_FOO_CANONICAL_PATH_NAME);
        when(slashFoo.getCanonicalPath()).thenReturn(SLASH_FOO_CANONICAL_PATH_NAME);

        File slashFooSlashXSlashY = mock(File.class);
        when(slashFooSlashXSlashY.isDirectory()).thenReturn(true);
        when(slashFooSlashXSlashY.listFiles()).thenReturn(new File[]{});
        when(slashFooSlashXSlashY.getCanonicalPath()).thenReturn(SLASH_FOO_CANONICAL_PATH_NAME);

        File slashFooSlashX = mock(File.class);
        when(slashFooSlashX.isDirectory()).thenReturn(true);
        when(slashFooSlashX.listFiles()).thenReturn(new File[]{slashFooSlashXSlashY});
        when(slashFooSlashX.getCanonicalPath()).thenReturn(SLASH_FOO_CANONICAL_PATH_NAME);

        when(slashFoo.listFiles()).thenReturn(new File[]{slashFooSlashX, slashFooSlashXSlashY, mockedMp3File});

        // when
        audioFileLoader.openDirectory(slashFoo);

        // then
        verify(bus, times(1)).post(isA(OpenFileEvent.class));
    }

    @Test
    public void detectsAllFilesInRecursiveFolderStructure() throws IOException {

        // given
        File mockedMp3File = aValidMp3File();
        File slashFoo = mock(File.class);
        when(slashFoo.isDirectory()).thenReturn(true);
        when(slashFoo.getName()).thenReturn("/foo");
        when(slashFoo.getCanonicalPath()).thenReturn("/foo");

        File slashFooSlashXSlashY = mock(File.class);
        when(slashFooSlashXSlashY.isDirectory()).thenReturn(true);
        when(slashFooSlashXSlashY.listFiles()).thenReturn(new File[]{mockedMp3File});
        when(slashFooSlashXSlashY.getCanonicalPath()).thenReturn("/foo/x/y");

        File slashFooSlashX = mock(File.class);
        when(slashFooSlashX.isDirectory()).thenReturn(true);
        when(slashFooSlashX.listFiles()).thenReturn(new File[]{slashFooSlashXSlashY, mockedMp3File});
        when(slashFooSlashX.getCanonicalPath()).thenReturn("/foo/x");

        when(slashFoo.listFiles()).thenReturn(new File[]{slashFooSlashX, slashFooSlashXSlashY, mockedMp3File});

        // when
        audioFileLoader.openDirectory(slashFoo);

        // then
        verify(bus, times(3)).post(isA(OpenFileEvent.class));
    }

    @Test
    public void doesNotDetectBadlyNamedFolderAsFile() throws IOException {

        // given
        File validFile = aValidMp3File();

        File badlyNamedFolder = mock(File.class);
        when(badlyNamedFolder.isDirectory()).thenReturn(true);
        when(badlyNamedFolder.getName()).thenReturn("bar.mp3");
        when(badlyNamedFolder.getCanonicalPath()).thenReturn("bar.mp3");
        when(badlyNamedFolder.listFiles()).thenReturn(new File[]{validFile});

        // when
        audioFileLoader.openDirectory(badlyNamedFolder);

        // then
        verify(bus, times(1)).post(isA(OpenFileEvent.class));
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
