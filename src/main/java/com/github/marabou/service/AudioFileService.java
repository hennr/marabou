/**
 * Copyright (C) 2012 - 2014 Jan-Hendrik Peters
 *
 * This file is part of marabou.
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
package com.github.marabou.service;

import com.github.marabou.audio.AudioFileFilter;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class AudioFileService {

    Logger log = LoggerFactory.getLogger(getClass());

    private final AudioFileFilter audioFileFilter;


    public AudioFileService(AudioFileFilter audioFileFilter) {

        this.audioFileFilter = audioFileFilter;
    }

    public List<File> findAcceptableFilesRecursively(File dirToScan) {

        log.info("recursively scanning directory for compatible files: " + dirToScan.getName());

        Set<String> alreadyVisitedPaths = Sets.newHashSet();
        return findAcceptableFilesRecursively(dirToScan, alreadyVisitedPaths);
    }

    private List<File> findAcceptableFilesRecursively(File dirToScan, Set<String> alreadyVisitedPaths) {

        alreadyVisitedPaths.add(getCanonicalPath(dirToScan));

        List<File> acceptedFiles = new ArrayList<>();
        File[] foundFiles = dirToScan.listFiles();

        if (foundFiles != null) {

            List<File> directoryContent = new ArrayList<>(Arrays.asList(foundFiles));

            for (File file : directoryContent) {
                if (file.isDirectory() && !alreadyVisitedPaths.contains(getCanonicalPath(file))) {
                    acceptedFiles.addAll(findAcceptableFilesRecursively(file, alreadyVisitedPaths));
                } else if (audioFileFilter.accept(file)) {
                    acceptedFiles.add(file);
                }
            }
        }
        return acceptedFiles;
    }

    private String getCanonicalPath(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
