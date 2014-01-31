package com.github.marabou.helper;

import org.junit.Test;

import java.util.Properties;

import static org.fest.assertions.api.Assertions.assertThat;

public class PropertiesHelperTest {

    @Test
    public void testGetApplicationProperties() {

        // given
        Properties appProperties = PropertiesHelper.getApplicationProperties();

        // expect
        assertThat(appProperties.getProperty("version")).isNotEqualTo("${project.version}");
    }
}
