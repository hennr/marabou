package com.github.marabou.helper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader {


    public Properties loadProperties(InputStream userPropertiesStream) {

        try {
            Properties properties = new Properties();
            properties.load(userPropertiesStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load given input stream " + userPropertiesStream);
        }
    }
}
