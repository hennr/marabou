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

package com.github.marabou.audio;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class AudioFileFilter implements FileFilter {

    Pattern fileNamePattern = Pattern.compile(".+\\.mp3$", Pattern.CASE_INSENSITIVE);

    List<File> subFolders = new ArrayList<>();

    public boolean accept(File file) {
        if (!file.canRead()) {
            return false;
        }
        if (file.isDirectory()) {
            subFolders.add(file);
            return false;
        } else {
            return fileNamePattern.matcher(file.getName()).matches();
        }
    }

    public List<File> getSubFolders() {
        return subFolders;
    }

}
