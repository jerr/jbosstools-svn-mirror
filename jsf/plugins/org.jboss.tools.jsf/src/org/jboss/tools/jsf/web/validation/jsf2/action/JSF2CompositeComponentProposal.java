/*******************************************************************************
 * Copyright (c) 2007-2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/

package org.jboss.tools.jsf.web.validation.jsf2.action;

import java.text.MessageFormat;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.ui.IMarkerResolution;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.jboss.tools.jsf.JSFModelPlugin;
import org.jboss.tools.jsf.jsf2.util.JSF2ResourceUtil;
import org.jboss.tools.jsf.messages.JSFUIMessages;

/**
 * 
 * @author yzhishko
 * 
 */

public class JSF2CompositeComponentProposal implements IMarkerResolution {
	private IResource resource;
	private String componentPath = null;
	private String[] attrs = null;
	private String elementName;

	public JSF2CompositeComponentProposal(IResource resource, String compPath, String elementName, String[] attrs) {
		this.resource = resource;
		this.componentPath = compPath;
		this.attrs = attrs;
		this.elementName=elementName;
	}

	@Override
	public String getLabel() {
		return MessageFormat.format(JSFUIMessages.Create_JSF_2_Composite_Component,elementName,
				componentPath);
	}

	@Override
	public void run(IMarker marker) {
		try{
			final IFile createdFile = JSF2ResourceUtil
					.createCompositeComponentFile(resource.getProject(),
							new Path(componentPath), attrs);
			if (createdFile != null) {
				IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow()
						.getActivePage(), createdFile);
			}
		}catch(CoreException ex){
			JSFModelPlugin.getPluginLog().logError(ex);
		}
	}

}
