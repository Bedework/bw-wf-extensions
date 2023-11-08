/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.subsystem.extension.undertow;

import org.bedework.subsystem.extension.undertow.handler.SystemAvailabilityDefinition;
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

/**
 * User: mike Date: 10/31/23 Time: 16:27
 */
public class BwUndertowExtension implements Logged, Extension {
  /**
   * The name space used for the {@code subsystem} element
   */
  public static final String NAMESPACE = "urn:org.bedework.subsystem.undertow:1.0";

  /**
   * The name of our subsystem within the model.
   */
  public static final String SUBSYSTEM_NAME = "bedework-undertow";
  private static final ModelVersion MGMT_API_VERSION =
          ModelVersion.create(1, 0, 0);
  static final PathElement PATH_SUBSYSTEM =
          PathElement.pathElement(SUBSYSTEM, SUBSYSTEM_NAME);

  /**
   * The parser used for parsing our subsystem
   */
  private final BwUndertowSubsystemParser parser =
          new BwUndertowSubsystemParser();

  private static final String RESOURCE_NAME =
          BwUndertowExtension.class.getPackage().getName() + ".LocalDescriptions";

  protected static final PathElement SUBSYSTEM_PATH =
          PathElement.pathElement(SUBSYSTEM, SUBSYSTEM_NAME);

  public static final ResourceDefinition systemAvailabilityResource =
          SystemAvailabilityDefinition.INSTANCE;

  public static StandardResourceDescriptionResolver getResourceDescriptionResolver(
          final String... keyPrefix) {
    final StringBuilder prefix = new StringBuilder(SUBSYSTEM_NAME);
    for (final String kp : keyPrefix) {
      prefix.append('.').append(kp);
    }
    return new StandardResourceDescriptionResolver(
            prefix.toString(),
            RESOURCE_NAME,
            BwUndertowExtension.class.getClassLoader(),
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
      debug("Activating Bedework Undertow Extension");
    }
    final SubsystemRegistration subsystem =
            context.registerSubsystem(SUBSYSTEM_NAME, MGMT_API_VERSION);

    final ManagementResourceRegistration subsystemRegistration =
            subsystem.registerSubsystemModel(systemAvailabilityResource);

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
