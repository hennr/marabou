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
import junit.framework.TestCase;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Properties;

public class UserPropertiesTest {

    @Test
    public void rememberWindowSizeReturnsFalseAsDefault() throws Exception {

        // given
        UserProperties userProperties = emptyUserProperties();

        // when
        boolean result = userProperties.rememberWindowSize();

        //then
        assertFalse(result);
    }

    @Test
    public void rememberWindowSizeUsesCorrectPropertiesKey() throws Exception {

        // given
        Properties properties = new Properties();
        properties.put("rememberWindowSize", "true");
        UserProperties userProperties = userProperties(properties);

        // when
        boolean result = userProperties.rememberWindowSize();

        //then
        assertTrue(result);
    }

    @Test
    public void setRememberWindowSizeUsesCorrectPropertiesKey() {

        // given
        Properties properties = new Properties();
        properties.put("rememberWindowSize", "false");
        UserProperties userProperties = userProperties(properties);

        // when
        userProperties.setRememberWindowSize(true);

        //then
        assertEquals("true", properties.getProperty("rememberWindowSize"));
    }

    @Test
    public void getTagBarWeightReturnsCorrectDefaultValue() throws Exception {

        // given
        UserProperties userProperties = emptyUserProperties();

        // expect
        TestCase.assertEquals(2, userProperties.getTagBarWeight());
    }

    @Test
    public void getTagBarWeightUsesCorrectPropertiesKey() throws Exception {

        // given
        Properties properties = new Properties();
        properties.put("tagBarWeight", "1");
        UserProperties userProperties = userProperties(properties);

        // when
        int result = userProperties.getTagBarWeight();

        //then
        TestCase.assertEquals(1, result);
    }

    @Test
    public void setTagBarWeightUsesCorrectPropertiesKey() {

        // given
        Properties properties = new Properties();
        properties.put("tagBarWeight", "2");
        UserProperties userProperties = userProperties(properties);

        // when
        userProperties.setTagBarWeight(1);

        //then
        assertEquals("1", properties.getProperty("tagBarWeight"));
    }

    @Test
    public void getTableWeightReturnsCorrectDefaultValue() throws Exception {

        // given
        UserProperties userProperties = emptyUserProperties();

        // expect
        TestCase.assertEquals(5, userProperties.getTableWeight());
    }

    @Test
    public void getTableWeightUsesCorrectPropertiesKey() throws Exception {

        // given
        Properties properties = new Properties();
        properties.put("tableWeight", "1");
        UserProperties userProperties = userProperties(properties);

        // when
        int result = userProperties.getTableWeight();

        //then
        TestCase.assertEquals(1, result);
    }

    @Test
    public void setTableWeightUsesCorrectPropertiesKey() {

        // given
        Properties properties = new Properties();
        properties.put("tableWeight", "2");
        UserProperties userProperties = userProperties(properties);

        // when
        userProperties.setTableWeight(1);

        //then
        assertEquals("1", properties.getProperty("tableWeight"));
    }

    @Test
    public void getWindowSizeXReturnsCorrectDefaultValue() throws Exception {

        // given
        UserProperties userProperties = emptyUserProperties();

        // expect
        TestCase.assertEquals(-1, userProperties.getWindowSizeX());
    }

    @Test
    public void getWindowSizeXUsesCorrectPropertiesKey() throws Exception {

        // given
        Properties properties = new Properties();
        properties.put("windowSizeX", "1");
        UserProperties userProperties = userProperties(properties);

        // when
        int result = userProperties.getWindowSizeX();

        //then
        TestCase.assertEquals(1, result);
    }

    @Test
    public void setWindowSizeXUsesCorrectPropertiesKey() {

        // given
        Properties properties = new Properties();
        properties.put("windowSizeX", "-1");
        UserProperties userProperties = userProperties(properties);

        // when
        userProperties.setWindowSizeX(1);

        //then
        assertEquals("1", properties.getProperty("windowSizeX"));
    }

    @Test
    public void getWindowSizeYReturnsCorrectDefaultValue() throws Exception {

        // given
        UserProperties userProperties = emptyUserProperties();

        // expect
        TestCase.assertEquals(-1, userProperties.getWindowSizeY());
    }

    @Test
    public void getWindowSizeYUsesCorrectPropertiesKey() throws Exception {

        // given
        Properties properties = new Properties();
        properties.put("windowSizeY", "1");
        UserProperties userProperties = userProperties(properties);

        // when
        int result = userProperties.getWindowSizeY();

        //then
        TestCase.assertEquals(1, result);
    }

    @Test
    public void setWindowSizeYUsesCorrectPropertiesKey() {

        // given
        Properties properties = new Properties();
        properties.put("windowSizeY", "-1");
        UserProperties userProperties = userProperties(properties);

        // when
        userProperties.setWindowSizeY(1);

        //then
        assertEquals("1", properties.getProperty("windowSizeY"));
    }

    @Test
    public void rememberLastPathReturnsCorrectDefaultValue() throws Exception {

        // given
        UserProperties userProperties = emptyUserProperties();

        // expect
        TestCase.assertEquals(true, userProperties.rememberLastPath());
    }

    @Test
    public void rememberLastPathUsesCorrectPropertiesKey() throws Exception {

        // given
        Properties properties = new Properties();
        properties.put("safeLastPath", "false");
        UserProperties userProperties = userProperties(properties);

        // when
        boolean result = userProperties.rememberLastPath();

        //then
        TestCase.assertEquals(false, result);
    }

    @Test
    public void setRememberLastPathUsesCorrectPropertiesKey() {

        // given
        Properties properties = new Properties();
        properties.put("safeLastPath", "true");
        UserProperties userProperties = userProperties(properties);

        // when
        userProperties.setRememberLastPath(false);

        //then
        assertEquals("false", properties.getProperty("safeLastPath"));
    }

    @Test
    public void getLastPathReturnsCorrectDefaultValue() throws Exception {

        // given
        UserProperties userProperties = emptyUserProperties();

        // expect
        TestCase.assertEquals("", userProperties.getLastPath());
    }

    @Test
    public void getLastPathUsesCorrectPropertiesKey() throws Exception {

        // given
        Properties properties = new Properties();
        properties.put("lastPath", "foo");
        UserProperties userProperties = userProperties(properties);

        // when
        String result = userProperties.getLastPath();

        //then
        TestCase.assertEquals("foo", result);
    }

    @Test
    public void setLastPathUsesCorrectPropertiesKey() {

        // given
        Properties properties = new Properties();
        properties.put("lastPath", "");
        UserProperties userProperties = userProperties(properties);

        // when
        userProperties.setLastPath("foo");

        //then
        assertEquals("foo", properties.getProperty("lastPath"));
    }

    private UserProperties emptyUserProperties() {
        return new UserProperties(new Properties(), new PropertiesHelper(new PathHelper(), new PropertiesLoader(new PathHelper())));
    }

    private UserProperties userProperties(Properties properties) {
        return new UserProperties(properties, new PropertiesHelper(new PathHelper(), new PropertiesLoader(new PathHelper())));
    }
}
