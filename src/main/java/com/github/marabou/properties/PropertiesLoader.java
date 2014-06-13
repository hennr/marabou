package com.github.marabou.properties;

import com.github.marabou.helper.PathHelper;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

//TODO: write a cool test
public class PropertiesLoader {

    private Logger log = Logger.getLogger(PropertiesLoader.class.getName());

    public Properties loadProperties(InputStream userPropertiesStream) {
        try {
            Properties properties = new Properties();
            properties.load(userPropertiesStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load given input stream " + userPropertiesStream);
        }
    }

    void persistUserProperties(Properties userProperties, PathHelper pathHelper) {
        try {
            createUserPropertiesPath(pathHelper);
            writeUserProperties(userProperties, pathHelper);
        } catch (IOException e) {
            log.log(Level.SEVERE, "Couldn't save config file.", e);
        }
    }

    private void writeUserProperties(Properties userProperties, PathHelper pathHelper) throws IOException {
        BufferedWriter userConf = createWriter(pathHelper);
        userProperties.store(userConf, null);
        // flush and close streams
        userConf.flush();
        userConf.close();
    }

    private BufferedWriter createWriter(PathHelper pathHelper) throws IOException {
        return new BufferedWriter(new FileWriter(pathHelper.getUserPropertiesFilePath()));
    }

    private void createUserPropertiesPath(PathHelper pathHelper) throws IOException {
        File userPropertiesDirectory = pathHelper.getUserPropertiesDirectory();

        if (!userPropertiesDirectory.exists()) {
            FileUtils.forceMkdir(userPropertiesDirectory);
        }
    }

}
