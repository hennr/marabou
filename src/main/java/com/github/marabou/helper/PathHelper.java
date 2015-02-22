/**
 * Marabou - Audio Tagger
 *
 * Copyright (C) 2012 - 2015 Jan-Hendrik Peters
 *
 * https://github.com/hennr/marabou
 *
 * Marabou is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package com.github.marabou.helper;

import java.io.File;

/**
 * this class is intended to find the correct path for files under several
 * operating systems
 */
public class PathHelper {

	private boolean isUnix;
	private boolean isWindows;
	private boolean isMacOS;
	/** contains ending slash under Unix or backslash under Windows */
	private String userHome;
    final String PROPERTIES_FILE_NAME = "marabou.properties";

    /**
	 * detects and remembers the OS marabou is running on
	 */
	public PathHelper() {
		// Unix
		if (System.getProperty("os.name").toLowerCase().contains("nix")
				|| System.getProperty("os.name").toLowerCase().contains("nux")
				|| System.getProperty("os.name").toLowerCase().contains("bsd")) {
			isUnix = true;
			userHome = System.getProperty("user.home") + "/";
		// Windows
		} else if (System.getProperty("os.name").toLowerCase().contains("win")) {
			isWindows = true;
			userHome = System.getProperty("user.home") + "\\";
		// MacOS
		} else if (System.getProperty("os.name").toLowerCase().contains("mac")) {
			isMacOS = true;
			userHome = System.getProperty("user.home") + "/";
		}
	}

	/**
	 * returns the path to marabou's config folder in the users home
	 * including "/" at the end of the path
	 * Does not check if file exists!
	 */
	public String getMarabouHomeFolder() {
		// Unix
		if (isUnix) {
			return userHome + ".marabou/";
		} else if (isWindows){
			// TODO windows
			return "";
		} else if (isMacOS) {
			// TODO macos
			return "";
		} else {
			throw new RuntimeException("Your operating system couldn't get detected properly. Please file a bug report.");
		}
	}

    public String getUserPropertiesFilePath() {
        return getMarabouHomeFolder() + PROPERTIES_FILE_NAME;
    }

    public String getUserPropertiesFileName() {
        return PROPERTIES_FILE_NAME;
    }

    public File getUserPropertiesDirectory() {
        return new File(new File(getUserPropertiesFilePath()).getParentFile().getAbsolutePath());
    }
}
