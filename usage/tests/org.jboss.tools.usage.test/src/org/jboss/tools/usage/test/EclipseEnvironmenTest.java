/*******************************************************************************
 * Copyright (c) 2010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.usage.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Platform;
import org.jboss.tools.usage.reporting.EclipseEnvironment;
import org.jboss.tools.usage.reporting.JBossBundleGroups;
import org.junit.Test;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.Version;

/**
 * @author Andre Dietisheim
 */
public class EclipseEnvironmenTest {

	private static final String GANALYTICS_ACCOUNTNAME = "UA-17645367-1";
	private static final String HOSTNAME = "jboss.org";
	private static final String REFERRAL = "0";
	private static final String LOCALE_US = "en_US";

	@Test
	public void testMacOs() {
		EclipseEnvironment eclipseEnvironment = new EclipseEnvironmentFake(GANALYTICS_ACCOUNTNAME, HOSTNAME, REFERRAL,
				Platform.OS_MACOSX, LOCALE_US);
		String userAgent = eclipseEnvironment.getUserAgent();
		assertApplicationNameAndVersion("com.jboss.jbds.product", "3.0.1", userAgent);
		assertOs("Macintosh", "Intel Mac OS X 10.5", userAgent);
		assertLanguage("en-US", userAgent);
	}

	@Test
	public void testLinux() {
		EclipseEnvironment eclipseEnvironment = new EclipseEnvironmentFake(GANALYTICS_ACCOUNTNAME, HOSTNAME, REFERRAL,
				Platform.OS_LINUX, LOCALE_US);
		String userAgent = eclipseEnvironment.getUserAgent();
		assertApplicationNameAndVersion("com.jboss.jbds.product", "3.0.1", userAgent);
		assertOs("X11", "Linux i686", userAgent);
		assertLanguage("en-US", userAgent);
	}

	@Test
	public void testWindows() {
		EclipseEnvironment eclipseEnvironment = new EclipseEnvironmentFake(GANALYTICS_ACCOUNTNAME, HOSTNAME, REFERRAL,
				Platform.OS_WIN32, LOCALE_US);
		String userAgent = eclipseEnvironment.getUserAgent();
		assertApplicationNameAndVersion("com.jboss.jbds.product", "3.0.1", userAgent);
		assertOs("Windows", "Windows NT 6.1", userAgent);
		assertLanguage("en-US", userAgent);
	}

	@Test
	public void testKeyword() {
		EclipseEnvironment eclipseEnvironment = new EclipseEnvironmentFake(GANALYTICS_ACCOUNTNAME, HOSTNAME, REFERRAL,
				Platform.OS_WIN32, LOCALE_US) {
			@Override
			protected Bundle[] getBundles() {
				return new Bundle[] {
						new BundleSymbolicNameFake("org.jboss.tools.seam.ui"),
						new BundleSymbolicNameFake("org.jboss.tools.seam.core"),
						new BundleSymbolicNameFake("org.jboss.tools.gwt.ui"),
						new BundleSymbolicNameFake("org.jboss.tools.gwt.core"),
						new BundleSymbolicNameFake("org.jboss.tools.smooks.core"),
						new BundleSymbolicNameFake("org.eclipse.core.runtime"),
				};
			}
		};
		String keyword = eclipseEnvironment.getKeyword();

		Matcher matcher = Pattern.compile("(([A-Z]+)-){3}").matcher(keyword);
		assertTrue(matcher.matches());
		assertTrue(keyword.indexOf(JBossBundleGroups.BundleGroup.GWT.name()) >= 0);
		assertTrue(keyword.indexOf(JBossBundleGroups.BundleGroup.SEAM.name()) >= 0);
		assertTrue(keyword.indexOf(JBossBundleGroups.BundleGroup.SMOOKS.name()) >= 0);
	}
	
	private void assertApplicationNameAndVersion(String applicationName, String applicationVersion, String userAgent) {
		Matcher matcher = Pattern.compile("([a-zA-Z\\.]+)/([0-9\\.]+).+").matcher(userAgent);
		assertTrue(matcher.matches());
		assertEquals(2, matcher.groupCount());
		assertEquals(applicationName, matcher.group(1));
		assertEquals(applicationVersion, matcher.group(2));
	}

	private void assertOs(String platform, String os, String userAgent) {
		Matcher matcher = Pattern.compile(".+ \\((.+); U; (.+); .+\\)").matcher(userAgent);
		assertTrue(matcher.matches());
		assertEquals(2, matcher.groupCount());
		assertEquals(platform, matcher.group(1));
		assertEquals(os, matcher.group(2));
	}

	private void assertLanguage(String language, String userAgent) {
		Matcher matcher = Pattern.compile(".+ \\(.+; U; .+ .+; (.+)\\)").matcher(userAgent);
		assertTrue(matcher.matches());
		assertEquals(1, matcher.groupCount());
		assertEquals(language, matcher.group(1));
	}
	
	private class BundleSymbolicNameFake implements Bundle {

		private String symbolicName;

		public BundleSymbolicNameFake(String symbolicName) {
			this.symbolicName = symbolicName;
		}

		@SuppressWarnings("unchecked")
		public Enumeration findEntries(String path, String filePattern, boolean recurse) {
			throw new UnsupportedOperationException();
		}

		public BundleContext getBundleContext() {
			throw new UnsupportedOperationException();
		}

		public long getBundleId() {
			throw new UnsupportedOperationException();
		}

		public URL getEntry(String path) {
			throw new UnsupportedOperationException();
		}

		@SuppressWarnings("unchecked")
		public Enumeration getEntryPaths(String path) {
			throw new UnsupportedOperationException();
		}

		@SuppressWarnings("unchecked")
		public Dictionary getHeaders() {
			throw new UnsupportedOperationException();
		}

		@SuppressWarnings("unchecked")
		public Dictionary getHeaders(String locale) {
			throw new UnsupportedOperationException();
		}

		public long getLastModified() {
			throw new UnsupportedOperationException();
		}

		public String getLocation() {
			throw new UnsupportedOperationException();
		}

		public ServiceReference[] getRegisteredServices() {
			throw new UnsupportedOperationException();
		}

		public URL getResource(String name) {
			throw new UnsupportedOperationException();
		}

		@SuppressWarnings("unchecked")
		public Enumeration getResources(String name) throws IOException {
			throw new UnsupportedOperationException();
		}

		public ServiceReference[] getServicesInUse() {
			throw new UnsupportedOperationException();
		}

		@SuppressWarnings("unchecked")
		public Map getSignerCertificates(int signersType) {
			throw new UnsupportedOperationException();
		}

		public int getState() {
			throw new UnsupportedOperationException();
		}

		public String getSymbolicName() {
			return this.symbolicName;
		}

		public Version getVersion() {
			throw new UnsupportedOperationException();

		}

		public boolean hasPermission(Object permission) {
			throw new UnsupportedOperationException();

		}

		@SuppressWarnings("unchecked")
		public Class loadClass(String name) throws ClassNotFoundException {
			throw new UnsupportedOperationException();
		}

		public void start() throws BundleException {
			throw new UnsupportedOperationException();

		}

		public void start(int options) throws BundleException {
			throw new UnsupportedOperationException();
		}

		public void stop() throws BundleException {
			throw new UnsupportedOperationException();
		}

		public void stop(int options) throws BundleException {
			throw new UnsupportedOperationException();
		}

		public void uninstall() throws BundleException {
			throw new UnsupportedOperationException();
		}

		public void update() throws BundleException {
			throw new UnsupportedOperationException();
		}

		public void update(InputStream input) throws BundleException {
			throw new UnsupportedOperationException();
		}
	}
}
