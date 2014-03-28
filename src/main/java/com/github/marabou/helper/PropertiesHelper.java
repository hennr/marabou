package com.github.marabou.helper;

import com.github.marabou.properties.ApplicationProperties;
import com.github.marabou.properties.UserProperties;

import java.io.*;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Helps you getting, setting, updating and saving users properties
 */
public class PropertiesHelper {

    static File userPropertiesFile;
    static Properties userProperties = new Properties();
    final static Logger log = Logger.getLogger(PropertiesHelper.class.getName());
    public static final String DEFAULT_USER_PROPERTIES_FILE_PATH = "marabou.properties";
    private PathHelper pathHelper;
    private PropertiesLoader propertiesLoader;



    public PropertiesHelper(PathHelper pathHelper, PropertiesLoader propertiesLoader) {
        this.pathHelper = pathHelper;
        this.propertiesLoader = propertiesLoader;
        readOrCreateDefaultUserProperties();
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
        Properties properties = new Properties();

        // TODO use OS specific path to load user properties
        // TODO create UserProperties on first program run
        // use default properties if properties can not be created

        return new UserProperties(properties);
    }

	private int readOrCreateDefaultUserProperties() {

		// creating a new userPropertiesFile file if none exists yet
        userPropertiesFile = new  File(getMarabouHomeFolder() + DEFAULT_USER_PROPERTIES_FILE_PATH);
		// if config is found, check if updates are needed
		if (userPropertiesFile.exists()) {
			if (!userPropertiesFile.canRead() || !userPropertiesFile.canWrite()) {
                throw new RuntimeException("Couldn't read or write config file. " +
                        "Please make sure that your file permissions are set properly.");
			} else {
				try {
					userProperties.load(new FileReader(userPropertiesFile.getAbsolutePath()));
                    addNewConfigurationKeysToUsersConfigFile(userProperties);
				} catch (IOException e) {
					log.severe("Couldn't load config file: " + userPropertiesFile.getAbsolutePath());
                    throw new RuntimeException("Couldn't read config file. " +
                            "Please make sure that your file permissions are set properly.");
				}

                userProperties = loadDefaultUserProperties();
			}
			// userPropertiesFile is not existent yet
		} else {
			try {
				File mhfFile = new File(getMarabouHomeFolder());
				// create folder if no folder exists yet
				if (!mhfFile.exists()) {
					if (!mhfFile.mkdir()) {
						log.severe("Couldn't create marabou folder in your home.\n Please file a bug report.");
						return 1;
					}
				}
				// create marabou.properties file in new folder
				userPropertiesFile.createNewFile();
			} catch (IOException e) {
				log.severe("Couldn't create config file, please check your file permission in your home folder.");
				return 1;
			}
                userProperties = loadDefaultUserProperties();

				// copy all entries to the new userPropertiesFile
				persistSettings(userProperties, userPropertiesFile);
		}
		return 0;
	}

    private String getMarabouHomeFolder() {
        try {
            return pathHelper.getMarabouHomeFolder();
        } catch (UnknownPlatformException e1) {
            throw new RuntimeException("Your operating system couldn't get detected properly. Please file a bug report.", e1);
        }
    }

    private Properties loadDefaultUserProperties() {
        InputStream userPropertiesStream = getClass().getClassLoader().getResourceAsStream(DEFAULT_USER_PROPERTIES_FILE_PATH);
        return propertiesLoader.loadProperties(userPropertiesStream);
    }

    private void addNewConfigurationKeysToUsersConfigFile(Properties userProperties) {
        Set<Object> vendorKeys = userProperties.keySet();
        Set<Object> userKeys = PropertiesHelper.userProperties.keySet();

        // copy missing new key/value pairs
        for (Object key : vendorKeys) {
            if (!userKeys.contains(key)) {
                PropertiesHelper.userProperties.put(key, userProperties.get(key));
            }

        }
        persistSettings(userProperties, userPropertiesFile);
    }

    public void persistUserProperties() {
        persistSettings(userProperties, userPropertiesFile);
    }

	// helper methods

	/**
	 * persists all changes of the users properties
	 */
	private static void persistSettings(Properties propertiesToPersist, File propertiesFile) {
		try {
			BufferedWriter userConf = new BufferedWriter(new FileWriter(propertiesFile.getAbsolutePath()));
            propertiesToPersist.store(userConf, null);
			// flush and close streams
			userConf.flush();
			userConf.close();
		} catch (IOException e) {
			log.severe("Couldn't save config file.");
		}
	}

}
