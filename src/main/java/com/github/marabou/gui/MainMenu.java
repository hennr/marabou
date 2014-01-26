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
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import com.github.marabou.audio.AudioFileFilter;
import com.github.marabou.db.DBController;
import com.github.marabou.db.GUINotConnectedException;
import com.github.marabou.db.HSQLDBController;
import com.github.marabou.helper.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import java.util.ArrayList;
import java.util.logging.Level;

/**
 * 
 * This class creates and fills (if init is invoked) a menu object which will be
 * used as SWT.BAR menu in the main window.
 * 
 * @author Jan-Hendrik Peters
 * 
 */
public class MainMenu {
    private final ImageLoader imageLoader;
    private Shell shell;
	private TableShell tableShell;
	private Menu menu;
	private DBController controller;

	/**
	 * Constructor for the MainMenu which will be used in the main window.
	 * 
	 * @param shell
	 *            the shell which will hold the menu
	 */
	public MainMenu(final Shell shell) {
		menu = new Menu(shell, SWT.BAR);
		controller = HSQLDBController.getInstance();
		this.shell = shell;
        imageLoader = new ImageLoader(shell.getDisplay());
	}

	/**
	 * sets the tableShell that is needed when invoking "open file" etc.
	 * 
	 * @param tableShell
	 */
	public final void setTableShell(final TableShell tableShell) {
		this.tableShell = tableShell;
	}

	/**
	 * Simple getter for a Menu object used in this class.
	 * 
	 * @return returns a Menu object
	 */
	public final Menu getMenu() {
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
				String lastPath = PropertiesHelper.getProp(PropertiesAllowedKeys.lastPath);
				fileDialog.setFilterPath(lastPath);

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
				boolean safeLastPath = PropertiesHelper.getProp(PropertiesAllowedKeys.safeLastPath).equals("true");
				if (safeLastPath && dirToOpen != null) {
					// if it's null, the user aborted the opening process
					PropertiesHelper.setProp(PropertiesAllowedKeys.lastPath, dirToOpen);
				}

				// open the selected files
				String[] filesToOpen = fileDialog.getFileNames();
				String filterPath = fileDialog.getFilterPath();
				if (filesToOpen != null) {
					for (String file: filesToOpen) {
						openFile(new File(filterPath + "/" + file));
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
		// TODO IMPORTANT threading!
		// TODO progressbar
		// TODO should we add an option to open only *.xxx files from the directory?
		// TODO let the user choose to open dirs recursively or not
		// TODO make opening of multiple dirs at once possible
		// the problem is that not every platform seems to supports opening of
		// multiple dirs at once
		// we have to create our own dialog if we want that functionality :(
		// http://java-gui.info/Wiley-Professional.Java.Interfaces.with.SWT.JFace/12093/BBL0061.html

		openDirectoryItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(final Event e) {

				DirectoryDialog directoryDialog = new DirectoryDialog(shell);
				directoryDialog.setText(_("Choose directory..."));

				String lastPath = PropertiesHelper.getProp(PropertiesAllowedKeys.lastPath);
				directoryDialog.setFilterPath(lastPath);
				String dirToOpen = directoryDialog.open();
				boolean safeLastPath = PropertiesHelper.getProp(PropertiesAllowedKeys.safeLastPath).equals("true");
				if (safeLastPath && dirToOpen != null) {
					// if it's null, the user aborted the opening process
					PropertiesHelper.setProp(PropertiesAllowedKeys.lastPath, dirToOpen);
				}
				log.log(Level.INFO, "Directory to open: {0}", dirToOpen);

				if (dirToOpen != null) {
					tableShell.setKeyboardFocus();
					   ArrayList<File> files = findFiles(dirToOpen);
					openFiles(files);
				}
			}
		});

		// TODO Controller: use the HSQLDBController here
		// File -> save current file
		MenuItem saveItem = new MenuItem(fileMenu, SWT.PUSH);
		saveItem.setText(_("&Save\t Ctrl+S"));
		saveItem.setAccelerator(SWT.CTRL + 'S');
		// TODO RELEASE replace with image loader helper call
		saveItem.setImage(imageLoader.getImage(AvailableImage.SAVE_ICON));

		saveItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				try {
					controller.saveSelectedFiles();
				} catch (Exception e1) {
				}
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

		Menu helpmenu = new Menu(shell, SWT.DROP_DOWN);
		help.setMenu(helpmenu);

		MenuItem aboutItem = new MenuItem(helpmenu, SWT.PUSH);
		aboutItem.setText(_("&About\t F1"));
		aboutItem.setAccelerator(SWT.F1);

		aboutItem.setImage(imageLoader.getImage(AvailableImage.HELP_ICON));

		// listener for Help -> About
		aboutItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(final Event e) {
				// create a new about window
				AboutWindow.showAbout();
			}
		});
	}

	// Helper methods

	// TODO When Java 7 gets released, our isLink() implementation should be
	// replaced by Java's isLink()
	/**
	 * scans a folder in a recursive way and returns found files
	 * 
	 * @param dirToScan
	 *            the directory to start with
	 */
	public static ArrayList<File> findFiles(String dirToScan) {

	    File dir = new File(dirToScan);
        AudioFileFilter filter = new AudioFileFilter();
	    List<File> foundFiles = Arrays.asList(dir.listFiles(filter));

        for(File subFolder : filter.getSubFolders()) {
            foundFiles.addAll(findFiles(subFolder.getAbsolutePath()));
        }

        return removeFileSystemLinks(foundFiles);
    }

    private static ArrayList<File> removeFileSystemLinks(List<File> files) {
        ArrayList<File> locatedFiles = new ArrayList<>();
        for (File file : files) {
            if (!IsLinkHelper.isLink(file)) {
                locatedFiles.add(file);
            }
        }
        return locatedFiles;
    }

    /**
	 * Utilises the {@link HSQLDBController} to open given files
	 * Will notify the user if files fail to open
	 */
	private void openFiles(ArrayList<File> files) {
		
		for (File file: files) {
			openFile(file);
		}
	}
	
	/**
	 * Utilises the {@link HSQLDBController} to open given files.
	 * Will notify the user if files fail to open
	 */
	private void openFile(File file) {
	
		if (!isFileSupported(file)) {
			return;
		}
		try {
		    controller.insertFile(file);
		} catch (InvalidDataException | IOException | UnsupportedTagException e) {
			ErrorWindow.appendError("couldn't open file: " + file);
		}
		try {
		    controller.addAllTableItems();
		} catch (GUINotConnectedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private boolean isFileSupported(File file) {
		System.out.println(file);
		// TODO use file filter here
		return file.getName().toLowerCase().endsWith("mp3");
	}
	
}
