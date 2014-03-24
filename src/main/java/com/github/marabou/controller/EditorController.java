package com.github.marabou.controller;

import com.github.marabou.db.HSQLDBController;

public class EditorController {

    HSQLDBController hsqldbController = HSQLDBController.getInstance();

    public void saveSelectedFiles() {
        hsqldbController.saveSelectedFiles();
    }

}
