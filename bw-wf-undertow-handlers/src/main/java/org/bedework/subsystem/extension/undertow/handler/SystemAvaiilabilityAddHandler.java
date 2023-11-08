/*
 * Copyright 2016 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @author tags. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.bedework.subsystem.extension.undertow.handler;

import org.bedework.subsystem.extension.undertow.BwUndertowExtensionDefinitions;

import org.jboss.as.controller.AbstractAddStepHandler;
import org.jboss.as.controller.AttributeDefinition;
import org.jboss.as.controller.OperationFailedException;
import org.jboss.as.controller.PathAddress;
import org.jboss.dmr.ModelNode;

import static java.lang.String.format;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADD;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.ADDRESS;
import static org.jboss.as.controller.descriptions.ModelDescriptionConstants.OP;

/**
 *
 */
public class SystemAvaiilabilityAddHandler extends AbstractAddStepHandler {
  public static SystemAvaiilabilityAddHandler INSTANCE =
          new SystemAvaiilabilityAddHandler();

  private SystemAvaiilabilityAddHandler() {}

  @Override
  protected void populateModel(final ModelNode operation,
                               final ModelNode model) throws OperationFailedException {
    // TODO: localize exception. get id number
    if (!operation.get(OP).asString().equals(ADD)) {
      throw new OperationFailedException("Unexpected operation for add system." +
                                                 " operation=" + operation);
    }

    final PathAddress address = PathAddress.pathAddress(operation.get(ADDRESS));
    final String lastVal = address.getLastElement().getValue();
    if (!lastVal.equals(BwUndertowExtensionDefinitions.defaultValue)) {
      throw new OperationFailedException(
              format("%s resource with name %s not allowed.",
                     SystemAvailabilityDefinition.TAG_NAME, lastVal));
    }

    for (final AttributeDefinition def: SystemAvailabilityDefinition.ATTRIBUTES) {
      def.validateAndSet(operation, model);
    }
  }
}
