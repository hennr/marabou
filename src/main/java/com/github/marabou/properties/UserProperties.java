package com.github.marabou.properties;

import com.github.marabou.helper.PathHelper;
import com.github.marabou.helper.PropertiesHelper;
import com.github.marabou.helper.PropertiesLoader;

import java.util.Properties;

public class UserProperties {

    private Properties properties;

    public UserProperties(Properties properties) {
        this.properties = properties;
    }

    public boolean rememberWindowSize() {
        return Boolean.valueOf(properties.getProperty("rememberWindowSize", "false"));
    }

    // FIXME decide where and how to persist user properites
    public void setRememberWindowSize(boolean rememberWindowSize) {

        properties.setProperty("rememberWindowSize", String.valueOf(rememberWindowSize));
        PropertiesHelper propertiesHelper = new PropertiesHelper(new PathHelper(), new PropertiesLoader());
        propertiesHelper.persistUserProperties(properties);
    }

//    //    // the actual window size when user closes marabou
//    windowSizeX(""),
//    windowSizeY(""),
//    //    // should we safe the last used path?
//    safeLastPath(""),
//    //    // the last path that the user opened
//    lastPath(""),
//    // size of the tag bar in relation to the table
//    tagBarWeight("2"),
//    // size of the table in relation to the tag bar
//    tableWeight("5");
}
