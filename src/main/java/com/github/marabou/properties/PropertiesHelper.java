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
        Properties userProperties;

        if (! userPropertiesFile.exists()) {
            log.info("Couldn't find marabou configuration. Loading defaults.");
            userProperties = loadDefaultUserProperties();
            userPropertiesInstance = new UserProperties(userProperties, this);
        } else {
            userProperties = loadUserProperties(userPropertiesFile);
            userPropertiesInstance = new UserProperties(userProperties, this);
        }
    }

    private Properties loadUserProperties(File userPropertiesFile) {

        Properties properties = new Properties();

            // if config is found, check if updates are needed
		if (userPropertiesFile.exists()) {

				try {
					properties.load(new FileReader(userPropertiesFile.getAbsolutePath()));
                    addNewConfigurationKeysToUsersConfigFile(properties);
				} catch (IOException e) {
					log.severe("Couldn't load config file: " + userPropertiesFile.getAbsolutePath());
                    throw new RuntimeException("Couldn't read config file. " +
                            "Please make sure that your file permissions are set properly.");
				}

                properties = loadDefaultUserProperties();

			// userPropertiesFile is not existent yet
		} else {
			try {
				File mhfFile = new File(pathHelper.getMarabouHomeFolder());
				// create folder if no folder exists yet
				if (!mhfFile.exists()) {
					if (!mhfFile.mkdir()) {
						log.severe("Couldn't create marabou folder in your home.\n Please file a bug report.");
					}
				}
				// create marabou.properties file in new folder
				userPropertiesFile.createNewFile();
			} catch (IOException e) {
				log.severe("Couldn't create config file, please check your file permission in your home folder.");
			}
                properties = loadDefaultUserProperties();

				// copy all entries to the new userPropertiesFile
            persistUserProperties(properties);
		}
        return  properties;
    }

    Properties loadDefaultUserProperties() {
        InputStream userPropertiesStream = getClass().getClassLoader().getResourceAsStream(pathHelper.getDefaultUserPropertiesPath());
        return propertiesLoader.loadProperties(userPropertiesStream);
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
