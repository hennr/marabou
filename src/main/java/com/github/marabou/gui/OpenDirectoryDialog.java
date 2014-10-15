package com.github.marabou.gui;

import org.eclipse.swt.widgets.DirectoryDialog;

import java.util.logging.Level;
import java.util.logging.Logger;

import static com.github.marabou.helper.I18nHelper._;

public class OpenDirectoryDialog extends BaseGuiClass {

    private Logger log = Logger.getLogger(OpenDirectoryDialog.class.getName());

    public String getDirectoryToOpen(String openPath) {

        DirectoryDialog directoryDialog = new DirectoryDialog(shell);
        directoryDialog.setText(_("Choose directory..."));

        directoryDialog.setFilterPath(openPath);
        String dirToOpen = directoryDialog.open();
        log.log(Level.INFO,"Directory to open: {0}",dirToOpen);

        return dirToOpen;
    }

}
