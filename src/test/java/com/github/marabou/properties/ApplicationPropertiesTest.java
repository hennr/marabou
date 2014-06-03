package com.github.marabou.properties;

import com.github.marabou.helper.PathHelper;
import com.github.marabou.helper.PropertiesLoader;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;

public class ApplicationPropertiesTest {

    @Test
    public void programVersionIsSetInApplicationProperties() {

        // given
        ApplicationProperties appProperties = new PropertiesHelper(new PathHelper(), new PropertiesLoader()).getApplicationProperties();

        // expect
        assertThat(appProperties.getVersion()).isNotEqualTo("${project.version}");
    }

}
