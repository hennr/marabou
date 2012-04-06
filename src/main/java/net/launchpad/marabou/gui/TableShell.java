/*
	Marabou Audio Tagger - A cross platform audio tagger using SWT
	
	Copyright (C) 2009  Jan-Hendrik Peters, Markus Herpich
	
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

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

import net.launchpad.marabou.audio.AudioFileItem;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.TagException;

import com.mpatric.mp3agic.InvalidDataException;

public class TableShell {

	final Table table;
	final Shell shell;
	final Display display;
	final static HashMap<String, Integer> columnsOrder = new HashMap<String, Integer>();
	final static Logger log = Logger.getLogger(TableShell.class.getName());
	
	/**
	 * Creates a new table object
	 * @param composite the comp to hold the table
	 */
	public TableShell(Composite composite) {
		display = Display.getCurrent();
		this.shell = composite.getParent().getShell();
		// TODO let one delete and add columns(via right mouse; like aMule)
		// table
		this.table = new Table(composite, SWT.MULTI);
		this.getTable().setLinesVisible(true);
		this.getTable().setHeaderVisible(true);
		
		// fill a HashMap with the name and the index of the columns 
		columnsOrder.put("ARTIST", 0);
		columnsOrder.put("TITLE", 1);
		columnsOrder.put("ALBUM", 2);
		columnsOrder.put("DURATION", 3);
		columnsOrder.put("TRACKNUMBER", 4);
		columnsOrder.put("BITRATE", 5);
		columnsOrder.put("SAMPLERATE", 6);
		columnsOrder.put("CHANNELS", 7);
		columnsOrder.put("YEAR", 8);
		columnsOrder.put("GENRE", 9);
		columnsOrder.put("COMMENTS", 10);
		columnsOrder.put("DISC_NUMBER", 11);
		columnsOrder.put("COMPOSER", 12);
		columnsOrder.put("FILETYPE", 13);
		columnsOrder.put("PATH", 14);
		
		// get indices of table columns
		final int ARTIST 		= 	TableShell.getColumindex("ARTIST");
		final int TITLE 		=	TableShell.getColumindex("TITLE");
		final int ALBUM 		= 	TableShell.getColumindex("ALBUM");
		final int TRACKNUMBER 	=	TableShell.getColumindex("TRACKNUMBER");
		final int YEAR 			= 	TableShell.getColumindex("YEAR");
		final int GENRE 		= 	TableShell.getColumindex("GENRE");
		final int COMMENTS 		=	TableShell.getColumindex("COMMENTS");
		final int DISC_NUMBER 	=	TableShell.getColumindex("DISC_NUMBER");
		final int COMPOSER 		=	TableShell.getColumindex("COMPOSER");
		final int PATH 			=	TableShell.getColumindex("PATH");
		
		this.getTable().addSelectionListener(new SelectionListener() {
			/**
			 * adds the chosen entry to the drop dows in the tagTab
			 */
			@Override
			public void widgetSelected(final SelectionEvent e) {
				// TODO MVC clear all fields on the left side if entries from the table get removed
				// TODO this only works for one entry at a time
				TagTab.addTags(
						(getTable().getItem(getTable().getSelectionIndex()).getText(ARTIST)),
						(getTable().getItem(getTable().getSelectionIndex()).getText(TITLE)),
						(getTable().getItem(getTable().getSelectionIndex()).getText(ALBUM)),
						(getTable().getItem(getTable().getSelectionIndex()).getText(TRACKNUMBER)),
						(getTable().getItem(getTable().getSelectionIndex()).getText(YEAR)),
						(getTable().getItem(getTable().getSelectionIndex()).getText(GENRE)),
						(getTable().getItem(getTable().getSelectionIndex()).getText(COMMENTS)),
						(getTable().getItem(getTable().getSelectionIndex()).getText(DISC_NUMBER)),
						(getTable().getItem(getTable().getSelectionIndex()).getText(COMPOSER)));
			}

			// no need to handle this
			@Override
			public void widgetDefaultSelected(final SelectionEvent e) {}});

		this.getTable().addListener(SWT.MouseDoubleClick, new Listener() {
			// handles a double click on a table item
			@Override
			public void handleEvent(Event event) {
				int index = getTable().getSelectionIndex();
				String path = getTable().getItem(index).getText(PATH);

				try {
					log.info("Trying to open file with default mediaplayer: " + path);
					Desktop.getDesktop().open(new File(path));
				} catch (IOException e) {
					ErrorWindow.appendError(_("Error while opening file: ") + path);
					log.warning("Couldn't open file because of an IOException: " + path);
				} catch (UnsupportedOperationException e) {
					log.warning("awt couldn't detect the platform, so no media player can be determined.");
				}
			}
		});
		
		// create table columns
		// TODO MARKUS DRY
		// *NOTE* Add new entry also to the columsOrder HashMap!
		
//		Object[][] test = new Object[10][10];
//		test[0][0] = "Artist";
//		test[0][1] = new Integer(200);
		// TODO hier muss darauf geachtet werden das die Spalten später auch verschoben werden können
		// zudem soll diese Verschobene Ansicht auch gespeichert werden *und* es dürfen auch Splaten ausgeblendet werden
		
		// artist
		TableColumn artist = new TableColumn(this.getTable(), SWT.LEFT);
		artist.setWidth(200);
		artist.setText(_("Artist"));
		artist.setMoveable(true);
		
		// title
		TableColumn title = new TableColumn(this.getTable(), SWT.LEFT);
		title.setWidth(200);
		title.setText(_("Title"));
		title.setMoveable(true);
		
		// album
		TableColumn album = new TableColumn(this.getTable(), SWT.LEFT);
		album.setWidth(200);
		album.setText(_("Album"));
		album.setMoveable(true);
		
		// duration
		TableColumn length = new TableColumn(this.getTable(), SWT.LEFT);
		length.setWidth(60);
		length.setText(_("Length"));
		length.setMoveable(true);
		
		// tracknumber
		TableColumn track= new TableColumn(this.getTable(), SWT.LEFT);
		track.setWidth(60);
		track.setText(_("Track"));
		track.setMoveable(true);
		
		// bitrate
		TableColumn bitrate = new TableColumn(this.getTable(), SWT.LEFT);
		bitrate.setWidth(60);
		bitrate.setText(_("Bitrate"));
		bitrate.setMoveable(true);
		
		// samplerate
		TableColumn samplerate = new TableColumn(this.getTable(), SWT.LEFT);
		samplerate.setWidth(60);
		samplerate.setText(_("Samplerate"));
		samplerate.setMoveable(true);
		
		// channels
		TableColumn channels = new TableColumn(this.getTable(), SWT.LEFT);
		channels.setWidth(60);
		channels.setText(_("Channels"));
		channels.setMoveable(true);
		
		// year
		TableColumn year = new TableColumn(this.getTable(), SWT.LEFT);
		year.setWidth(60);
		year.setText(_("Year"));
		year.setMoveable(true);
		
		// genre
		TableColumn genre = new TableColumn(this.getTable(), SWT.LEFT);
		genre.setWidth(120);
		genre.setText(_("Genre"));
		genre.setMoveable(true);
		
		// comments
		TableColumn comments = new TableColumn(this.getTable(), SWT.LEFT);
		comments.setWidth(120);
		comments.setText(_("Comments"));
		comments.setMoveable(true);
		
		// disc number
		TableColumn diskNumber = new TableColumn(this.getTable(), SWT.LEFT);
		diskNumber.setWidth(60);
		diskNumber.setText(_("Disk Number"));
		diskNumber.setMoveable(true);
		
		// composer
		TableColumn composer = new TableColumn(this.getTable(), SWT.LEFT);
		composer.setWidth(120);
		composer.setText(_("Composer"));
		composer.setMoveable(true);
		
		// filetype
		TableColumn format = new TableColumn(this.getTable(), SWT.LEFT);
		format.setWidth(100);
		format.setText(_("Format"));
		format.setMoveable(true);
				
		// path
		TableColumn path = new TableColumn(this.getTable(), SWT.LEFT);
		path.setWidth(1000);
		path.setText(_("Path"));
		path.setMoveable(true);

		// setting the default sort direction
		this.getTable().setSortColumn(path);
		this.getTable().setSortDirection(SWT.DOWN);

		// TODO MVC sort the coloumns
		path.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				if (getTable().getSortDirection() == SWT.UP) {
					getTable().setSortDirection(SWT.DOWN);
				} else {
					getTable().setSortDirection(SWT.UP);
				}
			}
		});
	}

	
	// helper methods
	
	public static int getColumindex(String key) {
		return(columnsOrder.get(key));
	}
	
	public static int getColumnsCount() {
		return columnsOrder.size();
	}
	
	/**
	 * sets the keyboard focus on the table
	 */
	public void setFocus() {
		this.getTable().setFocus();
	}

	public Table getTable() {
		return table;
	}
}
