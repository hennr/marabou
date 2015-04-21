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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;

import java.awt.*;
import java.net.URI;

import static com.github.marabou.helper.I18nHelper._;

public class AboutWindow {

    private final String projectVersion;

    public AboutWindow(String projectVersion) {
        this.projectVersion = projectVersion;
    }

	/**
	 * shows various information about marabou in a small window
	 */
	public void show() {
		final Display display = Display.getCurrent();
		final Shell shell = new Shell(display);
		shell.setText(_("About Marabou"));
		shell.setImage(new ImageLoader().getImage(AvailableImage.LOGO_SMALL));
		FormLayout formLayout = new FormLayout();
		formLayout.marginBottom = 10;
		formLayout.marginTop = 10;
		formLayout.marginLeft = 10;
		formLayout.marginRight = 10;
		shell.setLayout(formLayout);

		Composite comp1 = new Composite(shell, SWT.NONE);
		RowLayout rowLayout = new RowLayout(1);
		rowLayout.center = true;
		comp1.setLayout(rowLayout);

		// close window on ESC
		shell.addListener(SWT.Traverse, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.detail == SWT.TRAVERSE_ESCAPE) {
					shell.dispose();
				}
			}
		});

		// project name and version
		Label text = new Label(comp1, SWT.NONE);
        text.setText(_("Marabou - Audio Tagger \n" + "Version " + projectVersion));

		Image logo = new ImageLoader().getImage(AvailableImage.LOGO_BIG);
		Label labelImage = new Label(comp1, SWT.NONE);
		labelImage.setImage(logo);
		labelImage.pack();

		Label labelText = new Label(comp1, SWT.NONE);
		labelText.setAlignment(SWT.CENTER);
		labelText
				.setText(_("\nThe Marabou is a scavenger and so is this software.\n"
						+ "It's written to eat badly tagged music files.\n"));
		labelText.pack();

		// button with project's url
		final String url = "https://github.com/hennr/marabou";
		final Button linkButton = new Button(comp1, SWT.PUSH);
		linkButton.setText(url);
		linkButton.pack();
		linkButton.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				
				try {
					Desktop.getDesktop().browse(new URI(url));
				} catch (Exception e) {
					// no browsing today :(
				}
			}
		});

		// dirty hack to get vertical space between the buttons
		Label space = new Label(comp1, SWT.NONE);
		space.pack();

		// horizontal row for buttons
		Composite comp2 = new Composite(comp1, SWT.None);
		RowLayout rowLayout2 = new RowLayout();
		comp2.setLayout(rowLayout2);

		Button credits = new Button(comp2, SWT.None);
		credits.setText(_("Cr&edits"));
		credits.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				CreditsWindow.showCredits();
			}
		});
		credits.pack();

		final Button licence = new Button(comp2, SWT.None);
		licence.setText(_("&Licence"));
		licence.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				new LicenceWindow().showLicence();
			}
		});
		licence.pack();

		Button close = new Button(comp2, SWT.NONE);
		close.setText(_("&Close"));
		close.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				shell.dispose();
			}
		});
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
