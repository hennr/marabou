/**
 * Marabou - Audio Tagger
 * <p/>
 * Copyright (C) 2012 - 2015 Jan-Hendrik Peters
 * <p/>
 * https://github.com/hennr/marabou
 * <p/>
 * Marabou is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package com.github.marabou.audio.store;

import com.github.marabou.audio.AudioFile;
import com.github.marabou.audio.AudioFileFactory;
import com.google.common.eventbus.EventBus;
import com.mpatric.mp3agic.*;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AudioFileStoreTest {

    @Test
    public void getAudioFileByFilePathAfterAdd() throws Exception {

        // given
        AudioFileFactory fileFactoryMock = mock(AudioFileFactory.class);
        AudioFileStore audioFileStore = new AudioFileStore(new EventBus(), fileFactoryMock);

        File dummyFile = mock(File.class);
        AudioFile audioFile = new AudioFile("/path");
        when(fileFactoryMock.createAudioFile(any(File.class))).thenReturn(audioFile);

        // when
        audioFileStore.addFile(dummyFile);
        AudioFile result = audioFileStore.getAudioFileByFilePath("/path");

        // then
        assertNotNull(result);
    }
}
