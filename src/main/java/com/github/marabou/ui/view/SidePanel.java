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
package com.github.marabou.ui.view;

import com.github.marabou.helper.Constants;
import com.github.marabou.ui.controller.SidePanelController;
import com.github.marabou.ui.events.ComboPropertyChange;
import com.github.marabou.audio.AudioFile;
import com.github.marabou.audio.AudioFileProperty;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import java.util.*;

import static com.github.marabou.audio.AudioFileProperty.*;

import static java.util.Arrays.asList;

public class SidePanel {

    Map<AudioFileProperty, Combo> comboBoxes = new HashMap<>();
    private SidePanelController controller;

    public SidePanel withController(SidePanelController controller) {
        this.controller = controller;
        return this;
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

        for (AudioFileProperty property : AudioFileProperty.values()) {
            Label label = new Label(composite, SWT.None);
            label.setText(property.getLabelName());

            Combo combo = new Combo(composite, SWT.DROP_DOWN);
            combo.setData(property);
            combo.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
            comboBoxes.put(property, combo);

            combo.addModifyListener(addOnModifyListener());
        }
    }

    private ModifyListener addOnModifyListener() {
        return (event -> {
            Combo combo = (Combo) event.getSource();
            AudioFileProperty property = (AudioFileProperty) combo.getData();

            controller.onPropertyChange(new ComboPropertyChange(property, combo.getText()));
        });
    }

    public void updateComboBoxes(Set<AudioFile> audioFiles) {
        cleanComboBoxes();
        addFilesToComboBoxes(audioFiles);
        addDummyDropDownEntries(audioFiles);
        makeFirstEntryVisible(comboBoxes.values());
    }

    private void cleanComboBoxes() {
        comboBoxes.values().forEach(Combo::removeAll);
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
        if (!value.isEmpty() && !asList(combo.getItems()).contains(value)) {
            combo.add(value);
        }
    }

    private void addDummyDropDownEntries(Set<AudioFile> audioFiles) {
        if (audioFiles.size() > 1) {
            comboBoxes.get(Title).add(Constants.IGNORE_THIS_WHEN_SAVING, 0);
            comboBoxes.get(Track).add(Constants.IGNORE_THIS_WHEN_SAVING, 0);
            comboBoxes.get(Comments).add(Constants.IGNORE_THIS_WHEN_SAVING);
            comboBoxes.get(Disc_number).add(Constants.IGNORE_THIS_WHEN_SAVING);
        }
    }

    private void makeFirstEntryVisible(Collection<Combo> comboBoxes) {
        comboBoxes.stream().forEach(combo -> combo.select(0));
    }
}
