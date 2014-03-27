package com.github.marabou.properties;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.Properties;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class UserPropertiesTest {

    @Test
    public void rememberWindowSizeReturnsFalseAsDefault() throws Exception {

        // given
        UserProperties userProperties = new UserProperties(new Properties());

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
        UserProperties userProperties = new UserProperties(properties);

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
        UserProperties userProperties = new UserProperties(properties);

        // when
        userProperties.setRememberWindowSize(true);

        //then
        assertEquals("true", properties.getProperty("rememberWindowSize"));
    }

    @Test
    public void getTagBarWeightReturnsCorrectDefaultValue() throws Exception {

        // given
        UserProperties userProperties = new UserProperties(new Properties());

        // expect
        TestCase.assertEquals("2", userProperties.getTagBarWeight());
    }

    @Test
    public void getTagBarWeightUsesCorrectPropertiesKey() throws Exception {

        // given
        Properties properties = new Properties();
        properties.put("tagBarWeight", "1");
        UserProperties userProperties = new UserProperties(properties);

        // when
        String result = userProperties.getTagBarWeight();

        //then
        TestCase.assertEquals("1", result);
    }

    @Test
    public void setTagBarWeightUsesCorrectPropertiesKey() {

        // given
        Properties properties = new Properties();
        properties.put("tagBarWeight", "2");
        UserProperties userProperties = new UserProperties(properties);

        // when
        userProperties.setTagBarWeight(1);

        //then
        assertEquals("1", properties.getProperty("tagBarWeight"));
    }

    @Test
    public void getTableWeightReturnsCorrectDefaultValue() throws Exception {

        // given
        UserProperties userProperties = new UserProperties(new Properties());

        // expect
        TestCase.assertEquals("5", userProperties.getTableWeight());
    }

    @Test
    public void getTableWeightUsesCorrectPropertiesKey() throws Exception {

        // given
        Properties properties = new Properties();
        properties.put("tableWeight", "1");
        UserProperties userProperties = new UserProperties(properties);

        // when
        String result = userProperties.getTableWeight();

        //then
        TestCase.assertEquals("1", result);
    }

    @Test
    public void setTableWeightUsesCorrectPropertiesKey() {

        // given
        Properties properties = new Properties();
        properties.put("tableWeight", "2");
        UserProperties userProperties = new UserProperties(properties);

        // when
        userProperties.setTableWeight(1);

        //then
        assertEquals("1", properties.getProperty("tableWeight"));
    }

    @Test
    public void getWindowSizeXReturnsCorrectDefaultValue() throws Exception {

        // given
        UserProperties userProperties = new UserProperties(new Properties());

        // expect
        TestCase.assertEquals(-1, userProperties.getWindowSizeX());
    }

    @Test
    public void getWindowSizeXUsesCorrectPropertiesKey() throws Exception {

        // given
        Properties properties = new Properties();
        properties.put("windowSizeX", "1");
        UserProperties userProperties = new UserProperties(properties);

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
        UserProperties userProperties = new UserProperties(properties);

        // when
        userProperties.setWindowSizeX(1);

        //then
        assertEquals("1", properties.getProperty("windowSizeX"));
    }

    @Test
    public void getWindowSizeYReturnsCorrectDefaultValue() throws Exception {

        // given
        UserProperties userProperties = new UserProperties(new Properties());

        // expect
        TestCase.assertEquals(-1, userProperties.getWindowSizeY());
    }

    @Test
    public void getWindowSizeYUsesCorrectPropertiesKey() throws Exception {

        // given
        Properties properties = new Properties();
        properties.put("windowSizeY", "1");
        UserProperties userProperties = new UserProperties(properties);

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
        UserProperties userProperties = new UserProperties(properties);

        // when
        userProperties.setWindowSizeY(1);

        //then
        assertEquals("1", properties.getProperty("windowSizeY"));
    }
}
