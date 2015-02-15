package com.github.marabou.audio;

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
