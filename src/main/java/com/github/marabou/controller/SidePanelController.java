package com.github.marabou.controller;

import com.github.marabou.events.FilesSelectedEvent;
import com.github.marabou.model.AudioFile;
import com.github.marabou.view.SidePanel;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import java.util.HashSet;
import java.util.Set;

public class SidePanelController {

    private final SidePanel sidePanel;

    public SidePanelController(EventBus bus, SidePanel sidePanel) {
        bus.register(this);
        this.sidePanel = sidePanel;
    }

    @Subscribe
    public void updateSidePanel(FilesSelectedEvent event) {

        Set<String> artists = new HashSet<>();
        Set<String>titles = new HashSet<>();
        Set<String>albums = new HashSet<>();
        Set<String>tracks = new HashSet<>();
        Set<String>years = new HashSet<>();
        Set<String>genres = new HashSet<>();
        Set<String>comments = new HashSet<>();
        Set<String>discNumbers = new HashSet<>();
        Set<String>composers = new HashSet<>();

        for (AudioFile audioFile : event.selectedAudioFiles) {
            artists.add(audioFile.getArtist());
            titles.add(audioFile.getTitle());
            albums.add(audioFile.getAlbum());
            tracks.add(audioFile.getTrack());
            years.add(audioFile.getYear());
            genres.add(audioFile.getGenre());
            comments.add(audioFile.getComments());
            discNumbers.add(audioFile.getDiscNumber());
            composers.add(audioFile.getComposer());
        }

        for (AudioFile audioFile : event.selectedAudioFiles) {
            sidePanel.addNewDataSet(
                    audioFile.getArtist(),
                    audioFile.getTitle(),
                    audioFile.getAlbum(),
                    audioFile.getTrack(),
                    audioFile.getYear(),
                    audioFile.getGenre(),
                    audioFile.getComments(),
                    audioFile.getDiscNumber(),
                    audioFile.getComposer()
            );
        }
    }




}
