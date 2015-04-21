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
package com.github.marabou.ui.view;

import com.github.marabou.ui.controller.SidePanelController;
import com.github.marabou.audio.AudioFile;
import com.github.marabou.audio.AudioFileProperty;
import com.google.common.collect.Sets;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static com.github.marabou.audio.AudioFileProperty.*;
import static org.mockito.Mockito.mock;

public class SidePanelTest {

    @Test
    public void holdsAllGivenEntriesInTheCombosButNoDuplicates() {

        // given
        SidePanel sidePanel = aSidePanelWithSwtMocked();

        String trackNumber = "1";
        String discNumber = "1";

        AudioFile audioFile = aValidCompleteAudioFile().withTrack(trackNumber).withDiscNumber(discNumber);
        AudioFile anotherAudioFile = anotherValidCompleteAudioFile().withTrack(trackNumber).withDiscNumber(discNumber);
        AudioFile yetAnotherAudioFile = anotherValidCompleteAudioFile().withTrack(trackNumber).withDiscNumber(discNumber);

        // when
        sidePanel.updateComboBoxes(Sets.newHashSet(audioFile, anotherAudioFile, yetAnotherAudioFile));

        // then
        assertEquals(2, sidePanel.comboBoxes.get(Artist).getItemCount());
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(Artist).getItems()).contains(audioFile.getArtist()));
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(Artist).getItems()).contains(anotherAudioFile.getArtist()));

        assertEquals(2, sidePanel.comboBoxes.get(Title).getItemCount());
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(Title).getItems()).contains(audioFile.getTitle()));
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(Title).getItems()).contains(anotherAudioFile.getTitle()));

        assertEquals(2, sidePanel.comboBoxes.get(Album).getItemCount());
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(Album).getItems()).contains(audioFile.getAlbum()));
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(Album).getItems()).contains(anotherAudioFile.getAlbum()));

        assertEquals(1, sidePanel.comboBoxes.get(Track).getItemCount());
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(Track).getItems()).contains(trackNumber));

        assertEquals(2, sidePanel.comboBoxes.get(Year).getItemCount());
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(Year).getItems()).contains(audioFile.getYear()));
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(Year).getItems()).contains(anotherAudioFile.getYear()));

        assertEquals(2, sidePanel.comboBoxes.get(Genre).getItemCount());
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(Genre).getItems()).contains(audioFile.getGenre()));
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(Genre).getItems()).contains(anotherAudioFile.getGenre()));

        assertEquals(2, sidePanel.comboBoxes.get(Comments).getItemCount());
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(Comments).getItems()).contains(audioFile.getComment()));
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(Comments).getItems()).contains(anotherAudioFile.getComment()));

        assertEquals(1, sidePanel.comboBoxes.get(Disc_number).getItemCount());
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(Disc_number).getItems()).contains(discNumber));

        assertEquals(2, sidePanel.comboBoxes.get(Composer).getItemCount());
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(Composer).getItems()).contains(audioFile.getComposer()));
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(Composer).getItems()).contains(anotherAudioFile.getComposer()));
    }

    @Test
    public void ignoresEmptyAudioFileValues() {

        // given
        SidePanel sidePanel = aSidePanelWithSwtMocked();

        AudioFile validAudioFile = aValidCompleteAudioFile();
        AudioFile emptyAudioFile = anEmptyValidAudioFile();

        // when
        sidePanel.updateComboBoxes(Sets.newHashSet(validAudioFile, emptyAudioFile));

        // then
        for (AudioFileProperty name : values()) {
            assertEquals(1, sidePanel.comboBoxes.get(name).getItemCount());
        }

        // and
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(Artist).getItems()).contains(validAudioFile.getArtist()));
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(Title).getItems()).contains(validAudioFile.getTitle()));
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(Album).getItems()).contains(validAudioFile.getAlbum()));
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(Track).getItems()).contains(validAudioFile.getTrack()));
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(Year).getItems()).contains(validAudioFile.getYear()));
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(Genre).getItems()).contains(validAudioFile.getGenre()));
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(Comments).getItems()).contains(validAudioFile.getComment()));
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(Disc_number).getItems()).contains(validAudioFile.getDiscNumber()));
        assertTrue(Arrays.asList(sidePanel.comboBoxes.get(Composer).getItems()).contains(validAudioFile.getComposer()));
    }

    @Test
    public void doesNotHoldOldEntriesAfterUpdateGetsCalled() {

        // given
        SidePanel sidePanel = aSidePanelWithSwtMocked();

        // when
        AudioFile audioFile = aValidCompleteAudioFile();
        sidePanel.updateComboBoxes(Sets.newHashSet(audioFile));

        AudioFile secondAudioFile = anotherValidCompleteAudioFile();
        sidePanel.updateComboBoxes(Sets.newHashSet(secondAudioFile));

        // then
        for (AudioFileProperty name : values()) {
            assertEquals(1, sidePanel.comboBoxes.get(name).getItemCount());
        }

        // and
        assertEquals(secondAudioFile.getArtist(), sidePanel.comboBoxes.get(AudioFileProperty.Artist).getText());
        assertEquals(secondAudioFile.getTitle(), sidePanel.comboBoxes.get(AudioFileProperty.Title).getText());
        assertEquals(secondAudioFile.getAlbum(), sidePanel.comboBoxes.get(AudioFileProperty.Album).getText());
        assertEquals(secondAudioFile.getTrack(), sidePanel.comboBoxes.get(AudioFileProperty.Track).getText());
        assertEquals(secondAudioFile.getYear(), sidePanel.comboBoxes.get(AudioFileProperty.Year).getText());
        assertEquals(secondAudioFile.getGenre(), sidePanel.comboBoxes.get(AudioFileProperty.Genre).getText());
        assertEquals(secondAudioFile.getComment(), sidePanel.comboBoxes.get(AudioFileProperty.Comments).getText());
        assertEquals(secondAudioFile.getDiscNumber(), sidePanel.comboBoxes.get(AudioFileProperty.Disc_number).getText());
        assertEquals(secondAudioFile.getComposer(), sidePanel.comboBoxes.get(AudioFileProperty.Composer).getText());
    }

    private SidePanel aSidePanelWithSwtMocked() {
        SashForm sashFormMock = new SashForm(new Shell(), 0);
        return new SidePanel(sashFormMock).withController(mock(SidePanelController.class));
    }

    private AudioFile aValidCompleteAudioFile() {
        return new AudioFile("filePath")
                    .withArtist("Slayer")
                    .withTitle("Angel of death")
                    .withAlbum("Reign in Blood")
                    .withTrack("1")
                    .withYear("1986")
                    .withGenre("Thrash Metal")
                    .withComment("Slayer rules")
                    .withDiscNumber("1")
                    .withComposer("Jeff Hanneman");
    }

    private AudioFile anotherValidCompleteAudioFile() {
        return new AudioFile("filePath")
                .withArtist("Britney Spears")
                .withTitle("Oops! … I Did It Again")
                .withAlbum("Oops! … I Did It Again")
                .withTrack("1")
                .withYear("2000")
                .withGenre("Pop")
                .withComment("Oh man")
                .withDiscNumber("1")
                .withComposer("Who cares?");
    }

    private AudioFile anEmptyValidAudioFile() {
        return new AudioFile("filePath")
                .withArtist("")
                .withTitle("")
                .withAlbum("")
                .withTrack("")
                .withYear("")
                .withGenre("")
                .withComment("")
                .withDiscNumber("")
                .withComposer("");
    }
}