package com.github.marabou.gui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class BaseGuiClass {

    protected static Shell shell;
    protected static Display display;

    public BaseGuiClass() {
        if (display == null) {
            display = new Display();
        }
        if (shell == null) {
            shell = new Shell(display);
        }
    }
}
