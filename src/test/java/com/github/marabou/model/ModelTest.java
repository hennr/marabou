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
package com.github.marabou.model;

import com.google.common.eventbus.EventBus;
import com.mpatric.mp3agic.ID3v1Tag;
import com.mpatric.mp3agic.ID3v2;
import com.mpatric.mp3agic.Mp3File;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ModelTest {

    @Test
    public void addFileCanHandleNastyReturnsInId3v1Tags() throws IOException {

        // given
        File fileMock = mock(File.class);
        when(fileMock.getCanonicalPath()).thenReturn("foo");

        final Mp3File mp3FileMock = mock(Mp3File.class);
        ID3v1Tag id3v1TagMock = mock(ID3v1Tag.class);
        when(mp3FileMock.hasId3v1Tag()).thenReturn(true);
        when(mp3FileMock.getId3v1Tag()).thenReturn(id3v1TagMock);

        when(id3v1TagMock.getArtist()).thenReturn(null);
        when(id3v1TagMock.getTitle()).thenReturn(null);
        when(id3v1TagMock.getAlbum()).thenReturn(null);
        when(id3v1TagMock.getTrack()).thenReturn(null);
        when(id3v1TagMock.getYear()).thenReturn(null);
        when(id3v1TagMock.getComment()).thenReturn(null);

        Model model = new Model(new EventBus()) {
            protected Mp3File getMp3File(File audioFile) {
                return mp3FileMock;
            }
        };

        // when
        model.addFile(fileMock);
        AudioFile audioFile = model.getAudioFileByFilePath("foo");

        // then
        assertEquals("", audioFile.getArtist());
        assertEquals("", audioFile.getTitle());
        assertEquals("", audioFile.getAlbum());
        assertEquals("", audioFile.getTrack());
        assertEquals("", audioFile.getYear());
        assertEquals("", audioFile.getComments());
    }

    @Test
    public void addFileCanHandleNastyReturnsInId3v2Tags() throws IOException {

        // given
        File fileMock = mock(File.class);
        when(fileMock.getCanonicalPath()).thenReturn("foo");

        final Mp3File mp3FileMock = mock(Mp3File.class);
        ID3v2 id3v2TagMock = mock(ID3v2.class);
        when(mp3FileMock.hasId3v2Tag()).thenReturn(true);
        when(mp3FileMock.getId3v2Tag()).thenReturn(id3v2TagMock);

        when(id3v2TagMock.getArtist()).thenReturn(null);
        when(id3v2TagMock.getTitle()).thenReturn(null);
        when(id3v2TagMock.getAlbum()).thenReturn(null);
        when(id3v2TagMock.getTrack()).thenReturn(null);
        when(id3v2TagMock.getYear()).thenReturn(null);
        when(id3v2TagMock.getComment()).thenReturn(null);

        Model model = new Model(new EventBus()) {
            protected Mp3File getMp3File(File audioFile) {
                return mp3FileMock;
            }
        };

        // when
        model.addFile(fileMock);
        AudioFile audioFile = model.getAudioFileByFilePath("foo");

        // then
        assertEquals("", audioFile.getArtist());
        assertEquals("", audioFile.getTitle());
        assertEquals("", audioFile.getAlbum());
        assertEquals("", audioFile.getTrack());
        assertEquals("", audioFile.getYear());
        assertEquals("", audioFile.getComments());
    }

    @Test
    public void getAudioFileByFilePath() throws Exception {

        // given
        final Mp3File mp3FileMock = mock(Mp3File.class);

        Model model = new Model(new EventBus()) {
            protected Mp3File getMp3File(File audioFile) {
              return mp3FileMock;
            }
        };

        File audioFile = mock(File.class);
        when(audioFile.getCanonicalPath()).thenReturn("foo");

        // when
        model.addFile(audioFile);
        AudioFile result = model.getAudioFileByFilePath("foo");

        // then
        assertNotNull(result);
    }
}