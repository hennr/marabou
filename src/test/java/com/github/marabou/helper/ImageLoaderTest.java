package com.github.marabou.helper;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ImageLoaderTest {

    @Test
    public void filePathMappingsAreCorrectlySet() {

        // given
        ImageLoader imageLoader = new ImageLoader();

        // expect
        assertEquals("audio_file.png", imageLoader.filePathMapping.get(AvailableImage.AUDIO_ICON));
        assertEquals("marabou_16.png", imageLoader.filePathMapping.get(AvailableImage.LOGO_SMALL));
        assertEquals("marabou_300.png", imageLoader.filePathMapping.get(AvailableImage.LOGO_BIG));
        assertEquals("exit.png", imageLoader.filePathMapping.get(AvailableImage.EXIT_ICON));
        assertEquals("folder.png", imageLoader.filePathMapping.get(AvailableImage.FOLDER_ICON));
        assertEquals("help.png", imageLoader.filePathMapping.get(AvailableImage.HELP_ICON));
        assertEquals("save.png", imageLoader.filePathMapping.get(AvailableImage.SAVE_ICON));
    }
}
