package com.github.marabou.helper;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Properties;

import static org.fest.assertions.api.Assertions.assertThat;

public class PropertiesHelperTest {

    @Test
    public void programVersionIsSetInApplicationProperties() {

        // given
        Properties appProperties = PropertiesHelper.getApplicationProperties();

        // expect
        assertThat(appProperties.getProperty("version")).isNotEqualTo("${project.version}");
    }

    @Test
    public void gettingDefaultTableToTagBarRatioWorks() {

        // given
        PropertiesHelper propertiesHelper = new PropertiesHelper();
        String tagBarWeight = propertiesHelper.getProp(PropertiesAllowedKeys.tagBarWeight);
        String tableWeight = propertiesHelper.getProp(PropertiesAllowedKeys.tableWeight);

        // expect
        assertThat(tagBarWeight).isEqualTo("2");
        assertThat(tableWeight).isEqualTo("5");
    }

    @Ignore
    @Test
    public void gettingTableToTagBarRatioFromPropertiesWorks() {

    }

}
