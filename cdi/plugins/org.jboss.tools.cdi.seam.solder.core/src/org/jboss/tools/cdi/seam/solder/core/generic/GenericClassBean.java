/******************************************************************************* 
 * Copyright (c) 2011 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.cdi.seam.solder.core.generic;

import java.util.Set;

import org.jboss.tools.cdi.core.IBean;
import org.jboss.tools.cdi.internal.core.impl.AbstractBeanElement;
import org.jboss.tools.cdi.internal.core.impl.ClassBean;
import org.jboss.tools.cdi.internal.core.impl.definition.AbstractMemberDefinition;
import org.jboss.tools.cdi.seam.solder.core.CDISeamSolderConstants;

/**
 * 
 * @author Viacheslav Kabanovich
 *
 */
public class GenericClassBean extends ClassBean {
	protected AbstractMemberDefinition genericProducerBean;
	
	public GenericClassBean() {}

	public void setGenericProducerBeanDefinition(AbstractMemberDefinition def) {
		genericProducerBean = def;
	}

	public IBean getGenericProducerBean() {
		Set<IBean> bs = getCDIProject().getBeans(genericProducerBean.getTypeDefinition().getType().getPath());
		for (IBean b: bs) {
			if(b instanceof AbstractBeanElement) {
				if(((AbstractBeanElement)b).getDefinition() == genericProducerBean) {
					return b;
				}
			}
		}
		return null;
	}

	protected void computeScope() {
		if(definition.isAnnotationPresent(CDISeamSolderConstants.APPLY_SCOPE_ANNOTATION_TYPE_NAME)) {
			IBean generic = getGenericProducerBean();
			if(generic != null) {
				scope = generic.getScope();
			}
		}
		if(scope == null) {
			super.computeScope();
		}
	}

}
