/**
 * Marabou - Audio Tagger
 * Copyright (C) 2012 - 2016 Jan-Hendrik Peters
 * https://github.com/hennr/marabou
 * Marabou is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
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

        PropertiesHelper propertiesHelper = spy(new PropertiesHelper(pathHelperMock, propertiesLoader));

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
        verify(propertiesLoaderMock).persistUserProperties(any());
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
