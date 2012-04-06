package net.launchpad.marabou.gui;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;

import static net.launchpad.marabou.helper.I18nHelper._;

/**
 * 
 * Class to show the left tab in the main window which 
 * holds data of the selected files in drop-down lists
 *  
 *  TODO should this get realised as a singleton as well?
 *  
 * @author Jan-Hendrik Peters
 * 
 */

//TODO do it OO! why is all this crap static?
public class TagTab {
	
	// empty Combo object array that will be accessed when adding entries
	static Combo[] combos = new Combo[9];
	private final static String[] labels = {"Artist", "Title", "Album", "Track", "Year",
			"Genre", "Comments", "Disc Number", "Composer"};
	/**
	 * creates a new composite holding a 2 column grid layout with labels and drop-downs
	 *  
	 * @param tabFolder
	 *            the object which should hold the returned composite object
	 * @return a composite object with a 2 colomn GridLayout
	 */
	public static Composite init(TabFolder tabFolder) {
		
		Composite composite = new Composite(tabFolder, SWT.None);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		composite.setLayout(gridLayout);		
		
		for (int i = 0; i < labels.length; i++) {
			Label label = new Label(composite, SWT.None);
			label.setText(_(labels[i]));
			// create all drop-downs; these will be shown in the right side of the grid
			combos[i] = new Combo(composite, SWT.DROP_DOWN);
			combos[i].setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			combos[i].addFocusListener(new FocusListener() {
				
				@Override
				public void focusLost(FocusEvent arg0) {
					// TODO Auto-generated method stub
					// TODO update DB and Table 
				}
				
				@Override
				public void focusGained(FocusEvent arg0) {
					// TODO Auto-generated method stub
					
				}
			});
		}
		return composite;
	}

	/**
	 * add Strings to the drop-down lists, check 
	 * for empty Strings and for those that are already in the list
	 **/
	public static void addTags(final String artist, final String title,
			final String album, final String track, final String year,
			final String genre, final String comments, final String disc_no,
			final String composer) {
		
		if (addEntry(artist, combos[0])) {
			combos[0].setText(artist);
			combos[0].add(artist);	
		}
		if (addEntry(title, combos[1])) {
			combos[1].setText(title);
			combos[1].add(title);
		}
		if (addEntry(album, combos[2])) {
			combos[2].setText(album);
			combos[2].add(album);
		}
		if (addEntry(track, combos[3])) {
			combos[3].setText(track);
			combos[3].add(track);
		}
		if (addEntry(year, combos[4])) {
			combos[4].setText(year);
			combos[4].add(year);
		}
		if (addEntry(genre, combos[5])) {
			combos[5].setText(genre);
			combos[5].add(genre);
		}
		if (addEntry(comments, combos[6])) {
			combos[6].setText(comments);
			combos[6].add(comments);
		}
		if (addEntry(disc_no, combos[7])) {
			combos[7].setText(disc_no);
			combos[7].add(disc_no);
		}
		if (addEntry(composer, combos[8])) {
			combos[8].setText(composer);
			combos[8].add(composer);
		}
	}
	
	// helper methods
	/**
	 * checks if the given string is either empty or 
	 * already in the given Combo
	 */
	private static boolean addEntry(String newEntry, Combo c) {
		// don't add empty strings
		if (newEntry.equals("")) {
			return false;
		}
		// don't add strings twice
		String[] entries = c.getItems();
		for (String s : entries) {
			if (s.equals(newEntry)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * returns a HashMap with all sections (like genre, etc) and attributes of selected file
	 */
	public static HashMap<String,String> getTags() {
		HashMap<String,String> tags = new HashMap<String, String>();
		for (int i=0; i < combos.length; ++i) {
			tags.put(labels[i], combos[i].getText());
		}
		return tags;
	}
}
