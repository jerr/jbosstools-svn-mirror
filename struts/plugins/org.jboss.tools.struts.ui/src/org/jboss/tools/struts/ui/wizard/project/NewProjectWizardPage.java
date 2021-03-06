/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.struts.ui.wizard.project;

import org.jboss.tools.struts.StrutsPreference;
import org.jboss.tools.jst.web.project.helpers.NewWebProjectContext;
import org.jboss.tools.jst.web.ui.wizards.project.NewWebProjectWizardPage;

public class NewProjectWizardPage extends NewWebProjectWizardPage {

	public NewProjectWizardPage(NewWebProjectContext context) {
		super(context);
	}

	protected String getKey() {
		return "newStrutsProjectPage1";
	}

	protected String getProjectRootOption() {
		boolean useDefault = "yes".equals(StrutsPreference.USE_DEFAULT_PROJECT_ROOT.getValue());
		return (useDefault) ? null : StrutsPreference.DEFAULT_PROJECT_ROOT_DIR.getValue();
	}
	
}

