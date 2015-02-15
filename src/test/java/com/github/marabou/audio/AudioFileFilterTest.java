package com.github.marabou.audio;

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
