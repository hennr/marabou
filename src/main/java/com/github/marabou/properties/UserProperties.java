package com.github.marabou.properties;

import java.util.Properties;

public class UserProperties {

    protected Properties properties;
    private PropertiesHelper propertiesHelper;

    UserProperties(Properties properties, PropertiesHelper propertiesHelper) {
        this.properties = properties;
        this.propertiesHelper = propertiesHelper;
    }

    public boolean rememberWindowSize() {
        return Boolean.valueOf(properties.getProperty("rememberWindowSize", "false"));
    }

    public void setRememberWindowSize(boolean rememberWindowSize) {
        properties.setProperty("rememberWindowSize", String.valueOf(rememberWindowSize));
    }

    // size of the tag bar in relation to the table
    public String getTagBarWeight() {
        return getStoredValueOrDefault("tagBarWeight", "2");
    }

    public void setTagBarWeight(int weight) {
        properties.setProperty("tagBarWeight", String.valueOf(weight));
    }

    // size of the table in relation to the tag bar
    public String getTableWeight() {
        return getStoredValueOrDefault("tableWeight", "5");
    }

    public void setTableWeight(int weight) {
        properties.setProperty("tableWeight", String.valueOf(weight));
    }

    public int getWindowSizeX() {
        return getStoredValueOrDefaultAsInt("windowSizeX", -1);
    }

    public void setWindowSizeX(int windowSizeX) {
        properties.setProperty("windowSizeX", String.valueOf(windowSizeX));
    }

    public int getWindowSizeY() {
        return getStoredValueOrDefaultAsInt("windowSizeY", -1);
    }

    public void setWindowSizeY(int windowSizeY) {
        properties.setProperty("windowSizeY", String.valueOf(windowSizeY));
    }

    public boolean rememberLastPath() {
        return getStoredValueOrDefaultAsBoolean("safeLastPath", true);
    }

    public void setRememberLastPath(boolean toggle) {
        properties.setProperty("safeLastPath", String.valueOf(toggle));
    }

    public String getLastPath() {
        return getStoredValueOrDefault("lastPath", "");
    }

    public void setLastPath(String lastPath) {
        properties.setProperty("lastPath", lastPath);
    }

    private String getStoredValueOrDefault(String key, String defaultValue) {
        String propertyValue = properties.getProperty(key, defaultValue);
        if (propertyValue.isEmpty()) {
            return defaultValue;
        } else {
            return propertyValue;
        }
    }

    private int getStoredValueOrDefaultAsInt(String key, int defaultValue) {
        int result = defaultValue;
        try {
            result = Integer.valueOf(properties.getProperty(key, String.valueOf(defaultValue)));
            return result;
        } catch (Exception e) {
            return result;
        }
    }

    private boolean getStoredValueOrDefaultAsBoolean(String key, boolean defaultValue) {
        boolean result = defaultValue;
        try {
            result = Boolean.valueOf(properties.getProperty(key, String.valueOf(defaultValue)));
            return result;
        } catch (Exception e) {
            return result;
        }
    }

    public void persistUserProperties() {
        propertiesHelper.persistUserProperties(properties);
    }
}
