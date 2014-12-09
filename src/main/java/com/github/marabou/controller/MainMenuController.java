package com.github.marabou.controller;

import com.github.marabou.audio.AudioFileFilter;
import com.github.marabou.events.SaveSelectedFilesEvent;
import com.github.marabou.model.Model;
import com.github.marabou.view.AboutWindow;
import com.github.marabou.view.ErrorWindow;
import com.github.marabou.view.OpenDirectoryDialog;
import com.github.marabou.view.OpenFileDialog;
import com.github.marabou.properties.UserProperties;
import com.github.marabou.service.AudioFileService;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import com.google.common.eventbus.EventBus;
import org.eclipse.swt.widgets.Shell;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainMenuController {

    private final UserProperties userProperties;
    protected Model model;
    private final AudioFileFilter audioFileFilter;
    private final AudioFileService audioFileService;
    private final AboutWindow aboutWindow;
    protected EventBus bus;

    public MainMenuController(EventBus bus, Model model, AudioFileFilter audioFileFilter, UserProperties userProperties, AudioFileService audioFileService, AboutWindow aboutWindow) {
        this.bus = bus;
        this.model = model;
        this.audioFileFilter = audioFileFilter;
        this.userProperties = userProperties;
        this.audioFileService = audioFileService;
        this.aboutWindow = aboutWindow;
    }

    protected void openFiles(List<File> files) {

        for (File file : files) {
            openFile(file);
        }
    }

    protected void openFile(File file) {

        if (!audioFileFilter.accept(file)) {
            return;
        }
        try {
            model.addFile(file);
        } catch (InvalidDataException | IOException | UnsupportedTagException e) {
            ErrorWindow.appendError("Couldn't open file: " + file);
        }
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
            List<File> files = audioFileService.findFiles(new File(dirToOpen));
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
