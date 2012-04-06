package net.launchpad.marabou.db;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

import net.launchpad.marabou.gui.ErrorWindow;
import net.launchpad.marabou.gui.TagTab;
import net.launchpad.marabou.helper.AudioFileHelper;
import net.launchpad.marabou.helper.UnknownGenreException;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableItem;

import com.mpatric.mp3agic.ID3v1;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.NotSupportedException;
import com.mpatric.mp3agic.UnsupportedTagException;

/**
 * 
 * @author Markus Herpich
 * @author Jan-Hendrik Peters
 * 
 */
public final class HSQLDBController extends DBController {

	/**
	 * (static) Reference to the only instance of HSQLDBController.
	 */
	private static HSQLDBController singleton;

	/**
	 * Reference to the Database client.
	 */
	private final HSQLDBClient db;

	/**
	 * Creates the HSQLDBController instance.
	 */
	private HSQLDBController() {
		this.db = new HSQLDBClient();

	}
	
	final static Logger log = Logger.getLogger(HSQLDBController.class.getName());

	/**
	 * Get the singleton instance of HSQLDBController.
	 * 
	 * @return instance of HSQLDBController
	 */
	public static HSQLDBController getInstance() {
		if (singleton == null) {
			singleton = new HSQLDBController();
		}
		return singleton;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int insertFile(final File audioFile) {
		// TODO open files with help of executor thread pool
		int rows = -1;

		Mp3File file = null;
		ID3v1 id31Tag = null;
		ID3v2 id32Tag = null;
		boolean id31 = false;
		boolean id32 = false;
		
		try {
			file = new Mp3File(audioFile.getAbsolutePath());
			if (file.hasId3v1Tag()) {
				id31 = true;
				id31Tag = file.getId3v1Tag();
			}
			if (file.hasId3v2Tag()) {
				id32 = true;
				id32Tag = file.getId3v2Tag();
			}
		} catch (UnsupportedTagException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (InvalidDataException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO: handle exception
		}
		
		String artist = "";
		String title = "";
		String album = "";
		String trackNumber = "";
		String year = "";
		String genre = "";
		String comment = "";
		String discNo = "";
		String composer = "";

		// FIXME what if mp3agic returns null?!
		
		// if the file got an id31 tag
		if (id31) {
			// artist
			artist = id31Tag.getArtist();
			// title
			title = id31Tag.getTitle();
			// album
			album = id31Tag.getAlbum();
			// track number
			trackNumber = id31Tag.getTrack();
			// year
			year = id31Tag.getYear();
			// genre
			int g = id31Tag.getGenre();
			genre = AudioFileHelper.getGenreByID(g);
			// comments
			comment = id31Tag.getComment();
			// disc number
			discNo = "Not supported yet";
			// FIXME we are not allowed to write this to a file
			// composer
			composer = "Not supported in idv31";
		}
		
		// if the file got an id32 tag
		if (id32) {
			// artist
			artist = id32Tag.getArtist();
			// title
			title = id32Tag.getTitle();
			// album
			album = id32Tag.getAlbum();
			// track number
			trackNumber = id32Tag.getTrack();
			// year
			year = id32Tag.getYear();
			// genre
			int g = id32Tag.getGenre();
			genre = AudioFileHelper.getGenreByID(g);
			// comments
			comment = id32Tag.getComment();
			// disc number
			discNo = "Not supported yet";
			// composer
			composer = id32Tag.getComposer();
		}
		
		
		// TODO create idv32 tag if not tag is available in the file
		
		
		// get values that are not tag version specific
		
		// duration
		String duration = AudioFileHelper.calculateTrackLength((int) file.getLengthInSeconds());
		// bit rate
		String bitRate = Integer.toString(file.getBitrate());
		// sample rate
		String sampleRate = Integer.toString(file.getSampleRate());
		// channels
		String channels = file.getChannelMode();
		// encoding
		String encoding = "mp3";
		// full path to file
		String fullPath = audioFile.getAbsolutePath();

		// insert values into database
		rows = insertValues(artist, title, album, trackNumber, year, genre,
				comment, discNo, composer, duration, bitRate, sampleRate,
				channels, encoding, fullPath);
		return rows;
	}

	/**
	 * Actual insert method that also checks values.
	 * 
	 * @param artist
	 *            artist
	 * @param title
	 *            title
	 * @param album
	 *            album
	 * @param trackNumber
	 *            trackNumber
	 * @param year
	 *            year
	 * @param genre
	 * @param comment
	 * @param discNo
	 * @param composer
	 * @param duration
	 * @param bitRate
	 * @param sampleRate
	 * @param channels
	 * @param encoding
	 * @param fullPath
	 */
	private int insertValues(final String artist, final String title,
			final String album, final String trackNumber, final String year,
			final String genre, final String comment, final String discNo,
			final String composer, final String duration, final String bitRate,
			final String sampleRate, final String channels,
			final String encoding, final String fullPath) {

		int row = -1;
		PreparedStatement stmt = this.db.getPreparedInsertStatement();
		if (stmt == null) {
			return -1;
		}
		try {
			// TODO use constants for parameterIndex
			stmt.setString(1, fullPath);
			stmt.setString(2, artist);
			stmt.setString(3, title);
			stmt.setString(4, album);
			stmt.setString(5, trackNumber);
			stmt.setString(6, year);
			stmt.setString(7, genre);
			stmt.setString(8, comment);
			stmt.setString(9, discNo);
			stmt.setString(10, composer);
			stmt.setString(11, duration);
			stmt.setString(12, bitRate);
			stmt.setString(13, sampleRate);
			stmt.setString(14, channels);
			stmt.setString(15, encoding);

			row = stmt.executeUpdate();
			stmt.close();			

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return row;
	}

	/**
	 * {@inheritDoc}}
	 */
	@Override
	public int addTableItemByFilename(String filename)
			throws GUINotConnectedException {

		if (!isTableConnected()) {
			throw new GUINotConnectedException();
		}
		try {

			PreparedStatement prepStmt = this.db
					.getPreparedSelectStatement("file");
			prepStmt.setString(1, filename);

			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				setTableItemValues(rs,
						new TableItem(table.getTable(), SWT.None));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public int addAllTableItems() throws GUINotConnectedException {
		if (!isTableConnected()) {
			throw new GUINotConnectedException();
		}
		try {
			this.table.getTable().removeAll();
			// query table marabou for everything
			ResultSet rs = db.query("SELECT * FROM marabou");

			// iterate through resultset
			while (rs.next()) {
				setTableItemValues(rs,
						new TableItem(table.getTable(), SWT.None));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block add ErrorAppending stuff
			e.printStackTrace();
		}
		return -1;
	}
	
	/**
	 * TODO
	 * @param newTags
	 * @param tableItem
	 * @param multiUpdate
	 */
	private void setTableItemValues(HashMap<String, String> newTags, TableItem tableItem, boolean multiUpdate) {
		
		tableItem.setText(0, newTags.get("Artist"));
		tableItem.setText(3, newTags.get("Album"));
		
		//TODO set all tags
	}

	/**
	 * TODO
	 * @param rs
	 * @param tableItem
	 * @throws SQLException
	 */
	private void setTableItemValues(ResultSet rs, TableItem tableItem)
			throws SQLException {
		TableItem tbItem = tableItem;
		tbItem.setText(new String[] { rs.getString("artist"),
				rs.getString("title"), rs.getString("album"),
				rs.getString("length"), rs.getString("track"),
				rs.getString("bitrate"), rs.getString("samplerate"),
				rs.getString("channels"), rs.getString("year"),
				rs.getString("genre"), rs.getString("comments"),
				rs.getString("disc_number"), rs.getString("composer"),
				rs.getString("encoding"), rs.getString("file") });
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void saveFile(int index) {
		PreparedStatement stmt = this.db.getPreparedSelectStatement("id");
		try {
			stmt.setInt(1, index);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				String filename = rs.getString("file");
				
				try {
					Mp3File mf = new Mp3File(filename);
					HashMap<String,String> newTags = TagTab.getTags();
					
					if (! mf.hasId3v2Tag()) {
						// TODO how to create a new id3 tag?
					}
					// XXX we only save version 2 tags, don't we?
					ID3v2 tag = mf.getId3v2Tag();
					
					// ARTIST
					tag.setArtist(newTags.get("Artist"));
					// ALBUM
					tag.setAlbum(newTags.get("Album"));
					// TITLE
					tag.setTitle(newTags.get("Title"));
					// TRACK
					if (! newTags.get("Track").equals("")) {
						try {
							Integer track = Integer.parseInt(newTags.get("Track"));
							tag.setTrack(track.toString());
							// neither empty nor a number
						} catch (NumberFormatException nfe) {
							ErrorWindow.appendError("Couldn't save track number (only numbers allowed) " +
									"for file " + mf.getFilename());
						}
					} else {
						tag.setTrack("");
					}
					// YEAR
					tag.setYear(newTags.get("Year"));
					// GENRE
					// FIXME mp3agic does not seem to be able to save a genre as a string
					// see: https://github.com/mpatric/mp3agic/issues/6
					String genre = newTags.get("Genre");
					try {
					genre = AudioFileHelper.getIdForGenre(genre);
					tag.setGenre(Integer.parseInt(genre));
					} catch (UnknownGenreException e) {
						tag.setGenre(0);
					}
					// COMMENT
					tag.setComment(newTags.get("Comments"));
					// DISC NUMBER
					// FIXME not support yet?
					// see: https://github.com/mpatric/mp3agic/issues/7
					// tag.set...newTags.get("Disc Number");
				
					// COMPOSER
					tag.setComment(newTags.get("Composer"));
					
					// save file
					mf.save(filename);
					
					try {
						updateDBandTable();
					} catch (GUINotConnectedException e) {}
					
					
				} catch (UnsupportedTagException e) {
					ErrorWindow.appendError("Couldn't save track " + filename);
					log.warning(e.getMessage());
				} catch (InvalidDataException e) {
					ErrorWindow.appendError("Couldn't save track " + filename);
					log.warning(e.getMessage());
				} catch (NotSupportedException e) {
					ErrorWindow.appendError("Couldn't save track " + filename);
					log.warning(e.getMessage());
				} catch (IOException e) {
					ErrorWindow.appendError("Couldn't save track " + filename);
					log.warning(e.getMessage());
				}
			}
				
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean saveSelectedFiles() {
		
		TableItem[] filesToSave = table.getTable().getSelection();
		if (filesToSave.length != 0) {
			for (TableItem t : filesToSave) {
				saveFile(table.getTable().indexOf(t));
			}
		}
		return true;		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateTableItemById(final int id)
			throws GUINotConnectedException {
		if (!isTableConnected()) {
			throw new GUINotConnectedException();
		}
		try {

			PreparedStatement prepStmt = this.db
					.getPreparedSelectStatement("id");
			prepStmt.setInt(1, id);

			ResultSet rs = prepStmt.executeQuery();
			if (rs.next()) {
				TableItem tblItem = this.table.getTable().getItem(id);
				setTableItemValues(rs, tblItem);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteTableItemById(int id)
			throws GUINotConnectedException {
		// TODO Auto-generated method stub
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateDBandTable() throws GUINotConnectedException {
		
		if (!isTabFolderConnected() || !isTableConnected()) {
			throw new GUINotConnectedException();
		}
		
		boolean multiUpdate = false;
		
		// TODO make TagTab an object; 
		HashMap<String, String> newTags = TagTab.getTags();
		TableItem[] tblItems = table.getTable().getSelection();
		
		if (tblItems.length > 1) {
			multiUpdate = true;
		}
		
		for (TableItem ti : tblItems) {
			setTableItemValues(newTags, ti, multiUpdate);
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateItem(int id) throws GUINotConnectedException {
		// TODO Auto-generated method stub
		
	}
	
}
