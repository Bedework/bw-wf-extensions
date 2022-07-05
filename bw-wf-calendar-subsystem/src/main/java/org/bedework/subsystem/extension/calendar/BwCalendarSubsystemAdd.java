package org.bedework.subsystem.extension.calendar;

import org.jboss.as.controller.AbstractBoottimeAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;

/**
 * Handler responsible for adding the subsystem resource to the model
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
class BwCalendarSubsystemAdd extends AbstractBoottimeAddStepHandler {

  static final BwCalendarSubsystemAdd INSTANCE = new BwCalendarSubsystemAdd();

  private final Logger log = Logger.getLogger(BwCalendarSubsystemAdd.class);

  private BwCalendarSubsystemAdd() {
  }

  @Override
  protected void populateModel(final ModelNode operation,
                               final ModelNode model) {

  }

  @Override
  protected void performBoottime(final OperationContext context,
                                 final ModelNode operation,
                                 final ModelNode model) {
  }
}
