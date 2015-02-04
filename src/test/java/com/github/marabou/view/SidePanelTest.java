package com.github.marabou.view;

import com.github.marabou.events.SidePanelUpdatableEvent;
import com.github.marabou.model.AudioFile;
import com.google.common.collect.Sets;
import com.google.common.eventbus.EventBus;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

import java.util.Set;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

public class SidePanelTest {

    @Test
    public void updatesListOnSidePanelUpdatableEvent() {

        // given
        EventBus eventBus = new EventBus();
        SashForm sashForm = new SashForm(new Shell(), 0);
        SidePanel sidePanelSpy = spy(new SidePanel(eventBus, sashForm));
        // need to register the spy proxy instance too, as  the SidePanel constructor registers "this" so the spy would never get called...
        eventBus.register(sidePanelSpy);

        AudioFile audioFile = new AudioFile("invalid dummy audio file");
        Set<AudioFile> audioFileSet = Sets.newHashSet(audioFile);
        SidePanelUpdatableEvent sidePanelUpdatableEvent = new SidePanelUpdatableEvent(audioFileSet);

        // when
        eventBus.post(sidePanelUpdatableEvent);

        // then
        verify(sidePanelSpy).updateLists(sidePanelUpdatableEvent);
    }
}
