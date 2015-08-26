/**
 * Marabou - Audio Tagger
 * <p>
 * Copyright (C) 2012 - 2015 Jan-Hendrik Peters
 * <p>
 * https://github.com/hennr/marabou
 * <p>
 * Marabou is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package com.github.marabou.properties;

import com.github.marabou.helper.PathHelper;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
