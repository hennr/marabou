package com.github.marabou.properties;

import com.github.marabou.helper.PathHelper;
import com.github.marabou.helper.PropertiesLoader;

import java.io.*;
import java.util.Properties;
import java.util.Set;
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
        if (userPropertiesInstance == null) {
            initialiseUserProperties();
        }
        return userPropertiesInstance;
    }

    private void initialiseUserProperties() {

        File userPropertiesFile = new File(pathHelper.getUserPropertiesFilePath());

        if (! userPropertiesFile.exists()) {
            log.info("Couldn't find marabou configuration. Loading defaults.");
            userPropertiesInstance = loadDefaultUserProperties();
            // TODO persist
        } else {
            userPropertiesInstance = getExistingUserProperties();
        }
    }

    protected UserProperties getExistingUserProperties() {
        InputStream userPropertiesStream = null;
        try {
            userPropertiesStream = new FileInputStream(new File(pathHelper.getUserPropertiesFilePath()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Properties properties = propertiesLoader.loadProperties(userPropertiesStream);

        UserProperties userProperties = new UserProperties(properties, this);
        return userProperties;
    }

    UserProperties loadDefaultUserProperties() {
        InputStream userPropertiesStream = getClass().getClassLoader().getResourceAsStream(pathHelper.getDefaultUserPropertiesPath());
        UserProperties userProperties = new UserProperties(propertiesLoader.loadProperties(userPropertiesStream), this);
        return userProperties;
    }

    private void addNewConfigurationKeysToUsersConfigFile(Properties userProperties) {
        Set<Object> vendorKeys = userProperties.keySet();
        Set<Object> userKeys = userProperties.keySet();

        // copy missing new key/value pairs
        for (Object key : vendorKeys) {
            if (!userKeys.contains(key)) {
                userProperties.put(key, userProperties.get(key));
            }

        }
        persistUserProperties(userProperties);
    }

	// helper methods

	public void persistUserProperties(Properties userProperties) {
		try {
			BufferedWriter userConf = new BufferedWriter(new FileWriter(pathHelper.getUserPropertiesFilePath()));
            userProperties.store(userConf, null);
			// flush and close streams
			userConf.flush();
			userConf.close();
		} catch (IOException e) {
			log.severe("Couldn't save config file.");
		}
	}

}
