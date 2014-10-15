/*
	Marabou Audio Tagger - A cross platform audio tagger using SWT
	
	Copyright (C) 2009  Jan-Hendrik Peters, Markus Herpich
	
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

package com.github.marabou.gui;

import com.github.marabou.db.HSQLDBController;
import com.github.marabou.helper.AvailableImage;
import com.github.marabou.helper.ImageLoader;
import com.github.marabou.properties.UserProperties;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import static com.github.marabou.helper.I18nHelper._;

public class MainWindow extends BaseGuiClass {

    Composite composite;
    SashForm sashForm;
    HSQLDBController controller;
    ImageLoader imageLoader;
    private UserProperties userProperties;

    /**
		 * the main window holds elements such as the menu, the table,
		 *  and the tabs on the left
		 */
		public MainWindow(MainMenu mainMenu, ImageLoader imageLoader, UserProperties userProperties) {
            this.imageLoader = imageLoader;
            this.controller = HSQLDBController.getInstance();
            this.userProperties = userProperties;

            createWidgetsAndLayout(mainMenu);
	}

    protected void createWidgetsAndLayout(MainMenu mainMenu) {
        shell.setLayout(new FillLayout(SWT.VERTICAL));
        shell.setImage(imageLoader.getImage(AvailableImage.LOGO_SMALL));
        shell.setText(_("Marabou - Audio tagger"));


        composite = new Composite(shell, SWT.NONE);

        //Create a menu, place it in the shell and fill the menu
        shell.setMenuBar(mainMenu.getMenu());

        // the composite is a child of the shell which holds the toolbar
        GridLayout gl = new GridLayout();
        gl.marginHeight = 0;
        gl.marginWidth = 0;
        gl.horizontalSpacing = 0;
        gl.verticalSpacing = 0;
        composite.setLayout(gl);
        GridData gd = new GridData(GridData.FILL_BOTH);
        composite.setLayoutData(gd);

        // the upper toolbar below the menu
        ToolBar toolbar = new ToolBar(composite, SWT.HORIZONTAL);
        ToolItem ti = new ToolItem(toolbar, SWT.PUSH);
        ti.setImage(imageLoader.getImage(AvailableImage.SAVE_ICON));
        ti.addListener(SWT.Selection, new Listener() {
            @Override
            public void handleEvent(Event event) {
                controller.saveSelectedFiles();
            }
    });
        toolbar.pack();

        //Create a sashForm which will hold composites
        sashForm = new SashForm(composite, SWT.HORIZONTAL);
        sashForm.setLayoutData(gd);

        //Create the composites that will hold the table
        //on the right side and the tabs on the left

	    /* Left side */
        FileAttributeSidePanel fileAttributeSidePanel = new FileAttributeSidePanel();
        fileAttributeSidePanel.init(sashForm);

		/* Right side */
        Composite rightComp = new Composite(sashForm, SWT.NONE);
        rightComp.setLayout(new FillLayout());

        // Table
        TableShell table = new TableShell(rightComp);
        // link table shell with controller
        controller.connectTableShell(table);

        // Now we have the composites, and can set a ratio between them
        int[] sashWeights = getStoredSashRation();
        sashForm.setWeights(sashWeights);
        sashForm.pack();
    }

    private int[] getStoredSashRation() {
        int[] result = new int[2];
        result[0] = userProperties.getTagBarWeight();
        result[1] = userProperties.getTableWeight();
        return result;
    }

    public void init() {

		if (!userProperties.rememberWindowSize()) {
            shell.setMaximized(true);
            shell.open();
        }
        int x =userProperties.getWindowSizeX();
        int y =userProperties.getWindowSizeY();
        if (x < 0 || y < 0) {
            shell.setMaximized(true);
        } else {
            shell.setSize(x, y);
        }
		shell.open();
		
        saveUserSpecificSettingsOnExit();

        while (!shell.isDisposed()) {
			if (!shell.getDisplay().readAndDispatch())
				shell.getDisplay().sleep();
		}
		shell.getDisplay().dispose();
    }

    private void saveUserSpecificSettingsOnExit() {
        shell.addDisposeListener(new PersistPropertiesListener(this, userProperties, shell));
    }
}
