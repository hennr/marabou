package com.github.marabou.properties;

import com.github.marabou.helper.PathHelper;
import com.github.marabou.helper.PropertiesLoader;
import com.github.marabou.helper.UnknownPlatformException;

import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class PropertiesHelperTest {

//    @Test
//    public void persistsUserProperties() throws Exception {
//
//        String propertiesFilePath = "/tmp/foo";
//
//        // given
//        PathHelper pathHelper = mock(PathHelper.class);
//        when(pathHelper.getMarabouHomeFolder()).thenReturn(propertiesFilePath);
//
//        PropertiesLoader propertiesLoader = new PropertiesLoader();
//        String propertiesContent = "foo=bar";
//        propertiesLoader.loadProperties(new ByteArrayInputStream(propertiesContent.getBytes()));
//
//        PropertiesHelper propertiesHelper = new PropertiesHelper(pathHelper, propertiesLoader);
//
//        //when
//        propertiesHelper.persistUserProperties();
//
//        // then
//        Properties properties = new Properties();
//        properties.load(this.getClass().getClassLoader().getResourceAsStream(propertiesFilePath));
//    }


    @Test(expected = RuntimeException.class)
    public void throwsRuntimeExceptionOnUnknownPlatform() throws UnknownPlatformException {

        // given
        PathHelper pathHelper = mock(PathHelper.class);
        when(pathHelper.getMarabouHomeFolder()).thenThrow(UnknownPlatformException.class);

        // expect exception
        new PropertiesHelper(pathHelper, new PropertiesLoader());
    }

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
    public void loadsDefaultUserPropertiesWhenFilePermissionsNotSufficient() {

        // given
        PathHelper pathHelperMock = mock(PathHelper.class);
        when(pathHelperMock.getUserPropertiesFilePath()).thenReturn("");
        when(pathHelperMock.getDefaultUserPropertiesPath()).thenReturn("");

        PropertiesLoader propertiesLoaderMock = mock(PropertiesLoader.class);

        // when
        new PropertiesHelper(pathHelperMock, propertiesLoaderMock);

        // then
        verify(pathHelperMock).getDefaultUserPropertiesPath();
    }

//    @Test
//    public void loadsUserPropertiesIfFileExistsAndCanBeRead() {
//
//
//        // TODO
//
//    }
}
