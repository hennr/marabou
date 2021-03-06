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
package com.github.marabou.ui.view;

import static com.github.marabou.helper.I18nHelper.i18n;

import com.github.marabou.ui.controller.MainMenuController;
import com.github.marabou.helper.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

/**
 * This class creates and fills (if init is invoked) a menu object which will be
 * used as SWT.BAR menu in the main window.
 */
public class MainMenu extends BaseGuiClass {
    private final ImageLoader imageLoader;
    private Menu menu;
    private MainMenuController mainMenuController;


    public MainMenu(MainMenuController mainMenuController) {
        this.mainMenuController = mainMenuController;
        this.menu = new Menu(shell, SWT.BAR);
        this.imageLoader = new ImageLoader();
    }

    public Menu getMenu() {
        return menu;
    }

    public void init() {
        // File
        MenuItem file = new MenuItem(menu, SWT.CASCADE);
        file.setText(i18n("&File"));

        Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
        file.setMenu(fileMenu);

        // File -> Open File
        MenuItem openFileItem = new MenuItem(fileMenu, SWT.PUSH);
        openFileItem.setText(i18n("Open &file\t Ctrl+F"));

        openFileItem.setAccelerator(SWT.CTRL + 'F');
        openFileItem.setImage(imageLoader.getImage(AvailableImage.AUDIO_ICON));

        // listener for File -> open file
        openFileItem.addListener(SWT.Selection, event -> mainMenuController.handleOpenFileEvent());

        // File -> open directory
        MenuItem openDirectoryItem = new MenuItem(fileMenu, SWT.PUSH);
        openDirectoryItem.setText(i18n("Open &directory\t Ctrl+D"));
        openDirectoryItem.setAccelerator(SWT.CTRL + 'D');

        openDirectoryItem.setImage(imageLoader.getImage(AvailableImage.FOLDER_ICON));

        // listener for File -> open directory
        openDirectoryItem.addListener(SWT.Selection, event -> mainMenuController.handleOpenDirectoryEvent());

        // File -> save current file
        MenuItem saveItem = new MenuItem(fileMenu, SWT.PUSH);
        saveItem.setText(i18n("&Save\t Ctrl+S"));
        saveItem.setAccelerator(SWT.CTRL + 'S');
        saveItem.setImage(imageLoader.getImage(AvailableImage.SAVE_ICON));

        saveItem.addListener(SWT.Selection, event -> mainMenuController.handleSaveSelectedFilesEvent());

        // File -> Exit
        MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
        exitItem.setImage(imageLoader.getImage(AvailableImage.EXIT_ICON));
        exitItem.setText(i18n("&Exit\t Alt+F4"));

        // listener for File -> Exit
        exitItem.addListener(SWT.Selection, event -> mainMenuController.handleExitEvent(shell));

        // Help
        MenuItem help = new MenuItem(menu, SWT.CASCADE);
        help.setText(i18n("&Help"));

        Menu helpMenu = new Menu(shell, SWT.DROP_DOWN);
        help.setMenu(helpMenu);

        MenuItem aboutItem = new MenuItem(helpMenu, SWT.PUSH);
        aboutItem.setText(i18n("&About\t F1"));
        aboutItem.setAccelerator(SWT.F1);

        aboutItem.setImage(imageLoader.getImage(AvailableImage.HELP_ICON));

        // listener for Help -> About
        aboutItem.addListener(SWT.Selection, event -> mainMenuController.handleShowAboutWindow());

        // separator
        new MenuItem(menu, SWT.SEPARATOR);

        // Buttons
        MenuItem saveButton = new MenuItem(menu, SWT.PUSH);
        saveButton.setImage(imageLoader.getImage(AvailableImage.SAVE_ICON));
        saveButton.addListener(SWT.Arm, event -> mainMenuController.handleSaveSelectedFilesEvent());
    }
}
