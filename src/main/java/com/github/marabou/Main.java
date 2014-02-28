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

import com.github.marabou.gui.AboutWindow;
import com.github.marabou.gui.MainMenu;
import com.github.marabou.gui.MainWindow;
import com.github.marabou.helper.ImageLoader;
import com.github.marabou.helper.LoggingHelper;
import com.github.marabou.helper.PathHelper;
import com.github.marabou.helper.PropertiesHelper;
import com.github.marabou.properties.ApplicationProperties;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class Main {

	public static void main(String[] args) {

        ApplicationProperties applicationProperties = PropertiesHelper.getApplicationProperties();
        PathHelper pathHelper = new PathHelper();
        PropertiesHelper propertiesHelper = new PropertiesHelper(pathHelper);

        if (startedWithDebugFlag(args)) {
            LoggingHelper.initLoggingDebug();
        } else {
            LoggingHelper.initLogging();
        }



        setupMainWindow(applicationProperties, propertiesHelper);
	}

    private static void setupMainWindow(ApplicationProperties applicationProperties, PropertiesHelper propertiesHelper) {
        Display display = new Display();
        Shell mainWindowShell = new Shell(display);
        ImageLoader imageLoader = new ImageLoader(display);
        AboutWindow aboutWindow = new AboutWindow(applicationProperties);

        MainMenu mainMenu = new MainMenu(mainWindowShell, aboutWindow, propertiesHelper);
        mainMenu.init();

        MainWindow mainWindow = new MainWindow(mainWindowShell, mainMenu, propertiesHelper, imageLoader);
        mainWindow.init();
    }

    // determines if marabou was started with --debug flag
	private static boolean startedWithDebugFlag(String[] args) {

		for (String arg : args) {
			if (arg.equalsIgnoreCase("--debug")) {
				System.out.println("Starting marabou in debug mode.");
				return true;
			}
		}
		return false;
	}
}
