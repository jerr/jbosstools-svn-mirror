/*******************************************************************************
 * Copyright (c) 2007-2009 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.jst.css.common;

import java.util.Map;

/**
 * @author Sergey Dzmitrovich
 * 
 */
public abstract class StyleContainer {

	public abstract Map<String, String> getStyleAttributes();

	public abstract void applyStyleAttributes(Map<String, String> attributes);

	public abstract Object getStyleObject();

	public boolean equals(Object obj) {
		if (obj instanceof StyleContainer)
			obj = ((StyleContainer) obj).getStyleObject();
		return getStyleObject().equals(obj);
	}

}
