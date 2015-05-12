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

import com.github.marabou.helper.UnknownGenreException;
import org.junit.Test;

import static org.junit.Assert.*;

public class GenresTest {

    @Test
    public void shouldReturnCorrectWinampResults() throws Exception {
        assertEquals("Blues", Genres.getGenreById(0));
        assertEquals("Jpop", Genres.getGenreById(146));
        assertEquals("Psybient", Genres.getGenreById(191));
    }

    @Test
    public void getsCorrectIndexForString() throws UnknownGenreException {
        assertEquals(0, Genres.getIndexForName("Blues"));
    }

    @Test(expected = UnknownGenreException.class)
    public void throwsUnknownGenreExceptionOnUnknownGenre() throws UnknownGenreException {
        Genres.getIndexForName("Bleus");
    }

    @Test(expected = UnknownGenreException.class)
    public void shouldThrowExceptionForNegativeIds() throws UnknownGenreException {
        Genres.getGenreById(-1);
    }

    @Test(expected = UnknownGenreException.class)
    public void shouldThrowExceptionForUnknownIds() throws UnknownGenreException {
        Genres.getGenreById(192);
    }
}
