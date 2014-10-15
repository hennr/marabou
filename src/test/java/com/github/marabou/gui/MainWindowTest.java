package com.github.marabou.gui;

import com.github.marabou.helper.ImageLoader;
import com.github.marabou.properties.UserProperties;

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
        when(shell.getDisplay()).thenReturn(mock(Display.class));
        when(shell.isDisposed()).thenReturn(true);
        aBaseGuiClassWith(shell);

        MainMenu mainMenu = mock(MainMenu.class);
        UserProperties userProperties = mock(UserProperties.class);
        ImageLoader imageLoader = mock(ImageLoader.class);
        MainWindow mainWindow = new MainWindow(mainMenu, imageLoader, userProperties)  {
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
        MainWindow mainWindow = new MainWindow(mainMenu, imageLoader, userProperties)  {
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
