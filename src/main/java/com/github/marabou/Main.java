/*
	Marabou Audio Tagger - A cross platform audio tagger using SWT
	
	Copyright (C) 2009-2010  Jan-Hendrik Peters, Markus Herpich
	
	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	      
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.marabou;

import com.github.marabou.audio.AudioFileFilter;
import com.github.marabou.controller.MainMenuController;
import com.github.marabou.model.HSQLDBClient;
import com.github.marabou.model.Model;
import com.github.marabou.view.*;
import com.github.marabou.helper.*;
import com.github.marabou.properties.ApplicationProperties;
import com.github.marabou.properties.PropertiesHelper;
import com.github.marabou.properties.PropertiesLoader;
import com.github.marabou.properties.UserProperties;
import com.github.marabou.service.AudioFileService;
import com.google.common.eventbus.EventBus;

public class Main {

	public static void main(String[] args) {

        PathHelper pathHelper = new PathHelper();
        PropertiesLoader propertiesLoader = new PropertiesLoader(pathHelper);
        PropertiesHelper propertiesHelper = new PropertiesHelper(pathHelper, propertiesLoader);
        UserProperties userProperties = propertiesHelper.getUserProperties();
        ApplicationProperties applicationProperties = propertiesHelper.getApplicationProperties();

        setupLogging(args);
        setupMainWindow(applicationProperties, userProperties);
	}

    private static void setupLogging(String[] args) {
        if (startedInDebugMode(args)) {
            System.out.println("Starting marabou in debug mode.");
            LoggingHelper.initLoggingDebug();
        } else {
            LoggingHelper.initLogging();
        }
    }

    private static boolean startedInDebugMode(String[] args) {

        for (String arg : args) {
            if (arg.equalsIgnoreCase("--debug")) {
                return true;
            }
        }
        return false;
    }

    private static void setupMainWindow(ApplicationProperties applicationProperties, UserProperties userProperties) {
        new BaseGuiClass();
        ImageLoader imageLoader = new ImageLoader();
        AboutWindow aboutWindow = new AboutWindow(applicationProperties.getVersion());

        AudioFileFilter audioFileFilter = new AudioFileFilter();
        AudioFileService audioFileService = new AudioFileService(audioFileFilter);
        FileAttributeSidePanel fileAttributeSidePanel = new FileAttributeSidePanel();
        HSQLDBClient hsqldbClient = new HSQLDBClient();
        EventBus bus = new EventBus();
        Model model = new Model(bus, hsqldbClient, fileAttributeSidePanel);
        bus.register(model);
        MainMenuController mainMenuController = new MainMenuController(bus, model, audioFileFilter, userProperties, audioFileService, aboutWindow);
        MainMenu mainMenu = new MainMenu(mainMenuController);
        mainMenu.init();

        MainWindow mainWindow = new MainWindow(bus, mainMenu, imageLoader, userProperties, model);
        mainWindow.init();
    }
}
