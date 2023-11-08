/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.subsystem.extension.undertow.handler;

import org.bedework.util.logging.BwLogger;
import org.bedework.util.logging.Logged;

import io.undertow.Handlers;
import io.undertow.predicate.Predicate;
import io.undertow.server.HttpHandler;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinition;
import org.jboss.as.controller.SimpleAttributeDefinitionBuilder;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;
import org.wildfly.extension.undertow.AbstractHandlerDefinition;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * User: mike Date: 10/31/23 Time: 15:29
 */
public class SystemAvailabilityDefinition
        extends AbstractHandlerDefinition
        implements Logged {
  public static final String TAG_NAME = "system-availability";

  public static final SimpleAttributeDefinition SERVER_ONLINE = new SimpleAttributeDefinitionBuilder("server-online", ModelType.BOOLEAN)
          .setAllowExpression(true)
          .setRequired(true)
          .build();

  public static final SimpleAttributeDefinition SERVER_READONLY = new SimpleAttributeDefinitionBuilder("server-readonly", ModelType.BOOLEAN)
          .setAllowExpression(true)
          .setRequired(true)
          .build();

  public static final SimpleAttributeDefinition READONLY_FROM = new SimpleAttributeDefinitionBuilder("readonly-from", ModelType.STRING)
          .setAllowExpression(true)
          .setRequired(false)
          .build();

  public static final SimpleAttributeDefinition READONLY_TO = new SimpleAttributeDefinitionBuilder("readonly-to", ModelType.STRING)
          .setAllowExpression(true)
          .setRequired(false)
          .build();

  public static final SimpleAttributeDefinition READONLY_FILE = new SimpleAttributeDefinitionBuilder("readonly-file", ModelType.STRING)
          .setAllowExpression(true)
          .setRequired(false)
          .build();

  public static final Collection<SimpleAttributeDefinition> ATTRIBUTES = Collections.unmodifiableCollection(
          Arrays.asList(SERVER_ONLINE, SERVER_READONLY,
                        READONLY_FROM, READONLY_TO, READONLY_FILE));

  private static final Map<String, AttributeDefinition> DEFINITION_LOOKUP = new HashMap<>();
  static {
    for (final AttributeDefinition def: ATTRIBUTES) {
      DEFINITION_LOOKUP.put(def.getXmlName(), def);
    }
  }

  public static final SystemAvailabilityDefinition INSTANCE =
          new SystemAvailabilityDefinition();

  /* Do this backwards */
  private static SystemAvailabilityHandler handler;

  private SystemAvailabilityDefinition() {
    super("system-availability", "filter",
          SystemAvaiilabilityAddHandler.INSTANCE,
          SystemAvailabilityRemoveHandler.INSTANCE);
  }

  public void registerHandler(final SystemAvailabilityHandler val) {
    handler = val;


  }

                              @Override
  public HttpHandler createHttpHandler(
          final Predicate predicate,
          final ModelNode model,
          final HttpHandler next) {
    final var serverOnline =
            model.get(SERVER_ONLINE.getName()).asBoolean();
    final var serverReadOnly =
            model.get(SERVER_READONLY.getName()).asBoolean();
    final var readOnlyFrom =
            model.get(READONLY_FROM.getName()).asString();
    final var readOnlyTo =
            model.get(READONLY_TO.getName()).asString();
    final var readOnlyFile =
            model.get(READONLY_FILE.getName()).asString();

    final SystemAvailabilityHandler handler =
            new SystemAvailabilityHandler(
                    serverOnline,
                    serverReadOnly,
                    readOnlyFrom,
                    readOnlyTo,
                    Paths.get(readOnlyFile));
    handler.setNext(next);
    if(predicate == null) {
      return handler;
    } else {
      return Handlers.predicate(predicate, handler, next);
    }
  }

  public static SimpleAttributeDefinition lookup(final String name) {
    return (SimpleAttributeDefinition)DEFINITION_LOOKUP.get(name);
  }

  /* ==============================================================
   *                   Logged methods
   * ============================================================== */

  private final BwLogger logger = new BwLogger();

  @Override
  public BwLogger getLogger() {
    if ((logger.getLoggedClass() == null) &&
            (logger.getLoggedName() == null)) {
      logger.setLoggedClass(getClass());
    }

    return logger;
  }
}
