package com.github.marabou.db;

import com.github.marabou.gui.ErrorWindow;
import com.github.marabou.gui.FileAttributeSidePanel;
import com.github.marabou.gui.TableShell;
import com.github.marabou.helper.AudioFileHelper;
import com.github.marabou.helper.UnknownGenreException;
import com.mpatric.mp3agic.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableItem;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

public class HSQLDBController {

    final static Logger log = Logger.getLogger(HSQLDBController.class.getName());

    /**
     * (static) Reference to the only instance of HSQLDBController.
     */
    private static HSQLDBController singleton;

    /**
     * Reference to the Database client.
     */
    private final HSQLDBClient db;

    FileAttributeSidePanel fileAttributeSidePanel;

    protected TableShell table;

    private HSQLDBController(FileAttributeSidePanel fileAttributeSidePanel) {
        this.db = new HSQLDBClient();
        this.fileAttributeSidePanel = fileAttributeSidePanel;
    }

    public static HSQLDBController getInstance() {
        if (singleton == null) {
            singleton = new HSQLDBController(new FileAttributeSidePanel());
        }
        return singleton;
    }

    /**
     * Insert audio file into DB.
     *
     * If you want the file to show up in the GUI you have to call addAllTableItems (or similar function) afterwards
     *
     * @throws InvalidDataException if file is not a supported file type or if it's corrupt
     */
    public int insertFile(final File audioFile) throws InvalidDataException, IOException, UnsupportedTagException {
        int rows = -1;

        Mp3File file = null;
        ID3v1 id31Tag = null;
        ID3v2 id32Tag = null;
        boolean id31 = false;
        boolean id32 = false;

        file = new Mp3File(audioFile.getAbsolutePath());
        if (file.hasId3v1Tag()) {
            id31 = true;
            id31Tag = file.getId3v1Tag();
        }
        if (file.hasId3v2Tag()) {
            id32 = true;
            id32Tag = file.getId3v2Tag();
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
            try {
                genre = AudioFileHelper.getGenre(g);
            } catch (UnknownGenreException e) {
                genre = "";
            }

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
            try {
                genre = AudioFileHelper.getGenre(g);
            } catch (UnknownGenreException e) {
                genre = "";
            }
            // comments
            comment = id32Tag.getComment();
            // disc number
            discNo = "unsupported";
            // composer
            composer = id32Tag.getComposer();
        }

        // values not tag version specific

        // duration
        String duration;
        try {
            duration = AudioFileHelper.calculateTrackLength((int) file.getLengthInSeconds());
        } catch (IllegalArgumentException e) {
            duration = "0";
        }
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

    public int addAllTableItems() {
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

    private void setTableItemValues(HashMap<String, String> newTags, TableItem tableItem) {

        tableItem.setText(0, newTags.get("Artist"));
        tableItem.setText(3, newTags.get("Album"));

        //TODO set all tags
    }

    private void setTableItemValues(ResultSet rs, TableItem tableItem) throws SQLException {
        tableItem.setText(new String[]{rs.getString("artist"),
                rs.getString("title"), rs.getString("album"),
                rs.getString("length"), rs.getString("track"),
                rs.getString("bitrate"), rs.getString("samplerate"),
                rs.getString("channels"), rs.getString("year"),
                rs.getString("genre"), rs.getString("comments"),
                rs.getString("disc_number"), rs.getString("composer"),
                rs.getString("encoding"), rs.getString("file")});
    }

    public void saveFile(int index) {
        PreparedStatement stmt = this.db.getPreparedSelectStatement("id");
        try {
            stmt.setInt(1, index);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String filename = rs.getString("file");

                try {
                    Mp3File mp3File = new Mp3File(filename);
                    HashMap<String, String> newTags = fileAttributeSidePanel.getTags();

                    if (!mp3File.hasId3v2Tag()) {
                        // TODO how to create a new id3 tag?
                    }
                    // XXX we only save version 2 tags, don't we?
                    ID3v2 tag = mp3File.getId3v2Tag();

                    // ARTIST
                    tag.setArtist(newTags.get("Artist"));
                    // ALBUM
                    tag.setAlbum(newTags.get("Album"));
                    // TITLE
                    tag.setTitle(newTags.get("Title"));
                    // TRACK
                    if (!newTags.get("Track").equals("")) {
                        try {
                            Integer track = Integer.parseInt(newTags.get("Track"));
                            tag.setTrack(track.toString());
                            // neither empty nor a number
                        } catch (NumberFormatException nfe) {
                            ErrorWindow.appendError("Couldn't save track number (only numbers allowed) "
                                    + "for file " + mp3File.getFilename());
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
                    mp3File.save(filename);

                    updateDBandTable();


                } catch (UnsupportedTagException| InvalidDataException | NotSupportedException | IOException  e) {
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
     * gets selected files from the table and saves them
     */
    public boolean saveSelectedFiles() {

        TableItem[] filesToSave = table.getTable().getSelection();
        for (TableItem t : filesToSave) {
            saveFile(table.getTable().indexOf(t));
        }
        return true;
    }

    public void updateDBandTable() {

        HashMap<String, String> newTags = fileAttributeSidePanel.getTags();
        TableItem[] tblItems = table.getTable().getSelection();

        for (TableItem ti : tblItems) {
            setTableItemValues(newTags, ti);
        }
    }

    public final void connectTableShell(TableShell tableShell) {
        table = tableShell;
    }

}
