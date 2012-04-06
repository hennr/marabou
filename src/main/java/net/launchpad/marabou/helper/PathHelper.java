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

import java.io.File;
import java.io.FileNotFoundException;

/**
 * this class is intended to find the correct path for files under several
 * operating systems
 * 
 * @author Jan-Hendrik Peters
 * 
 */
public class PathHelper {

	private boolean isUnix;
	private boolean isWindows;
	private boolean isMacOS;
	/** contains ending slash under Unix or backslash under Windows */
	private String userHome;

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
	 * tries to find the correct path for a given filename in the users home folder
	 * e.g. in "~/.marabou/" under unix systems
	 * 
	 * @return the full path for the filename given
	 */
	public String getUserPathForFile(String filename) throws FileNotFoundException, UnknowPlatformException {
		if (isUnix) {
			File f = new File(userHome + ".marabou/" + filename);
			if (!f.exists() || !f.isFile()) {
				throw new FileNotFoundException();
			} else {
				return f.getAbsolutePath();
			}
		} else if (isWindows) {
			// TODO windows
			return "";
		} else if (isMacOS) {
			// TODO macos
			return "";
		} else {
			throw new UnknowPlatformException();
		}
	}
	
	/**
	 * tries to find the correct path for a given filename in a system path
	 * e.g. in "/usr/share/marabou/" or similar paths
	 * 
	 * @return the full path for the filename given
	 */
	public String getSystemPathForFile(String filename) throws FileNotFoundException, UnknowPlatformException {
		if (isUnix) {
			// TODO RELEASE
			// STUB: "src/main/resources"
			return "";
		} else if (isWindows) {
			// TODO windows
			return "";
		} else if (isMacOS) {
			// TODO macos
			return "";
		} else {
			throw new UnknowPlatformException();
		}
	}
	
	/**
	 * 
	 * @return the path to the users home folder, incl. "/" under Unix or MacOS
	 */
	public String getUsersHomeFolder() {
		if (isUnix) {
			return System.getProperty("user.home" + "/");
		} else if (isMacOS) {
			return System.getProperty("user.home" + "/");
		} else {
			return System.getProperty("user.home");
		}
	}
	
	/**
	 * returns the path to marabou's config folder in the users home
	 * including "/" at the end of the path
	 * Does not check if file exists!
	 */
	public String getMarabouHomeFolder() throws UnknowPlatformException {
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
			throw new UnknowPlatformException();
		}
	}
	
}
