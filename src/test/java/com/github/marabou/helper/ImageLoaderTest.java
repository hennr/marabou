/**
 * Marabou - Audio Tagger
 * Copyright (C) 2012 - 2015 Jan-Hendrik Peters
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
