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

package net.launchpad.marabou.gui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;

import static net.launchpad.marabou.helper.I18nHelper._;

public class CreditsWindow {
	
	/**
	 * shows the devs, translators and libs that marabou uses
	 * all text comes from the translation files
	 */
	public static void showCredits() {
		final Display display = Display.getCurrent();
		
		final Shell shell = new Shell(display);
		shell.setText(_("Credits"));
		shell.setImage(new Image(display, "src/main/resources/graphics/marabou_16.png"));
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		
		shell.setLayout(gridLayout);

		// close window on ESC
		shell.addListener(SWT.Traverse, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.detail == SWT.TRAVERSE_ESCAPE) {
					shell.dispose();
				}
			}
		});
		
		// Tabs
		TabFolder tabFolder = new TabFolder(shell, SWT.NONE);
		
		Composite devsComp = new Composite(tabFolder, SWT.NONE);
		
		Composite transComp = new Composite(tabFolder, SWT.NONE);
		
		Composite depsComp = new Composite(tabFolder, SWT.NONE);

		TabItem codersTab = new TabItem(tabFolder, SWT.None);
		codersTab.setText(_("Developers"));
		codersTab.setControl(devsComp);
		
		TabItem translatorsTab = new TabItem(tabFolder, SWT.None);
		translatorsTab.setText(_("Translators"));
		translatorsTab.setControl(transComp);
		
		TabItem depenciesTab = new TabItem(tabFolder, SWT.None);
		depenciesTab.setText(_("Dependencies"));
		depenciesTab.setControl(depsComp);

		Link developers = new Link(devsComp, SWT.None);
		developers.setText(_(
				"Jan-Hendrik Peters  <a>hennr@hennr.name</a>\n" + 
				"Markus Herpich <a>herpich@cs.tu-berlin.de</a>"
				));
		developers.pack();
		// listener to open mail client for the E-Mail-address clicked
		developers.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
			if (Desktop.isDesktopSupported()) {
				try {
					Desktop.getDesktop().mail(new URI("mailto", event.text, null));
				}
				//  no mail client found 
				catch (IOException e) {
				// FIXME on my system no MUA is found :( Works fine in Markus' environment though... 
				} catch (URISyntaxException e) {/* shouldn't happen */}
			}
			}
		});

// FIXME does github or something we can use for free a web interface for translation volunteers as well?
//		final String transUrl = "https://translations.launchpad.net/marabou";
//		Link transNote = new Link(transComp, SWT.None);
//		transNote.setText(_("Want to help translate marabou?\n" + 
//				"Check out the online interface at:\n" + 
//				"<a>") + transUrl + _("</a>"));
//		transNote.pack();
//		// handle clicks on the link and open it in a browser
//		transNote.addListener(SWT.Selection, new Listener() {
//			@Override
//			public void handleEvent(Event event) {
//				if (Desktop.isDesktopSupported()) {
//				try {
//					try {
//						Desktop.getDesktop().browse(new URI(transUrl));
//						} catch (IOException e) { /* no browsing today :( */ }
//				} catch (URISyntaxException e) {/* Shouldn't happen at all */ }}
//			}});
		
		Link translators = new Link(transComp, SWT.None);
		// FIXME these line breaks are necessary, otherwise the first label covers this one, why?
		translators.setText(_("\n\n\n\n" + 
				"English:   Jan-Hendrik Peters  <a>hennr@hennr.name</a>\n" + 
				"German:  Jan-Hendrik Peters  <a>hennr@hennr.name</a>"));
		translators.pack();
		
		Link depencies = new Link(depsComp, SWT.None);
		depencies.setText(_(
				"Marabou devs want to thank the following projects and persons:\n\n" + 
				"SWT for the gui; " + "EPL; " + "<a>http://www.eclipse.org/swt/</a>\n" + 
				"Michael Patricios for mp3agic as our backend libraby; " + "GPL 3; " + "<a>https://github.com/mpatric/mp3agic/</a>\n" +  
				"junit for our unit tests; " + "CPL; " + "<a>http://junit.sourceforge.net/</a>\n" + 
				"gettext-commons for internationalization; " + "Apache licence; " + "<a>http://code.google.com/p/gettext-commons/</a>\n" +
				"Tango Icon Library; " + "Public Domain; " + "<a>http://tango.freedesktop.org/Tango_Icon_Library</a>\n" +
				"HSQLDB; " + "BSD based licence; " + "<a>http://hsqldb.org/</a>\n" + 
				"maven as a great build tool and depency resolver; " + "Apache licence; " + "<a>http://maven.apache.org/</a>\n")+
				"Dries Bargheer for the logo; " + "CC-BY-SA" + "\n"
				);
		depencies.pack();

		Button close = new Button(shell, SWT.NONE);
		close.setText(_("&Close"));
		close.addListener(SWT.Selection, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
			shell.dispose();
			}
		});
		close.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		close.pack();
		
		shell.pack();
		shell.open();
		// close as well if display gets disposed
		while (!shell.isDisposed() && display.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
}
