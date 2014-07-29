package com.github.marabou.properties;

import com.github.marabou.helper.PathHelper;

import java.io.*;
import java.util.Properties;
import java.util.logging.Logger;

public class PropertiesHelper {

    UserProperties userPropertiesInstance = null;
    private Logger log = Logger.getLogger(PropertiesHelper.class.getName());
    private PathHelper pathHelper;
    private PropertiesLoader propertiesLoader;


    public PropertiesHelper(PathHelper pathHelper, PropertiesLoader propertiesLoader) {
        this.pathHelper = pathHelper;
        this.propertiesLoader = propertiesLoader;
    }

    public ApplicationProperties getApplicationProperties() {
        Properties properties = new Properties();
        try {
            properties.load(PropertiesHelper.class.getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ApplicationProperties(properties);
    }

    public UserProperties getUserProperties() {
        if (!userPropertiesInitialized()) {
            initialiseUserProperties();
        }
        return userPropertiesInstance;
    }

    private boolean userPropertiesInitialized() {
        return userPropertiesInstance != null;
    }

    private void initialiseUserProperties() {

        if (userPropertiesNonExistent()) {
            log.info("Couldn't find marabou configuration. Loading defaults.");
            userPropertiesInstance = loadDefaultUserProperties();
            persistUserProperties(userPropertiesInstance.properties);
        } else if(userPropertiesNotReadable()) {
            log.info("Couldn't find marabou configuration. Loading defaults.");
            userPropertiesInstance = loadDefaultUserProperties();
        }else{
            userPropertiesInstance = getExistingUserProperties();
        }
    }

    protected boolean userPropertiesNonExistent() {
        File userPropertiesFile = getUserPropertiesFile();
       return !userPropertiesFile.exists();
    }

    protected boolean userPropertiesNotReadable() {
        File userPropertiesFile = getUserPropertiesFile();
        return !userPropertiesFile.canRead();
    }

    protected File getUserPropertiesFile() {
        return new File(pathHelper.getUserPropertiesFilePath());
    }

    protected UserProperties getExistingUserProperties() {
        InputStream userPropertiesStream = null;
        try {
            userPropertiesStream = new FileInputStream(getUserPropertiesFile());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Properties properties = propertiesLoader.loadProperties(userPropertiesStream);

        UserProperties userProperties = new UserProperties(properties, this);
        return userProperties;
    }

    UserProperties loadDefaultUserProperties() {
        InputStream userPropertiesStream = getClass().getClassLoader().getResourceAsStream(pathHelper.getUserPropertiesFileName());
        UserProperties userProperties = new UserProperties(propertiesLoader.loadProperties(userPropertiesStream), this);
        return userProperties;
    }

    // helper methods

    public void persistUserProperties(Properties userProperties) {
        propertiesLoader.persistUserProperties(userProperties);
    }
}
