/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.subsystem.extension.calendar.indexing;

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
public class IndexingResourceDefinition
        extends SimpleResourceDefinition {
  public static final String TAG_NAME = "indexing";

  static final SimpleAttributeDefinition ACCOUNT =
          new SimpleAttributeDefinitionBuilder("account",
                                               ModelType.STRING, true)
                  .setAllowExpression(true)
                  .setDefaultValue(new ModelNode("admin"))
                  .setRestartAllServices()
                  .build();

  static final SimpleAttributeDefinition INDEXER_URL =
          new SimpleAttributeDefinitionBuilder("indexerUrl",
                                               ModelType.STRING, true)
                  .setAllowExpression(true)
                  .setDefaultValue(new ModelNode("${com.bedework.oschurls}"))
                  .setRestartAllServices()
                  .build();

  static final SimpleAttributeDefinition INDEXER_USER =
          new SimpleAttributeDefinitionBuilder("indexerUser",
                                               ModelType.STRING, true)
                  .setAllowExpression(true)
                  .setDefaultValue(new ModelNode("admin"))
                  .setRestartAllServices()
                  .build();

  static final SimpleAttributeDefinition INDEXER_PW =
          new SimpleAttributeDefinitionBuilder("indexerPw",
                                               ModelType.STRING, true)
                  .setAllowExpression(true)
                  .setDefaultValue(new ModelNode("admin"))
                  .setRestartAllServices()
                  .build();

  static final SimpleAttributeDefinition KEYSTORE =
          new SimpleAttributeDefinitionBuilder("keystore",
                                               ModelType.STRING, true)
                  .setAllowExpression(true)
                  .setDefaultValue(new ModelNode("${org.bedework.config.dir}/opensearch/config/osch.keystore"))
                  .setRestartAllServices()
                  .build();

  static final SimpleAttributeDefinition KEYSTORE_PW =
          new SimpleAttributeDefinitionBuilder("keystorePw",
                                               ModelType.STRING, true)
                  .setAllowExpression(true)
                  .setDefaultValue(new ModelNode("nopassword"))
                  .setRestartAllServices()
                  .build();

  static final List<AttributeDefinition> ALL_ATTRIBUTES = new ArrayList<>();

  static {
    ALL_ATTRIBUTES.add(ACCOUNT);
    ALL_ATTRIBUTES.add(INDEXER_URL);
    ALL_ATTRIBUTES.add(INDEXER_USER);
    ALL_ATTRIBUTES.add(INDEXER_PW);
    ALL_ATTRIBUTES.add(KEYSTORE);
    ALL_ATTRIBUTES.add(KEYSTORE_PW);
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

  @Override
  public void registerOperations(
          final ManagementResourceRegistration resourceRegistration) {
    super.registerOperations(resourceRegistration);

    resourceRegistration.registerOperationHandler(
            ListIndexOperation.DEFINITION,
            new ListIndexOperation());
  }

  public IndexingResourceDefinition() {
    super(PathElement.pathElement(TAG_NAME),
          BwCalendarExtension.getResourceDescriptionResolver(TAG_NAME),
          IndexingResourceAddHandler.INSTANCE,
          IndexingResourceRemoveHandler.INSTANCE
    );
  }

  public static SimpleAttributeDefinition lookup(final String name) {
    return (SimpleAttributeDefinition)DEFINITION_LOOKUP.get(name);
  }

}
