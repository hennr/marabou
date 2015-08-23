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
package com.github.marabou;

import com.github.marabou.audio.AudioFileFactory;
import com.github.marabou.audio.loader.AudioFileFilter;
import com.github.marabou.ui.controller.MainMenuController;
import com.github.marabou.ui.controller.SidePanelController;
import com.github.marabou.ui.controller.TableController;
import com.github.marabou.audio.store.AudioFileStore;
import com.github.marabou.ui.view.*;
import com.github.marabou.helper.*;
import com.github.marabou.properties.ApplicationProperties;
import com.github.marabou.properties.PropertiesHelper;
import com.github.marabou.properties.PropertiesLoader;
import com.github.marabou.properties.UserProperties;
import com.github.marabou.audio.loader.AudioFileLoader;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.SubscriberExceptionContext;
import com.google.common.eventbus.SubscriberExceptionHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    static Logger log = LoggerFactory.getLogger("Main.class");

    public static void main(String[] args) {

        PathHelper pathHelper = new PathHelper();
        PropertiesLoader propertiesLoader = new PropertiesLoader(pathHelper);
        PropertiesHelper propertiesHelper = new PropertiesHelper(pathHelper, propertiesLoader);
        UserProperties userProperties = propertiesHelper.getUserProperties();
        ApplicationProperties applicationProperties = propertiesHelper.getApplicationProperties();
        setupMainWindow(applicationProperties, userProperties);
	}

    private static void setupMainWindow(ApplicationProperties applicationProperties, UserProperties userProperties) {
        ImageLoader imageLoader = new ImageLoader();
        AboutWindow aboutWindow = new AboutWindow(applicationProperties.getVersion());

        AudioFileFilter audioFileFilter = new AudioFileFilter();
        AudioFileLoader audioFileLoader = new AudioFileLoader(audioFileFilter);
        SubscriberExceptionHandler eventBusExceptionHandler = new SubscriberExceptionHandler() {
            @Override
            public void handleException(Throwable exception, SubscriberExceptionContext context) {
                log.error("A terrible thing happened in marabou.\n" +
                        "If you read this please copy the whole text and report a bug at the project web site.", exception);
            }
        };
        EventBus bus = new EventBus(eventBusExceptionHandler);
        AudioFileFactory audioFileFactory = new AudioFileFactory(bus);
        AudioFileStore audioFileStore = new AudioFileStore(bus, audioFileFactory);
        MainMenuController mainMenuController = new MainMenuController(bus, audioFileStore, userProperties, audioFileLoader, aboutWindow);
        MainMenu mainMenu = new MainMenu(mainMenuController);
        mainMenu.init();

        Composite MainWindowComposite = new Composite(BaseGuiClass.shell, SWT.NONE);
        SashForm mainWindowSashForm = new SashForm(MainWindowComposite, SWT.HORIZONTAL);

        SidePanel sidePanel = new SidePanel(mainWindowSashForm);

        SidePanelController sidePanelController = new SidePanelController(bus, sidePanel);
        sidePanel.withController(sidePanelController);

        ErrorWindow errorWindow = new ErrorWindow();
        bus.register(errorWindow);

        MainWindow mainWindow = new MainWindow(bus, mainMenu, imageLoader, userProperties, MainWindowComposite, mainWindowSashForm);
        Table table = new Table(mainWindow.getTableComposite(), SWT.MULTI);
        new TableController(bus, table, audioFileStore);

        mainWindow.init();
    }
}
