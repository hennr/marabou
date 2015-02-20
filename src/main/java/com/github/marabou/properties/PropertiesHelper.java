/**
 * Copyright (C) 2012 - 2015 Jan-Hendrik Peters
 *
 * This file is part of marabou.
 *
 * Marabou is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package com.github.marabou.properties;

import com.github.marabou.helper.PathHelper;

import java.io.*;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PropertiesHelper {

    UserProperties userPropertiesInstance = null;
    private Logger log = LoggerFactory.getLogger(PropertiesHelper.class);
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

        return new UserProperties(properties, this);
    }

    UserProperties loadDefaultUserProperties() {
        InputStream userPropertiesStream = getClass().getClassLoader().getResourceAsStream(pathHelper.getUserPropertiesFileName());
        return  new UserProperties(propertiesLoader.loadProperties(userPropertiesStream), this);
    }

    public void persistUserProperties(Properties userProperties) {
        propertiesLoader.persistUserProperties(userProperties);
    }
}
