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
package org.jboss.tools.jsf.ui.attribute.adapter;

import java.util.Set;
import java.util.TreeSet;

import org.jboss.tools.common.model.ui.attribute.IListContentProvider;
import org.jboss.tools.common.model.ui.attribute.adapter.DefaultComboBoxValueAdapter;
import org.eclipse.jface.viewers.Viewer;

import org.jboss.tools.common.model.XModelObject;

public class DefaultRendererKitIdAdapter extends DefaultComboBoxValueAdapter implements IListContentProvider {

	public Object getAdapter(Class adapter) {
		if (adapter == IListContentProvider.class) {
			return this;
		}
		return super.getAdapter(adapter);
	}

	public Object[] getElements(Object inputElement) {
		XModelObject o = modelObject.getParent().getChildByPath("Render Kits"); //$NON-NLS-1$
		if(o == null) return new String[0];
		Set<String> tags = new TreeSet<String>();
		XModelObject[] cs = o.getChildren();
		for (int i = 0; i < cs.length; i++) {
			tags.add(cs[i].getAttributeValue("render-kit-id")); //$NON-NLS-1$
		}
		return tags.toArray(new String[0]);
	}

	public void dispose() {}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
	
}
