package com.github.marabou.properties;

import com.github.marabou.helper.PathHelper;

import org.junit.Test;
import static org.junit.Assert.*;

public class ApplicationPropertiesTest {

    @Test
    public void programVersionIsSetInApplicationProperties() {

        // given
        ApplicationProperties appProperties = new PropertiesHelper(new PathHelper(), new PropertiesLoader(new PathHelper())).getApplicationProperties();

        // expect
        assertNotEquals("${project.version}", appProperties.getVersion());
    }

}
