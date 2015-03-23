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
import static java.util.Arrays.asList;

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

    public void updateComboBoxes(Set<AudioFile> audioFiles) {
        cleanComboBoxes();
        addFilesToComboBoxes(audioFiles);
        setFirstValueAsText(comboBoxes.values());
    }

    private void setFirstValueAsText(Collection<Combo> comboBoxes) {
        for (Combo combo : comboBoxes) {
            if (combo.getItemCount() > 0) {
                combo.setText(combo.getItems()[0]);
            }
        }
    }

    private void addFilesToComboBoxes(Set<AudioFile> audioFiles) {
        for (AudioFile audioFile : audioFiles) {
            addToComboBoxIfNotPresent(comboBoxes.get(Artist), audioFile.getArtist());
            addToComboBoxIfNotPresent(comboBoxes.get(Title), audioFile.getTitle());
            addToComboBoxIfNotPresent(comboBoxes.get(Album), audioFile.getAlbum());
            addToComboBoxIfNotPresent(comboBoxes.get(Track), audioFile.getTrack());
            addToComboBoxIfNotPresent(comboBoxes.get(Year), audioFile.getYear());
            addToComboBoxIfNotPresent(comboBoxes.get(Genre), audioFile.getGenre());
            addToComboBoxIfNotPresent(comboBoxes.get(Comments), audioFile.getComment());
            addToComboBoxIfNotPresent(comboBoxes.get(Disc_number), audioFile.getDiscNumber());
            addToComboBoxIfNotPresent(comboBoxes.get(Composer), audioFile.getComposer());
        }
    }

    private void addToComboBoxIfNotPresent(Combo combo, String value) {
        if (asList(combo.getItems()).contains(value)) {
            return;
        } else if (value != null && !value.isEmpty())  {
            combo.add(value);
        }
    }

    private void cleanComboBoxes() {
        for (Combo combo : comboBoxes.values()) {
            combo.removeAll();
        }
    }
}
