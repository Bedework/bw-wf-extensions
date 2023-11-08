package org.bedework.subsystem.extension.undertow;

import org.jboss.as.controller.AbstractBoottimeAddStepHandler;
import org.jboss.as.controller.OperationContext;
import org.jboss.dmr.ModelNode;
import org.jboss.logging.Logger;

/**
 * Handler responsible for adding the subsystem resource to the model
 *
 */
class BwUndertowSubsystemAdd extends AbstractBoottimeAddStepHandler {

  static final BwUndertowSubsystemAdd INSTANCE = new BwUndertowSubsystemAdd();

  private final Logger log = Logger.getLogger(BwUndertowSubsystemAdd.class);

  private BwUndertowSubsystemAdd() {
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
