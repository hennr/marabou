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
package com.github.marabou.ui.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.marabou.helper.I18nHelper._;

public class OpenFileDialog extends BaseGuiClass {

    private Logger log = LoggerFactory.getLogger(OpenFileDialog.class);
    private String lastPath = "";

    public List<File> getFilesToOpen(String openPath) {

        ArrayList<File> filesToOpen = new ArrayList<>();

        FileDialog fileDialog = new FileDialog(shell, SWT.MULTI);
        fileDialog.setText(_("Choose file..."));
        fileDialog.setFilterPath(openPath);

        /**
         * currently supported file endings, text for GUI
         */
        final String[] supportedFileEndingsDesc = {
                _("all supported audio files"),
                "*.mp3",
                _("all files") };

        /**
         *  currently supported file endings
         */
        final String[] supportedFileEndings = {
                "*.[m|M][p|P]3",
                "*.[m|M][p|P]3",
                "*" };

        fileDialog.setFilterExtensions(supportedFileEndings);
        fileDialog.setFilterNames(supportedFileEndingsDesc);

        fileDialog.open();
        lastPath = fileDialog.getFilterPath();

        for (String file : fileDialog.getFileNames()) {
            File f = new File (lastPath + "/" + file);
            filesToOpen.add(f);
        }

        log.info("Files to open: {}", filesToOpen.toString());

        return filesToOpen;
    }

    public String getLastPath() {
        return lastPath;
    }
}
