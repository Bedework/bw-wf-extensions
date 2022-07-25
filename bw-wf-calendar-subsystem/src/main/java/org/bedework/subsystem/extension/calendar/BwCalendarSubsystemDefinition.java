package org.bedework.subsystem.extension.calendar;

import org.jboss.as.controller.SimpleResourceDefinition;
import org.jboss.as.controller.operations.common.GenericSubsystemDescribeHandler;
import org.jboss.as.controller.registry.ManagementResourceRegistration;

/**
 * @author <a href="tcerar@redhat.com">Tomaz Cerar</a>
 */
public class BwCalendarSubsystemDefinition extends SimpleResourceDefinition {
  public static final BwCalendarSubsystemDefinition INSTANCE = new BwCalendarSubsystemDefinition();

  BwCalendarSubsystemDefinition() {
    super(BwCalendarExtension.SUBSYSTEM_PATH,
          BwCalendarExtension.getResourceDescriptionResolver("subsystem"),
          //We always need to add an 'add' operation
          BwCalendarSubsystemAdd.INSTANCE,
          //Every resource that is added, normally needs a remove operation
          BwCalendarSubsystemRemove.INSTANCE);
  }

  @Override
  public void registerOperations(
          final ManagementResourceRegistration resourceRegistration) {
    super.registerOperations(resourceRegistration);

    resourceRegistration.registerOperationHandler(
            GenericSubsystemDescribeHandler.DEFINITION,
            GenericSubsystemDescribeHandler.INSTANCE);
  }
}
