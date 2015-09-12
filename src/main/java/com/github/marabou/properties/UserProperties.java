/**
 * Marabou - Audio Tagger
 * Copyright (C) 2012 - 2015 Jan-Hendrik Peters
 * https://github.com/hennr/marabou
 * Marabou is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
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
    public int getTagBarWeight() {
        return getStoredValueOrDefaultAsInt("tagBarWeight", 2);
    }

    public void setTagBarWeight(int weight) {
        properties.setProperty("tagBarWeight", String.valueOf(weight));
    }

    // size of the table in relation to the tag bar
    public int getTableWeight() {
        return getStoredValueOrDefaultAsInt("tableWeight", 5);
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
