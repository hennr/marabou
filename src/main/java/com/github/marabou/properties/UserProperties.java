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

    public int getWindowSizeX() {
        return Integer.valueOf(properties.getProperty("windowSizeX", "-1"));
    }

    public void setWindowSizeX(int windowSizeX) {
        properties.setProperty("windowSizeX", String.valueOf(windowSizeX));
    }

    public int getWindowSizeY() {
        return Integer.valueOf(properties.getProperty("windowSizeY", "-1"));
    }

    public void setWindowSizeY(int windowSizeY) {
        properties.setProperty("windowSizeY", String.valueOf(windowSizeY));
    }

    public boolean rememberLastPath() {
        return Boolean.valueOf(properties.getProperty("safeLastPath", "true"));
    }

    public void setRememberLastPath(boolean toggle) {
        properties.setProperty("safeLastPath", String.valueOf(toggle));
    }

    public String getLastPath() {
        return properties.getProperty("lastPath", "");
    }

    public void setLastPath(String lastPath) {
        properties.setProperty("lastPath", lastPath);
    }
}
