package org.bedework.subsystem.extension.calendar;

import org.jboss.as.controller.AbstractRemoveStepHandler;
import org.jboss.logging.Logger;

/**
 * Handler responsible for removing the subsystem resource from the model
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 */
class BwCalendarSubsystemRemove extends AbstractRemoveStepHandler {

    static final BwCalendarSubsystemRemove INSTANCE = new BwCalendarSubsystemRemove();

    private final Logger log = Logger.getLogger(
            BwCalendarSubsystemRemove.class);

    private BwCalendarSubsystemRemove() {
    }
}
