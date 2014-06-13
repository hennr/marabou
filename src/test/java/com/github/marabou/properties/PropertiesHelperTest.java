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
        PropertiesHelper propertiesHelper = spy( new PropertiesHelper(pathHelperMock, propertiesLoader));

        // when
        UserProperties userProperties = propertiesHelper.getUserProperties();

        // then
        verify(propertiesHelper).loadDefaultUserProperties();
        verify(propertiesHelper, times(0)).getExistingUserProperties();

        UserProperties defaultProperties = propertiesHelper.loadDefaultUserProperties();
        assertEquals(defaultProperties.properties, userProperties.properties);
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
