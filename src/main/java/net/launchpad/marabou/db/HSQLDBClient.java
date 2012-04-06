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

package net.launchpad.marabou.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 
 * @author Markus Herpich
 * 
 */
final class HSQLDBClient {

	private Connection conn;

	protected HSQLDBClient() {
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
	 * 
	 * @param expression
	 * @return
	 * @throws SQLException
	 */
	protected synchronized ResultSet query(String expression) throws SQLException {

		Statement st = null;
		ResultSet rs = null;

		st = conn.createStatement(); // statement objects can be reused with

		// repeated calls to execute but we
		// choose to make a new one each time
		rs = st.executeQuery(expression); // run the query

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
	 * Get a {@link PreparedStatement} to securely delete data. Needed value is id.
	 * 
	 * @return {@link PreparedStatement} for insertion
	 */
	protected final PreparedStatement getPreparedDeleteStatement() {
		PreparedStatement stmt = null;
		try {
			stmt = this.conn
					.prepareStatement("DELETE FROM marabou WHERE (id) = (?)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stmt;
	}
	
	/**
	 * Get a {@link PreparedStatement} to securely update values. The order of
	 * values is file, artist,title, album, track, year, genre, comments,
	 * disc_number, composer, length, bitrate, samplerate, channels, encoding, id.
	 * 
	 * @return {@link PreparedStatement} for insertion
	 */
	protected final PreparedStatement getPreparedUpdateStatement() {
		PreparedStatement stmt = null;
		try {
			stmt = this.conn
					.prepareStatement("UPDATE marabou SET (file, artist, title, album, track, year, genre, comments, disc_number, composer, length, bitrate, samplerate, channels, encoding) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) WHERE (id) = (?)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stmt;
	}
	
	/**
	 * Get a {@link PreparedStatement} to securely select values. The order of
	 * values is file, artist,title, album, track, year, genre, comments,
	 * disc_number, composer.
	 * 
	 * @return {@link PreparedStatement} for insertion
	 */
	protected final PreparedStatement getPreparedSelectStatement(String value) {
		PreparedStatement stmt = null;
		try {
			stmt = this.conn
					.prepareStatement("SELECT * FROM marabou WHERE " + value + "=(?)");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return stmt;
	}

	/**
	 * Writing method for SQL commands CREATE, DROP, INSERT and UPDATE.
	 * 
	 * @param prepStmt {@link PreparedStatement} to write to database
 	 * @throws SQLException
	 * @return affected rows
	 */
	protected synchronized int update(final PreparedStatement prepStmt) throws SQLException {

		int i = prepStmt.executeUpdate();
		
		prepStmt.close();
		
		return i;
	}
	
	/**
	 * Writing method for SQL commands CREATE, DROP, INSERT and UPDATE.
	 * 
	 * @param expression
	 * @throws SQLException
	 */
	private synchronized void update(String expression) throws SQLException {

		Statement st = null;

		st = conn.createStatement(); 
		st.executeUpdate(expression);

		st.close();
	}

	protected void shutdown() {

		Statement st;
		try {
			st = conn.createStatement();
			st.execute("SHUTDOWN");
			st.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
