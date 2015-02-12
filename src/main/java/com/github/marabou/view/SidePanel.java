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
        Disc_Number(_("Disc Number")),
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
        for (AudioFile audioFile : audioFiles) {
            addNewDataSet(
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

        comboBoxes.get(ComboAndLabelNames.Disc_Number).setText(disc_no);
        comboBoxes.get(ComboAndLabelNames.Disc_Number).add(disc_no);

        comboBoxes.get(ComboAndLabelNames.Composer).setText(composer);
        comboBoxes.get(ComboAndLabelNames.Composer).add(composer);
    }

}
