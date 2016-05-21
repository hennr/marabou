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
package com.github.marabou.ui.controller;

import com.github.marabou.audio.store.AudioFileSavedEvent;
import com.github.marabou.ui.events.FilesSelectedEvent;
import com.github.marabou.audio.store.AudioFileAddedEvent;
import com.github.marabou.audio.AudioFile;
import com.github.marabou.audio.store.AudioFileStore;

import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.github.marabou.helper.I18nHelper.i18n;

public class TableController {

    EventBus bus;
    Table table;
    private final AudioFileStore audioFileStore;
    public static final int TABLE_COLUMN_FILE_PATH = 14;

    public TableController(EventBus bus, Table table, AudioFileStore audioFileStore) {
        this.bus = bus;
        bus.register(this);
        setupTable(table);
        this.audioFileStore = audioFileStore;
    }

    private void setupTable(Table table) {
        this.table = table;
        this.table.setLinesVisible(true);
        this.table.setHeaderVisible(true);
        setupTableColumns(table);
        addDoubleClickListener(table);
        addPressedEnterListener(table);
        addItemsSelectedListener(table);
        addRemoveItemsListener(table);
    }

    private void setupTableColumns(Table table) {
        // artist
        TableColumn artist = new TableColumn(table, SWT.LEFT);
        artist.setWidth(200);
        artist.setText(i18n("Artist"));
        artist.setMoveable(true);

        // title
        TableColumn title = new TableColumn(table, SWT.LEFT);
        title.setWidth(200);
        title.setText(i18n("Title"));
        title.setMoveable(true);

        // album
        TableColumn album = new TableColumn(table, SWT.LEFT);
        album.setWidth(200);
        album.setText(i18n("Album"));
        album.setMoveable(true);

        // duration
        TableColumn length = new TableColumn(table, SWT.LEFT);
        length.setWidth(60);
        length.setText(i18n("Length"));
        length.setMoveable(true);

        // track number
        TableColumn track = new TableColumn(table, SWT.LEFT);
        track.setWidth(60);
        track.setText(i18n("Track number"));
        track.setMoveable(true);

        // bit rate
        TableColumn bitRate = new TableColumn(table, SWT.LEFT);
        bitRate.setWidth(60);
        bitRate.setText(i18n("Bit rate"));
        bitRate.setMoveable(true);

        // sample rate
        TableColumn sampleRate = new TableColumn(table, SWT.LEFT);
        sampleRate.setWidth(60);
        sampleRate.setText(i18n("Samplerate"));
        sampleRate.setMoveable(true);

        // channels
        TableColumn channels = new TableColumn(table, SWT.LEFT);
        channels.setWidth(60);
        channels.setText(i18n("Channels"));
        channels.setMoveable(true);

        // year
        TableColumn year = new TableColumn(table, SWT.LEFT);
        year.setWidth(60);
        year.setText(i18n("Year"));
        year.setMoveable(true);

        // genre
        TableColumn genre = new TableColumn(table, SWT.LEFT);
        genre.setWidth(120);
        genre.setText(i18n("Genre"));
        genre.setMoveable(true);

        // comments
        TableColumn comments = new TableColumn(table, SWT.LEFT);
        comments.setWidth(120);
        comments.setText(i18n("Comments"));
        comments.setMoveable(true);

        // disc number
        TableColumn diskNumber = new TableColumn(table, SWT.LEFT);
        diskNumber.setWidth(60);
        diskNumber.setText(i18n("Disk number"));
        diskNumber.setMoveable(true);

        // composer
        TableColumn composer = new TableColumn(table, SWT.LEFT);
        composer.setWidth(120);
        composer.setText(i18n("Composer"));
        composer.setMoveable(true);

        // file type
        TableColumn format = new TableColumn(table, SWT.LEFT);
        format.setWidth(100);
        format.setText(i18n("Format"));
        format.setMoveable(true);

        // path
        TableColumn path = new TableColumn(table, SWT.LEFT);
        path.setWidth(1000);
        path.setText(i18n("Path"));
        path.setMoveable(true);
    }

    private void addDoubleClickListener(Table table) {
        table.addListener(SWT.MouseDoubleClick, event -> {
            int index = table.getSelectionIndex();
            if (index != -1) {
                Program.launch(table.getItem(index).getText(TABLE_COLUMN_FILE_PATH));
            }
        });
    }

    private void addPressedEnterListener(Table table) {
        table.addTraverseListener(event -> {
            if (event.detail == SWT.TRAVERSE_RETURN) {
                int index = table.getSelectionIndex();
                if (index != -1) {
                    Program.launch(table.getItem(index).getText(TABLE_COLUMN_FILE_PATH));
                }
            }
        });
    }

    private void addItemsSelectedListener(Table table) {
        table.addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {

                Set<TableItem> selectedItems = Sets.newHashSet();
                Arrays.stream(table.getSelectionIndices()).forEach(index -> selectedItems.add(table.getItem(index)));

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

    private void addRemoveItemsListener(Table table) {
        table.addListener(SWT.KeyDown, event -> {
            if (event.keyCode == SWT.DEL) {
                Arrays.stream(table.getSelection()).forEach(tableItem -> {
                    AudioFile audioFile = (AudioFile) tableItem.getData();
                    removeTableItem(audioFile.getFilePath());
                    audioFileStore.removeAudioFile(audioFile.getFilePath());
                });
                clearSidePanelEntries();
            }
        });
    }

    private void clearSidePanelEntries() {
        bus.post(new FilesSelectedEvent(Sets.newHashSet(new AudioFile(""))));
    }

    @Subscribe
    public void addNewFile(AudioFileAddedEvent audioFileAddedEvent) {
        addTableItem(audioFileAddedEvent.getAudioFile());
        table.setFocus();
    }

    @Subscribe
    public void onAudioFileSavedEvent(AudioFileSavedEvent audioFileSavedEvent) {
        removeTableItem(audioFileSavedEvent.oldFilePath);

        AudioFile audioFile = audioFileStore.getAudioFileByFilePath(audioFileSavedEvent.newFilePath);
        addTableItem(audioFile);
        correctFocusAndSidePanelAfterSaving(audioFile);
    }

    private void correctFocusAndSidePanelAfterSaving(AudioFile audioFile) {
        table.select(0);
        table.setFocus();
        bus.post(new FilesSelectedEvent(Sets.newHashSet(audioFile)));
    }

    private void removeTableItem(String filePath) {
        Arrays.stream(table.getItems()).forEach(tableItem -> {
            AudioFile audioFile = (AudioFile) tableItem.getData();
            if (audioFile.getFilePath().equals(filePath)) {
                table.remove(table.indexOf(tableItem));
            }
        });
    }

    private void addTableItem(AudioFile audioFile) {

        TableItem tableItem = createNewTableItem(audioFile);

        tableItem.setText(new String[]{
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

    private TableItem createNewTableItem(AudioFile audioFile) {
        TableItem tableItem = new TableItem(table, SWT.None);
        tableItem.setData(audioFile);
        return tableItem;
    }
}
