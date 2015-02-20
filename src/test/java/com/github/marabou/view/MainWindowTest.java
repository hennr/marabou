/**
 * Copyright (C) 2012 - 2015 Jan-Hendrik Peters
 *
 * This file is part of marabou.
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
package com.github.marabou.view;

import com.github.marabou.helper.ImageLoader;
import com.github.marabou.properties.UserProperties;

import com.google.common.eventbus.EventBus;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static junit.framework.TestCase.assertFalse;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class MainWindowTest {

    @BeforeClass
    public static void setupBaseGuiClass() {
        new BaseGuiClass();
    }

    @Test
    public void persistsUserSettingsOnClose() {

        // given
        Shell shell = mock(Shell.class);
        EventBus bus = new EventBus();
        when(shell.getDisplay()).thenReturn(mock(Display.class));
        when(shell.isDisposed()).thenReturn(true);
        aBaseGuiClassWith(shell);

        MainMenu mainMenu = mock(MainMenu.class);
        UserProperties userProperties = mock(UserProperties.class);
        ImageLoader imageLoader = mock(ImageLoader.class);
        Composite composite = mock(Composite.class);
        SashForm sashForm = mock(SashForm.class);
        MainWindow mainWindow = new MainWindow(bus, mainMenu, imageLoader, userProperties, composite, sashForm)  {
            @Override
            protected void createWidgetsAndLayout(MainMenu mainMenu) {
            }
        };

        // when
        mainWindow.init();

        // then
        verify(shell).addDisposeListener(any(PersistPropertiesListener.class));
    }

    @Test
    public void respectsPersistedWindowSize() {

        // given
        Shell spyShell = spy(new Shell());
        EventBus bus = new EventBus();
        when(spyShell.getDisplay()).thenReturn(mock(Display.class));
        when(spyShell.isDisposed()).thenReturn(true);
        aBaseGuiClassWith(spyShell);
        MainMenu mainMenu = mock(MainMenu.class);
        ImageLoader imageLoader = mock(ImageLoader.class);
        UserProperties userProperties = mock(UserProperties.class);
        when(userProperties.getWindowSizeX()).thenReturn(666);
        when(userProperties.getWindowSizeY()).thenReturn(667);
        when(userProperties.getTagBarWeight()).thenReturn(1);
        when(userProperties.getTableWeight()).thenReturn(1);
        Composite composite = mock(Composite.class);
        SashForm sashForm = mock(SashForm.class);
        MainWindow mainWindow = new MainWindow(bus, mainMenu, imageLoader, userProperties, composite, sashForm)  {
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

    private void aBaseGuiClassWith(Shell shell) {
        BaseGuiClass baseGuiClass = new BaseGuiClass();
        baseGuiClass.shell = shell;
    }
}
