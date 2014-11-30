package com.github.marabou.view;

import com.github.marabou.properties.UserProperties;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Shell;

public class PersistPropertiesListener implements DisposeListener {

    private MainWindow mainWindow;
    private UserProperties userProperties;
    private Shell shell;

    public PersistPropertiesListener(MainWindow mainWindow, UserProperties userProperties, Shell shell) {
        this.mainWindow = mainWindow;
        this.userProperties = userProperties;
        this.shell = shell;
    }

    @Override
    public void widgetDisposed(DisposeEvent e) {
        persistWindowSize();
        persistTagAndTableBarRation();
        userProperties.persistUserProperties();
    }

    private void persistWindowSize() {
        if (userProperties.rememberWindowSize()) {
            if (shell.getMaximized()) {
                userProperties.setWindowSizeX(-1);
                userProperties.setWindowSizeY(-1);
            } else {
                userProperties.setWindowSizeX(shell.getSize().x);
                userProperties.setWindowSizeY(shell.getSize().y);
            }
        }
    }

    private void persistTagAndTableBarRation() {
        userProperties.setTagBarWeight(mainWindow.sashForm.getWeights()[0]);
        userProperties.setTableWeight(mainWindow.sashForm.getWeights()[1]);
    }
}
