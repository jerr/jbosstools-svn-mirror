/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.tools.esb.core.model.impl;

import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.esb.core.model.ESBCustomizedObjectImpl;

/**
 * @author Viacheslav Kabanovich
 */
public class ContentBasedRouter extends ESBCustomizedObjectImpl {
	private static final long serialVersionUID = 1L;
	public static String ATTR_CBR_ALIAS = "cbr alias";
	public static String ATTR_RULE_LANGUAGE = "rule language";
	public static String ATTR_RULE_SET = "rule set";
	public static String ATTR_RULE_RELOAD = "rule reload";

	public ContentBasedRouter() {}

	@Override
	public boolean isAttributeEditable(String name) {
		if(ATTR_RULE_LANGUAGE.equals(name)) {
			String alias = getAttributeValue(ATTR_CBR_ALIAS);
			if(isRegexOrXPath(alias)) {
				return false;
			}
		} else if(ATTR_RULE_RELOAD.equals(name)) {
			String ruleSet = getAttributeValue(ATTR_RULE_SET);
			if(ruleSet == null || ruleSet.length() == 0) {
				return false;
			}
		}
		return super.isAttributeEditable(name);
	}

	private boolean isRegexOrXPath(String alias) {
		return ("Regex".equalsIgnoreCase(alias) || "Xpath".equalsIgnoreCase(alias));
	}

	@Override
	protected void onAttributeValueEdit(String name, String oldValue,
			String newValue) throws XModelException {
		if(ATTR_CBR_ALIAS.equals(name)) {
			if(isRegexOrXPath(newValue)) {
				setAttributeValue(ATTR_RULE_LANGUAGE, "");
			}
		} else if(ATTR_RULE_SET.equals(name)) {
			if(newValue == null || newValue.length() == 0) {
				setAttributeValue(ATTR_RULE_RELOAD, getModelEntity().getAttribute(ATTR_RULE_RELOAD).getDefaultValue());
			}
		}
	}

	
}
