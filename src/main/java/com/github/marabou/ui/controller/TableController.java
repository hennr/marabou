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
package com.github.marabou.ui.controller;

import com.github.marabou.audio.store.AudioFilesUpdated;
import com.github.marabou.ui.events.ErrorEvent;
import com.github.marabou.ui.events.FilesSelectedEvent;
import com.github.marabou.audio.store.AudioFileAddedEvent;
import com.github.marabou.audio.AudioFile;
import com.github.marabou.audio.store.AudioFileStore;
import com.github.marabou.ui.view.ErrorWindow;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static com.github.marabou.helper.I18nHelper._;

public class TableController {

    EventBus bus;
    Table table;
    private final AudioFileStore audioFileStore;
    public static final int TABLE_COLUMN_FILE_PATH = 14;
    final static Logger log = LoggerFactory.getLogger(TableController.class);

    public TableController(EventBus bus, Table table, AudioFileStore audioFileStore) {
        bus.register(this);
        this.bus = bus;
        this.table = table;
        this.audioFileStore = audioFileStore;

        setupTableColumns();
        addDoubleClickListener();
        addSelectionListener();
    }

    private void setupTableColumns() {
        // artist
        TableColumn artist = new TableColumn(table, SWT.LEFT);
        artist.setWidth(200);
        artist.setText(_("Artist"));
        artist.setMoveable(true);

        // title
        TableColumn title = new TableColumn(table, SWT.LEFT);
        title.setWidth(200);
        title.setText(_("Title"));
        title.setMoveable(true);

        // album
        TableColumn album = new TableColumn(table, SWT.LEFT);
        album.setWidth(200);
        album.setText(_("Album"));
        album.setMoveable(true);

        // duration
        TableColumn length = new TableColumn(table, SWT.LEFT);
        length.setWidth(60);
        length.setText(_("Length"));
        length.setMoveable(true);

        // track number
        TableColumn track= new TableColumn(table, SWT.LEFT);
        track.setWidth(60);
        track.setText(_("Track number"));
        track.setMoveable(true);

        // bit rate
        TableColumn bitRate = new TableColumn(table, SWT.LEFT);
        bitRate.setWidth(60);
        bitRate.setText(_("Bit rate"));
        bitRate.setMoveable(true);

        // sample rate
        TableColumn sampleRate = new TableColumn(table, SWT.LEFT);
        sampleRate.setWidth(60);
        sampleRate.setText(_("Samplerate"));
        sampleRate.setMoveable(true);

        // channels
        TableColumn channels = new TableColumn(table, SWT.LEFT);
        channels.setWidth(60);
        channels.setText(_("Channels"));
        channels.setMoveable(true);

        // year
        TableColumn year = new TableColumn(table, SWT.LEFT);
        year.setWidth(60);
        year.setText(_("Year"));
        year.setMoveable(true);

        // genre
        TableColumn genre = new TableColumn(table, SWT.LEFT);
        genre.setWidth(120);
        genre.setText(_("Genre"));
        genre.setMoveable(true);

        // comments
        TableColumn comments = new TableColumn(table, SWT.LEFT);
        comments.setWidth(120);
        comments.setText(_("Comments"));
        comments.setMoveable(true);

        // disc number
        TableColumn diskNumber = new TableColumn(table, SWT.LEFT);
        diskNumber.setWidth(60);
        diskNumber.setText(_("Disk number"));
        diskNumber.setMoveable(true);

        // composer
        TableColumn composer = new TableColumn(table, SWT.LEFT);
        composer.setWidth(120);
        composer.setText(_("Composer"));
        composer.setMoveable(true);

        // file type
        TableColumn format = new TableColumn(table, SWT.LEFT);
        format.setWidth(100);
        format.setText(_("Format"));
        format.setMoveable(true);

        // path
        TableColumn path = new TableColumn(table, SWT.LEFT);
        path.setWidth(1000);
        path.setText(_("Path"));
        path.setMoveable(true);
    }

    private void addDoubleClickListener() {
        table.setLinesVisible(true);
        table.setHeaderVisible(true);
        table.addListener(SWT.MouseDoubleClick, new Listener() {
            @Override
            public void handleEvent(Event event) {

                int index = table.getSelectionIndex();
                if (index == -1) {
                    return;
                }
                String path = table.getItem(index).getText(TABLE_COLUMN_FILE_PATH);

                try {
                    log.info("Trying to open file with default media player: " + path);
                    Desktop.getDesktop().open(new File(path));
                } catch (IOException e) {
                    bus.post(new ErrorEvent(_("Error while opening file: ") + path));
                    log.warn("Couldn't open file because of an IOException: " + path);
                } catch (UnsupportedOperationException e) {
                    log.warn("awt couldn't detect the platform, so no media player can be determined.");
                }
            }
        });
    }

    @Subscribe
    public void addNewFile(AudioFileAddedEvent audioFileAddedEvent) {
        updateTableItem(createNewTableItem(audioFileAddedEvent.getAudioFile()));
        table.setFocus();
    }

    private void addSelectionListener() {
        table.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {

                TableItem[] selectedItems = table.getSelection();
                Set<AudioFile> selectedAudioFiles = new HashSet<>();

                for (TableItem item : selectedItems) {
                    AudioFile audioFile = audioFileStore.getAudioFileByFilePath(item.getText(TABLE_COLUMN_FILE_PATH));
                    selectedAudioFiles.add(audioFile);
                }
                bus.post(new FilesSelectedEvent(selectedAudioFiles));
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {
            }
        });
    }

    private TableItem createNewTableItem(AudioFile audioFile) {
        TableItem tableItem = new TableItem(table, SWT.None);
        tableItem.setData(audioFile);
        return tableItem;
    }

    @Subscribe
    public void onAudioFilesUpdated(AudioFilesUpdated audioFilesUpdated) {
      updateTableItems();
    }

    private void updateTableItems() {
      for (TableItem tableItem : table.getSelection()) {
        updateTableItem(tableItem);
      }
    }

    private void updateTableItem(TableItem tableItem) {
      if (tableItem.getData() instanceof AudioFile) {
        AudioFile audioFile = (AudioFile) tableItem.getData();

        tableItem.setText(new String[] {
          audioFile.getArtist(),
          audioFile.getTitle(),
          audioFile.getAlbum(),
          audioFile.getDuration(),
          audioFile.getTrack(),
          audioFile.getBitRate(),
          audioFile.getSamplerate(),
          audioFile.getChannels(),
          audioFile.getYear(),
          audioFile.getGenre(),
          audioFile.getComment(),
          audioFile.getDiscNumber(),
          audioFile.getComposer(),
          audioFile.getEncoding(),
          audioFile.getFilePath()});
      }
    }
}
