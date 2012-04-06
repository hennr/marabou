/*
	Marabou Audio Tagger - A cross platform audio tagger using SWT
	
	Copyright (C) 2009-2010  Jan-Hendrik Peters, Markus Herpich
	
	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	      
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.launchpad.marabou.audio;

import java.io.IOException;

import net.launchpad.marabou.gui.TableShell;
import net.launchpad.marabou.helper.AudioFileHelper;
import net.launchpad.marabou.helper.UnknownGenreException;

import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

@Deprecated
/**
 * Reads values of various audio files via the mp3agic lib
 */
public class AudioFileItem {
	
	TableItem item;
	ID3v2 tag;
	Mp3File mp3;
	
	// get indices of table columns
	public static final int ARTIST 			= 	TableShell.getColumindex("ARTIST");
	public static final int TITLE 			=	TableShell.getColumindex("TITLE");
	public static final int ALBUM 			= 	TableShell.getColumindex("ALBUM");
	public static final int DURATION 		=	TableShell.getColumindex("DURATION");
	public static final int TRACKNUMBER 	=	TableShell.getColumindex("TRACKNUMBER");
	public static final int BITRATE 		= 	TableShell.getColumindex("BITRATE");
	public static final int SAMPLERATE 		= 	TableShell.getColumindex("SAMPLERATE");
	public static final int CHANNELS 		= 	TableShell.getColumindex("CHANNELS");
	public static final int YEAR 			= 	TableShell.getColumindex("YEAR");
	public static final int GENRE 			= 	TableShell.getColumindex("GENRE");
	public static final int COMMENTS 		=	TableShell.getColumindex("COMMENTS");
	public static final int DISC_NUMBER 	=	TableShell.getColumindex("DISC_NUMBER");
	public static final int COMPOSER 		=	TableShell.getColumindex("COMPOSER");
	public static final int FILETYPE 		= 	TableShell.getColumindex("FILETYPE");
	public static final int PATH 			=	TableShell.getColumindex("PATH");
	
	/**
	 * Reads values of various audio files via the jaudiotagger lib
	 * 
	 * @param fileToOpen full path of the file to be opened
	 * @param table the table which holds the new tableItem
	 * @param style style for the tableItem
	 * @throws InvalidDataException 
	 * @throws CannotReadException
	 * @throws IOException
	 * @throws TagException
	 * @throws ReadOnlyFileException
	 * @throws InvalidAudioFrameException
	 */
	public AudioFileItem(String fileToOpen, Table table, int style) throws InvalidDataException, IOException {

		if (fileToOpen != null) {
			try {
				mp3 = new Mp3File(fileToOpen);
			} catch (UnsupportedTagException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvalidDataException e) {
				throw new InvalidDataException();
			} catch (IOException e) {
				throw new IOException();
			}
	        this.tag = mp3.getId3v2Tag();
			item = new TableItem(table, style);
		}
	}
	
	/**
	 * creates and inserts a new table item with values such as artist, bitrate, path etc.
	 */
	public void createTableItem() throws UnsupportedOperationException {
		
		String[] texts = new String[TableShell.getColumnsCount()];
		for (int i = 0; i >= texts.length; i++) {
			texts[i] = "";
		}
		
		// don't get values of files that have no tags 
		if (tag != null) {
			// artist
			texts[ARTIST] 		=  this.tag.getArtist();
			// title
			texts[TITLE] 		=  this.tag.getTitle();
			// album
			texts[ALBUM] 		=  this.tag.getAlbum();
			// tracknumber
			texts[TRACKNUMBER] 	=  	this.tag.getTrack();
			// year
			texts[YEAR] 		=  this.tag.getYear();
			// genre
			texts[GENRE] 		= getGenreByID(tag);
			// comments
			texts[COMMENTS] 	=  this.tag.getComment();
			// disc number
			// FIXME id3v2 does not specify such a frame. Anyway. id3v2 does allow to define own frames,
			// how can we check if such a frame exists in this file? 
			texts[DISC_NUMBER] 	=  new String("");
			// composer
			// FIXME this frame is specified in id3v2 but not supported by the lib it seems :(
			texts[COMPOSER] 	=  new String("");
			}
		// duration
		texts[DURATION] 	=  this.calculateTrackLength();
		// bitrate
		texts[BITRATE] 		=  Integer.toString(this.mp3.getBitrate());
		// samplerate
		texts[SAMPLERATE] 	=  Integer.toString(this.mp3.getSampleRate());
		// channels
		texts[CHANNELS] 	=  this.mp3.getChannelMode();
		// filetype
		texts[FILETYPE] 	=  ""; //this.mf.getf ncodingType();
		// full path to file
		texts[PATH] =  "";
		
		this.item.setText(texts);
	}

	// helper methods
	
	/** 
	 * converts seconds to the following format: min:secs
	 * @return min:secs as a string
	 */
	private String calculateTrackLength() {

		// FIXME use long here as well?
		int secs = (int) this.mp3.getLengthInSeconds();
		int mins = secs / 60;
		
		if (mins == 0) {
			if (secs < 10) {
				return "0:0" + secs;
			} else {
				return "0:" + secs;
			}
			// mins != 0
		} else {
			String seconds = String.valueOf(secs - (60 * mins));
			if (seconds.length() < 2) {
				seconds = "0" + seconds;
			}
			return mins + ":" + seconds;
		}
	}
	
	/**
	 * 
	 * @param tag the tag of the file that we want to read
	 * @return the genre. Resolves genre IDs from 0-147
	 * or the stored string
	 */
	private String getGenreByID(ID3v2 tag) {
		
		int id = tag.getGenre();
		
//		try {
//		id = Integer.parseInt(genre);
//		// genre is not listed in the id list but saved as a string instead
//		} catch (NumberFormatException e) {
//			return genre;
//		}
		try {
			return AudioFileHelper.getGenre(id);
		} catch (UnknownGenreException uge) {
			return tag.getGenreDescription();
		}
	}

}
