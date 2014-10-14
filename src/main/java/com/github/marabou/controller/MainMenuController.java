package com.github.marabou.controller;

import com.github.marabou.audio.AudioFileFilter;
import com.github.marabou.db.HSQLDBController;
import com.github.marabou.gui.AboutWindow;
import com.github.marabou.gui.ErrorWindow;
import com.github.marabou.gui.OpenDirectoryDialog;
import com.github.marabou.gui.OpenFileDialog;
import com.github.marabou.properties.UserProperties;
import com.github.marabou.service.AudioFileService;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.eclipse.swt.widgets.Shell;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainMenuController {

    private final Shell shell;
    private final UserProperties userProperties;
    private AudioFileFilter audioFileFilter;
    HSQLDBController hsqldbController = HSQLDBController.getInstance();
    private AudioFileService audioFileService;
    private AboutWindow aboutWindow;

    public MainMenuController(AudioFileFilter audioFileFilter, Shell shell, UserProperties userProperties, AudioFileService audioFileService, AboutWindow aboutWindow) {
        this.shell = shell;
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
            hsqldbController.insertFile(file);
        } catch (InvalidDataException | IOException | UnsupportedTagException e) {
            ErrorWindow.appendError("Couldn't open file: " + file);
        }
        hsqldbController.addAllTableItems();
    }

    public void handleOpenDirectoryEvent() {

        OpenDirectoryDialog openDirectoryDialog = new OpenDirectoryDialog(shell);
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

        OpenFileDialog openFileDialog = new OpenFileDialog(shell);
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
}
