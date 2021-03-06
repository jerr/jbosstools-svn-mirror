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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.ISourceRange;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.SourceRange;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMemberValuePairBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.jboss.tools.ws.jaxrs.core.internal.utils.Logger;

public class JavaMethodSignaturesVisitor extends ASTVisitor {

	private final ICompilationUnit compilationUnit;

	private final IMethod method;

	private final List<JavaMethodSignature> methodSignatures = new ArrayList<JavaMethodSignature>();

	/**
	 * Constructor to use when you need all Java Method signatures in the given
	 * compilation unit
	 * 
	 * @param method
	 */
	public JavaMethodSignaturesVisitor(ICompilationUnit compilationUnit) {
		this.compilationUnit = compilationUnit;
		this.method = null;
	}

	/**
	 * Constructor to use when you only need a single Java Method signature
	 * 
	 * @param method
	 */
	public JavaMethodSignaturesVisitor(IMethod method) {
		this.compilationUnit = method.getCompilationUnit();
		this.method = method;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jdt.core.dom.ASTVisitor#visit(org.eclipse
	 * .jdt.core.dom.MethodDeclaration)
	 */
	@Override
	public boolean visit(MethodDeclaration declaration) {
		try {
			final IJavaElement element = compilationUnit.getElementAt(declaration.getStartPosition());
			if (element == null || element.getElementType() != IJavaElement.METHOD) {
				return true;
			}
			IMethod method = (IMethod) element;
			if (this.method != null && !this.method.getHandleIdentifier().equals(method.getHandleIdentifier())) {
				return true;
			}

			final IMethodBinding methodBinding = declaration.resolveBinding();
			// sometimes, the binding cannot be resolved
			if (methodBinding == null) {
				Logger.warn("Could not resolve bindings form method " + method.getElementName());
			} else {
				final IType returnedType = getReturnType(methodBinding);
						//.getReturnType().getJavaElement() : null;
				List<JavaMethodParameter> methodParameters = new ArrayList<JavaMethodParameter>();
				@SuppressWarnings("unchecked")
				List<SingleVariableDeclaration> parameters = declaration.parameters();
				for (SingleVariableDeclaration parameter : parameters) {
					final String paramName = parameter.getName().getFullyQualifiedName();
					final IVariableBinding paramBinding = parameter.resolveBinding();
					final String paramTypeName = paramBinding.getType().getQualifiedName();
					final List<org.jboss.tools.ws.jaxrs.core.jdt.Annotation> paramAnnotations = new ArrayList<org.jboss.tools.ws.jaxrs.core.jdt.Annotation>();
					final List<?> modifiers = (List<?>) (parameter
							.getStructuralProperty(SingleVariableDeclaration.MODIFIERS2_PROPERTY));
					for (Object modifier : modifiers) {
						if (modifier instanceof Annotation) {
							final Annotation annotation = (Annotation) modifier;
							IAnnotationBinding annotationBinding = annotation.resolveAnnotationBinding();
							final String annotationName = annotationBinding.getAnnotationType().getQualifiedName();
							final Map<String, List<String>> annotationElements = resolveAnnotationElements(annotationBinding);
							final ISourceRange sourceRange = new SourceRange(annotation.getStartPosition(),
									annotation.getLength());
							paramAnnotations.add(new org.jboss.tools.ws.jaxrs.core.jdt.Annotation(null, annotationName,
									annotationElements, sourceRange));
						}
					}
					final ISourceRange sourceRange = new SourceRange(parameter.getStartPosition(), parameter.getLength());
					methodParameters.add(new JavaMethodParameter(paramName, paramTypeName, paramAnnotations, sourceRange));
				}

				// TODO : add support for thrown exceptions
				this.methodSignatures.add(new JavaMethodSignature(method, returnedType, methodParameters));
			}
		} catch (JavaModelException e) {
			Logger.error("Failed to analyse compilation unit methods", e);
		}
		return true;
	}

	/**
	 * Returns the ReturnType for the given method or null of the return type could not be found or is 'void'
	 * @param methodBinding
	 * @return
	 */
	private IType getReturnType(final IMethodBinding methodBinding) {
		if (methodBinding.getReturnType() != null && methodBinding.getReturnType().getJavaElement() != null) {
			return (IType) methodBinding.getReturnType().getJavaElement().getAdapter(IType.class);
		}
		return null;
	}

	private static Map<String, List<String>> resolveAnnotationElements(IAnnotationBinding annotationBinding) {
		final Map<String, List<String>> annotationElements = new HashMap<String, List<String>>();
		for (IMemberValuePairBinding binding : annotationBinding.getAllMemberValuePairs()) {
			final List<String> values = new ArrayList<String>();
			if(binding.getValue() != null) {
				if (binding.getValue() instanceof Object[]) {
					for (Object v : (Object[]) binding.getValue()) {
						values.add(v.toString());
					}
				} else {
					values.add(binding.getValue().toString());
				}
			}
			annotationElements.put(binding.getName(), values);
		}
		return annotationElements;
	}

	/** @return the methodDeclarations */
	public JavaMethodSignature getMethodSignature() {
		if (this.methodSignatures.size() == 0) {
			Logger.debug("*** no method signature found ?!? ***");
			return null;
		}
		return this.methodSignatures.get(0);

	}

	/** @return the methodDeclarations */
	public List<JavaMethodSignature> getMethodSignatures() {
		return this.methodSignatures;
	}
}