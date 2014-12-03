package com.github.marabou.view;

import org.eclipse.swt.widgets.DirectoryDialog;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.github.marabou.helper.I18nHelper._;

public class OpenDirectoryDialog extends BaseGuiClass {

    private Logger log = LoggerFactory.getLogger(OpenDirectoryDialog.class);

    public String getDirectoryToOpen(String openPath) {

        DirectoryDialog directoryDialog = new DirectoryDialog(shell);
        directoryDialog.setText(_("Choose directory..."));

        directoryDialog.setFilterPath(openPath);
        String dirToOpen = directoryDialog.open();
        log.info("Directory to open: {}", dirToOpen);

        return dirToOpen;
    }

}
