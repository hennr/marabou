package com.github.marabou.properties;

import com.github.marabou.helper.PathHelper;
import com.github.marabou.helper.PropertiesLoader;
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
        UserProperties userProperties = new UserProperties(new Properties(), new PropertiesHelper(new PathHelper(), new PropertiesLoader()));

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
        UserProperties userProperties = new UserProperties(properties, new PropertiesHelper(new PathHelper(), new PropertiesLoader()));

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
        UserProperties userProperties = new UserProperties(properties, new PropertiesHelper(new PathHelper(), new PropertiesLoader()));

        // when
        userProperties.setRememberWindowSize(true);

        //then
        assertEquals("true", properties.getProperty("rememberWindowSize"));
    }

    @Test
    public void getTagBarWeightReturnsCorrectDefaultValue() throws Exception {

        // given
        UserProperties userProperties = new UserProperties(new Properties(), new PropertiesHelper(new PathHelper(), new PropertiesLoader()));

        // expect
        TestCase.assertEquals("2", userProperties.getTagBarWeight());
    }

    @Test
    public void getTagBarWeightUsesCorrectPropertiesKey() throws Exception {

        // given
        Properties properties = new Properties();
        properties.put("tagBarWeight", "1");
        UserProperties userProperties = new UserProperties(properties, new PropertiesHelper(new PathHelper(), new PropertiesLoader()));

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
        UserProperties userProperties = new UserProperties(properties, new PropertiesHelper(new PathHelper(), new PropertiesLoader()));

        // when
        userProperties.setTagBarWeight(1);

        //then
        assertEquals("1", properties.getProperty("tagBarWeight"));
    }

    @Test
    public void getTableWeightReturnsCorrectDefaultValue() throws Exception {

        // given
        UserProperties userProperties = new UserProperties(new Properties(), new PropertiesHelper(new PathHelper(), new PropertiesLoader()));

        // expect
        TestCase.assertEquals("5", userProperties.getTableWeight());
    }

    @Test
    public void getTableWeightUsesCorrectPropertiesKey() throws Exception {

        // given
        Properties properties = new Properties();
        properties.put("tableWeight", "1");
        UserProperties userProperties = new UserProperties(properties, new PropertiesHelper(new PathHelper(), new PropertiesLoader()));

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
        UserProperties userProperties = new UserProperties(properties, new PropertiesHelper(new PathHelper(), new PropertiesLoader()));

        // when
        userProperties.setTableWeight(1);

        //then
        assertEquals("1", properties.getProperty("tableWeight"));
    }

    @Test
    public void getWindowSizeXReturnsCorrectDefaultValue() throws Exception {

        // given
        UserProperties userProperties = new UserProperties(new Properties(), new PropertiesHelper(new PathHelper(), new PropertiesLoader()));

        // expect
        TestCase.assertEquals(-1, userProperties.getWindowSizeX());
    }

    @Test
    public void getWindowSizeXUsesCorrectPropertiesKey() throws Exception {

        // given
        Properties properties = new Properties();
        properties.put("windowSizeX", "1");
        UserProperties userProperties = new UserProperties(properties, new PropertiesHelper(new PathHelper(), new PropertiesLoader()));

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
        UserProperties userProperties = new UserProperties(properties, new PropertiesHelper(new PathHelper(), new PropertiesLoader()));

        // when
        userProperties.setWindowSizeX(1);

        //then
        assertEquals("1", properties.getProperty("windowSizeX"));
    }

    @Test
    public void getWindowSizeYReturnsCorrectDefaultValue() throws Exception {

        // given
        UserProperties userProperties = new UserProperties(new Properties(), new PropertiesHelper(new PathHelper(), new PropertiesLoader()));

        // expect
        TestCase.assertEquals(-1, userProperties.getWindowSizeY());
    }

    @Test
    public void getWindowSizeYUsesCorrectPropertiesKey() throws Exception {

        // given
        Properties properties = new Properties();
        properties.put("windowSizeY", "1");
        UserProperties userProperties = new UserProperties(properties, new PropertiesHelper(new PathHelper(), new PropertiesLoader()));

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
        UserProperties userProperties = new UserProperties(properties, new PropertiesHelper(new PathHelper(), new PropertiesLoader()));

        // when
        userProperties.setWindowSizeY(1);

        //then
        assertEquals("1", properties.getProperty("windowSizeY"));
    }

    @Test
    public void rememberLastPathReturnsCorrectDefaultValue() throws Exception {

        // given
        UserProperties userProperties = new UserProperties(new Properties(), new PropertiesHelper(new PathHelper(), new PropertiesLoader()));

        // expect
        TestCase.assertEquals(true, userProperties.rememberLastPath());
    }

    @Test
    public void rememberLastPathUsesCorrectPropertiesKey() throws Exception {

        // given
        Properties properties = new Properties();
        properties.put("safeLastPath", "false");
        UserProperties userProperties = new UserProperties(properties, new PropertiesHelper(new PathHelper(), new PropertiesLoader()));

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
        UserProperties userProperties = new UserProperties(properties, new PropertiesHelper(new PathHelper(), new PropertiesLoader()));

        // when
        userProperties.setRememberLastPath(false);

        //then
        assertEquals("false", properties.getProperty("safeLastPath"));
    }

    @Test
    public void getLastPathReturnsCorrectDefaultValue() throws Exception {

        // given
        UserProperties userProperties = new UserProperties(new Properties(), new PropertiesHelper(new PathHelper(), new PropertiesLoader()));

        // expect
        TestCase.assertEquals("", userProperties.getLastPath());
    }

    @Test
    public void getLastPathUsesCorrectPropertiesKey() throws Exception {

        // given
        Properties properties = new Properties();
        properties.put("lastPath", "foo");
        UserProperties userProperties = new UserProperties(properties, new PropertiesHelper(new PathHelper(), new PropertiesLoader()));

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
        UserProperties userProperties = new UserProperties(properties, new PropertiesHelper(new PathHelper(), new PropertiesLoader()));

        // when
        userProperties.setLastPath("foo");

        //then
        assertEquals("foo", properties.getProperty("lastPath"));
    }
}
