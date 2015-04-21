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
package com.github.marabou.audio;

import static com.github.marabou.helper.I18nHelper._;

public enum AudioFileProperty {

    Artist(_("Artist")),
    Title(_("Title")),
    Album(_("Album")),
    Track(_("Track")),
    Year(_("Year")),
    Genre(_("Genre")),
    Comments(_("Comments")),
    Disc_number(_("Disc number")),
    Composer(_("Composer"));

    final String labelName;

    private AudioFileProperty(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelName() {
        return labelName;
    }
}
