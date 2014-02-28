package com.github.marabou.helper;

import com.github.marabou.properties.ApplicationProperties;

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
    public static final String USER_PROPERTIES_FILE_PATH = "src/main/resources/marabou.properties";
    private PathHelper pathHelper;

    public PropertiesHelper(PathHelper pathHelper) {
        this.pathHelper = pathHelper;
        readOrCreateDefaultUserProperties();
    }


    public static ApplicationProperties getApplicationProperties() {
        Properties properties = new Properties();
        try {
            properties.load(PropertiesHelper.class.getResourceAsStream("/application.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new ApplicationProperties(properties);
    }

	private int readOrCreateDefaultUserProperties() {

		// creating a new userPropertiesFile file if none exists yet
        userPropertiesFile = new File(getHomeFolder() + "marabou.properties");
		// if config is found, check if updates are needed
		if (userPropertiesFile.exists()) {
			if (!userPropertiesFile.canRead() || !userPropertiesFile.canWrite()) {
                throw new RuntimeException("Couldn't read or write config file. " +
                        "Please make sure that your file permissions are set properly.");
			} else {
				try {
					userProperties.load(new FileReader(userPropertiesFile.getAbsolutePath()));
				} catch (IOException e) {
					log.severe("Couldn't load config file: " + userPropertiesFile.getAbsolutePath());
                    throw new RuntimeException("Couldn't read config file. " +
                            "Please make sure that your file permissions are set properly.");
				}
				Properties userProperties = new Properties();

				try {
					userProperties.load(new FileReader(USER_PROPERTIES_FILE_PATH));
				} catch (IOException e) {
                    throw new RuntimeException("Couldn't read config file. " +
                            "Please make sure that your file permissions are set properly.");
				}

                addNewConfigurationKeysToUsersConfigFile(userProperties);
			}
			// userPropertiesFile is not existent yet
		} else {
			try {
				File mhfFile = new File(getHomeFolder());
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
			try {

				BufferedReader vendorConf = new BufferedReader(new FileReader(USER_PROPERTIES_FILE_PATH));

				userProperties.load(vendorConf);
				// copy all entries to the new userPropertiesFile
				persistSettings();
				vendorConf.close();
			} catch (IOException e) {
				log.warning("Couldn't create config file in " + System.getProperty("user.home"));
				log.warning(e.toString());
			}
		}
		return 0;
	}

    private String getHomeFolder() {
        try {
            return pathHelper.getMarabouHomeFolder();
        } catch (UnknownPlatformException e1) {
            throw new RuntimeException("Your OS couldn't get detected properly. Please file a bug report.", e1);
        }
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
        persistSettings();
    }

	public String getProp(PropertiesAllowedKeys key) {
        String result = userProperties.getProperty(key.toString(), "");
        if (result.isEmpty()) {
            result = key.getDefaultValue();
        }
        return result;
	}

	/**
	 * sets a setting and saves it to users config file
	 */
	public static void setProp(PropertiesAllowedKeys key, String value) {
		userProperties.setProperty(key.toString(), value);
		persistSettings();
	}

	// helper methods

	/**
	 * persists all changes of the users properties
	 */
	private static void persistSettings() {
		try {
			BufferedWriter userConf = new BufferedWriter(new FileWriter(
					userPropertiesFile.getAbsolutePath()));
			userProperties.store(userConf, null);
			// flush and close streams
			userConf.flush();
			userConf.close();
		} catch (IOException e) {
			log.severe("Couldn't save config file.");
		}
	}

}
