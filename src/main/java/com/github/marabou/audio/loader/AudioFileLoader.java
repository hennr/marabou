/**
 * Marabou - Audio Tagger
 * Copyright (C) 2012 - 2016 Jan-Hendrik Peters
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
package com.github.marabou.audio.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.github.marabou.ui.events.OpenFileEvent;
import com.google.common.eventbus.EventBus;

import static java.util.Arrays.asList;

public class AudioFileLoader {

    Logger log = LoggerFactory.getLogger(AudioFileLoader.class);

    private final AudioFileFilter audioFileFilter;
    private final EventBus bus;

    public AudioFileLoader(AudioFileFilter audioFileFilter, EventBus bus) {
        this.audioFileFilter = audioFileFilter;
        this.bus = bus;
    }

    public void openDirectory(File directory) {
        log.info("recursively scanning directory for compatible files: " + directory.getName());

        List<File> files = findAcceptableFilesRecursively(directory, new HashSet<>());
        files.forEach(this::openFile);
    }

    public void openFile(File file) {
        bus.post(new OpenFileEvent(file));
    }

    private List<File> findAcceptableFilesRecursively(File dirToScan, Set<String> alreadyVisitedPaths) {
        alreadyVisitedPaths.add(getCanonicalPath(dirToScan));

        List<File> acceptedFiles = new ArrayList<>();
        for (File file : getFilesInDirectory(dirToScan)) {
            if (file.isDirectory() && !alreadyVisitedPaths.contains(getCanonicalPath(file))) {
                acceptedFiles.addAll(findAcceptableFilesRecursively(file, alreadyVisitedPaths));
            } else if (audioFileFilter.accept(file)) {
                acceptedFiles.add(file);
            }
        }
        return acceptedFiles;
    }

    private List<File> getFilesInDirectory(File dirToScan) {
        File[] filesInDir = dirToScan.listFiles();
        if (filesInDir == null) {
            return Collections.emptyList();
        } else {
            return asList(filesInDir);
        }
    }

    private String getCanonicalPath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
