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
package org.jboss.tools.cdi.ui.wizard;

import org.eclipse.jdt.ui.wizards.NewAnnotationWizardPage;
import org.jboss.tools.cdi.ui.CDIUIMessages;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class NewInterceptorBindingCreationWizard extends NewCDIAnnotationCreationWizard {

    public NewInterceptorBindingCreationWizard() {
    	setWindowTitle(CDIUIMessages.NEW_INTERCEPTOR_BINDING_WIZARD_TITLE);
    }

	protected NewAnnotationWizardPage createAnnotationWizardPage() {
		return new NewInterceptorBindingWizardPage();
	}

}
