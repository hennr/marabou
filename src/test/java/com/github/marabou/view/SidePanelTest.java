package com.github.marabou.view;

import com.github.marabou.model.AudioFile;
import com.github.marabou.view.SidePanel.ComboAndLabelNames;
import com.google.common.collect.Sets;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SidePanelTest {

    @Test
    public void holdsAllGivenEntriesInTheCombos() {

        // given
        SidePanel sidePanel = aSidePanelWithSwtMocked();
        AudioFile audioFile = aValidCompleteAudioFile();

        // when
        sidePanel.updateLists(Sets.newHashSet(audioFile));

        // then
        assertEquals(audioFile.getArtist(), sidePanel.comboBoxes.get(ComboAndLabelNames.Artist).getText());
        assertEquals(audioFile.getTitle(), sidePanel.comboBoxes.get(ComboAndLabelNames.Title).getText());
        assertEquals(audioFile.getAlbum(), sidePanel.comboBoxes.get(ComboAndLabelNames.Album).getText());
        assertEquals(audioFile.getTrack(), sidePanel.comboBoxes.get(ComboAndLabelNames.Track).getText());
        assertEquals(audioFile.getYear(), sidePanel.comboBoxes.get(ComboAndLabelNames.Year).getText());
        assertEquals(audioFile.getGenre(), sidePanel.comboBoxes.get(ComboAndLabelNames.Genre).getText());
        assertEquals(audioFile.getComments(), sidePanel.comboBoxes.get(ComboAndLabelNames.Comments).getText());
        assertEquals(audioFile.getDiscNumber(), sidePanel.comboBoxes.get(ComboAndLabelNames.Disc_Number).getText());
        assertEquals(audioFile.getComposer(), sidePanel.comboBoxes.get(ComboAndLabelNames.Composer).getText());
    }

    @Test
    public void doesNotHoldOldEntriesAfterUpdateGetsCalled() {

        // given
        SidePanel sidePanel = aSidePanelWithSwtMocked();

        // when
        AudioFile audioFile = aValidCompleteAudioFile();
        sidePanel.updateLists(Sets.newHashSet(audioFile));

        //
        AudioFile secondAudioFile = anotherValidCompleteAudioFile();
        sidePanel.updateLists(Sets.newHashSet(secondAudioFile));

        // then
        for (ComboAndLabelNames name : ComboAndLabelNames.values()) {
            assertEquals(1, sidePanel.comboBoxes.get(name).getItemCount());
        }

        assertEquals(secondAudioFile.getArtist(), sidePanel.comboBoxes.get(ComboAndLabelNames.Artist).getText());
        assertEquals(secondAudioFile.getTitle(), sidePanel.comboBoxes.get(ComboAndLabelNames.Title).getText());
        assertEquals(secondAudioFile.getAlbum(), sidePanel.comboBoxes.get(ComboAndLabelNames.Album).getText());
        assertEquals(secondAudioFile.getTrack(), sidePanel.comboBoxes.get(ComboAndLabelNames.Track).getText());
        assertEquals(secondAudioFile.getYear(), sidePanel.comboBoxes.get(ComboAndLabelNames.Year).getText());
        assertEquals(secondAudioFile.getGenre(), sidePanel.comboBoxes.get(ComboAndLabelNames.Genre).getText());
        assertEquals(secondAudioFile.getComments(), sidePanel.comboBoxes.get(ComboAndLabelNames.Comments).getText());
        assertEquals(secondAudioFile.getDiscNumber(), sidePanel.comboBoxes.get(ComboAndLabelNames.Disc_Number).getText());
        assertEquals(secondAudioFile.getComposer(), sidePanel.comboBoxes.get(ComboAndLabelNames.Composer).getText());
    }

    private SidePanel aSidePanelWithSwtMocked() {
        SashForm sashFormMock = new SashForm(new Shell(), 0);
        return new SidePanel(sashFormMock);
    }

    private AudioFile aValidCompleteAudioFile() {
        return new AudioFile("666")
                    .withArtist("Slayer")
                    .withTitle("Angel of death")
                    .withAlbum("Reign in Blood")
                    .withTrack("1")
                    .withYear("1986")
                    .withGenre("Thrash Metal")
                    .withComments("Slayer rules")
                    .withDiscNumber("1")
                    .withComposer("Jeff Hanneman");
    }

    private AudioFile anotherValidCompleteAudioFile() {
        return new AudioFile("0")
                .withArtist("Britney Spears")
                .withTitle("Oops! … I Did It Again")
                .withAlbum("Oops! … I Did It Again")
                .withTrack("1")
                .withYear("2000")
                .withGenre("Pop")
                .withComments("Oh man")
                .withDiscNumber("1")
                .withComposer("Who cares?");
    }
}