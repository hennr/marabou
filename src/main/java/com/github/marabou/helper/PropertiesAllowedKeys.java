package com.github.marabou.helper;

/**
 * allowed keys for marabou.properties
 */
public enum PropertiesAllowedKeys {

//    // the actual window size when user closes marabou
    windowSizeX(""),
    windowSizeY(""),
//    // should we safe the last used path?
    safeLastPath(""),
//    // the last path that the user opened
    lastPath("");

    private String defaultValue;


    PropertiesAllowedKeys(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    String getDefaultValue() {
        return defaultValue;
    }

}
