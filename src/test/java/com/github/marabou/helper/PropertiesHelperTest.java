package com.github.marabou.helper;

import com.github.marabou.properties.ApplicationProperties;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Properties;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.fail;

public class PropertiesHelperTest {

    @Test
    public void programVersionIsSetInApplicationProperties() {

        // given
        ApplicationProperties appProperties = PropertiesHelper.getApplicationProperties();

        // expect
        assertThat(appProperties.getVersion()).isNotEqualTo("${project.version}");
    }

    @Test
    public void gettingDefaultTableToTagBarRatioWorks() {

        // given
        PropertiesHelper propertiesHelper = new PropertiesHelper(new PathHelper());
        String tagBarWeight = propertiesHelper.getProp(PropertiesAllowedKeys.tagBarWeight);
        String tableWeight = propertiesHelper.getProp(PropertiesAllowedKeys.tableWeight);

        // expect
        assertThat(tagBarWeight).isEqualTo("2");
        assertThat(tableWeight).isEqualTo("5");
    }

    @Test
    public void afterCreationUserPropertiesAreAccessible() {

        // given
        PropertiesHelper propertiesHelper = new PropertiesHelper(new PathHelper());

        // expect
        assertThat(propertiesHelper.getProp(PropertiesAllowedKeys.rememberWindowSize) != null);
    }

    @Test(expected = RuntimeException.class)
    public void throwsRuntimeExceptionOnUnknownPlatform() throws UnknownPlatformException {

        // given
        PathHelper pathHelper = Mockito.mock(PathHelper.class);
        Mockito.when(pathHelper.getMarabouHomeFolder()).thenThrow(UnknownPlatformException.class);

        // expect exception
        new PropertiesHelper(pathHelper);
    }

    @Ignore
    @Test
    public void gettingTableToTagBarRatioFromPropertiesWorks() {

    }

    @Ignore
    @Test
    public void testGetPropertyDefault() throws IOException {

        // given
        // TODO inject mocked properties object in the PropertiesHelper
        Properties properties = new Properties();
        properties.load(new ByteArrayInputStream(new String("key=").getBytes("UTF-8")));
        Object value = properties.getProperty("bar", "default");

        // expect
        assertThat(value).isEqualTo("");
    }

}
