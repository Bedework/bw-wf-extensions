/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.subsystem.extension.calendar;

import org.bedework.subsystem.extension.calendar.indexing.IndexingResourceDefinition;
import org.bedework.subsystem.extension.calendar.indexing.IndexingResourceParser;
import org.bedework.subsystem.extension.calendar.system.SystemResourceDefinition;
import org.bedework.subsystem.extension.calendar.system.SystemResourceParser;

import org.jboss.as.controller.PathAddress;
import org.jboss.as.controller.operations.common.Util;
import org.jboss.as.controller.parsing.ParseUtils;
import org.jboss.as.controller.persistence.SubsystemMarshallingContext;
import org.jboss.dmr.ModelNode;
import org.jboss.staxmapper.XMLElementReader;
import org.jboss.staxmapper.XMLElementWriter;
import org.jboss.staxmapper.XMLExtendedStreamReader;
import org.jboss.staxmapper.XMLExtendedStreamWriter;

import java.util.List;

import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;

/**
 * User: mike Date: 7/2/22 Time: 21:36
 */
public class BwCalendarSubsystemParser
        implements XMLStreamConstants, XMLElementReader<List<ModelNode>>,
        XMLElementWriter<SubsystemMarshallingContext> {
  public void readElement(final XMLExtendedStreamReader reader,
                          final List<ModelNode> list) throws XMLStreamException {
    // Require no attributes
    ParseUtils.requireNoAttributes(reader);

    //Add the main subsystem 'add' operation
    final ModelNode addBwCalSub =
            Util.createAddOperation(PathAddress.pathAddress(
                    BwCalendarExtension.PATH_SUBSYSTEM));
    list.add(addBwCalSub);

    //Read the children
    while (reader.hasNext() && reader.nextTag() != END_ELEMENT) {
      if (reader.getLocalName().equals(
              IndexingResourceDefinition.TAG_NAME)) {
        IndexingResourceParser.read(list, reader);
      } else if (reader.getLocalName().equals(
              SystemResourceDefinition.TAG_NAME)) {
        SystemResourceParser.read(list, reader);
      } else {
        throw new XMLStreamException("Unknown keycloak-server subsystem tag: " + reader.getLocalName());
      }
    }
  }

  @Override
  public void writeContent(final XMLExtendedStreamWriter writer,
                           final SubsystemMarshallingContext context) throws XMLStreamException {
    context.startSubsystemElement(BwCalendarExtension.NAMESPACE, false);

    IndexingResourceParser.write(writer, context);
    SystemResourceParser.write(writer, context);

    writer.writeEndElement();
  }
}
