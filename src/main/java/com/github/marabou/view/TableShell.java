package com.github.marabou.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.*;

import java.awt.*;
import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.marabou.helper.I18nHelper._;

public class TableShell extends BaseGuiClass {

	final Table table;
	public static final int TABLE_COLUMN_FILE_PATH = 14;
	final static Logger log = LoggerFactory.getLogger(TableShell.class);

	public TableShell(Composite composite) {
		this.table = new Table(composite, SWT.MULTI);
		this.getTable().setLinesVisible(true);
		this.getTable().setHeaderVisible(true);
		this.getTable().addListener(SWT.MouseDoubleClick, new Listener() {
			// handles a double click on a table item
			@Override
			public void handleEvent(Event event) {

				int index = getTable(). getSelectionIndex();
                if (index == -1) {
                    return;
                }
                String path = getTable().getItem(index).getText(TABLE_COLUMN_FILE_PATH);

				try {
					log.info("Trying to open file with default media player: " + path);
					Desktop.getDesktop().open(new File(path));
				} catch (IOException e) {
					ErrorWindow.appendError(_("Error while opening file: ") + path);
					log.warn("Couldn't open file because of an IOException: " + path);
				} catch (UnsupportedOperationException e) {
					log.warn("awt couldn't detect the platform, so no media player can be determined.");
				}
			}
		});

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

	public void setKeyboardFocus() {
		this.getTable().setFocus();
	}

	public Table getTable() {
		return table;
	}
}
