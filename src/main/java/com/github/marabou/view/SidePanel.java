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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

        for (AudioFile audioFile : audioFiles) {
            addNewDataSet(
                    audioFile.getArtist(),
                    audioFile.getTitle(),
                    audioFile.getAlbum(),
                    audioFile.getTrack(),
                    audioFile.getYear(),
                    audioFile.getGenre(),
                    audioFile.getComment(),
                    audioFile.getDiscNumber(),
                    audioFile.getComposer()
            );
        }
    }

    private void cleanComboBoxes() {

        for (Combo combo : comboBoxes.values()) {
            combo.removeAll();
        }

    }

    private void addNewDataSet(
            String artist,
            String title,
            String album,
            String track,
            String year,
            String genre,
            String comments,
            String disc_no,
            String composer) {

        comboBoxes.get(ComboAndLabelNames.Artist).setText(artist);
        comboBoxes.get(ComboAndLabelNames.Artist).add(artist);

        comboBoxes.get(ComboAndLabelNames.Title).setText(title);
        comboBoxes.get(ComboAndLabelNames.Title).add(title);

        comboBoxes.get(ComboAndLabelNames.Album).setText(album);
        comboBoxes.get(ComboAndLabelNames.Album).add(album);

        comboBoxes.get(ComboAndLabelNames.Track).setText(track);
        comboBoxes.get(ComboAndLabelNames.Track).add(track);

        comboBoxes.get(ComboAndLabelNames.Year).setText(year);
        comboBoxes.get(ComboAndLabelNames.Year).add(year);

        comboBoxes.get(ComboAndLabelNames.Genre).setText(genre);
        comboBoxes.get(ComboAndLabelNames.Genre).add(genre);

        comboBoxes.get(ComboAndLabelNames.Comments).setText(comments);
        comboBoxes.get(ComboAndLabelNames.Comments).add(comments);

        comboBoxes.get(ComboAndLabelNames.Disc_number).setText(disc_no);
        comboBoxes.get(ComboAndLabelNames.Disc_number).add(disc_no);

        comboBoxes.get(ComboAndLabelNames.Composer).setText(composer);
        comboBoxes.get(ComboAndLabelNames.Composer).add(composer);
    }

}
