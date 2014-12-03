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

    @Test(expected = UnknownGenreException.class)
    public void shouldThrowExceptionForNegativeIds() throws UnknownGenreException {
        Genres.getGenreById(-1);
    }

    @Test(expected = UnknownGenreException.class)
    public void shouldThrowExceptionForUnknownIds() throws UnknownGenreException {
        Genres.getGenreById(192);
    }
}
