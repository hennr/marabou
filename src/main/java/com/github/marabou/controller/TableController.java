package com.github.marabou.controller;

import com.github.marabou.events.FilesSelectedEvent;
import com.github.marabou.events.NewFileEvent;
import com.github.marabou.model.AudioFile;
import com.github.marabou.model.Model;
import com.github.marabou.view.TableShell;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.TableItem;

import java.util.HashSet;
import java.util.Set;

public class TableController {

    EventBus bus;
    TableShell tableShell;
    private final Model model;

    public TableController(EventBus bus, Model model, TableShell tableShell) {
        bus.register(this);
        this.bus = bus;
        this.model = model;
        this.tableShell = tableShell;
        propagateTableSelectionViaEventBus();
    }

    @Subscribe
    public void addNewFile(NewFileEvent newFileEvent) {
        createNewTableItem(newFileEvent.getAudioFile());
        tableShell.setKeyboardFocus();
    }

    private void propagateTableSelectionViaEventBus() {
        tableShell.getTable().addSelectionListener(new SelectionListener() {
            @Override
            public void widgetSelected(SelectionEvent e) {

                TableItem[] selectedItems = tableShell.getTable().getSelection();
                Set<AudioFile> selectedAudioFiles = new HashSet<>();

                for (TableItem item : selectedItems) {
                    AudioFile audioFile = model.getAudioFileByFilePath(item.getText(TableShell.TABLE_COLUMN_FILE_PATH));
                    selectedAudioFiles.add(audioFile);
                }
                bus.post(new FilesSelectedEvent(selectedAudioFiles));
            }

            @Override
            public void widgetDefaultSelected(SelectionEvent e) {}
        });
    }

    private TableItem createNewTableItem(AudioFile audioFile) {
        TableItem tableItem = new TableItem(tableShell.getTable(), SWT.None);
        tableItem.setText(new String[]{
                audioFile.getArtist(),
                audioFile.getTitle(), audioFile.getAlbum(),
                audioFile.getDuration(), audioFile.getTrack(),
                audioFile.getBitrate(), audioFile.getSamplerate(),
                audioFile.getChannels(), audioFile.getYear(),
                audioFile.getGenre(), audioFile.getComments(),
                audioFile.getDiscNumber(), audioFile.getComposer(),
                audioFile.getEncoding(), audioFile.getFilePath()});
        return tableItem;
    }

}
