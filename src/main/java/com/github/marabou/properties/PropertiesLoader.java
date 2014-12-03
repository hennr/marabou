package com.github.marabou.properties;

import com.github.marabou.helper.PathHelper;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TODO: write a cool test
public class PropertiesLoader {

    private final PathHelper pathHelper;
    private Logger log = LoggerFactory.getLogger(PropertiesLoader.class);

    public PropertiesLoader(PathHelper pathHelper) {
        this.pathHelper = pathHelper;
    }

    public Properties loadProperties(InputStream userPropertiesStream) {
        try {
            Properties properties = new Properties();
            properties.load(userPropertiesStream);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Couldn't load given input stream " + userPropertiesStream);
        }
    }

    void persistUserProperties(Properties userProperties) {
        try {
            createUserPropertiesDirectoryIfNonExistent();
            writeUserProperties(userProperties);
        } catch (IOException e) {
            log.error("Couldn't save config file.", e);
        }
    }

    private void createUserPropertiesDirectoryIfNonExistent() throws IOException {
        File userPropertiesDirectory = pathHelper.getUserPropertiesDirectory();

        if (!userPropertiesDirectory.exists()) {
            FileUtils.forceMkdir(userPropertiesDirectory);
        }
    }

    private void writeUserProperties(Properties userProperties) throws IOException {
        BufferedWriter userConf = createWriter(pathHelper.getUserPropertiesFilePath());
        userProperties.store(userConf, null);
        userConf.flush();
        userConf.close();
    }

    private BufferedWriter createWriter(String filePath) throws IOException {
        return new BufferedWriter(new FileWriter(filePath));
    }

}
