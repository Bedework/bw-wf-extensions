/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.subsystem.extension.calendar.system;

import org.bedework.subsystem.extension.calendar.BwCalendarExtension;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.ReloadRequiredWriteAttributeHandler;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.registry.ManagementResourceRegistration;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: mike Date: 7/2/22 Time: 23:08
 */
public class SystemResourceDefinition
        extends SimpleResourceDefinition {
  public static final String TAG_NAME = "system";

  static final SimpleAttributeDefinition EVENTREG_ADMIN_TOKEN =
          new SimpleAttributeDefinitionBuilder("eventregAdminToken",
                                               ModelType.STRING, true)
                  .setAllowExpression(true)
                  .setRestartAllServices()
                  .build();

  static final SimpleAttributeDefinition EVENTREG_URL =
          new SimpleAttributeDefinitionBuilder("eventregUrl",
                                               ModelType.STRING, true)
                  .setAllowExpression(true)
                  .setDefaultValue(new ModelNode("http://localhost:8080/eventreg/"))
                  .setRestartAllServices()
                  .build();

  static final SimpleAttributeDefinition ROOT_USERS =
          new SimpleAttributeDefinitionBuilder("rootUsers",
                                               ModelType.STRING, true)
                  .setAllowExpression(true)
                  .setDefaultValue(new ModelNode("admin"))
                  .setRestartAllServices()
                  .build();

  static final SimpleAttributeDefinition SUGGESTION_ENABLED =
          new SimpleAttributeDefinitionBuilder("suggestionEnabled",
                                               ModelType.BOOLEAN, true)
                  .setAllowExpression(true)
                  .setDefaultValue(new ModelNode("false"))
                  .setRestartAllServices()
                  .build();

  static final SimpleAttributeDefinition SYSTEMID =
          new SimpleAttributeDefinitionBuilder("systemid",
                                               ModelType.STRING, true)
                  .setAllowExpression(true)
                  .setDefaultValue(new ModelNode("demobedework@mysite.edu"))
                  .setRestartAllServices()
                  .build();

  static final SimpleAttributeDefinition TZID =
          new SimpleAttributeDefinitionBuilder("tzid",
                                               ModelType.STRING, true)
                  .setAllowExpression(true)
                  .setDefaultValue(new ModelNode("America/New_York"))
                  .setRestartAllServices()
                  .build();

  static final SimpleAttributeDefinition WORKFLOW_ENABLED =
          new SimpleAttributeDefinitionBuilder("workflowEnabled",
                                               ModelType.BOOLEAN, true)
                  .setAllowExpression(true)
                  .setDefaultValue(new ModelNode("false"))
                  .setRestartAllServices()
                  .build();

  static final List<AttributeDefinition> ALL_ATTRIBUTES = new ArrayList<>();

  static {
    ALL_ATTRIBUTES.add(EVENTREG_ADMIN_TOKEN);
    ALL_ATTRIBUTES.add(EVENTREG_URL);
    ALL_ATTRIBUTES.add(ROOT_USERS);
    ALL_ATTRIBUTES.add(SUGGESTION_ENABLED);
    ALL_ATTRIBUTES.add(SYSTEMID);
    ALL_ATTRIBUTES.add(TZID);
    ALL_ATTRIBUTES.add(WORKFLOW_ENABLED);
  }

  protected static final ReloadRequiredWriteAttributeHandler WRITE_ATTR_HANDLER = new ReloadRequiredWriteAttributeHandler(ALL_ATTRIBUTES);

  private static final Map<String, AttributeDefinition> DEFINITION_LOOKUP = new HashMap<>();
  static {
    for (final AttributeDefinition def: ALL_ATTRIBUTES) {
      DEFINITION_LOOKUP.put(def.getXmlName(), def);
    }
  }

  @Override
  public void registerAttributes(
          final ManagementResourceRegistration resourceRegistration) {
    super.registerAttributes(resourceRegistration);

    for (final AttributeDefinition def : ALL_ATTRIBUTES) {
      resourceRegistration.registerReadWriteAttribute(
              def, null, WRITE_ATTR_HANDLER);
    }
  }

  public SystemResourceDefinition() {
    super(PathElement.pathElement(TAG_NAME),
          BwCalendarExtension.getResourceDescriptionResolver(TAG_NAME),
          SystemResourceAddHandler.INSTANCE,
          SystemResourceRemoveHandler.INSTANCE
    );
  }

  public static SimpleAttributeDefinition lookup(final String name) {
    return (SimpleAttributeDefinition)DEFINITION_LOOKUP.get(name);
  }

}
