package org.bedework.subsystem.extension.undertow;

import org.jboss.as.controller.AbstractRemoveStepHandler;
import org.jboss.logging.Logger;

/**
 * Handler responsible for removing the subsystem resource from the model
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
class BwUndertowSubsystemRemove extends AbstractRemoveStepHandler {

    static final BwUndertowSubsystemRemove INSTANCE = new BwUndertowSubsystemRemove();

    private final Logger log = Logger.getLogger(
            BwUndertowSubsystemRemove.class);

    private BwUndertowSubsystemRemove() {
    }
}
