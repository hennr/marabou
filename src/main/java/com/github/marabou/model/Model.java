package com.github.marabou.model;

import com.github.marabou.events.FileOpenedEvent;
import com.github.marabou.view.FileAttributeSidePanel;
import com.github.marabou.view.TableShell;
import com.github.marabou.helper.AudioFileHelper;
import com.github.marabou.helper.UnknownGenreException;
import com.google.common.eventbus.Subscribe;
import com.mpatric.mp3agic.*;
import com.google.common.eventbus.EventBus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TableItem;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Model {

    private final EventBus bus;
    protected TableShell table;
    private HSQLDBClient dbClient;
    FileAttributeSidePanel fileAttributeSidePanel;

    public Model(EventBus bus, HSQLDBClient dbClient, FileAttributeSidePanel fileAttributeSidePanel) {
        this.bus = bus;
        this.dbClient = dbClient;
        this.fileAttributeSidePanel = fileAttributeSidePanel;
    }

    /**
     * Insert audio file into DB.
     *
     * @throws InvalidDataException if file is not a supported file type or if it's corrupt
     */
    public void insertFile(final File audioFile) throws InvalidDataException, IOException, UnsupportedTagException {
        ID3v1 id31Tag = null;
        ID3v2 id32Tag = null;
        boolean id31 = false;
        boolean id32 = false;

        Mp3File mp3File = new Mp3File(audioFile.getAbsolutePath());
        if (mp3File.hasId3v1Tag()) {
            id31 = true;
            id31Tag = mp3File.getId3v1Tag();
        }
        if (mp3File.hasId3v2Tag()) {
            id32 = true;
            id32Tag = mp3File.getId3v2Tag();
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
            discNo = "Not supported yet";
            // composer
            composer = id32Tag.getComposer();
        }

        // values not tag version specific

        // duration
        String duration;
        try {
            duration = AudioFileHelper.calculateTrackLength((int) mp3File.getLengthInSeconds());
        } catch (IllegalArgumentException e) {
            duration = "0";
        }
        // bit rate
        String bitRate = Integer.toString(mp3File.getBitrate());
        // sample rate
        String sampleRate = Integer.toString(mp3File.getSampleRate());
        // channels
        String channels = mp3File.getChannelMode();
        // encoding
        String encoding = "mp3";
        // full path to file
        String fullPath = audioFile.getAbsolutePath();

        // insert values into database
        insertValues(artist, title, album, trackNumber, year, genre,
                comment, discNo, composer, duration, bitRate, sampleRate,
                channels, encoding, fullPath);

        bus.post(new FileOpenedEvent());
    }

    private void insertValues(final String artist, final String title,
                             final String album, final String trackNumber, final String year,
                             final String genre, final String comment, final String discNo,
                             final String composer, final String duration, final String bitRate,
                             final String sampleRate, final String channels,
                             final String encoding, final String fullPath) {

        PreparedStatement stmt = this.dbClient.getPreparedInsertStatement();

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

            stmt.executeUpdate();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // TODO move to own class
    @Subscribe
    public void addAllTableItemsEventBased(FileOpenedEvent e) {
        addAllTableItems();
    }

    public void addAllTableItems() {
        try {
            this.table.getTable().removeAll();
            // query table marabou for everything
            ResultSet loadedFiles = dbClient.query("SELECT * FROM marabou");

            while (loadedFiles.next()) {
                setTableItemValues(loadedFiles, new TableItem(table.getTable(), SWT.None));
            }
            this.table.setKeyboardFocus();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public final void connectTableShell(TableShell tableShell) {
        table = tableShell;
    }

}
