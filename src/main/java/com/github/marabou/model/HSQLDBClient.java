package com.github.marabou.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HSQLDBClient {

	private Connection conn;

	public HSQLDBClient() {
		try {
			conn = DriverManager.getConnection("jdbc:hsqldb:mem:maraboudb",
					"SA", "");
			// create table
			//TODO use sane sizes
			update("CREATE TABLE marabou (id INTEGER IDENTITY, file VARCHAR(256) UNIQUE, artist VARCHAR(256), "
					+ "title VARCHAR(256), album VARCHAR(256), track VARCHAR(256), year VARCHAR(256), genre VARCHAR(256),"
					+ "comments VARCHAR(256), disc_number VARCHAR(256), composer VARCHAR(256)," 
					+ "length VARCHAR(28), bitrate VARCHAR(256), samplerate VARCHAR(256), channels VARCHAR(28)," 
					+ "encoding VARCHAR(256))");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Reading method for SQL command SELECT.
	 */
	protected synchronized ResultSet query(String expression) throws SQLException {

		Statement st = conn.createStatement(); // statement objects can be reused with

		// repeated calls to execute but we
		// choose to make a new one each time
		ResultSet rs = st.executeQuery(expression); // run the query

		// do something with the result set.

		st.close(); // NOTE!! if you close a statement the associated ResultSet
					// is

		// closed too
		// so you should copy the contents to some other object.
		// the result set is invalidated also if you recycle an Statement
		// and try to execute some other query before the result set has been
		// completely examined.

		return rs;
	}

	/**
	 * Get a {@link PreparedStatement} to securely insert values. The order of
	 * values is file, artist,title, album, track, year, genre, comments,
	 * disc_number, composer.
	 * 
	 * @return {@link PreparedStatement} for insertion
	 */
	protected final PreparedStatement getPreparedInsertStatement() {
		PreparedStatement stmt = null;
		try {
			stmt = this.conn
					.prepareStatement("INSERT INTO marabou (file, artist, title, album, track, year, genre, comments, disc_number, composer, length, bitrate, samplerate, channels, encoding) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stmt;
	}
	
	/**
	 * Writing method for SQL commands CREATE, DROP, INSERT and UPDATE.
	 */
	private synchronized void update(String expression) throws SQLException {
		Statement st = conn.createStatement();
		st.executeUpdate(expression);

		st.close();
	}
}
