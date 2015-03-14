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
package com.github.marabou.view;

import com.github.marabou.model.AudioFile;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import java.util.*;

import static com.github.marabou.view.SidePanel.ComboAndLabelNames.*;

import static com.github.marabou.helper.I18nHelper._;

public class SidePanel {

    Map<ComboAndLabelNames, Combo> comboBoxes = new HashMap<>();

    protected enum ComboAndLabelNames {

        Artist(_("Artist")),
        Title(_("Title")),
        Album(_("Album")),
        Track(_("Track")),
        Year(_("Year")),
        Genre(_("Genre")),
        Comments(_("Comments")),
        Disc_number(_("Disc number")),
        Composer(_("Composer"));
        
        final String labelName;

        private ComboAndLabelNames(String labelName) {
            this.labelName = labelName;
        }
    }
    
    public SidePanel(SashForm parent) {
        Composite composite = setupLayout(parent);
        createComboBoxesAndLabels(composite);
    }

    private Composite setupLayout(SashForm parent) {
        Composite composite = new Composite(parent, SWT.None);
        GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        composite.setLayout(gridLayout);
        return composite;
    }

    private void createComboBoxesAndLabels(Composite composite) {

        for (ComboAndLabelNames comboName : ComboAndLabelNames.values()) {
            Label label = new Label(composite, SWT.None);
            label.setText(comboName.labelName);

            Combo combo = new Combo(composite, SWT.DROP_DOWN);
            combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            comboBoxes.put(comboName, combo);
        }
    }

    public void updateLists(Set<AudioFile> audioFiles) {

        cleanComboBoxes();

        Set<String> artists = new TreeSet<>();
        Set<String> titles = new TreeSet<>();
        Set<String> albums = new TreeSet<>();
        Set<String> tracks = new TreeSet<>();
        Set<String> years = new TreeSet<>();
        Set<String> genres = new TreeSet<>();
        Set<String> comments = new TreeSet<>();
        Set<String> discNumbers = new TreeSet<>();
        Set<String> composers = new TreeSet<>();

        for (AudioFile audioFile : audioFiles) {

            if (!audioFile.getArtist().isEmpty()) {
                artists.add(audioFile.getArtist());
            }

            if (!audioFile.getTitle().isEmpty()) {
                titles.add(audioFile.getTitle());
            }

            if (!audioFile.getAlbum().isEmpty()) {
                albums.add(audioFile.getAlbum());
            }

            if (!audioFile.getTrack().isEmpty()) {
                tracks.add(audioFile.getTrack());
            }

            if (!audioFile.getYear().isEmpty()) {
                years.add(audioFile.getYear());
            }

            if (!audioFile.getGenre().isEmpty()) {
                genres.add(audioFile.getGenre());
            }

            if (!audioFile.getComment().isEmpty()) {
                comments.add(audioFile.getComment());
            }

            if (!audioFile.getDiscNumber().isEmpty()) {
                discNumbers.add(audioFile.getDiscNumber());
            }

            if (!audioFile.getComposer().isEmpty()) {
                composers.add(audioFile.getComposer());
            }
        }

        for (String artist : artists) {
            comboBoxes.get(Artist).setText(artist);
            comboBoxes.get(Artist).add(artist);
        }

        for (String title : titles) {
            comboBoxes.get(Title).setText(title);
            comboBoxes.get(Title).add(title);
        }

        for (String album :albums) {
            comboBoxes.get(Album).setText(album);
            comboBoxes.get(Album).add(album);
        }

        for (String track : tracks) {
            comboBoxes.get(Track).setText(track);
            comboBoxes.get(Track).add(track);
        }

        for (String year : years) {
            comboBoxes.get(Year).setText(year);
            comboBoxes.get(Year).add(year);
        }

        for (String genre : genres) {
            comboBoxes.get(Genre).setText(genre);
            comboBoxes.get(Genre).add(genre);
        }

        for (String comment : comments) {
            comboBoxes.get(Comments).setText(comment);
            comboBoxes.get(Comments).add(comment);
        }

        for (String discNumber : discNumbers) {
            comboBoxes.get(Disc_number).setText(discNumber);
            comboBoxes.get(Disc_number).add(discNumber);
        }

        for (String composer : composers) {
            comboBoxes.get(Composer).setText(composer);
            comboBoxes.get(Composer).add(composer);
        }
    }

    private void cleanComboBoxes() {

        for (Combo combo : comboBoxes.values()) {
            combo.removeAll();
        }
    }
}
