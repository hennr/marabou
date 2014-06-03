package com.github.marabou.gui;

import com.github.marabou.helper.ImageLoader;
import com.github.marabou.properties.UserProperties;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class MainWindowTest {

    @Test
    public void persistsUserSettingsOnClose() {

        // given
        Shell shell = mock(Shell.class);
        when(shell.getDisplay()).thenReturn(mock(Display.class));
        when(shell.isDisposed()).thenReturn(true);

        MainMenu mainMenu = mock(MainMenu.class);
        UserProperties userProperties = mock(UserProperties.class);
        ImageLoader imageLoader = mock(ImageLoader.class);
        MainWindow mainWindow = new MainWindow(shell, mainMenu,  imageLoader, userProperties)  {
            @Override
            protected void createWidgetsAndLayout(MainMenu mainMenu) {
            }
        };

        // when
        mainWindow.init();

        // then
        verify(shell).addDisposeListener(any(PersistPropertiesListener.class));
    }
}
