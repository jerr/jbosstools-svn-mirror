/******************************************************************************* 
 * Copyright (c) 2008 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Xavier Coulon - Initial API and implementation 
 ******************************************************************************/
package org.jboss.tools.ws.jaxrs.core.internal.metamodel.domain;

import static org.jboss.tools.ws.jaxrs.core.internal.metamodel.builder.JaxrsElementDelta.F_APPLICATION_PATH_VALUE;
import static org.jboss.tools.ws.jaxrs.core.jdt.EnumJaxrsClassname.APPLICATION_PATH;

import org.eclipse.jdt.core.IType;
import org.jboss.tools.ws.jaxrs.core.jdt.Annotation;
import org.jboss.tools.ws.jaxrs.core.metamodel.EnumElementCategory;
import org.jboss.tools.ws.jaxrs.core.metamodel.EnumElementKind;
import org.jboss.tools.ws.jaxrs.core.metamodel.IJaxrsApplication;

/**
 * This domain element describes a subtype of {@link jvax.ws.rs.Application} annotated with
 * {@link jvax.ws.rs.ApplicationPath}.
 * 
 * @author xcoulon
 */
public class JaxrsJavaApplication extends JaxrsJavaElement<IType> implements IJaxrsApplication {

	/**
	 * Full constructor.
	 * 
	 * @param javaType
	 * @param applicationPathAnnocation
	 * @param metamodel
	 */
	public JaxrsJavaApplication(IType javaType, Annotation applicationPathAnnocation, JaxrsMetamodel metamodel) {
		super(javaType, applicationPathAnnocation, metamodel);
	}

	@Override
	public EnumElementCategory getElementCategory() {
		return EnumElementCategory.APPLICATION;
	}

	@Override
	public EnumElementKind getElementKind() {
		if (getAnnotation(APPLICATION_PATH.qualifiedName) != null) {
			return EnumElementKind.APPLICATION_JAVA;
		}
		return EnumElementKind.UNDEFINED;
	}

	/**
	 * {@inheritDoc}
	 */
	public String getApplicationPath() {
		final Annotation applicationPathAnnotation = getAnnotation(APPLICATION_PATH.qualifiedName);
		if (applicationPathAnnotation != null) {
			return applicationPathAnnotation.getValue("value");
		}
		return null;
	}
	
	/**
	 * Update this Application with the elements of the given Application
	 * 
	 * @param application
	 * @return the flags indicating the kind of changes that occurred during the
	 *         update.
	 */
	public int update(JaxrsJavaApplication application) {
		int flags = 0;
		final Annotation annotation = this.getAnnotation(APPLICATION_PATH.qualifiedName);
		final Annotation otherAnnotation = application.getAnnotation(APPLICATION_PATH.qualifiedName);
		if (annotation != null && otherAnnotation != null && !annotation.equals(otherAnnotation)
				&& annotation.update(otherAnnotation)) {
			flags += F_APPLICATION_PATH_VALUE;
		}
		return flags;
	}
	
	@Override
	public String toString() {
		return ("Application '" + getJavaElement().getElementName() + "': " + getApplicationPath());
	}


}
