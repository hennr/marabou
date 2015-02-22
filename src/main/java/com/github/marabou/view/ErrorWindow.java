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
package com.github.marabou.view;

import java.util.Calendar;

import com.github.marabou.helper.AvailableImage;
import com.github.marabou.helper.ImageLoader;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;

import static com.github.marabou.helper.I18nHelper._;

public class ErrorWindow {
	private Shell shell;
	private List errorText;
	private static ErrorWindow instance = null;
	
	private ErrorWindow() {
		// create new GUI-Set using the display from the main window
		Display display = Display.getCurrent();
		shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setSize(500, 300);
		shell.setText(_("Marabou - Error Occurred"));
		shell.setImage(new ImageLoader().getImage(AvailableImage.LOGO_SMALL));
		errorText = new List(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);

		// when the window gets closed destroy the 
		// instance of errorWindow as well
		DisposeListener listener = new DisposeListener() {
			@Override
			public void widgetDisposed(DisposeEvent e) {
				instance = null;
			}
		};
		shell.addDisposeListener(listener);
		// close also if display gets disposed
		while (!shell.isDisposed() && display.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();				
		}
	}
	
	private static ErrorWindow getInstance() {
		if (instance == null) {
			instance = new ErrorWindow();
		}
		return instance;
	}

	/**
	 * Appends given error message to the error window.
	 * If no instance exists already, this method will create one for you
	 * 
	 * @param errorMsg the Message the user shall see
	 */
	public static void appendError(final String errorMsg) {
		ErrorWindow ew = getInstance();
		Calendar c = Calendar.getInstance();
		ew.errorText.add(c.get(Calendar.HOUR) + ":" + c.get(Calendar.MINUTE) + ": " + errorMsg);
		ew.shell.open();
	}
}