/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.subsystem.extension;

import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.Property;

import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADDRESS;

/** Used to update the bedework config which is jmx based from the
 * wildfly config.
 *
 * User: mike Date: 7/2/22 Time: 23:27
 */
public class BwCalendarConfigService {
  public static final BwCalendarConfigService INSTANCE = new BwCalendarConfigService();

  static ModelNode fullConfig = new ModelNode();

  private BwCalendarConfigService() {
  }

  public void updateConfig(final ModelNode operation,
                           final ModelNode config) {
    PathAddress address = PathAddress.pathAddress(operation.get(ADDRESS));
    address = address.subAddress(1); // remove root (subsystem=keycloak-server)

    final ModelNode newConfig = fullConfig.clone();
    ModelNode subNode = newConfig;
    for (final PathElement pathElement: address) {
      subNode = subNode.get(pathElement.getKey(), pathElement.getValue());
    }

    subNode.set(config.clone());

    // remove undefined properties
    for (final Property prop: subNode.asPropertyList()) {
      if (!prop.getValue().isDefined()) {
        subNode.remove(prop.getName());
      }
    }

    fullConfig = newConfig;
  }

  public ModelNode get(final String... names) {
    return fullConfig.get(names);
  }

  public String getStringVal(final String... names) {
    final var mn = fullConfig.get(names);
    if (!mn.isDefined()) {
      return null;
    }

    return mn.asString();
  }

  ModelNode getConfig() {
    return fullConfig.clone();
  }
}
