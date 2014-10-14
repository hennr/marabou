/*
	Marabou Audio Tagger - A cross platform audio tagger using SWT
	
	Copyright (C) 2009-2010  Jan-Hendrik Peters, Markus Herpich
	
	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	      
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.marabou.gui;

import static com.github.marabou.helper.I18nHelper._;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.github.marabou.audio.AudioFileFilter;
import com.github.marabou.controller.EditorController;
import com.github.marabou.controller.MainMenuController;
import com.github.marabou.helper.*;

import com.github.marabou.properties.UserProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * This class creates and fills (if init is invoked) a menu object which will be
 * used as SWT.BAR menu in the main window.
 */
public class MainMenu {
    private final ImageLoader imageLoader;
    private Shell shell;
	private TableShell tableShell;
	private Menu menu;
    private AboutWindow aboutWindow;
    private EditorController editorController;
    private MainMenuController mainMenuController;
    private UserProperties userProperties;


    public MainMenu(Shell shell, AboutWindow aboutWindow, EditorController editorController, MainMenuController mainMenuController, UserProperties userProperties) {
        this.editorController = editorController;
        this.mainMenuController = mainMenuController;
        this.userProperties = userProperties;
        this.menu = new Menu(shell, SWT.BAR);
		this.shell = shell;
        this.imageLoader = new ImageLoader(shell.getDisplay());
        this.aboutWindow = aboutWindow;
	}

	/**
	 * sets the tableShell that is needed when invoking "open file" etc.
	 * 
	 */
	public void setTableShell(final TableShell tableShell) {
		this.tableShell = tableShell;
	}

	public Menu getMenu() {
		return menu;
	}

	final static Logger log = Logger.getLogger(MainMenu.class.getName());

	/**
	 * Fills the main menu with entries.
	 */
	public void init() {
		// File
		MenuItem file = new MenuItem(menu, SWT.CASCADE);
		file.setText(_("&File"));

		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		file.setMenu(fileMenu);

		// File -> Open File
		MenuItem openFileItem = new MenuItem(fileMenu, SWT.PUSH);
		openFileItem.setText(_("Open &file\t Ctrl+F"));

		openFileItem.setAccelerator(SWT.CTRL + 'F');
		openFileItem.setImage(imageLoader.getImage(AvailableImage.AUDIO_ICON));

		// listener for File -> open file
		openFileItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(final Event e) {
				FileDialog fileDialog = new FileDialog(shell, SWT.MULTI);
				fileDialog.setText(_("Choose file..."));

				// set the filter path, restore from the config file if possible
				fileDialog.setFilterPath(userProperties.getLastPath());

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
				// safe the last path if user wants us to
				String dirToOpen = fileDialog.getFilterPath();
				if (userProperties.rememberLastPath() && dirToOpen != null) {
					// if it's null, the user aborted the opening process
					userProperties.setLastPath(dirToOpen);
				}

				// open the selected files
				String[] filesToOpen = fileDialog.getFileNames();
				String filterPath = fileDialog.getFilterPath();
				if (filesToOpen != null) {
					for (String file: filesToOpen) {
                        mainMenuController.openFile(new File(filterPath + "/" + file));
					}
					tableShell.setKeyboardFocus();
				}
			}
		});

		// File -> open directory
		MenuItem openDirectoryItem = new MenuItem(fileMenu, SWT.PUSH);
		openDirectoryItem.setText(_("Open &directory\t Ctrl+D"));
		openDirectoryItem.setAccelerator(SWT.CTRL + 'D');

		openDirectoryItem.setImage(imageLoader.getImage(AvailableImage.FOLDER_ICON));

		// listener for File -> open directory
		openDirectoryItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {

				DirectoryDialog directoryDialog = new DirectoryDialog(shell);
				directoryDialog.setText(_("Choose directory..."));

				directoryDialog.setFilterPath(userProperties.getLastPath());
				String dirToOpen = directoryDialog.open();
				if (userProperties.rememberLastPath() && dirToOpen != null) {
					// if it's null, the user aborted the opening process
					userProperties.setLastPath(dirToOpen);
				}
				log.log(Level.INFO, "Directory to open: {0}", dirToOpen);

				if (dirToOpen != null) {
                    tableShell.setKeyboardFocus();
                    List<File> files = findFiles(new File(dirToOpen));
					mainMenuController.openFiles(files);
				}
			}
		});

		// File -> save current file
		MenuItem saveItem = new MenuItem(fileMenu, SWT.PUSH);
		saveItem.setText(_("&Save\t Ctrl+S"));
		saveItem.setAccelerator(SWT.CTRL + 'S');
		saveItem.setImage(imageLoader.getImage(AvailableImage.SAVE_ICON));

		saveItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
					editorController.saveSelectedFiles();
			}

		});

		// File -> Exit
		MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
		exitItem.setImage(imageLoader.getImage(AvailableImage.EXIT_ICON));
		exitItem.setText(_("&Exit\t Alt+F4"));

		// listener for File -> Exit
		exitItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(final Event e) {
				System.exit(0);
			}
		});

		// Help
		MenuItem help = new MenuItem(menu, SWT.CASCADE);
		help.setText(_("&Help"));

		Menu helpMenu = new Menu(shell, SWT.DROP_DOWN);
		help.setMenu(helpMenu);

		MenuItem aboutItem = new MenuItem(helpMenu, SWT.PUSH);
		aboutItem.setText(_("&About\t F1"));
		aboutItem.setAccelerator(SWT.F1);

		aboutItem.setImage(imageLoader.getImage(AvailableImage.HELP_ICON));

		// listener for Help -> About
		aboutItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(final Event e) {
				aboutWindow.showAbout();
			}
		});
	}

	// Helper methods

	/**
	 * scans a folder in a recursive way and returns found files
	 */
	public static List<File> findFiles(File dirToScan) {

        AudioFileFilter filter = new AudioFileFilter();
	    List<File> directoryContent = new ArrayList<>(Arrays.asList(dirToScan.listFiles()));

        List<File> acceptedFiles = new ArrayList<>();

        for(File file : directoryContent ) {
            if (file.isDirectory() && !Files.isSymbolicLink(file.toPath())) {
                acceptedFiles.addAll(findFiles(file));
            } else if (filter.accept(file)) {
                acceptedFiles.add(file);
            }
        }
        return acceptedFiles;
    }
}
