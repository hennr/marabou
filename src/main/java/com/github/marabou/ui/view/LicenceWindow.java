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
package com.github.marabou.ui.view;

import com.github.marabou.helper.AvailableImage;
import com.github.marabou.helper.ImageLoader;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.RowLayout;

import java.io.IOException;
import java.net.URL;

import static com.github.marabou.helper.I18nHelper._;

public class LicenceWindow {

    Display display = Display.getCurrent();
    private Shell shell = new Shell(display);

	public void showLicence() {

		shell.setText(_("Licence"));
		shell.setImage(new ImageLoader().getImage(AvailableImage.LOGO_SMALL));
		
		RowLayout rowLayout = new RowLayout(1);
		rowLayout.center = true;
		rowLayout.marginBottom = 10;
		rowLayout.marginTop = 10;
		rowLayout.marginLeft = 10;
		rowLayout.marginRight = 10;
		shell.setLayout(rowLayout);
		
		// close window on ESC
		shell.addListener(SWT.Traverse, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.detail == SWT.TRAVERSE_ESCAPE) {
					shell.dispose();
				}
			}
		});
		
		// label
		Label label = new Label(shell, SWT.NONE);
		label.setAlignment(SWT.CENTER);

        URL url = Resources.getResource("licence.txt");
        String licenceText;
        try {
            licenceText = Resources.toString(url, Charsets.UTF_8);
        } catch (IOException e) {
            licenceText = "error reading licence file";
        }

        label.setText(licenceText);
		label.pack();
		
		// close button
		Button close = new Button(shell, SWT.NONE);
		close.setText(_("&Close"));
		close.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
			shell.dispose();
			}
		});
		close.setFocus();
		close.pack();

		shell.pack();
		shell.open();
		
		// close also if display gets disposed
		while (!shell.isDisposed() && display.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
}
