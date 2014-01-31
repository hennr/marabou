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

package com.github.marabou;

import com.github.marabou.gui.MainWindow;
import com.github.marabou.helper.LoggingHelper;
import com.github.marabou.helper.PropertiesHelper;

import java.io.IOException;
import java.util.Properties;

public class Main {
	// TODO parse passed arguments like a folder name or a file name to open it directly
	public static void main(String[] args) {


        Properties applicationProperties = PropertiesHelper.getApplicationProperties();

        MainWindow mw = new MainWindow(applicationProperties);

		// initilalise user properties, exit if it fails
		if (PropertiesHelper.initUserProperties() != 0) {
			System.err.println("Properties couldn't get initialized properly.\n" +
					"Please file a bugreport.");
			System.exit(1);
		}
		
		// initilalise logging
		boolean debug = determineDebugMode(args);
		if (debug) {
			LoggingHelper.initLoggingDebug();
		} else {
			LoggingHelper.initLogging();
		}
		
		// create the gui
		mw.init();
	}

    // helper methods
	
	// determines if marabou was started with --debug flag
	private static boolean determineDebugMode(String[] args) {
		boolean debug = false;
		for (String arg : args) {
			if (arg.equalsIgnoreCase("--debug")) {
				System.out.println("Starting marabou in debug mode.");
				debug = true;
			}
		}
		return debug;
	}
}
