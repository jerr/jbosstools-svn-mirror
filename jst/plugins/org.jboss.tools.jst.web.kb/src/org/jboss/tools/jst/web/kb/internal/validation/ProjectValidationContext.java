 /*******************************************************************************
  * Copyright (c) 2007 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributors:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.jboss.tools.jst.web.kb.internal.validation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.jboss.tools.common.el.core.ELReference;
import org.jboss.tools.common.xml.XMLUtilities;
import org.jboss.tools.jst.web.kb.validation.IProjectValidationContext;
import org.w3c.dom.Element;

/**
 * Contains information for validators that must be saved between
 * validation invoking.
 * @author Alexey Kazakov
 */
public class ProjectValidationContext implements IProjectValidationContext {

	// We should load/save these collections between eclipse sessions.
	private Map<String, LinkCollection> coreLinks = new HashMap<String, LinkCollection>();
	private Map<String, ELValidatorContext> elLinks = new HashMap<String, ELValidatorContext>();

	private Map<String, Set<String>> oldVariableNamesForELValidation = new HashMap<String, Set<String>>();

	private ValidationResourceRegister validationResourceRegister;

	private LinkCollection getCoreLinks(String validatorId) {
		LinkCollection linkCollection = coreLinks.get(validatorId);
		if(linkCollection==null) {
			linkCollection = new LinkCollection(validatorId);
			coreLinks.put(validatorId, linkCollection);
		}
		return linkCollection;
	}

	private ELValidatorContext getElLinks(String validatorId) {
		ELValidatorContext linkCollection = elLinks.get(validatorId);
		if(linkCollection==null) {
			linkCollection = new ELValidatorContext(validatorId);
			elLinks.put(validatorId, linkCollection);
		}
		return linkCollection;
	}

	private Set<String> getOldVariableNamesForELValidation(String validatorId) {
		Set<String> linkCollection = oldVariableNamesForELValidation.get(validatorId);
		if(linkCollection==null) {
			linkCollection = new HashSet<String>();
			oldVariableNamesForELValidation.put(validatorId, linkCollection);
		}
		return linkCollection;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#addLinkedCoreResource(java.lang.String, org.eclipse.core.runtime.IPath, boolean)
	 */
	public void addLinkedCoreResource(String validatorId, String variableName, IPath linkedResourcePath, boolean declaration) {
		getCoreLinks(validatorId).addLinkedResource(variableName, linkedResourcePath, declaration);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#removeLinkedCoreResource(java.lang.String, org.eclipse.core.runtime.IPath)
	 */
	public void removeLinkedCoreResource(String validatorId, String name, IPath linkedResourcePath) {
		getCoreLinks(validatorId).removeLinkedResource(name, linkedResourcePath);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#removeLinkedCoreResources(java.util.Set)
	 */
	public void removeLinkedCoreResources(String validatorId, Set<IPath> resources) {
		getCoreLinks(validatorId).removeLinkedResources(resources);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#removeLinkedCoreResource(org.eclipse.core.runtime.IPath)
	 */
	public void removeLinkedCoreResource(String validatorId, IPath resource) {
		getCoreLinks(validatorId).removeLinkedResource(resource);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#getCoreResourcesByVariableName(java.lang.String, boolean)
	 */
	public Set<IPath> getCoreResourcesByVariableName(String validatorId, String variableName, boolean declaration) {
		return getCoreLinks(validatorId).getResourcesByVariableName(variableName, declaration);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#getVariableNamesByCoreResource(org.eclipse.core.runtime.IPath, boolean)
	 */
	public Set<String> getVariableNamesByCoreResource(String validatorId, IPath fullPath, boolean declaration) {
		return getCoreLinks(validatorId).getVariableNamesByResource(fullPath, declaration);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#addUnnamedCoreResource(org.eclipse.core.runtime.IPath)
	 */
	public void addUnnamedCoreResource(String validatorId, IPath fullPath) {
		getCoreLinks(validatorId).addUnnamedResource(fullPath);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#getUnnamedCoreResources()
	 */
	public Set<IPath> getUnnamedCoreResources(String validatorId) {
		return getCoreLinks(validatorId).getUnnamedResources();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#removeUnnamedCoreResource(org.eclipse.core.runtime.IPath)
	 */
	public void removeUnnamedCoreResource(String validatorId, IPath fullPath) {
		getCoreLinks(validatorId).removeUnnamedResource(fullPath);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#addUnnamedElResource(org.eclipse.core.runtime.IPath)
	 */
	public void addUnnamedElResource(String validatorId, IPath fullPath) {
		getElLinks(validatorId).addUnnamedResource(fullPath);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#getUnnamedElResources()
	 */
	public Set<IPath> getUnnamedElResources() {
		Set<IPath> result = new HashSet<IPath>();
		for (ELValidatorContext elLinksCollection : elLinks.values()) {
			result.addAll(elLinksCollection.getUnnamedResources());
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#removeUnnamedElResource(org.eclipse.core.runtime.IPath)
	 */
	public void removeUnnamedElResource(IPath fullPath) {
		for (ELValidatorContext links : elLinks.values()) {
			links.removeUnnamedResource(fullPath);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#addVariableNameForELValidation(java.lang.String)
	 */
	public void addVariableNameForELValidation(String validatorId, String name) {
		getOldVariableNamesForELValidation(validatorId).add(name);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#removeLinkedEls(java.util.Set)
	 */
	public void removeLinkedEls(Set<IFile> resorces) {
		for (ELValidatorContext links : elLinks.values()) {
			links.removeLinkedEls(resorces);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#getElsForValidation(java.util.Set, boolean)
	 */
	public Set<ELReference> getElsForValidation(Set<IFile> changedFiles, boolean onlyChangedVariables) {
		Set<ELReference> result = new HashSet<ELReference>();
		for (String id : elLinks.keySet()) {
			ELValidatorContext elLinksCollection = getElLinks(id);
			Set<String> oldVariableNamesForELValidation = getOldVariableNamesForELValidation(id);
			// Collect all ELs which use new variables names
			for(IResource resource : changedFiles) {
				Set<String> newNames = getVariableNamesByCoreResource(id, resource.getFullPath(), true);
				if(newNames!=null) {
					for (String newName : newNames) {
						if(!onlyChangedVariables || ! oldVariableNamesForELValidation.contains(newName)) {
							Set<ELReference> els = elLinksCollection.getElsByVariableName(newName);
							if(els!=null) {
								result.addAll(els);
							}
						}
					}
				}
				for (String oldName :oldVariableNamesForELValidation) {
					if(!onlyChangedVariables || newNames==null || !newNames.contains(oldName)) {
						Set<ELReference> els = elLinksCollection.getElsByVariableName(oldName);
						if(els!=null) {
							result.addAll(els);
						}
					}
				}
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#clearAll()
	 */
	public void clearAll() {
		for (LinkCollection links : coreLinks.values()) {
			links.clearAll();
		}
		coreLinks.clear();
		for (ELValidatorContext links : elLinks.values()) {
			links.clearAll();
		}
		elLinks.clear();
		oldVariableNamesForELValidation.clear();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#clearOldVariableNameForElValidation()
	 */
	public void clearOldVariableNameForElValidation() {
		oldVariableNamesForELValidation.clear();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#addLinkedEl(java.lang.String, org.jboss.tools.jst.web.kb.validation.ELReference)
	 */
	public void addLinkedEl(String variableName, ELReference el) {
		for (ELValidatorContext elLinksCollection : elLinks.values()) {
			elLinksCollection.addLinkedEl(variableName, el);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#removeLinkedEl(java.lang.String, org.jboss.tools.jst.web.kb.validation.ELReference)
	 */
	public void removeLinkedEl(String name, ELReference el) {
		for (ELValidatorContext elLinksCollection : elLinks.values()) {
			elLinksCollection.removeLinkedEl(name, el);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#getElsByVariableName(java.lang.String)
	 */
	public Set<ELReference> getElsByVariableName(String variableName) {
		Set<ELReference> result = new HashSet<ELReference>();
		for (ELValidatorContext elLinksCollection : elLinks.values()) {
			result.addAll(elLinksCollection.getElsByVariableName(variableName));
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#store(org.w3c.dom.Element)
	 */
	public void store(Element root) {
		Element validation = XMLUtilities.createElement(root, "validation"); //$NON-NLS-1$
		for (LinkCollection links : coreLinks.values()) {
			Element core = XMLUtilities.createElement(validation, "core"); //$NON-NLS-1$
			core.setAttribute("validator-id", links.getId()); //$NON-NLS-1$
			links.store(core);
		}
		for (ELValidatorContext links : elLinks.values()) {
			Element el = XMLUtilities.createElement(validation, "el"); //$NON-NLS-1$
			el.setAttribute("validator-id", links.getId()); //$NON-NLS-1$
			links.store(el);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IValidationContext#load(org.w3c.dom.Element)
	 */
	public void load(Element root) {
		Element validation = XMLUtilities.getUniqueChild(root, "validation"); //$NON-NLS-1$
		if(validation == null) return;
		Element[] cores = XMLUtilities.getChildren(validation, "core"); //$NON-NLS-1$
		for (Element core : cores) {
			String id = core.getAttribute("validator-id"); //$NON-NLS-1$
			if(id!=null && id.trim().length()>0) {
				getCoreLinks(id).load(core);
			}
		}
		Element[] els = XMLUtilities.getChildren(validation, "el"); //$NON-NLS-1$
		for (Element el : els) {
			String id = el.getAttribute("validator-id"); //$NON-NLS-1$
			if(id!=null && id.trim().length()>0) {
				getElLinks(id).load(el);
			}
		}
	}

	public int getModificationsSinceLastStore() {
		int result = 0;
		for (LinkCollection links : coreLinks.values()) {
			result = result + links.getModificationsSinceLastStore();
		}
		for (ELValidatorContext links : elLinks.values()) {
			result = result + links.getModificationsSinceLastStore();
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IProjectValidationContext#setValidationResourceRegister(org.jboss.tools.jst.web.kb.internal.validation.ValidationResourceRegister)
	 */
	public void setValidationResourceRegister(ValidationResourceRegister validationResourceRegister) {
		this.validationResourceRegister = validationResourceRegister;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jboss.tools.jst.web.kb.validation.IProjectValidationContext#getValidationResourceRegister()
	 */
	public ValidationResourceRegister getValidationResourceRegister() {
		return validationResourceRegister;
	}
}