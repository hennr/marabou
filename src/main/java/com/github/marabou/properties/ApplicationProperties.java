package com.github.marabou.properties;

import java.util.Properties;

public class ApplicationProperties {

    private final Properties properties;

    public ApplicationProperties(Properties properties) {
        this.properties = properties;
    }

    public String getVersion() {
        return properties.getProperty("version", "UNKNOWN");
    }

}
