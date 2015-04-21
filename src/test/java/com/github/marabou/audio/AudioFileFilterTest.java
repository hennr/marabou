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
package com.github.marabou.audio;

import com.github.marabou.audio.loader.AudioFileFilter;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AudioFileFilterTest {

    @Test
    public void testAccept() throws Exception {

        // given
        File validFile = mock(File.class);

        when(validFile.getName()).thenReturn("foo.mp3");
        when(validFile.canRead()).thenReturn(true);
        when(validFile.isFile()).thenReturn(true);
        AudioFileFilter filter = new AudioFileFilter();

        // when
        assertTrue(filter.accept(validFile));
    }

    @Test
    public void ignoresFilesWithNonSupportedFileName() throws Exception{

        // given
        AudioFileFilter audioFileFilter = new AudioFileFilter();

        File unsupportedFile = mock(File.class);
        when(unsupportedFile.getName()).thenReturn("foo.ogg");
        when(unsupportedFile.canRead()).thenReturn(true);
        when(unsupportedFile.isFile()).thenReturn(true);

        // then
        assertFalse(audioFileFilter.accept(unsupportedFile));
    }

}
