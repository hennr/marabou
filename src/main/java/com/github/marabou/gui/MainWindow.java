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

import static com.github.marabou.helper.I18nHelper._;
import com.github.marabou.db.HSQLDBController;
import com.github.marabou.helper.AvailableImage;
import com.github.marabou.helper.ImageLoader;
import com.github.marabou.helper.PropertiesAllowedKeys;
import com.github.marabou.helper.PropertiesHelper;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

public class MainWindow {
		
		Display display = new Display();
		Shell shell = new Shell(display);
		Composite comp = new Composite(shell, SWT.NONE);
		SashForm sashForm;
		MainMenu menu;
		HSQLDBController controller;
        ImageLoader imageLoader;

		/**
		 * the main window holds elements such as the menu, the table,
		 *  and the tabs on the left
		 */
		public MainWindow() {

            imageLoader = new ImageLoader(display);
            shell.setLayout(new FillLayout(SWT.VERTICAL));
            shell.setImage(imageLoader.getImage(AvailableImage.LOGO_SMALL));
            shell.setText(_("Marabou - Audio tagger"));

            // get Controller instance
            controller = HSQLDBController.getInstance();

            //Create a menu, place it in the shell and fill the menu
            menu = new MainMenu(shell);
            menu.init();
            shell.setMenuBar(menu.getMenu());

            // the comp is a child of the shell which holds the toolbar
            GridLayout gl = new GridLayout();
            gl.marginHeight = 0;
            gl.marginWidth = 0;
            gl.horizontalSpacing = 0;
            gl.verticalSpacing = 0;
            comp.setLayout(gl);
            GridData gd = new GridData(GridData.FILL_BOTH);
            comp.setLayoutData(gd);

            // the upper toolbar below the menu
            ToolBar toolbar = new ToolBar(comp, SWT.HORIZONTAL);
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
		sashForm = new SashForm(comp, SWT.HORIZONTAL);
		sashForm.setLayoutData(gd);

		// the toolbar at the bottom
		ToolBar toolbar2 = new ToolBar(comp, SWT.HORIZONTAL);
		ToolItem ti2 = new ToolItem(toolbar2, SWT.PUSH);
		ti2.setText("bar");
		toolbar2.pack();
		
		//Create the composites that will hold the table
		//on the right side and the tabs on the left 
		
	    /* Left side */
		
	    Composite leftComp = new Composite(sashForm, SWT.NONE);
		leftComp.setLayout(new FillLayout());
		
		// Tab folder
		TabFolder tabFolder = new TabFolder(leftComp, SWT.NONE);
		// link tabfolder to controller
		controller.connectTabFolder(tabFolder);
		
		// Tab1 - Tags
		TabItem item1 = new TabItem(tabFolder, SWT.NONE);
		item1.setText(_("Tags"));
        FileAttributeSidePanel fileAttributeSidePanel = new FileAttributeSidePanel();
		item1.setControl(fileAttributeSidePanel.init(tabFolder));
		
		// Tab 2 - Filesystem
		TabItem item2 = new TabItem(tabFolder, SWT.NONE);
		item2.setText(_("Filesystem"));
		item2.setControl(FilesystemTab.init(tabFolder));
		//TODO add Listener to open files from tree
		
		/* Right side */
		Composite rightComp = new Composite(sashForm, SWT.NONE);
		rightComp.setLayout(new FillLayout());
		
		// Table
		TableShell table = new TableShell(rightComp);
		menu.setTableShell(table);
		// link tableshell with controller
		controller.connectTableShell(table);
		
		// Now we have the composites, and can set a ratio between them
            int[] sashWeights = getStoredSashRation();
		sashForm.setWeights(sashWeights);
		sashForm.pack();
	}

    private int[] getStoredSashRation() {
        int[] result = {2, 5};
        try {
            System.out.println(PropertiesHelper.getProp(PropertiesAllowedKeys.tagBarWeight));
            System.out.println(PropertiesHelper.getProp(PropertiesAllowedKeys.tableWeight));
            result[0] = Integer.parseInt(PropertiesHelper.getProp(PropertiesAllowedKeys.tagBarWeight));
            result[1] = Integer.parseInt(PropertiesHelper.getProp(PropertiesAllowedKeys.tableWeight));
        } catch (Exception e) {
            return result;
        }
        return result;
    }

    public void init() {
	
		// maximize on first run
		shell.setMaximized(true);
		if (Boolean.parseBoolean(PropertiesHelper.getProp(PropertiesAllowedKeys.saveWindowSize))) {
			String x = PropertiesHelper.getProp(PropertiesAllowedKeys.windowSizeX);
			String y = PropertiesHelper.getProp(PropertiesAllowedKeys.windowSizeY);
			if (x.equals("max") && y.equals("max")) {
			    shell.setMaximized(true);
			}
			try {
				int xInt = Integer.parseInt(x);
				int yInt = Integer.parseInt(y);
				shell.setSize(xInt, yInt);
			} catch (NumberFormatException e) {}
		} else {
			shell.setMaximized(true);
		}
		shell.open();
		
		// save window size on exit to open it in that size again
		shell.addDisposeListener(new DisposeListener() {
		    @Override
		    public void widgetDisposed(DisposeEvent arg0) {
                persistWindowSize();
                persistTagAndTableBarRation();
		    }
		});
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

    private void persistWindowSize() {
        if (Boolean.parseBoolean(PropertiesHelper.getProp(PropertiesAllowedKeys.saveWindowSize))) {
            if (shell.getMaximized()) {
                PropertiesHelper.setProp(PropertiesAllowedKeys.windowSizeX, "max");
                PropertiesHelper.setProp(PropertiesAllowedKeys.windowSizeY, "max");
            } else {
                String x = String.valueOf(shell.getSize().x);
                String y = String.valueOf(shell.getSize().y);
                PropertiesHelper.setProp(PropertiesAllowedKeys.windowSizeX, x);
                PropertiesHelper.setProp(PropertiesAllowedKeys.windowSizeY, y);
            }
        }
    }

    private void persistTagAndTableBarRation() {
        String tagBarWeight = Integer.toString(sashForm.getWeights()[0]);
        String tableWeight = Integer.toString(sashForm.getWeights()[1]);
        PropertiesHelper.setProp(PropertiesAllowedKeys.tagBarWeight, tagBarWeight);
        PropertiesHelper.setProp(PropertiesAllowedKeys.tableWeight, tableWeight);
    }
}
