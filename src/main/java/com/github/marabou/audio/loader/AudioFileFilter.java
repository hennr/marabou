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
package com.github.marabou.audio.loader;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Pattern;

public class AudioFileFilter implements FileFilter {

    private Pattern fileNamePattern = Pattern.compile(".+\\.mp3$", Pattern.CASE_INSENSITIVE);

    public boolean accept(File file) {
        if (!file.canRead() || !file.isFile()) {
            return false;
        } else {
            return fileNamePattern.matcher(file.getName()).matches();
        }
    }
}
