package net.launchpad.marabou.gui;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import net.launchpad.marabou.audio.AudioFileItem;
import net.launchpad.marabou.db.DBController;
import net.launchpad.marabou.db.HSQLDBController;
import net.launchpad.marabou.helper.AudioFileHelper;
import net.launchpad.marabou.helper.UnknownGenreException;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableItem;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.CannotWriteException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

final class SaveListener implements Listener {
	
	TableShell tableShell;
	
	protected SaveListener(TableShell tblShell){
		this.tableShell = tblShell;
	}
	
	public void handleEvent(Event e) {

		TableItem[] filesToSave = tableShell.getTable().getSelection();
		
		DBController controller = HSQLDBController.getInstance();

		for (TableItem t : filesToSave) {
			String filename = t.getText(AudioFileItem.PATH);
			
			//TODO update database with current tags then save
			
			
			
			int idx = this.tableShell.getTable().indexOf(t);
			
			//controller.updateTableItemById(idx);
			
			controller.saveFile(idx);
			
			try {
				AudioFile af = AudioFileIO.read(new File(filename));
				Tag tag = af.getTag();

				HashMap<String, String> newTags = TagTab.getTags();

				tag.setField(FieldKey.ARTIST, newTags.get("Artist"));
				// ALBUM
				tag.setField(FieldKey.ALBUM, newTags.get("Album"));
				// TITLE
				tag.setField(FieldKey.TITLE, newTags.get("Title"));

				// TRACK
				if (!newTags.get("Track").equals("")) {

					try {
						Integer track = Integer.parseInt(newTags
								.get("Track"));
						tag.setField(FieldKey.TRACK, track.toString());
					} catch (NumberFormatException nfe) {
						ErrorWindow
								.appendError("Couldn't save track number (only numbers allowed) "
										+ "for file "
										+ af.getFile()
												.getAbsolutePath());
					}
					// for the case the user wants to clear this field
				} else {
					tag.deleteField(FieldKey.TRACK);
				}
				// YEAR
				tag.setField(FieldKey.YEAR, newTags.get("Year"));
				// GENRE
				String genre = newTags.get("Genre");
				// genres are saved as IDs if possible
				try {
					genre = AudioFileHelper.getIdForGenre(genre);
				} catch (UnknownGenreException uge) {
					// if assignment does work, use the old value
					// instead
				}
				tag.setField(FieldKey.GENRE, genre);
				// COMMENT
				tag.setField(FieldKey.COMMENT, newTags.get("Comments"));

				// DISC NUMBER
				if (!newTags.get("Disc Number").equals("")) {
					try {
						Integer discNo = Integer.parseInt(newTags
								.get("Disc Number"));
						tag.setField(FieldKey.DISC_NO,
								discNo.toString());

					} catch (NumberFormatException nfe) {
						ErrorWindow
								.appendError("Couldn't save disc number (only numbers allowed) "
										+ "for file "
										+ af.getFile()
												.getAbsolutePath());
					}
					// for the case that the user wants to clear this
					// field
				} else {
					tag.deleteField(FieldKey.DISC_NO);
				}
				// COMPOSER
				tag.setField(FieldKey.COMPOSER, newTags.get("Composer"));

				af.commit();

				// TODO: refresh table with new tags

			} catch (CannotReadException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (CannotWriteException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (TagException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (ReadOnlyFileException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InvalidAudioFrameException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}