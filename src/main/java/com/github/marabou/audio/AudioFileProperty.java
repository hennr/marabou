/**
 * Marabou - Audio Tagger
 * Copyright (C) 2012 - 2016 Jan-Hendrik Peters
 * https://github.com/hennr/marabou
 * Marabou is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package com.github.marabou.audio;

import static com.github.marabou.helper.I18nHelper.i18n;

public enum AudioFileProperty {

    Artist(i18n("Artist")),
    Title(i18n("Title")),
    Album(i18n("Album")),
    Track(i18n("Track")),
    Year(i18n("Year")),
    Genre(i18n("Genre")),
    Comments(i18n("Comments")),
    Disc_number(i18n("Disc number")),
    Composer(i18n("Composer"));

    final String labelName;

    AudioFileProperty(String labelName) {
        this.labelName = labelName;
    }

    public String getLabelName() {
        return labelName;
    }
}
