package com.github.marabou.properties;

import com.github.marabou.helper.PathHelper;
import com.github.marabou.helper.PropertiesLoader;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class PropertiesHelperTest {

    @Test
    public void returnsApplicationPropertiesObject() {

        //given
        PropertiesHelper propertiesHelper = new PropertiesHelper(new PathHelper(), new PropertiesLoader());

        // when
        ApplicationProperties applicationProperties = propertiesHelper.getApplicationProperties();

        // then
        assertNotNull(applicationProperties);
    }

    @Test
    public void returnsUserPropertiesObject() {

        //given
        PropertiesHelper propertiesHelper = new PropertiesHelper(new PathHelper(), new PropertiesLoader());

        // when
        UserProperties userProperties = propertiesHelper.getUserProperties();

        // then
        assertNotNull(userProperties);
    }

    @Test
    public void loadsDefaultUserPropertiesWhenNoneAreFound() {

        // given
        PathHelper pathHelperMock = mock(PathHelper.class);
        when(pathHelperMock.getDefaultUserPropertiesPath()).thenReturn("this-does-not-exist");
        when(pathHelperMock.getUserPropertiesFilePath()).thenReturn("");

        PropertiesLoader propertiesLoaderMock = mock(PropertiesLoader.class);
        PropertiesHelper propertiesHelper = new PropertiesHelper(pathHelperMock, propertiesLoaderMock);

        // when
        propertiesHelper.getUserProperties();

        // then
        verify(pathHelperMock).getDefaultUserPropertiesPath();
    }
}
