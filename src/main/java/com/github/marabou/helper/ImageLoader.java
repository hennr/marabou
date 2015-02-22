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
package com.github.marabou.helper;

import com.github.marabou.view.BaseGuiClass;
import org.eclipse.swt.graphics.Image;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageLoader extends BaseGuiClass {

    private final Object IMAGE_PATH = "graphics/";
    Map<AvailableImage, String> filePathMapping = new HashMap<>();

    public ImageLoader() {
        initFilePathMapping();
    }

    private void initFilePathMapping() {
        filePathMapping.put(AvailableImage.LOGO_SMALL, "marabou_16.png");
        filePathMapping.put(AvailableImage.LOGO_BIG, "marabou_300.png");
        filePathMapping.put(AvailableImage.EXIT_ICON, "exit.png");
        filePathMapping.put(AvailableImage.FOLDER_ICON, "folder.png");
        filePathMapping.put(AvailableImage.HELP_ICON, "help.png");
        filePathMapping.put(AvailableImage.SAVE_ICON, "save.png");
        filePathMapping.put(AvailableImage.AUDIO_ICON, "audio_file.png");
    }

    public Image getImage(AvailableImage availableImage) {

        InputStream imageStream  = ImageLoader.class.getClassLoader().getResourceAsStream(getResourcePath(availableImage));

        return new Image(display, imageStream);
    }

    private String getResourcePath(AvailableImage availableImage) {
        return IMAGE_PATH + resolveFileName(availableImage);
    }

    private String resolveFileName(AvailableImage availableImage) {

        if (filePathMapping.containsKey(availableImage)) {
            return filePathMapping.get(availableImage);
        } else {
            throw new IllegalArgumentException("Unable to resolve file name for " + availableImage.name());
        }
    }
}
