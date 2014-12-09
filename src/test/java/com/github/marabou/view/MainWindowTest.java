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
        SidePanel sidePanel = new SidePanel(bus);
        Composite composite = new Composite(shell, 1);
        SashForm sashForm = new SashForm(composite, 1);
        MainWindow mainWindow = new MainWindow(bus, mainMenu, imageLoader, userProperties, sidePanel, composite, sashForm)  {
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
        SidePanel sidePanel = new SidePanel(bus);
        UserProperties userProperties = mock(UserProperties.class);
        when(userProperties.getWindowSizeX()).thenReturn(666);
        when(userProperties.getWindowSizeY()).thenReturn(667);
        when(userProperties.getTagBarWeight()).thenReturn(1);
        when(userProperties.getTableWeight()).thenReturn(1);
        Composite composite = new Composite(spyShell, 1);
        SashForm sashForm = new SashForm(composite, 1);
        MainWindow mainWindow = new MainWindow(bus, mainMenu, imageLoader, userProperties, sidePanel, composite, sashForm)  {
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
