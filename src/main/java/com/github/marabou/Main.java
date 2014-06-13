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
import com.github.marabou.controller.EditorController;
import com.github.marabou.controller.TableController;
import com.github.marabou.gui.AboutWindow;
import com.github.marabou.gui.MainMenu;
import com.github.marabou.gui.MainWindow;
import com.github.marabou.helper.*;
import com.github.marabou.properties.ApplicationProperties;
import com.github.marabou.properties.PropertiesHelper;
import com.github.marabou.properties.PropertiesLoader;
import com.github.marabou.properties.UserProperties;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Main {

	public static void main(String[] args) {

        PathHelper pathHelper = new PathHelper();
        PropertiesLoader propertiesLoader = new PropertiesLoader();
        PropertiesHelper propertiesHelper = new PropertiesHelper(pathHelper, propertiesLoader);
        UserProperties userProperties = propertiesHelper.getUserProperties();
        ApplicationProperties applicationProperties = propertiesHelper.getApplicationProperties();

        setupLogging(args);
        setupMainWindow(applicationProperties, userProperties);
	}

    private static void setupLogging(String[] args) {
        if (startedWithDebugFlag(args)) {
            System.out.println("Starting marabou in debug mode.");
            LoggingHelper.initLoggingDebug();
        } else {
            LoggingHelper.initLogging();
        }
    }

    // determines if marabou was started with --debug flag
    private static boolean startedWithDebugFlag(String[] args) {

        for (String arg : args) {
            if (arg.equalsIgnoreCase("--debug")) {
                return true;
            }
        }
        return false;
    }

    private static void setupMainWindow(ApplicationProperties applicationProperties, UserProperties userProperties) {
        Display display = new Display();
        Shell mainWindowShell = new Shell(display);
        ImageLoader imageLoader = new ImageLoader(display);
        AboutWindow aboutWindow = new AboutWindow(applicationProperties);

        EditorController editorController = new EditorController();
        AudioFileFilter audioFileFilter = new AudioFileFilter();
        TableController tableController = new TableController(audioFileFilter);
        MainMenu mainMenu = new MainMenu(mainWindowShell, aboutWindow, editorController, tableController, userProperties);
        mainMenu.init();

        MainWindow mainWindow = new MainWindow(mainWindowShell, mainMenu, imageLoader, userProperties);
        mainWindow.init();
    }
}
