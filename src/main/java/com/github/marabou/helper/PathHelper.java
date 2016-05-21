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

import java.io.File;

public class PathHelper {

    private final String PROPERTIES_FILE_NAME = "marabou.properties";

    public String getUserPropertiesFilePath() {
        return getMarabouHomeFolder() + PROPERTIES_FILE_NAME;
    }

    private String getMarabouHomeFolder() {
        if (isUnix()) {
            return System.getProperty("user.home") + File.separator + ".config" + File.separator + "marabou" + File.separator;
        } else if (isWindows()) {
            return System.getProperty("user.home") + File.separator + "marabou" + File.separator;
        } else if (isMacOS()) {
            return System.getProperty("user.home") + File.separator + ".marabou" + File.separator;
        } else {
            throw new RuntimeException("Your operating system couldn't get detected properly. Please file a bug report.");
        }
    }

    private boolean isUnix() {
        return
                System.getProperty("os.name").toLowerCase().contains("nix") ||
                        System.getProperty("os.name").toLowerCase().contains("nux") ||
                        System.getProperty("os.name").toLowerCase().contains("bsd");
    }

    private boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    private boolean isMacOS() {
        return System.getProperty("os.name").toLowerCase().contains("mac");
    }

    public String getUserPropertiesFileName() {
        return PROPERTIES_FILE_NAME;
    }

    public File getUserPropertiesDirectory() {
        return new File(new File(getUserPropertiesFilePath()).getParentFile().getAbsolutePath());
    }
}
