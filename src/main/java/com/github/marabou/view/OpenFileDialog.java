package com.github.marabou.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.github.marabou.helper.I18nHelper._;

public class OpenFileDialog extends BaseGuiClass {

    private Logger log = Logger.getLogger(OpenFileDialog.class.getName());
    private String lastPath = "";

    public List<File> getFilesToOpen(String openPath) {

        ArrayList<File> filesToOpen = new ArrayList<>();

        FileDialog fileDialog = new FileDialog(shell, SWT.MULTI);
        fileDialog.setText(_("Choose file..."));
        fileDialog.setFilterPath(openPath);

        /**
         * currently supported file endings, text for GUI
         */
        final String[] supportedFileEndingsDesc = {
                _("all supported audio files"),
                "*.mp3",
                _("all files") };

        /**
         *  currently supported file endings
         */
        final String[] supportedFileEndings = {
                "*.[m|M][p|P]3",
                "*.[m|M][p|P]3",
                "*" };

        fileDialog.setFilterExtensions(supportedFileEndings);
        fileDialog.setFilterNames(supportedFileEndingsDesc);

        fileDialog.open();
        lastPath = fileDialog.getFilterPath();

        for (String file : fileDialog.getFileNames()) {
            File f = new File (lastPath + "/" + file);
            filesToOpen.add(f);
        }

        log.log(Level.INFO,"Files to open: {0}", filesToOpen.toString());

        return filesToOpen;
    }

    public String getLastPath() {
        return lastPath;
    }

}
