package com.github.marabou.properties;

import com.github.marabou.helper.PathHelper;

import org.junit.Test;

import java.io.File;
import java.io.InputStream;
import java.util.Properties;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class PropertiesHelperTest {

    @Test
    public void loadsDefaultUserPropertiesWhenNoneAreFound() {
        // given
        File userPropertiesDirectoryMock = mock(File.class);
        when(userPropertiesDirectoryMock.exists()).thenReturn(true);

        PathHelper pathHelperMock = mock(PathHelper.class);
        when(pathHelperMock.getUserPropertiesDirectory()).thenReturn(userPropertiesDirectoryMock);
        when(pathHelperMock.getUserPropertiesFileName()).thenReturn("marabou.properties");
        // make it look, like there is no user properties file available in the file system
        when(pathHelperMock.getUserPropertiesFilePath()).thenReturn("this-does-not-exist");

        PropertiesLoader propertiesLoader = mock(PropertiesLoader.class);
        when(propertiesLoader.loadProperties(any(InputStream.class))).thenReturn(new Properties());

        PropertiesHelper propertiesHelper = spy( new PropertiesHelper(pathHelperMock, propertiesLoader));

        // when
        UserProperties userProperties = propertiesHelper.getUserProperties();

        // then
        verify(propertiesHelper).loadDefaultUserProperties();
        verify(propertiesHelper, times(0)).getExistingUserProperties();

        UserProperties defaultProperties = propertiesHelper.loadDefaultUserProperties();
        assertEquals(defaultProperties.properties, userProperties.properties);
    }

    @Test
    public void createsNewUserPropertiesWhenNoneAreFound() {

        // given
        PathHelper pathHelperMock = mock(PathHelper.class);
        when(pathHelperMock.getUserPropertiesFileName()).thenReturn("this-does-not-exist");
        when(pathHelperMock.getUserPropertiesFilePath()).thenReturn("user.properties");

        PropertiesLoader propertiesLoaderMock = mock(PropertiesLoader.class);
        PropertiesHelper propertiesHelper = new PropertiesHelper(pathHelperMock, propertiesLoaderMock);

        // when
        propertiesHelper.getUserProperties();

        // then
        verify(propertiesLoaderMock).persistUserProperties(any(Properties.class));
    }

    @Test
    public void checksUserPropertiesFileExistenceCorrectly() {
        // given
        PathHelper pathHelperMock = mock(PathHelper.class);
        when(pathHelperMock.getUserPropertiesFilePath()).thenReturn("this-file-does-not-exist");
        PropertiesLoader propertiesLoaderMock = mock(PropertiesLoader.class);
        PropertiesHelper propertiesHelper = new PropertiesHelper(pathHelperMock, propertiesLoaderMock);

        // expect
        assertTrue(propertiesHelper.userPropertiesNonExistent());
    }

    @Test
    public void checksUserPropertiesFileReadabilityCorrectly() {
        // given
        PathHelper pathHelperMock = mock(PathHelper.class);
        when(pathHelperMock.getUserPropertiesFilePath()).thenReturn("this-file-does-not-exist");
        File f = mock(File.class);
        when(f.canRead()).thenReturn(true);

        PropertiesLoader propertiesLoaderMock = mock(PropertiesLoader.class);
        PropertiesHelper propertiesHelper = spy(new PropertiesHelper(pathHelperMock, propertiesLoaderMock));
        File nonReadableFile = mock(File.class);
        when(nonReadableFile.canRead()).thenReturn(false);

        doReturn(nonReadableFile).when(propertiesHelper).getUserPropertiesFile();

        // expect
        assertTrue(propertiesHelper.userPropertiesNotReadable());
    }
}
