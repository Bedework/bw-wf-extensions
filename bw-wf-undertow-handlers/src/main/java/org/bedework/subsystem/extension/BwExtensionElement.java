/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.subsystem.extension;

import java.util.HashMap;
import java.util.Map;

/**
 * User: mike Date: 11/5/23 Time: 22:18
 */
public enum BwExtensionElement {
  HANDLER("handler"),

  SYSTEM_AVAILABILITY("system-availability"),

  UNKNOWN(null);

  private final String name;

  BwExtensionElement(final String name) {
    this.name = name;
  }

  /**
   * Get the local name of this element.
   *
   * @return the local name
   */
  public String getLocalName() {
    return name;
  }

  private static final Map<String, BwExtensionElement> MAP;

  static {
    final Map<String, BwExtensionElement> map =
            new HashMap<String, BwExtensionElement>();
    for (final BwExtensionElement element: values()) {
      final String name = element.getLocalName();
      if (name != null) map.put(name, element);
    }
    MAP = map;
  }

  public static BwExtensionElement forName(final String localName) {
    final BwExtensionElement element = MAP.get(localName);
    if (element == null) {
      return UNKNOWN;
    }
    return element;
  }
}
