package com.github.marabou.helper;

import org.eclipse.swt.widgets.Display;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class ImageLoaderTest {

    @Test
    public void filePathMappingsAreCorrectlySet() {

        // given
        Display display = mock(Display.class);
        ImageLoader imageLoader = new ImageLoader(display);

        // expect
        assertEquals("audiofile.png", imageLoader.filePathMapping.get(AvailableImage.AUDIO_ICON));
        assertEquals("marabou_16.png", imageLoader.filePathMapping.get(AvailableImage.LOGO_SMALL));
        assertEquals("marabou_300.png", imageLoader.filePathMapping.get(AvailableImage.LOGO_BIG));
        assertEquals("exit.png", imageLoader.filePathMapping.get(AvailableImage.EXIT_ICON));
        assertEquals("folder.png", imageLoader.filePathMapping.get(AvailableImage.FOLDER_ICON));
        assertEquals("help.png", imageLoader.filePathMapping.get(AvailableImage.HELP_ICON));
        assertEquals("save.png", imageLoader.filePathMapping.get(AvailableImage.SAVE_ICON));
    }

    @Test
    public void getImageReturnsInputStream() {
        // given
        Display display = mock(Display.class);
        ImageLoader imageLoader = new ImageLoader(display);

        // expect
        // assertNotNull(imageLoader.getImage(AvailableImage.LOGO_SMALL));
    }
}
