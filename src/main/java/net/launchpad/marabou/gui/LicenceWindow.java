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

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.RowLayout;

import static net.launchpad.marabou.helper.I18nHelper._;

public class LicenceWindow {

/**
 * shows our choosen licence from file LICENCE
 */
	public static void showLicence() {
		final Display display = Display.getCurrent();
		
		final Shell shell = new Shell(display);
		shell.setText(_("Licence"));
		shell.setImage(new Image(display, "src/main/resources/graphics/marabou_16.png"));
		
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
		label.setText(_(
		"Marabou Audio Tagger\n\n" +
		"A cross platform audio tagger using SWT\n\n" + 	
		"Copyright (C) 2009-2012  Jan-Hendrik Peters, Markus Herpich\n\n\n" + 
		
		"This program is free software: you can redistribute it and/or modify\n" + 
		"it under the terms of the GNU General Public License as published by\n" + 
		"the Free Software Foundation, either version 3 of the License, or\n" + 
		"(at your option) any later version.\n\n" +
		
		"This program is distributed in the hope that it will be useful,\n" + 
		"but WITHOUT ANY WARRANTY; without even the implied warranty of\n" + 
		"MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" + 
		"GNU General Public License for more details.\n\n" +
		
		"You should have received a copy of the GNU General Public License\n" + 
		"along with this program.  If not, see <http://www.gnu.org/licenses/>.")
		);
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