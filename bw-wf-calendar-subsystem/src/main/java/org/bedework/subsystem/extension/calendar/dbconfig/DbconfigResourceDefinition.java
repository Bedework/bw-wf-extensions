/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.subsystem.extension.calendar.dbconfig;

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
public class DbconfigResourceDefinition
        extends SimpleResourceDefinition {
  public static final String TAG_NAME = "dbconfig";

  static final SimpleAttributeDefinition HIBERNATE_DIALECT =
          new SimpleAttributeDefinitionBuilder("hibernateDialect",
                                               ModelType.STRING, true)
                  .setAllowExpression(true)
                  .setDefaultValue(new ModelNode("org.hibernate.dialect.H2Dialect"))
                  .setRestartAllServices()
                  .build();

  static final List<AttributeDefinition> ALL_ATTRIBUTES = new ArrayList<>();

  static {
    ALL_ATTRIBUTES.add(HIBERNATE_DIALECT);
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

    for (final AttributeDefinition def: ALL_ATTRIBUTES) {
      resourceRegistration.registerReadWriteAttribute(
              def, null, WRITE_ATTR_HANDLER);
    }
  }

  public DbconfigResourceDefinition() {
    super(PathElement.pathElement(TAG_NAME),
          BwCalendarExtension.getResourceDescriptionResolver(TAG_NAME),
          DbconfigResourceAddHandler.INSTANCE,
          DbconfigResourceRemoveHandler.INSTANCE
    );
  }

  public static SimpleAttributeDefinition lookup(final String name) {
    return (SimpleAttributeDefinition)DEFINITION_LOOKUP.get(name);
  }

}
