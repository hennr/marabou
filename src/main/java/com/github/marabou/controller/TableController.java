package com.github.marabou.controller;

import com.github.marabou.events.NewFileEvent;
import com.github.marabou.model.AudioFile;
import com.github.marabou.view.TableShell;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableItem;

public class TableController {
    TableShell tableShell;

    public TableController(TableShell tableShell, EventBus bus) {
        this.tableShell = tableShell;
        bus.register(this);
    }

    @Subscribe
    public void addNewFile(NewFileEvent newFileEvent) {
        createNewTableItem(newFileEvent.getAudioFile());
        tableShell.setKeyboardFocus();
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
