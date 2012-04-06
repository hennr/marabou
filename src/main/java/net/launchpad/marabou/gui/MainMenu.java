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
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Logger;

import net.launchpad.marabou.audio.AudioFileFilter;
import net.launchpad.marabou.db.DBController;
import net.launchpad.marabou.db.GUINotConnectedException;
import net.launchpad.marabou.db.HSQLDBController;
import net.launchpad.marabou.helper.IsLinkHelper;
import net.launchpad.marabou.helper.PropertiesAllowedKeys;
import net.launchpad.marabou.helper.PropertiesHelper;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

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

				// set the filter path, if possible restore it from the config
				// file
				String lastPath = PropertiesHelper
						.getProp(PropertiesAllowedKeys.lastPath);
				fileDialog.setFilterPath(lastPath);

				// file endings that are supported an their respective text
				// (regexp case insensitive)
				final String[] supportedFileEndings = {
//						"*.[o|O][g|G][g|G];*.[m|M][p|P]3;*.[m|M][p|P]4;*.[f|F][l|L][a|A][c|C];*.[w|W][m|M][a|A]",
//						"*.[o|O][g|G][g|G]",
						"*.[m|M][p|P]3",
//						"*.[m|M][p|P]4",
//						"*.[f|F][l|L][a|A][c|C]",
//						"*.[w|W][m|M][a|A]",
						"*" };
				final String[] fileEndingsDesc = 
						{_("all supported audio files"),
//						"*.ogg",
						"*.mp3",
//						"*.mp4",
//						"*.flac",
//						"*.wma",
						_("all files")};
				fileDialog.setFilterExtensions(supportedFileEndings);
				// names for the filters
				fileDialog.setFilterNames(fileEndingsDesc);

				fileDialog.open();
				// safe the last path if user wishes
				String dirToOpen = fileDialog.getFilterPath();
				boolean safeLastPath = PropertiesHelper.getProp(
						PropertiesAllowedKeys.safeLastPath).equals("true");
				if (safeLastPath && dirToOpen != null) { // if it's null, the
															// user aborted the
															// opening process
					PropertiesHelper.setProp(PropertiesAllowedKeys.lastPath,
							dirToOpen);
				}

				// open the selected files
				String[] filesToOpen = fileDialog.getFileNames();
				if (filesToOpen != null) {
					String filterPath = fileDialog.getFilterPath();

					for (String file : filesToOpen) {

						int i = controller.insertFile(new File(filterPath + "/"
								+ file));
						if (i > 0) {
							try {
								// TODO get the id because filenames can change!
								controller.addTableItemByFilename(filterPath
										+ "/" + file);
							} catch (GUINotConnectedException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}

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
		// we have to create our own dialog if we want that functionality

		// http://java-gui.info/Wiley-Professional.Java.Interfaces.with.SWT.JFace/12093/BBL0061.html
		
		openDirectoryItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(final Event e) {

				DirectoryDialog directoryDialog = new DirectoryDialog(shell);
				directoryDialog.setText(_("Choose directory..."));

				String lastPath = PropertiesHelper
						.getProp(PropertiesAllowedKeys.lastPath);
				directoryDialog.setFilterPath(lastPath);
				String dirToOpen = directoryDialog.open();
				boolean safeLastPath = PropertiesHelper.getProp(
						PropertiesAllowedKeys.safeLastPath).equals("true");
				if (safeLastPath && dirToOpen != null) { // if it's null, the
															// user aborted the
															// opening process
					PropertiesHelper.setProp(PropertiesAllowedKeys.lastPath,
							dirToOpen);
				}
				log.info("Directory to open: " + dirToOpen);

				// invoke helper method to scan folder
				if (dirToOpen != null) {
					Vector<String> files = findFiles(dirToOpen);
					Iterator<String> i = files.iterator();
					while (i.hasNext()) {
						controller.insertFile(new File(i.next()));
						// tableShell.fillTable(i.next());
					}
					try {
						controller.addAllTableItems();
					} catch (GUINotConnectedException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				tableShell.setFocus();
			}
		});
		
		// TODO Controller: use the HSQLDBController here
		// File -> save current file
		MenuItem saveItem = new MenuItem(filemenu, SWT.PUSH);
		saveItem.setText(_("&Save\t Ctrl+S"));
		saveItem.setAccelerator(SWT.CTRL + 'S');
		// TODO RELEASE replace with image loader helper call
		saveItem.setImage(new Image(shell.getDisplay(), "src/main/resources/graphics/save.png"));
		
		saveItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				try {
					controller.saveSelectedFiles();
				} catch (Exception e1) {}
			}
			
		});

		// File -> Exit
		MenuItem exitItem = new MenuItem(filemenu, SWT.PUSH);
		// TODO RELEASE replace with image loader helper call
		exitItem.setImage(new Image(shell.getDisplay(), "src/main/resources/graphics/exit.png"));
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
	private static Vector<String> locatedFiles = new Vector<String>();

	public static Vector<String> findFiles(String dirToScan) {

		File dir = new File(dirToScan);

		File[] files = dir.listFiles(new AudioFileFilter());
		if (files != null && files.length != 0) {
			for (int i = 0; i < files.length; i++) {
				if (IsLinkHelper.isLink(files[i])) {
					// we ignore softlinks, see HELP
				} else {
					if (files[i].isDirectory()) {
						findFiles(files[i].toString());
					} else {
						locatedFiles.add(files[i].toString());
					}
				}
			}
		}
		return locatedFiles;
	}
}