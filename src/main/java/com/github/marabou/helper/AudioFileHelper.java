package com.github.marabou.helper;

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

	public static String getGenreById(final int id) throws UnknownGenreException {
		if (id < 0) {
			throw new UnknownGenreException();
		} else if (id < genres.length) {
			return genres[id];
		} else {
			throw new UnknownGenreException();
		}
	}

	/**
	 * converts seconds to the following format: min:secs
	 * @return min:secs as a string
	 */
	public static String calculateTrackLength(int secs) throws IllegalArgumentException {

        if (secs < 0) throw new IllegalArgumentException();

        int mins = secs / 60;

        if (mins == 0) {
            if (secs < 10) {
                return "0:0" + secs;
            } else {
                return "0:" + secs;
            }
        } else {
            String seconds = String.valueOf(secs - (60 * mins));
            if (seconds.length() < 2) {
                seconds = "0" + seconds;
            }
            return mins + ":" + seconds;
        }
    }
}