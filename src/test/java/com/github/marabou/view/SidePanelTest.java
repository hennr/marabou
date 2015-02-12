package com.github.marabou.view;

import com.github.marabou.model.AudioFile;
import com.google.common.collect.Sets;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.assertEquals;

public class SidePanelTest {

    @Test
    public void holdsAllGivenEntriesInTheCombos() {

        // given
        SashForm sashFormMock = new SashForm(new Shell(), 0);
        SidePanel sidePanel = new SidePanel(sashFormMock);

        AudioFile audioFile = new AudioFile("666")
                .withArtist("Slayer")
                .withTitle("Angel of death")
                .withAlbum("Reign in Blood")
                .withTrack("1")
                .withYear("1986")
                .withGenre("Thrash Metal")
                .withComments("Slayer rules")
                .withDiscNumber("1")
                .withComposer("Jeff Hanneman");

        Set<AudioFile> audioFileSet = Sets.newHashSet(audioFile);

        // when
        sidePanel.updateLists(audioFileSet);

        // then
        assertEquals("Slayer", sidePanel.comboBoxes.get(SidePanel.ComboAndLabelNames.Artist).getText());
        assertEquals("Angel of death", sidePanel.comboBoxes.get(SidePanel.ComboAndLabelNames.Title).getText());
        assertEquals("Reign in Blood", sidePanel.comboBoxes.get(SidePanel.ComboAndLabelNames.Album).getText());
        assertEquals("1", sidePanel.comboBoxes.get(SidePanel.ComboAndLabelNames.Track).getText());
        assertEquals("1986", sidePanel.comboBoxes.get(SidePanel.ComboAndLabelNames.Year).getText());
        assertEquals("Thrash Metal", sidePanel.comboBoxes.get(SidePanel.ComboAndLabelNames.Genre).getText());
        assertEquals("Slayer rules", sidePanel.comboBoxes.get(SidePanel.ComboAndLabelNames.Comments).getText());
        assertEquals("1", sidePanel.comboBoxes.get(SidePanel.ComboAndLabelNames.Disc_Number).getText());
        assertEquals("Jeff Hanneman", sidePanel.comboBoxes.get(SidePanel.ComboAndLabelNames.Composer).getText());
    }
}