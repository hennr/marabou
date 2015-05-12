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

import org.eclipse.swt.widgets.DirectoryDialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.marabou.helper.I18nHelper._;

public class OpenDirectoryDialog extends BaseGuiClass {

    private Logger log = LoggerFactory.getLogger(OpenDirectoryDialog.class);

    public String getDirectoryToOpen(String openPath) {

        DirectoryDialog directoryDialog = new DirectoryDialog(shell);
        directoryDialog.setText(_("Choose directory..."));

        directoryDialog.setFilterPath(openPath);
        String dirToOpen = directoryDialog.open();
        log.info("Directory to open: {}", dirToOpen);

        return dirToOpen;
    }
}
