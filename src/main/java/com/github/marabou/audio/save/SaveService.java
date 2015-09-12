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
package com.github.marabou.audio.save;

import com.google.common.io.Files;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class SaveService {

    public String saveMp3File(Mp3File mp3File) {
        try {
            File tempFile = getTempFile();
            mp3File.save(tempFile.getAbsolutePath());

            Files.copy(tempFile, new File(mp3File.getFilename()));
            tempFile.delete();
        } catch (IOException | NotSupportedException saveException) {
            throw new RuntimeException(saveException);
        }
        return mp3File.getFilename();
    }

    private File getTempFile() {
        return new File(System.getProperty("java.io.tmpdir") + File.separatorChar + UUID.randomUUID().toString());
    }
}
