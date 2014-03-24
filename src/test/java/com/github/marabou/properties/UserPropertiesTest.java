package com.github.marabou.properties;

import org.junit.Test;

import java.util.Properties;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    public void rememberWindowSizeReturnsUserPropertiesValue() throws Exception {

        // given
        Properties properties = mock(Properties.class);
        when(properties.getProperty("rememberWindowSize", "false")).thenReturn("true");
        UserProperties userProperties = new UserProperties(properties);

        // when
        boolean result = userProperties.rememberWindowSize();

        //then
        assertTrue(result);
    }
}
