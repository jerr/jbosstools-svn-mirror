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

package org.jboss.tools.ws.jaxrs.core.internal.builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.HttpMethod;
import javax.ws.rs.Path;
import javax.ws.rs.ext.Provider;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.jboss.tools.ws.jaxrs.core.metamodel.MediaTypeCapabilities;
import org.jboss.tools.ws.jaxrs.core.utils.JdtUtils;

/**
 * Class that scan the projec's classpath to find JAX-RS Resources and
 * Providers.
 * 
 */
public final class JAXRSAnnotationsScanner {

	/**
	 * Private constructor of the utility class.
	 */
	private JAXRSAnnotationsScanner() {

	}

	/**
	 * 
	 * Returns all JAX-RS providers in the given scope (ex : javaProject), ie,
	 * types annotated with <code>javax.ws.rs.ext.Provider</code> annotation.
	 * 
	 * 
	 * @param scope
	 *            the search scope (project, compilation unit, type, etc.)
	 * @param includeLibraries
	 *            include project libraries in search scope or not
	 * @param progressMonitor
	 *            the progress monitor
	 * @return providers the JAX-RS provider types
	 * @throws CoreException
	 *             in case of exception
	 */
	public static List<IType> findProviderTypes(final IJavaElement scope, final boolean includeLibraries,
			final IProgressMonitor progressMonitor) throws CoreException {
		IJavaSearchScope searchScope = null;
		if (includeLibraries) {
			searchScope = SearchEngine.createJavaSearchScope(new IJavaElement[] { scope }, IJavaSearchScope.SOURCES
					| IJavaSearchScope.APPLICATION_LIBRARIES | IJavaSearchScope.REFERENCED_PROJECTS);
		} else {
			searchScope = SearchEngine.createJavaSearchScope(new IJavaElement[] { scope }, IJavaSearchScope.SOURCES
					| IJavaSearchScope.REFERENCED_PROJECTS);
		}
		return searchForAnnotatedTypes(Provider.class, searchScope, progressMonitor);
	}

	/**
	 * Returns all JAX-RS resources resourceMethods (ie, class resourceMethods
	 * annotated with an @HTTPMethod annotation) in the given scope (ex :
	 * javaProject).
	 * 
	 * @param scope
	 *            the search scope (project, compilation unit, type, etc.)
	 * @param httpMethodNames
	 *            the fully qualified names of the HTTPMethod java types.
	 * @param progressMonitor
	 *            the progress monitor
	 * @return JAX-RS resource resourceMethods in a map, indexed by the
	 *         declaring type of the resourceMethods
	 * @throws CoreException
	 *             in case of underlying exception
	 */
	public static List<IType> findResources(final IJavaElement scope, final Collection<String> httpMethodNames,
			final IProgressMonitor progressMonitor) throws CoreException {
		IJavaSearchScope searchScope = SearchEngine.createJavaSearchScope(new IJavaElement[] { scope },
				IJavaSearchScope.SOURCES | IJavaSearchScope.REFERENCED_PROJECTS);
		List<String> annotations = new ArrayList<String>(httpMethodNames.size() + 1);
		annotations.add(Path.class.getName());
		annotations.addAll(httpMethodNames);
		// look for type with @Path annotations, as looking for types with
		// annotated resourceMethods may return incomplete results
		return searchForAnnotatedTypes(Path.class, searchScope, progressMonitor);
	}

	/**
	 * Returns all HTTP Methods (ie, annotation meta-annotated with the
	 * <code>javax.ws.rs.HttpMethod</code> annotation) in the given scope (ex :
	 * javaProject).
	 * 
	 * @param scope
	 *            the search scope (project, compilation unit, type, etc.)
	 * @param progressMonitor
	 *            the progress monitor
	 * @return The found types
	 * @throws CoreException
	 *             in case of underlying exceptions.
	 */
	public static List<IType> findHTTPMethodTypes(final IJavaElement scope, final IProgressMonitor progressMonitor)
			throws CoreException {
		IJavaSearchScope searchScope = null;
		if (scope instanceof IJavaProject) {
			IJavaProject javaProject = (IJavaProject) scope;
			searchScope = SearchEngine.createJavaSearchScope(javaProject.getPackageFragmentRoots());
		} else {
			searchScope = SearchEngine.createJavaSearchScope(new IJavaElement[] { scope });
		}
		return searchForAnnotatedTypes(HttpMethod.class, searchScope, progressMonitor);
	}

	/**
	 * Search for types that are annotated with the given annotation name, in
	 * the given search scope.
	 * 
	 * @param annotationName
	 *            the annotation type name
	 * @param searchScope
	 *            the search scope
	 * @param progressMonitor
	 *            the progress monitor
	 * @return the found types
	 * @throws CoreException
	 *             in case of underlying exception
	 */
	private static List<IType> searchForAnnotatedTypes(final Class<?> annotation, final IJavaSearchScope searchScope,
			final IProgressMonitor progressMonitor) throws CoreException {
		JavaMemberSearchResultCollector collector = new JavaMemberSearchResultCollector(IJavaElement.TYPE, searchScope);
		SearchPattern pattern = SearchPattern.createPattern(annotation.getName(), IJavaSearchConstants.ANNOTATION_TYPE,
				IJavaSearchConstants.ANNOTATION_TYPE_REFERENCE | IJavaSearchConstants.TYPE, SearchPattern.R_EXACT_MATCH
						| SearchPattern.R_CASE_SENSITIVE);
		// perform search, results are added/filtered by the custom
		// searchRequestor defined above
		new SearchEngine().search(pattern, new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() },
				searchScope, collector, progressMonitor);
		return collector.getResult(IType.class);
	}

	/**
	 * Returns all JAX-RS resources resourceMethods (ie, class resourceMethods
	 * annotated with an @HTTPMethod annotation) in the given scope (ex :
	 * javaProject).
	 * 
	 * @param scope
	 *            the search scope
	 * @param httpMethodNames
	 *            the types annotated with <code>javax.ws.rs.HttpMethod</code>
	 *            annotation
	 * @param progressMonitor
	 *            the progress monitor
	 * @return JAX-RS resource resourceMethods in a map, indexed by the
	 *         declaring type of the resourceMethods
	 * @throws CoreException
	 *             in case of underlying exception
	 */
	public static List<IMethod> findResourceMethods(final IJavaElement scope, final Collection<String> httpMethodNames,
			final IProgressMonitor progressMonitor) throws CoreException {
		IJavaSearchScope searchScope = SearchEngine.createJavaSearchScope(new IJavaElement[] { scope },
				IJavaSearchScope.SOURCES | IJavaSearchScope.REFERENCED_PROJECTS);
		List<String> annotations = new ArrayList<String>(httpMethodNames.size() + 1);
		annotations.add(Path.class.getName());
		annotations.addAll(httpMethodNames);
		return searchForAnnotatedMethods(annotations, searchScope, progressMonitor);
	}

	/**
	 * Search for methods annotated with one of the given annotations, in the
	 * search scope.
	 * 
	 * @param annotationNames
	 *            the annotations fully qualified names
	 * @param searchScope
	 *            the search scope
	 * @param progressMonitor
	 *            the progress monitor
	 * @return the matching methods
	 * @throws CoreException
	 *             in case of underlying exception
	 */
	private static List<IMethod> searchForAnnotatedMethods(final List<String> annotationNames,
			final IJavaSearchScope searchScope, final IProgressMonitor progressMonitor) throws CoreException {
		JavaMemberSearchResultCollector collector = new JavaMemberSearchResultCollector(IJavaElement.METHOD,
				searchScope);
		SearchPattern pattern = null;
		for (String annotationName : annotationNames) {
			// TODO : apply on METHOD instead of TYPE ?
			SearchPattern subPattern = SearchPattern.createPattern(annotationName,
					IJavaSearchConstants.ANNOTATION_TYPE, IJavaSearchConstants.ANNOTATION_TYPE_REFERENCE,
					SearchPattern.R_EXACT_MATCH | SearchPattern.R_CASE_SENSITIVE);
			if (pattern == null) {
				pattern = subPattern;
			} else {
				pattern = SearchPattern.createOrPattern(pattern, subPattern);
			}
		}
		// perform search, results are added/filtered by the custom
		// searchRequestor defined above
		new SearchEngine().search(pattern, new SearchParticipant[] { SearchEngine.getDefaultSearchParticipant() },
				searchScope, collector, progressMonitor);
		// FIXME : wrong scope : returns all the annotated resourceMethods of
		// the enclosing type
		return collector.getResult(IMethod.class);
	}

	/**
	 * Resolves the media types capabilities of the given annotated member, for
	 * the given qualified name annotation.
	 * 
	 * @param annotatedMember
	 *            the annotated member
	 * @param compilationUnit
	 *            the compilation unit (AST3/DOM)
	 * @param annotationFullyQualifiedName
	 *            the fully qualified name of the annotation (should be
	 *            <code>javax.ws.rs.Consumes</code> or
	 *            <code>javax.ws.rs.Produces</code>)
	 * @return a list of media types capabilities along with their corresponding
	 *         (or source) annotation, or null if the given member does not have
	 *         the expected annotation
	 * @throws CoreException
	 *             in case of underlying exception
	 */
	public static MediaTypeCapabilities resolveMediaTypeCapabilities(final IMember annotatedMember,
			final CompilationUnit compilationUnit, Class<?> annotationName) throws CoreException {
		IAnnotationBinding annotationBinding = JdtUtils.resolveAnnotationBinding(annotatedMember, compilationUnit,
				annotationName);
		if (annotationBinding != null) {
			return new MediaTypeCapabilities(annotationBinding.getJavaElement(),
					asList(JdtUtils.resolveAnnotationAttributeValue(annotationBinding, "value")));
		} else {
			return new MediaTypeCapabilities(annotatedMember);
		}

	}

	/**
	 * Converts the type signature to a human-readable literal.
	 * 
	 * @param typeSignature
	 *            the type signature
	 * @return the human readable literal
	 */
	public static String getTypeSignatureLiteral(final String typeSignature) {
		if ("B".equals(typeSignature)) {
			return "byte";
		}
		if ("C".equals(typeSignature)) {
			return "char";
		}
		if ("D".equals(typeSignature)) {
			return "double";
		}
		if ("F".equals(typeSignature)) {
			return "float";
		}
		if ("I".equals(typeSignature)) {
			return "int";
		}
		if ("J".equals(typeSignature)) {
			return "long";
		}
		if ("S".equals(typeSignature)) {
			return "short";
		}
		if ("Z".equals(typeSignature)) {
			return "boolean";
		}
		return typeSignature;
	}

	/**
	 * Convert the given value(s) into a list of java.lang.String objects.
	 * 
	 * @param values
	 *            the value(s) to convert. The value can be a single string of
	 *            an array of strings.
	 * @return the list of strings
	 */
	private static List<String> asList(final Object values) {
		if (values == null) {
			return null;
		}
		if (values instanceof String) {
			return Arrays.asList((String) values);
		}
		List<String> list = new ArrayList<String>();
		for (Object value : (Object[]) values) {
			list.add((String) value);
		}
		return list;
	}
}
