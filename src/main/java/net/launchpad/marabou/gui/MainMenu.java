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

package net.launchpad.marabou.gui;

import static net.launchpad.marabou.helper.I18nHelper._;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.logging.Logger;

import net.launchpad.marabou.audio.AudioFileFilter;
import net.launchpad.marabou.db.DBController;
import net.launchpad.marabou.db.GUINotConnectedException;
import net.launchpad.marabou.db.HSQLDBController;
import net.launchpad.marabou.helper.IsLinkHelper;
import net.launchpad.marabou.helper.PropertiesAllowedKeys;
import net.launchpad.marabou.helper.PropertiesHelper;
import net.launchpad.marabou.helper.UnsupportedFileEndingException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

/**
 * 
 * This class creates and fills (if init is invoked) a menu object which will be
 * used as SWT.BAR menu in the main window.
 * 
 * @author Jan-Hendrik Peters
 * 
 */
public class MainMenu {
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

		Menu filemenu = new Menu(shell, SWT.DROP_DOWN);
		file.setMenu(filemenu);

		// File -> Open File
		MenuItem openFileItem = new MenuItem(filemenu, SWT.PUSH);
		openFileItem.setText(_("Open &file\t Ctrl+F"));

		openFileItem.setAccelerator(SWT.CTRL + 'F');
		// TODO RELEASE replace with image loader helper call
		openFileItem.setImage(new Image(shell.getDisplay(),
				"src/main/resources/graphics/audiofile.png"));

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
						openFile(filterPath + "/" + file);
					}
					tableShell.setFocus();
				}
			}
		});

		// File -> open directory
		MenuItem openDirectoryItem = new MenuItem(filemenu, SWT.PUSH);
		openDirectoryItem.setText(_("Open &directory\t Ctrl+D"));
		openDirectoryItem.setAccelerator(SWT.CTRL + 'D');

		// TODO RELEASE replace with image loader helper call
		openDirectoryItem.setImage(new Image(shell.getDisplay(),
				"src/main/resources/graphics/folder.png"));

		// listener for File -> open directory
		// TODO IMPORTANT threading!
		// TODO progressbar
		// TODO should we add an option to open only *.xxx files from the
		// directory?
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
				log.info("Directory to open: " + dirToOpen);

				if (dirToOpen != null) {
					tableShell.setFocus();
					Vector<String> files = findFiles(dirToOpen);
					openFiles(files);
				}
			}
		});

		// TODO Controller: use the HSQLDBController here
		// File -> save current file
		MenuItem saveItem = new MenuItem(filemenu, SWT.PUSH);
		saveItem.setText(_("&Save\t Ctrl+S"));
		saveItem.setAccelerator(SWT.CTRL + 'S');
		// TODO RELEASE replace with image loader helper call
		saveItem.setImage(new Image(shell.getDisplay(),
				"src/main/resources/graphics/save.png"));

		saveItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				try {
					controller.saveSelectedFiles();
				} catch (Exception e1) {
				}
			}

		});

		// File -> Exit
		MenuItem exitItem = new MenuItem(filemenu, SWT.PUSH);
		// TODO RELEASE replace with image loader helper call
		exitItem.setImage(new Image(shell.getDisplay(),
				"src/main/resources/graphics/exit.png"));
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

		// TODO replace with image loader helper call
		aboutItem.setImage(new Image(shell.getDisplay(),
				"src/main/resources/graphics/help.png"));

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
	public static Vector<String> findFiles(String dirToScan) {

	    File dir = new File(dirToScan);

	    File[] files = dir.listFiles(new AudioFileFilter());
	    Vector<String> locatedFiles = new Vector<>();
	    if (files != null && files.length != 0) {
		for (File f : files) {
		    if (!IsLinkHelper.isLink(f)) {
			if (f.isDirectory()) {
			    findFiles(f.toString());
			} else {
			    locatedFiles.add(f.toString());
			}
		    }
		}
	    }
	    return locatedFiles;
	}
	
	/**
	 * Utilises the {@link HSQLDBController} to open given files
	 * Will notify the user if files fail to open
	 * @param files Vector of files to open (absolute path)
	 */
	private void openFiles(Vector<String> files) {
		
		for (String file: files) {
			openFile(file);
		}
	}
	
	/**
	 * Utilises the {@link HSQLDBController} to open given files.
	 * Will notify the user if files fail to open
	 * @param file absolute path of file
	 */
	private void openFile(String file) {
		
		try {
		    isFileSupported(file);
		} catch (UnsupportedFileEndingException e) {
		    return;
		}
		try {
		    controller.insertFile(new File(file));
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

	/**
	 * Determines of given file is supported by our backend library.
	 * @param file 
	 * @return true if file ending is mp3 (lower or upper case)
	 * @throws UnsupportedFileEndingException if the file format seems to be unsupported by mp3agic
	 */
	private boolean isFileSupported(String file) throws UnsupportedFileEndingException {
		System.out.println(file);
		if (! file.toLowerCase().endsWith("mp3")) {
			throw new UnsupportedFileEndingException();
		} else {
			return true;
		}
	}
	
}