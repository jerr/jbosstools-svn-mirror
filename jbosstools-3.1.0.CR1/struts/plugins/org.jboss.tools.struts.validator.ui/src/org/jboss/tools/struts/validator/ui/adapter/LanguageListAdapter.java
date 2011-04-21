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
package org.jboss.tools.struts.validator.ui.adapter;

import org.jboss.tools.common.model.ui.attribute.IListContentProvider;
import org.jboss.tools.common.model.ui.attribute.adapter.*;
import org.jboss.tools.common.meta.*;

public class LanguageListAdapter extends DefaultComboBoxValueAdapter {

	protected IListContentProvider createListContentProvider(XAttribute attribute) {
		LanguageListContentProvider p = new LanguageListContentProvider();
		p.setAttribute(attribute);
		return p;	
	}
	
}
