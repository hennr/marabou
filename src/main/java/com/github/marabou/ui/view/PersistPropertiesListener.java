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

import com.github.marabou.properties.UserProperties;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Shell;

public class PersistPropertiesListener implements DisposeListener {

    private MainWindow mainWindow;
    private UserProperties userProperties;
    private Shell shell;

    public PersistPropertiesListener(MainWindow mainWindow, UserProperties userProperties, Shell shell) {
        this.mainWindow = mainWindow;
        this.userProperties = userProperties;
        this.shell = shell;
    }

    @Override
    public void widgetDisposed(DisposeEvent e) {
        persistWindowSize();
        persistTagAndTableBarRation();
        userProperties.persistUserProperties();
    }

    private void persistWindowSize() {
        if (userProperties.rememberWindowSize()) {
            if (shell.getMaximized()) {
                userProperties.setWindowSizeX(-1);
                userProperties.setWindowSizeY(-1);
            } else {
                userProperties.setWindowSizeX(shell.getSize().x);
                userProperties.setWindowSizeY(shell.getSize().y);
            }
        }
    }

    private void persistTagAndTableBarRation() {
        userProperties.setTagBarWeight(mainWindow.sashForm.getWeights()[0]);
        userProperties.setTableWeight(mainWindow.sashForm.getWeights()[1]);
    }
}
