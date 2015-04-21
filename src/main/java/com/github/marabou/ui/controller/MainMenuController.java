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
package com.github.marabou.ui.controller;

import com.github.marabou.audio.loader.AudioFileFilter;
import com.github.marabou.ui.events.ErrorEvent;
import com.github.marabou.ui.events.OpenFileEvent;
import com.github.marabou.ui.events.SaveSelectedFilesEvent;
import com.github.marabou.audio.store.AudioFileStore;
import com.github.marabou.ui.view.AboutWindow;
import com.github.marabou.ui.view.ErrorWindow;
import com.github.marabou.ui.view.OpenDirectoryDialog;
import com.github.marabou.ui.view.OpenFileDialog;
import com.github.marabou.properties.UserProperties;
import com.github.marabou.audio.loader.AudioFileLoader;
import com.google.common.eventbus.EventBus;
import org.eclipse.swt.widgets.Shell;

import java.io.File;
import java.util.List;

public class MainMenuController {

    private final UserProperties userProperties;
    protected AudioFileStore audioFileStore;
    private final AudioFileLoader audioFileLoader;
    private final AboutWindow aboutWindow;
    protected EventBus bus;

    public MainMenuController(EventBus bus, AudioFileStore audioFileStore, UserProperties userProperties, AudioFileLoader audioFileLoader, AboutWindow aboutWindow) {
        this.bus = bus;
        this.audioFileStore = audioFileStore;
        this.userProperties = userProperties;
        this.audioFileLoader = audioFileLoader;
        this.aboutWindow = aboutWindow;
    }

    protected void openFiles(List<File> files) {

        for (File file : files) {
            openFile(file);
        }
    }

    protected void openFile(File file) {
        bus.post(new OpenFileEvent(file));
    }

    public void handleSaveSelectedFilesEvent() {
        bus.post(new SaveSelectedFilesEvent());
    }

    public void handleOpenDirectoryEvent() {

        OpenDirectoryDialog openDirectoryDialog = new OpenDirectoryDialog();
        String dirToOpen = openDirectoryDialog.getDirectoryToOpen(userProperties.getLastPath());

        if (userProperties.rememberLastPath() && dirToOpen != null) {
            userProperties.setLastPath(dirToOpen);
        }

        if (dirToOpen != null) {
            List<File> files = audioFileLoader.findAcceptableFilesRecursively(new File(dirToOpen));
            openFiles(files);
        }
    }

    public void handleOpenFileEvent() {

        OpenFileDialog openFileDialog = new OpenFileDialog();
        List<File> filesToOpen = openFileDialog.getFilesToOpen(userProperties.getLastPath());

        if (userProperties.rememberLastPath() && !openFileDialog.getLastPath().isEmpty()) {
            userProperties.setLastPath(openFileDialog.getLastPath());
        }

            for (File file: filesToOpen) {
                openFile(file);
            }
    }

    public void handleShowAboutWindow() {
        aboutWindow.show();
    }

    public void handleExitEvent(Shell shell) {
            shell.dispose();
    }
}
