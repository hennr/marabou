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

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * this class is written to setup the loging the way we want it to have
 * marabou uses java.util.logging beacause jaudiotagger uses it as well
 * and we didn't want to setup two different logging frameworks
 * 
 * @author Jan-Hendrik Peters
 *
 */
public class LoggingHelper {

	// possible log levels 
	// ALL > SEVERE > WARNING > INFO > CONFIG > FINE > FINER > FINEST > OFF 
	
	// root logger
	private static Logger rootLogger = Logger.getLogger("");
	// all loggers used in jaudiotagger
	private static Logger jaudioLogger = Logger.getLogger("org.jaudiotagger");
	
	/**
	 * sets up the loggers used by marabou and jaudiotagger
	 * sets loglevel to severe and enables the use of a logfile
	 */
	public static void initLogging() {

		// set the root logger to log severe messages only
		rootLogger.setLevel(Level.SEVERE);

		// Create a FileHandler to log messages into a log file
		FileHandler fh;
		PathHelper ph = new PathHelper();
		String home;
		String logFilePath = "";
		try {
			home = ph.getMarabouHomeFolder();
			logFilePath = home + "marabou.log";
		} catch (UnknowPlatformException e1) {
			System.err.println("OS couldn't get detected properly, please file a bugreport!\n" +
					"No logfile will be used.");
		}
		if (! logFilePath.equals("")) {
			try {
				// use five logfiles with 1MB in size, do not append but use a new log file for every start
				fh = new FileHandler(logFilePath, 100000, 5, false);
				fh.setFormatter(new SimpleFormatter());
				// add the FileHandler, a Console Handler will get added automagically
				rootLogger.addHandler(fh);
			} catch (SecurityException e) {
				System.err.println("Couldn't create log file under: " + logFilePath);
			} catch (IOException e) {
				System.err.println("Couldn't create log file under: " + logFilePath);
			}
		}

		// setting the loglevel for all jaudiotagger related classes
		jaudioLogger.setLevel(Level.WARNING);
	}
	
	/**
	 * sets up the loggers used by marabou and jaudiotagger
	 * sets loglevel to ALL and create logfile
	 */
	public static void initLoggingDebug() {
		initLogging();
		rootLogger.setLevel(Level.ALL);
		jaudioLogger.setLevel(Level.ALL);
	}
}