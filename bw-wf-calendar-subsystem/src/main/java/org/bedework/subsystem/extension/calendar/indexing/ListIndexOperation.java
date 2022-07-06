/* ********************************************************************
    Appropriate copyright notice
*/
package org.bedework.subsystem.extension.calendar.indexing;

import org.bedework.indexer.BwIndexCtlMBean;
import org.bedework.subsystem.extension.calendar.BwCalendarExtension;
import org.bedework.util.jmx.MBeanUtil;
import org.bedework.util.logging.BwLogger;
import org.bedework.util.logging.Logged;

import org.jboss.as.controller.OperationContext;
import org.jboss.as.controller.OperationDefinition;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.OperationStepHandler;
import org.jboss.as.controller.SimpleOperationDefinitionBuilder;
import org.jboss.dmr.ModelNode;
import org.jboss.dmr.ModelType;

/**
 * User: mike Date: 7/5/22 Time: 15:06
 */
public class ListIndexOperation implements Logged, OperationStepHandler {
  public static final String OPERATION_NAME = "list-indexes";

  public static final OperationDefinition DEFINITION = new SimpleOperationDefinitionBuilder(OPERATION_NAME, BwCalendarExtension.getResourceDescriptionResolver())
          .setRuntimeOnly()
          .setReadOnly()
          .setReplyType(ModelType.STRING)
          .build();

  @Override
  public void execute(final OperationContext context,
                      final ModelNode operation) throws OperationFailedException {
    try {
      final var indexer =
              (BwIndexCtlMBean)MBeanUtil.getMBean(
              BwIndexCtlMBean.class,
              "org.bedework.bwengine:service=indexing");

      context.getResult().set(indexer.listIndexes());
    } catch (final Throwable t) {
      throw new OperationFailedException(t);
    }
  }

  /* ==============================================================
   *                   Logged methods
   * ============================================================== */

  private final BwLogger logger = new BwLogger();

  @Override
  public BwLogger getLogger() {
    if ((logger.getLoggedClass() == null) && (logger.getLoggedName() == null)) {
      logger.setLoggedClass(getClass());
    }

    return logger;
  }
}
