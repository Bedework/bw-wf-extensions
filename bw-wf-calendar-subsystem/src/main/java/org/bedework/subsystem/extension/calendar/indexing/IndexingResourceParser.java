/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.subsystem.extension.calendar.indexing;

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
public class IndexingResourceParser {
  public static void read(final List<ModelNode> list,
                          final XMLExtendedStreamReader reader) throws XMLStreamException {
    final ModelNode rnode = new ModelNode();
    rnode.get(ModelDescriptionConstants.OP).
          set(ModelDescriptionConstants.ADD);

    final PathAddress addr = PathAddress.pathAddress(
            PathElement.pathElement(ModelDescriptionConstants.SUBSYSTEM,
                                    BwCalendarExtension.SUBSYSTEM_NAME),
            PathElement.pathElement(IndexingResourceDefinition.TAG_NAME,
                                    BwExtensionDefinitions.defaultValue));
    rnode.get(ModelDescriptionConstants.OP_ADDR).set(addr.toModelNode());
    list.add(rnode);

    while (reader.hasNext() && (reader.nextTag() != END_ELEMENT)) {
      final String tagName = reader.getLocalName();

      final SimpleAttributeDefinition def =
              IndexingResourceDefinition.lookup(tagName);
      if (def == null) {
        throw new XMLStreamException("Unknown theme tag " + tagName);
      }
      def.parseAndSetParameter(reader.getElementText(), rnode, reader);
    }
  }

  public static void write(final XMLExtendedStreamWriter writer,
                           final SubsystemMarshallingContext context) throws XMLStreamException {
    if (!context.getModelNode().
                get(IndexingResourceDefinition.TAG_NAME).
                isDefined()) {
      return;
    }

    writer.writeStartElement(IndexingResourceDefinition.TAG_NAME);

    final ModelNode elements =
            context.getModelNode().
                   get(IndexingResourceDefinition.TAG_NAME,
                       BwExtensionDefinitions.defaultValue);
    for (final AttributeDefinition def: IndexingResourceDefinition.ALL_ATTRIBUTES) {
      if (!elements.hasDefined(def.getName())) {
        continue;
      }
      def.marshallAsElement(elements, writer);
    }

    writer.writeEndElement();
  }
}
