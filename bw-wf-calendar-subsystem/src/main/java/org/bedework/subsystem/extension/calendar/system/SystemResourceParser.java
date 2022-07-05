/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.subsystem.extension.calendar.system;

import org.bedework.subsystem.extension.BwExtensionDefinitions;
import org.bedework.subsystem.extension.calendar.BwCalendarExtension;

import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.PathElement;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.descriptions.ModelDescriptionConstants;
import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.dmr.ModelNode;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

import java.util.List;

import javax.xml.stream.XMLStreamException;

import static javax.xml.stream.XMLStreamConstants.END_ELEMENT;

/**
 * User: mike Date: 7/4/22 Time: 00:09
 */
public class SystemResourceParser {
  public static void read(final List<ModelNode> list,
                          final XMLExtendedStreamReader reader) throws XMLStreamException {
    final ModelNode system = new ModelNode();
    system.get(ModelDescriptionConstants.OP).
          set(ModelDescriptionConstants.ADD);

    final PathAddress addr = PathAddress.pathAddress(
            PathElement.pathElement(ModelDescriptionConstants.SUBSYSTEM,
                                    BwCalendarExtension.SUBSYSTEM_NAME),
            PathElement.pathElement(SystemResourceDefinition.TAG_NAME,
                                    BwExtensionDefinitions.defaultValue));
    system.get(ModelDescriptionConstants.OP_ADDR).set(addr.toModelNode());
    list.add(system);

    while (reader.hasNext() && (reader.nextTag() != END_ELEMENT)) {
      final String tagName = reader.getLocalName();

      final SimpleAttributeDefinition def =
              SystemResourceDefinition.lookup(tagName);
      if (def == null) {
        throw new XMLStreamException("Unknown theme tag " + tagName);
      }
      def.parseAndSetParameter(reader.getElementText(), system, reader);
    }
  }

  public static void write(final XMLExtendedStreamWriter writer,
                           final SubsystemMarshallingContext context) throws XMLStreamException {
    if (!context.getModelNode().
                get(SystemResourceDefinition.TAG_NAME).
                isDefined()) {
      return;
    }

    writer.writeStartElement(SystemResourceDefinition.TAG_NAME);

    final ModelNode elements =
            context.getModelNode().
                   get(SystemResourceDefinition.TAG_NAME,
                       BwExtensionDefinitions.defaultValue);
    for (final AttributeDefinition def: SystemResourceDefinition.ALL_ATTRIBUTES) {
      if (!elements.hasDefined(def.getName())) {
        continue;
      }
      def.marshallAsElement(elements, writer);
    }

    writer.writeEndElement();
  }
}
