package net.launchpad.marabou.helper;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;

/**
 * This class is intend to help you getting, setting, updating and saving users
 * properties
 * 
 * <b>NOTE</b>: This classes' initProperties() method gets invoked by the main
 * class, which means that a properties file gets created in any case
 */
public class PropertiesHelper {

	final static Logger log = Logger
			.getLogger(PropertiesHelper.class.getName());
	static Properties properties = new Properties();
	static File conf;

	static String homeFolder;

	/**
	 * opens the properties file in the correct path or creates a new one if
	 * none is found
	 */
	public static int initProperties() {

		// creating a new conf file if none exists yet
		PathHelper ph = new PathHelper();
		try {
			homeFolder = ph.getMarabouHomeFolder();
			conf = new File(ph.getMarabouHomeFolder() + "marabou.properties");
		} catch (UnknowPlatformException e1) {
			log.severe("Your OS couldn't get detected properly.\n"
					+ "Please file a bugreport.");
			return 1;
		}
		// if config is found, check if updates are needed
		if (conf.exists()) {
			if (!conf.canRead() || !conf.canWrite()) {
				log.severe("Couldn't read or write config file."
						+ " Please make sure that your file permissions are set properly.");
				return 1;
				// config is existens and accessable
			} else {
				try {
					properties.load(new FileReader(conf.getAbsolutePath()));
				} catch (FileNotFoundException e) {

					log.severe("Couldn't find user's config file in path: "
							+ conf.getAbsolutePath());

					return 1;
				} catch (IOException e) {
					log.severe("Couldn't load config file: "
							+ conf.getAbsolutePath());
					return 1;
				}
				Properties vendorProp = new Properties();

				// TODO RELEASE replace path with method call of PathHelper (for
				// specific OS)

				try {
					vendorProp.load(new FileReader(
							"src/main/resources/marabou.properties"));
				} catch (FileNotFoundException e) {
					log.severe("Couldn't find vendor config.");
					return 1;
				} catch (IOException e) {
					log.severe("Couldn't open vendor config.");
					return 1;
				}
				// if the user's conf is older than the current marabou version,
				// update the missing entries
				if (properties.getProperty("version").compareTo(
						vendorProp.getProperty("version")) < 0) {
					Set<Object> vendorKeys = vendorProp.keySet();
					Set<Object> userKeys = properties.keySet();

					// set the latest version in users conf
					properties.setProperty("version",
							vendorProp.getProperty("version"));

					// copy missing new key/value pairs
					for (Object key : vendorKeys) {
						if (!userKeys.contains(key)) {
							properties.put(key, vendorProp.get(key));
						}

					}
					persistSettings();
				}
			}
			// conf is not existent yet
		} else {
			try {
				File mhfFile = new File(homeFolder);
				// create foler if no folder exists yet
				if (!mhfFile.exists()) {
					if (!mhfFile.mkdir()) {
						log.severe("Couldn't create marabou folder in your home.\n"
								+ "Please file a bugreport.");
						return 1;
					}
				}
				// create marabou.properties file in new folder
				conf.createNewFile();
			} catch (IOException e) {
				log.severe("Couldn't create config file, please check your file permission in your home folder.");
				return 1;
			}
			try {

				// TODO RELEASE replace path with method call of PathHelper (for
				// specific OS)
				BufferedReader vendorConf = new BufferedReader(new FileReader(
						"src/main/resources/marabou.properties"));

				properties.load(vendorConf);
				// copy all entries to the new conf
				persistSettings();
				vendorConf.close();
			} catch (IOException e) {
				log.warning("Couldn't create config file in "
						+ System.getProperty("user.home"));
				log.warning(e.toString());
			}
		}
		return 0;
	}

	/**
	 * returns the users setting to the given key
	 * 
	 */
	public static String getProp(PropertiesAllowedKeys key) {
		try {
			return (String) properties.get(key.toString());
		} catch (NullPointerException e) {
			System.err
					.println("You just found a bug in marabou. Marabou requested a config key from marabou.properties that is non existent.\n"
							+ "Please file a bug report and include this messages and what you just did.");
			return "";
		}
	}

	/**
	 * sets a setting and saves it to users config file
	 */
	public static void setProp(PropertiesAllowedKeys key, String value) {
		properties.setProperty(key.toString(), value);
		persistSettings();
	}

	// helper methods

	/**
	 * persists all changes of the users properties
	 */
	private static void persistSettings() {
		try {
			BufferedWriter userConf = new BufferedWriter(new FileWriter(
					conf.getAbsolutePath()));
			properties.store(userConf, null);
			// flush and close streams
			userConf.flush();
			userConf.close();
		} catch (IOException e) {
			log.severe("Couldn't save config file.");
		}
	}

}
