/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.smooks.configuration.editors.wizard.IStructuredDataSelectionWizard;

/**
 * @author Dart Peng
 * @Date Aug 6, 2008
 */
public class StructuredDataSelectionWizardDailog extends WizardDialog {

	public StructuredDataSelectionWizardDailog(Shell parentShell,
			IWizard newWizard) {
		super(parentShell, newWizard);
	}
	
	public IStructuredDataSelectionWizard getCurrentCreationWizard(){
		IWizard w = getWizard();
		if(w != null && w instanceof IStructuredDataSelectionWizard){
			return (IStructuredDataSelectionWizard)w;
		}
		return null;
	}

}
