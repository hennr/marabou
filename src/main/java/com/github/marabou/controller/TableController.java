package com.github.marabou.controller;

import com.github.marabou.audio.AudioFileFilter;
import com.github.marabou.db.HSQLDBController;
import com.github.marabou.gui.ErrorWindow;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class TableController {

    private AudioFileFilter audioFileFilter;
    HSQLDBController hsqldbController = HSQLDBController.getInstance();

    public TableController(AudioFileFilter audioFileFilter) {
        this.audioFileFilter = audioFileFilter;
    }

    public void openFiles(List<File> files) {

        for (File file: files) {
            openFile(file);
        }
    }

    public void openFile(File file) {

        if (!audioFileFilter.accept(file)) {
            return;
        }
        try {
            hsqldbController.insertFile(file);
        } catch (InvalidDataException | IOException | UnsupportedTagException e) {
            ErrorWindow.appendError("couldn't open file: " + file);
        }
        hsqldbController.addAllTableItems();
    }
}
