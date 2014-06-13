package com.github.marabou.properties;

import com.github.marabou.helper.PathHelper;
import com.github.marabou.helper.PropertiesLoader;

import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

public class PropertiesHelperTest {

    @Test
    public void loadsDefaultUserPropertiesWhenNoneAreFound() {

        // given
        PathHelper pathHelperMock = mock(PathHelper.class);
        when(pathHelperMock.getDefaultUserPropertiesPath()).thenReturn("marabou.properties");
        // make it look, like there is no user properties file available in the file system
        when(pathHelperMock.getUserPropertiesFilePath()).thenReturn("this-does-not-exist");

        PropertiesLoader propertiesLoader = new PropertiesLoader();
        PropertiesHelper propertiesHelper = new PropertiesHelper(pathHelperMock, propertiesLoader);

        Properties defaultProperties = new Properties();
        try {
            defaultProperties.load(this.getClass().getClassLoader().getResourceAsStream("marabou.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // when
        UserProperties userProperties = propertiesHelper.getUserProperties();

        // then
        assertEquals(defaultProperties, userProperties.properties);
    }

    @Ignore
    @Test
    public void createsNewUserPropertiesWhenNoneAreFound() {

        // given
        PathHelper pathHelperMock = mock(PathHelper.class);
        when(pathHelperMock.getDefaultUserPropertiesPath()).thenReturn("this-does-not-exist");
        when(pathHelperMock.getUserPropertiesFilePath()).thenReturn("");

        PropertiesLoader propertiesLoaderMock = mock(PropertiesLoader.class);
        PropertiesHelper propertiesHelper = new PropertiesHelper(pathHelperMock, propertiesLoaderMock);

        // when
        propertiesHelper.getUserProperties();

        // then
        fail();
    }
}
