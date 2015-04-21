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

import com.github.marabou.ui.events.SaveSelectedFilesEvent;
import com.github.marabou.helper.AvailableImage;
import com.github.marabou.helper.ImageLoader;
import com.github.marabou.properties.UserProperties;
import com.google.common.eventbus.EventBus;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.*;

import static com.github.marabou.helper.I18nHelper._;

public class MainWindow extends BaseGuiClass {

    protected Composite tableComposite;
    protected Composite composite;
    protected SashForm sashForm;
    protected ImageLoader imageLoader;
    protected UserProperties userProperties;
    protected EventBus bus;

    public MainWindow(EventBus bus, MainMenu mainMenu, ImageLoader imageLoader, UserProperties userProperties, Composite composite,  SashForm sashForm) {
        this.bus = bus;
        this.imageLoader = imageLoader;
        this.userProperties = userProperties;
        this.composite = composite;
        this.sashForm = sashForm;

        createWidgetsAndLayout(mainMenu);
    }

    protected void createWidgetsAndLayout(MainMenu mainMenu) {
        shell.setLayout(new FillLayout(SWT.VERTICAL));
        shell.setImage(imageLoader.getImage(AvailableImage.LOGO_SMALL));
        shell.setText(_("Marabou - Audio tagger"));

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
                bus.post(new SaveSelectedFilesEvent());
            }
        });
        toolbar.pack();

        sashForm.setLayoutData(gd);

		/* Right side */
        tableComposite = new Composite(sashForm, SWT.NONE);
        tableComposite.setLayout(new FillLayout());

        setFormRatioAndPack();
    }

    private void setFormRatioAndPack() {
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
        int x = userProperties.getWindowSizeX();
        int y = userProperties.getWindowSizeY();
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
    }

    private void saveUserSpecificSettingsOnExit() {
        shell.addDisposeListener(new PersistPropertiesListener(this, userProperties, shell));
    }

    public Composite getTableComposite() {
        return tableComposite;
    }
}
