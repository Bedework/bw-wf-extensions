package org.bedework.subsystem.extension.calendar;

import org.bedework.subsystem.extension.calendar.indexing.IndexingResourceDefinition;
import org.bedework.subsystem.extension.calendar.system.SystemResourceDefinition;
import org.bedework.util.logging.BwLogger;
import org.bedework.util.logging.Logged;

import org.jboss.as.controller.Extension;
import org.jboss.as.controller.ExtensionContext;
import org.jboss.as.controller.ModelVersion;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ResourceDefinition;
import org.jboss.as.controller.SubsystemRegistration;
import org.jboss.as.controller.descriptions.StandardResourceDescriptionResolver;
import org.jboss.as.controller.parsing.ExtensionParsingContext;
import org.jboss.as.controller.registry.ManagementResourceRegistration;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.SUBSYSTEM;

public class BwCalendarExtension implements Logged, Extension {

  /**
   * The name space used for the {@code subsystem} element
   */
  public static final String NAMESPACE = "urn:org.bedework.subsystem.calendar:1.0";

  /**
   * The name of our subsystem within the model.
   */
  public static final String SUBSYSTEM_NAME = "bedework-calendar";
  private static final ModelVersion MGMT_API_VERSION =
          ModelVersion.create(1, 0, 0);
  static final PathElement PATH_SUBSYSTEM =
          PathElement.pathElement(SUBSYSTEM, SUBSYSTEM_NAME);

  /**
   * The parser used for parsing our subsystem
   */
  private final BwCalendarSubsystemParser parser =
          new BwCalendarSubsystemParser();

  private static final String RESOURCE_NAME =
          BwCalendarExtension.class.getPackage().getName() + ".LocalDescriptions";

  protected static final PathElement SUBSYSTEM_PATH =
          PathElement.pathElement(SUBSYSTEM, SUBSYSTEM_NAME);

  public static final ResourceDefinition BwCalendarSubsystemResource =
          new BwCalendarSubsystemDefinition();

  public static final IndexingResourceDefinition indexingResourceDefinition =
          new IndexingResourceDefinition();

  public static final SystemResourceDefinition systemResourceDefinition =
          new SystemResourceDefinition();

  public static StandardResourceDescriptionResolver getResourceDescriptionResolver(
          final String... keyPrefix) {
    final StringBuilder prefix = new StringBuilder(SUBSYSTEM_NAME);
    for (final String kp : keyPrefix) {
      prefix.append('.').append(kp);
    }
    return new StandardResourceDescriptionResolver(
            prefix.toString(),
            RESOURCE_NAME,
            BwCalendarExtension.class.getClassLoader(),
            true,
            false);
  }

  @Override
  public void initializeParsers(final ExtensionParsingContext context) {
    context.setSubsystemXmlMapping(SUBSYSTEM_NAME, NAMESPACE, parser);
  }

  @Override
  public void initialize(final ExtensionContext context) {
    if (debug()) {
      debug("Activating Bedework Calendar Extension");
    }
    final SubsystemRegistration subsystem =
            context.registerSubsystem(SUBSYSTEM_NAME, MGMT_API_VERSION);

    final ManagementResourceRegistration subsystemRegistration =
            subsystem.registerSubsystemModel(BwCalendarSubsystemResource);

    subsystemRegistration.registerSubModel(systemResourceDefinition);
    subsystemRegistration.registerSubModel(indexingResourceDefinition);

    subsystem.registerXMLElementWriter(parser);
  }

  /* ==============================================================
   *                   Logged methods
   * ============================================================== */

  private final BwLogger logger = new BwLogger();

  @Override
  public BwLogger getLogger() {
    if ((logger.getLoggedClass() == null) && (logger.getLoggedName() == null)) {
      logger.setLoggedClass(getClass());
    }

    return logger;
  }
}
