package com.github.marabou.helper;

import com.github.marabou.view.BaseGuiClass;
import org.eclipse.swt.graphics.Image;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ImageLoader extends BaseGuiClass {

    private final Object IMAGE_PATH = "graphics/";
    Map<AvailableImage, String> filePathMapping = new HashMap<>();

    public ImageLoader() {
        initFilePathMapping();
    }

    private void initFilePathMapping() {
        filePathMapping.put(AvailableImage.LOGO_SMALL, "marabou_16.png");
        filePathMapping.put(AvailableImage.LOGO_BIG, "marabou_300.png");
        filePathMapping.put(AvailableImage.EXIT_ICON, "exit.png");
        filePathMapping.put(AvailableImage.FOLDER_ICON, "folder.png");
        filePathMapping.put(AvailableImage.HELP_ICON, "help.png");
        filePathMapping.put(AvailableImage.SAVE_ICON, "save.png");
        filePathMapping.put(AvailableImage.AUDIO_ICON, "audio_file.png");
    }

    public Image getImage(AvailableImage availableImage) {

        InputStream imageStream  = ImageLoader.class.getClassLoader().getResourceAsStream(getResourcePath(availableImage));

        return new Image(display, imageStream);
    }

    private String getResourcePath(AvailableImage availableImage) {
        return IMAGE_PATH + resolveFileName(availableImage);
    }

    private String resolveFileName(AvailableImage availableImage) {

        if (filePathMapping.containsKey(availableImage)) {
            return filePathMapping.get(availableImage);
        } else {
            throw new IllegalArgumentException("Unable to resolve file name for " + availableImage.name());
        }
    }
}
