package org.bedework.subsystem.extension.calendar;

import org.bedework.subsystem.extension.BwCalendarConfigService;
import org.bedework.subsystem.extension.BwExtensionDefinitions;

import org.jboss.as.subsystem.test.AbstractSubsystemBaseTest;
import org.junit.After;

import java.io.IOException;
import java.util.Properties;

/**
 */
public class SubsystemParsingTestCase extends
        AbstractSubsystemBaseTest {
  public SubsystemParsingTestCase() {
    super(BwCalendarExtension.SUBSYSTEM_NAME, new BwCalendarExtension());
  }

  @After
  public void cleanup() throws Exception {
    final BwCalendarConfigService config =
            BwCalendarConfigService.INSTANCE;

    final var ru = config.getStringVal("system",
                                       BwExtensionDefinitions.defaultValue,
                                       "rootUsers");
    System.out.println("rootUsers=" + ru);
    super.cleanup();
  }

  @Override
  protected Properties getResolvedProperties() {
    final Properties properties = new Properties();
    properties.put("jboss.home.dir", System.getProperty("java.io.tmpdir"));
    properties.put("org.bedework.config.dir", "$(jboss.home.dir}/standalone/configuration/bedework");
    properties.put("org.bedework.oschurls", "http://localhost:9200");
    return properties;
  }

  @Override
  protected String getSubsystemXml() throws IOException {
    return readResource("bwcalendar-extension-1.0.xml");
  }

  @Override
  protected String getSubsystemXsdPath() throws Exception {
    return "schema/jboss-as-bwcalendar_1_0.xsd";
  }

  @Override
  protected String[] getSubsystemTemplatePaths() throws IOException {
    return new String[]{
            "/subsystem-templates/keycloak-server.xml"
    };
  }
}
