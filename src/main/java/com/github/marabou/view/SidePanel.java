package com.github.marabou.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import static com.github.marabou.helper.I18nHelper._;

public class SidePanel {

	private final static String[] labels = {
			"Artist",
			"Title",
			"Album",
			"Track",
			"Year",
			"Genre",
			"Comments",
			"Disc Number",
			"Composer"};

	private Combo[] combos = new Combo[labels.length];


	public SidePanel(SashForm parent) {
		Composite composite = setupLayout(parent);
		setupComboBoxes(composite);
	}

	private Composite setupLayout(SashForm parent) {
		Composite composite = new Composite(parent, SWT.None);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		composite.setLayout(gridLayout);
		return composite;
	}

	private void setupComboBoxes(Composite composite) {

		for (int i = 0; i < labels.length; i++) {
			Label label = new Label(composite, SWT.None);
			label.setText(_(labels[i]));
			// create all drop-downs; these will be shown in the right side of the grid
			combos[i] = new Combo(composite, SWT.DROP_DOWN);
			combos[i].setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}
	}

	public void addNewDataSet(
			String artist,
			String title,
			String album,
			String track,
			String year,
			String genre,
			String comments,
			String disc_no,
			String composer) {

		combos[0].setText(artist);
		combos[0].add(artist);

		combos[1].setText(title);
		combos[1].add(title);

		combos[2].setText(album);
		combos[2].add(album);

		combos[3].setText(track);
		combos[3].add(track);

		combos[4].setText(year);
		combos[4].add(year);

		combos[5].setText(genre);
		combos[5].add(genre);

		combos[6].setText(comments);
		combos[6].add(comments);

		combos[7].setText(disc_no);
		combos[7].add(disc_no);

		combos[8].setText(composer);
		combos[8].add(composer);
	}
	
}
