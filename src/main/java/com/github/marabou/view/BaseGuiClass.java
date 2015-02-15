package com.github.marabou.view;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class BaseGuiClass {

    public static Shell shell;
    protected static Display display;

    public BaseGuiClass() {
        display = Display.getCurrent();
        if (shell == null) {
            shell = new Shell(display);
        }
    }
}
