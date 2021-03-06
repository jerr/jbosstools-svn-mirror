/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.deltacloud.ui.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jboss.tools.common.log.StatusFactory;
import org.jboss.tools.deltacloud.core.DeltaCloudInstance;
import org.jboss.tools.deltacloud.core.job.DestroyCloudInstanceJob;
import org.jboss.tools.deltacloud.ui.Activator;
import org.jboss.tools.deltacloud.ui.views.CVMessages;
import org.jboss.tools.internal.deltacloud.ui.utils.WorkbenchUtils;

/**
 * @author Andre Dietisheim
 */
public class DestroyInstanceHandler extends AbstractInstanceHandler {
	private final static String DESTROY_INSTANCE_TITLE = "DestroyInstancesDialog.title"; //$NON-NLS-1$
	private final static String DESTROY_INSTANCE_MSG = "DestroyInstancesDialog.msg"; //$NON-NLS-1$

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		try {
			if (selection instanceof IStructuredSelection) {
				if (isSingleInstanceSelected(selection)) {
					DeltaCloudInstance cvInstance = WorkbenchUtils.getFirstAdaptedElement(selection, DeltaCloudInstance.class);
					destroyInstance(cvInstance);
				} else {
					destroyWithDialog((IStructuredSelection) selection);
				}
			}

			return Status.OK_STATUS;
		} catch (Exception e) {
			String errorMessage = CVMessages.getFormattedString("DestroyInstancesDialogError.msg",
					getInstanceNames(selection));
			return StatusFactory.getInstance(IStatus.ERROR, Activator.PLUGIN_ID, errorMessage, e);
		}
	}

	@SuppressWarnings("unchecked")
	private void destroyWithDialog(IStructuredSelection selection) {
		List<DeltaCloudInstance> deltaCloudInstances = 
			WorkbenchUtils.adapt((List<DeltaCloudInstance>) selection.toList(),DeltaCloudInstance.class);
		List<DeltaCloudInstance> destroyableInstances = getDestroyableInstances(deltaCloudInstances);
		DeltaCloudInstanceDialog dialog = new DeltaCloudInstanceDialog(
				WorkbenchUtils.getActiveShell()
					, destroyableInstances
					, CVMessages.getString(DESTROY_INSTANCE_TITLE)
					, CVMessages.getString(DESTROY_INSTANCE_MSG));
		dialog.setInitialElementSelections(destroyableInstances);
		if (Dialog.OK == dialog.open()) {
			destroyInstances(dialog.getResult());
		}
	}
	
	private List<DeltaCloudInstance> getDestroyableInstances(List<DeltaCloudInstance> deltaCloudInstances) {
		ArrayList<DeltaCloudInstance> stoppedInstances = new ArrayList<DeltaCloudInstance>();
		for (DeltaCloudInstance instance : deltaCloudInstances) {
			if (instance.canDestroy()) {
				stoppedInstances.add(instance);
			}
		}
		return stoppedInstances;
	}


	private void destroyInstances(Object[] cvInstances) {
		for (int i = 0; i < cvInstances.length; i++) {
			destroyInstance((DeltaCloudInstance) cvInstances[i]);
		}
	}

	private void destroyInstance(DeltaCloudInstance instance) {
		if (instance != null) {
			new DestroyCloudInstanceJob("Destroy" + instance.getName(), instance)
					.schedule();
		}
	}
}
