package com.github.marabou.properties;

import java.util.Properties;

public class UserProperties {

    private Properties properties;

    public UserProperties(Properties properties) {
        this.properties = properties;
    }

    public boolean rememberWindowSize() {
        return Boolean.valueOf(properties.getProperty("rememberWindowSize", "false"));
    }

    public void setRememberWindowSize(boolean rememberWindowSize) {

        properties.setProperty("rememberWindowSize", String.valueOf(rememberWindowSize));
    }

    // size of the tag bar in relation to the table
    public String getTagBarWeight() {
        return properties.getProperty("tagBarWeight", "2");
    }

    public void setTagBarWeight(int weight) {
        properties.setProperty("tagBarWeight", String.valueOf(weight));
    }

    // size of the table in relation to the tag bar
    public String getTableWeight() {
        return properties.getProperty("tableWeight", "5");
    }

    public void setTableWeight(int weight) {
        properties.setProperty("tableWeight", String.valueOf(weight));
    }

//    //    // the actual window size when user closes marabou
//    windowSizeX(""),
//    windowSizeY(""),
//    //    // should we safe the last used path?
//    safeLastPath(""),
//    //    // the last path that the user opened
//    lastPath(""),
}
