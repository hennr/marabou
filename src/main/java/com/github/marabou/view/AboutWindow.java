package com.github.marabou.view;

import com.github.marabou.helper.AvailableImage;
import com.github.marabou.helper.ImageLoader;
import com.github.marabou.properties.PropertiesHelper;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.marabou.helper.I18nHelper._;

public class AboutWindow {

    final static Logger log = LoggerFactory.getLogger(PropertiesHelper.class);
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
			// FIXME SWT has a bug detecting default browsers etc.
			// a bug report has been filed back in 2007...
			// https://bugs.eclipse.org/bugs/show_bug.cgi?id=209947
			// System.out.println(Program.findProgram("html"));
			// Program.launch("http://marabou.launchpad.net")
			@Override
			public void handleEvent(Event event) {
				
				try {
					Desktop.getDesktop().browse(new URI(url));
				} catch (Exception e) {
					// no browsing today :(
					log.warn("Couldn't open browser.\n" + e.getMessage());
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

		Button licence = new Button(comp2, SWT.None);
		licence.setText(_("&Licence"));
		licence.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				LicenceWindow.showLicence();
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