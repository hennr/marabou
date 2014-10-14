package com.github.marabou.service;

import com.github.marabou.audio.AudioFileFilter;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AudioFileService {

    private final AudioFileFilter audioFileFilter;

    public AudioFileService(AudioFileFilter audioFileFilter) {

        this.audioFileFilter = audioFileFilter;
    }

    /**
     * scans a folder in a recursive way and returns found files
     */
    public List<File> findFiles(File dirToScan) {

        List<File> directoryContent = new ArrayList<>(Arrays.asList(dirToScan.listFiles()));

        List<File> acceptedFiles = new ArrayList<>();

        for(File file : directoryContent ) {
            if (file.isDirectory() && !Files.isSymbolicLink(file.toPath())) {
                acceptedFiles.addAll(findFiles(file));
            } else if (audioFileFilter.accept(file)) {
                acceptedFiles.add(file);
            }
        }
        return acceptedFiles;
    }

}
