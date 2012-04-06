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

package net.launchpad.marabou.helper;

import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

/**
 * Some helper functions for audio files
 * @author Jan-Hendrik Peters
 *
 */
public class AudioFileHelper {

	// "official list" used by winamp
	private final static String[] genres = {
		"Blues",
		"Classic Rock",
		"Country",
		"Dance",
		"Disco",
		"Funk",
		"Grunge",
		"Hip-Hop",
		"Jazz",
		"Metal",
		"New Age",
		"Oldies",
		"Other",
		"Pop",
		"R&B",
		"Rap",
		"Reggae",
		"Rock",
		"Techno",
		"Industrial",
		"Alternative",
		"Ska",
		"Death Metal",
		"Pranks",
		"Soundtrack",
		"Euro-Techno",
		"Ambient",
		"Trip-Hop",
		"Vocal",
		"Jazz+Funk",
		"Fusion",
		"Trance",
		"Classical",
		"Instrumental",
		"Acid",
		"House",
		"Game",
		"Sound Clip",
		"Gospel",
		"Noise",
		"Alternative Rock",
		"Bass",
		"Soul",
		"Punk",
		"Space",
		"Meditative",
		"Instrumental Pop",
		"Instrumental Rock",
		"Ethnic",
		"Gothic",
		"Darkwave",
		"Techno-Industrial",
		"Electronic",
		"Pop-Folk",
		"Eurodance",
		"Dream",
		"Southern Rock",
		"Comedy",
		"Cult",
		"Gangsta",
		"Top ",
		"Christian Rap",
		"Pop/Funk",
		"Jungle",
		"Native US",
		"Cabaret",
		"New Wave",
		"Psychadelic",
		"Rave",
		"Showtunes",
		"Trailer",
		"Lo-Fi",
		"Tribal",
		"Acid Punk",
		"Acid Jazz",
		"Polka",
		"Retro",
		"Musical",
		"Rock & Roll",
		"Hard Rock",
		"Folk",
		"Folk-Rock",
		"National Folk",
		"Swing",
		"Fast Fusion",
		"Bebob",
		"Latin",
		"Revival",
		"Celtic",
		"Bluegrass",
		"Avantgarde",
		"Gothic Rock",
		"Progressive Rock",
		"Psychedelic Rock",
		"Symphonic Rock",
		"Slow Rock",
		"Big Band",
		"Chorus",
		"Easy Listening",
		"Acoustic",
		"Humour",
		"Speech",
		"Chanson",
		"Opera",
		"Chamber Music",
		"Sonata",
		"Symphony",
		"Booty Bass",
		"Primus",
		"Porn Groove",
		"Satire",
		"Slow Jam",
		"Club",
		"Tango",
		"Samba",
		"Folklore ",
		"Ballad",
		"Power Ballad",
		"Rhytmic Soul",
		"Freestyle",
		"Duet",
		"Punk Rock",
		"Drum Solo",
		"Acapella",
		"Euro-House",
		"Dance Hall",
		"Goa",
		"Drum & Bass",
		"Club-House",
		"Hardcore",
		"Terror",
		"Indie",
		"BritPop",
		"Negerpunk",
		"Polsk Punk",
		"Beat",
		"Christian Gangsta Rap",
		"Heavy Metal",
		"Black Metal",
		"Crossover",
		"Contemporary Christian",
		"Christian Rock ",
		"Merengue",
		"Salsa",
		"Trash Metal",
		"Anime",
		"Jpop",
		"Synthpop"
	};
	
	/**
	 * 
	 * @param id The ID saved in a music file at the genre field
	 * @return the genre as a String if it exists 
	 */
	public static String getGenre(final int id) throws UnknownGenreException {
		if (id < 0) {
			throw new UnknownGenreException();
		} else if (id < genres.length) {
			return genres[id];
		} else {
			throw new UnknownGenreException();
		}
	}
	
	/**
	 * 
	 * @param genre the genre you want an id for
	 * @return the id for the specified genre
	 * @throws UnknownGenreException if genre is not specified
	 */
	public static String getIdForGenre(String genre) throws UnknownGenreException {
		for (int i = 0; i < genres.length; i++) {
			if (genres[i].equals("genre")) {
				return Integer.toString(i);
			}
		}
		// if given genre is not in the list, throw Exception
		throw new UnknownGenreException();
	}
	
	/**
	 * 
	 * @param tag the tag of the file that we want to read
	 * @return the genre. Resolves genre IDs from 0-147
	 * or the stored string
	 */
	@Deprecated //This was used with jaudiotagger 
	public static String getGenreByID(Tag tag) {
		
		int id;
		String genre = tag.getFirst(FieldKey.GENRE);
		
		try {
		id = Integer.parseInt(genre);
		// genre is not listed in the id list but saved as a string instead
		} catch (NumberFormatException e) {
			return genre;
		}
		try {
			return getGenre(id);
		} catch (UnknownGenreException uge) {
			return genre;
		}
	}
	
	/**
	 * 
	 * @param tag the tag of the file that we want to read
	 * @return the genre. Resolves genre IDs from 0-147
	 * or the stored string
	 */
	public static String getGenreByID(int id) {
		
		String genre = "No ID yet";
		
		try {
		id = Integer.parseInt(genre);
		// genre is not listed in the id list but saved as a string instead
		} catch (NumberFormatException e) {
			return genre;
		}
		try {
			return getGenre(id);
		} catch (UnknownGenreException uge) {
			return genre;
		}
	}

	/** 
	 * converts seconds to the following format: min:secs
	 * @return min:secs as a string
	 */
	public static String calculateTrackLength(int secs) {

			// FIXME what if secs < 0?
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
}