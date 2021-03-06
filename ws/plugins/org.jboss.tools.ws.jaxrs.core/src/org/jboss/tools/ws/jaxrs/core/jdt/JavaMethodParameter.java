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

package org.jboss.tools.ws.jaxrs.core.jdt;

import java.util.List;

import org.eclipse.jdt.core.ISourceRange;

public class JavaMethodParameter {

	private final String typeName;

	private final List<Annotation> annotations;

	private final ISourceRange sourceRange;

	public JavaMethodParameter(String name, String typeName, List<Annotation> annotations, final ISourceRange sourceRange) {
		this.typeName = typeName;
		this.annotations = annotations;
		this.sourceRange = sourceRange;
	}

	/** @return the parameterType */
	public String getTypeName() {
		return this.typeName;
	}

	public List<Annotation> getAnnotations() {
		return annotations;
	}

	public Annotation getAnnotation(String name) {
		for (Annotation annotation : annotations) {
			if (annotation.getName().equals(name)) {
				return annotation;
			}
		}
		return null;
	}

	/**
	 * @return the region
	 */
	public ISourceRange getRegion() {
		return sourceRange;
	}

	@Override
	public String toString() {
		return "ResourceMethodAnnotatedParameter [type=" + typeName + ", annotations=" + annotations + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((annotations == null) ? 0 : annotations.hashCode());
		//result = prime * result + ((region == null) ? 0 : region.hashCode());
		result = prime * result + ((typeName == null) ? 0 : typeName.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		JavaMethodParameter other = (JavaMethodParameter) obj;
		if (annotations == null) {
			if (other.annotations != null) {
				return false;
			}
		} else if (!annotations.equals(other.annotations)) {
			return false;
		}
		/*if (region == null) {
			if (other.region != null) {
				return false;
			}
		} else if (!region.equals(other.region)) {
			return false;
		}*/
		if (typeName == null) {
			if (other.typeName != null) {
				return false;
			}
		} else if (!typeName.equals(other.typeName)) {
			return false;
		}
		return true;
	}

}
