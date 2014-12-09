package com.github.marabou.view;

import com.github.marabou.events.FilesSelectedEvent;
import com.github.marabou.model.AudioFile;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import java.util.HashSet;
import java.util.Set;

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
			"Composer"
	};
	private Combo[] combos = new Combo[labels.length];

	public SidePanel(EventBus bus) {
		bus.register(this);
	}

	/**
	 * creates a new composite holding a 2 column grid layout with labels and drop-downs
	 */
	// Controller einführen und diese init methode zum konstruktor machen?
	public Composite init(SashForm sashForm) {
		
		Composite composite = new Composite(sashForm, SWT.None);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		composite.setLayout(gridLayout);		
		
		for (int i = 0; i < labels.length; i++) {
			Label label = new Label(composite, SWT.None);
			label.setText(_(labels[i]));
			// create all drop-downs; these will be shown in the right side of the grid
			combos[i] = new Combo(composite, SWT.DROP_DOWN);
			combos[i].setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		}
		return composite;
	}

	// FIXME move to SidePanelController
	@Subscribe
	public void updateSidePanel(FilesSelectedEvent event) {

		Set<String>artists = new HashSet<>();
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

		// TODO unit test
		// TODO HashSets hinzufügen

		for (AudioFile audioFile : event.selectedAudioFiles) {
			addTags(
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

	private void addTags(
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
