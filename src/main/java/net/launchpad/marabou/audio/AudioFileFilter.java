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

package net.launchpad.marabou.audio;

import java.io.File;
import java.io.FileFilter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Checks if we support this file type.
 * @author Jan-Hendrik Peters
 */
public class AudioFileFilter implements FileFilter {

	/**
	 * checks if a given file meets criteria to be opened
	 * 
	 * criteria are:
	 * be readable and end with either mp3, ogg, flac, mp4 or wma
	 * 
	 * @return true if criteria are matched
	 */
	public boolean accept(File file) {
		if (file.canRead()) {
			try {
				if (file.isDirectory()) {
					return true;
				} else {
//					Pattern p = Pattern.compile(".+\\.mp3$|.+\\.ogg$|.+\\.flac$|.+\\.mp4$|.+\\.wma$", Pattern.CASE_INSENSITIVE);
					Pattern p = Pattern.compile(".+\\.mp3$|.+\\.ogg$|.+\\.flac$|.+\\.mp4$|.+\\.wma$", Pattern.CASE_INSENSITIVE);
					Matcher m = p.matcher(file.getName());
		
					if (m.matches()) {
						return true;
					} else {
						return false;
					}
				}
			} catch (SecurityException e) {
				return false;
			  }
		} else {
		return false;
		}
	}
}
