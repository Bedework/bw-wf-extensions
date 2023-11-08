/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.subsystem.extension.undertow;

import org.bedework.subsystem.extension.BwExtensionElement;
import org.bedework.subsystem.extension.undertow.handler.SystemAvailabilityDefinition;

import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.controller.parsing.ParseUtils;
import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.dmr.ModelNode;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLElementWriter;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

import java.util.EnumSet;
import java.util.List;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

import static org.jboss.as.controller.parsing.ParseUtils.requireNoAttributes;
import static org.jboss.as.controller.parsing.ParseUtils.unexpectedElement;

/**
 * User: mike Date: 7/2/22 Time: 21:36
 */
public class BwUndertowSubsystemParser
        implements XMLStreamConstants,
        XMLElementReader<List<ModelNode>>,
        XMLElementWriter<SubsystemMarshallingContext> {
  public void readElement(final XMLExtendedStreamReader reader,
                          final List<ModelNode> list)
          throws XMLStreamException {
    // Require no attributes
    ParseUtils.requireNoAttributes(reader);

    final PathAddress subsystemPathAddress = PathAddress.pathAddress(
            BwUndertowExtension.PATH_SUBSYSTEM);
    final ModelNode addBwCalSub = Util.createAddOperation(
            subsystemPathAddress);
    // add the subsystem to the ModelNode(s)
    list.add(addBwCalSub);

    // elements
    final EnumSet<BwExtensionElement> encountered =
            EnumSet.noneOf(BwExtensionElement.class);

    while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
      final BwExtensionElement element =
              BwExtensionElement.forName(reader.getLocalName());
      if (!encountered.add(element)) {
        throw unexpectedElement(reader);
      }
      switch (element) {
        case HANDLER: {
          parseHandler(reader, list, subsystemPathAddress);
          break;
        }
        default: {
          throw unexpectedElement(reader);
        }
      }
    }
  }

  static void parseHandler(final XMLExtendedStreamReader reader,
                           final List<ModelNode> operations,
                           final PathAddress subsystemPathAddress)
          throws XMLStreamException {
    requireNoAttributes(reader);
    while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
      final BwExtensionElement element = BwExtensionElement.forName(
              reader.getLocalName());
      switch (element) {
        case SYSTEM_AVAILABILITY: {
          parseSystemAvailability(reader, operations,
                                  subsystemPathAddress);
          break;
        }
        default: {
          throw unexpectedElement(reader);
        }
      }
    }
  }

  static void parseSystemAvailability(
          final XMLExtendedStreamReader reader,
          final List<ModelNode> operations,
          final PathAddress subsystemPathAddress)
          throws XMLStreamException {
    final ModelNode addOperation = Util.createAddOperation();
    while (reader.hasNext() && (reader.nextTag() != END_ELEMENT)) {
      final String tagName = reader.getLocalName();

      final SimpleAttributeDefinition def =
              SystemAvailabilityDefinition.lookup(tagName);
      if (def == null) {
        throw new XMLStreamException("Unknown theme tag " + tagName);
      }
      def.parseAndSetParameter(reader.getElementText(), addOperation,
                               reader);
    }
  }

  @Override
  public void writeContent(final XMLExtendedStreamWriter writer,
                           final SubsystemMarshallingContext context)
          throws XMLStreamException {
    context.startSubsystemElement(BwUndertowExtension.NAMESPACE,
                                  false);
    final ModelNode bwUndertowSubsystem = context.getModelNode();

    writeHandlerElement(writer, bwUndertowSubsystem);

    writer.writeEndElement();
  }

  private void writeHandlerElement(
          final XMLExtendedStreamWriter writer,
          final ModelNode subSystem) throws XMLStreamException {
    boolean started = false;
    if (subSystem.hasDefined(SystemAvailabilityDefinition.TAG_NAME)) {
      writer.writeStartElement(
              BwExtensionElement.HANDLER.getLocalName());
      started = true;
      writeSystemAvailability(writer,
                              subSystem.get(
                                      SystemAvailabilityDefinition.TAG_NAME));
    }
    /*
    if (subSystem.hasDefined(<some-other-tag>)) {
      if(!started) {
        writer.writeStartElement(BwExtensionElement.HANDLER.getLocalName());
        started = true;
      }
      writeOtherThing(writer, SomeOtherDefinition.TAG_NAME);
    } */
    if (started) {
      writer.writeEndElement();
    }
  }

  private void writeSystemAvailability(
          final XMLExtendedStreamWriter writer,
          final ModelNode subSystem) throws XMLStreamException {
    writer.writeStartElement(
            BwExtensionElement.SYSTEM_AVAILABILITY.getLocalName());

    for (final SimpleAttributeDefinition ad:
            SystemAvailabilityDefinition.ATTRIBUTES) {
      ad.marshallAsAttribute(subSystem, writer);
    }
    writer.writeEndElement();
  }
}
