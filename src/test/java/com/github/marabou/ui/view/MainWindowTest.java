/**
 * Marabou - Audio Tagger
 * <p>
 * Copyright (C) 2012 - 2015 Jan-Hendrik Peters
 * <p>
 * https://github.com/hennr/marabou
 * <p>
 * Marabou is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package com.github.marabou.ui.view;

import com.github.marabou.helper.ImageLoader;
import com.github.marabou.properties.UserProperties;

import com.google.common.eventbus.EventBus;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static junit.framework.TestCase.assertFalse;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class MainWindowTest {

    @Test
    public void persistsUserSettingsOnClose() {

        // given
        Shell shell = mock(Shell.class);
        EventBus bus = new EventBus();
        when(shell.getDisplay()).thenReturn(mock(Display.class));
        when(shell.isDisposed()).thenReturn(true);
        BaseGuiClass.shell = shell;

        MainMenu mainMenu = mock(MainMenu.class);
        UserProperties userProperties = mock(UserProperties.class);
        ImageLoader imageLoader = mock(ImageLoader.class);
        Composite composite = mock(Composite.class);
        SashForm sashForm = mock(SashForm.class);
        MainWindow mainWindow = new MainWindow(bus, mainMenu, imageLoader, userProperties, composite, sashForm) {
            @Override
            protected void createWidgetsAndLayout(MainMenu mainMenu) {
            }
        };

        // when
        mainWindow.init();

        // then
        ArgumentCaptor<PersistPropertiesListener> argumentCaptor = ArgumentCaptor.forClass(PersistPropertiesListener.class);
        verify(shell).addDisposeListener(argumentCaptor.capture());
        assertTrue(argumentCaptor.getValue() instanceof PersistPropertiesListener);
    }

    @Test
    public void respectsPersistedWindowSize() {

        // given
        Shell spyShell = spy(new Shell());
        EventBus bus = new EventBus();
        when(spyShell.getDisplay()).thenReturn(mock(Display.class));
        when(spyShell.isDisposed()).thenReturn(true);
        BaseGuiClass.shell = spyShell;
        MainMenu mainMenu = mock(MainMenu.class);
        ImageLoader imageLoader = mock(ImageLoader.class);
        UserProperties userProperties = mock(UserProperties.class);
        when(userProperties.getWindowSizeX()).thenReturn(666);
        when(userProperties.getWindowSizeY()).thenReturn(667);
        when(userProperties.getTagBarWeight()).thenReturn(1);
        when(userProperties.getTableWeight()).thenReturn(1);
        Composite composite = mock(Composite.class);
        SashForm sashForm = mock(SashForm.class);
        MainWindow mainWindow = new MainWindow(bus, mainMenu, imageLoader, userProperties, composite, sashForm) {
            @Override
            protected void createWidgetsAndLayout(MainMenu mainMenu) {
            }
        };

        // when
        mainWindow.init();

        // then
        assertEquals(666, spyShell.getSize().x);
        assertEquals(667, spyShell.getSize().y);
        assertFalse(spyShell.getMaximized());
    }
}
