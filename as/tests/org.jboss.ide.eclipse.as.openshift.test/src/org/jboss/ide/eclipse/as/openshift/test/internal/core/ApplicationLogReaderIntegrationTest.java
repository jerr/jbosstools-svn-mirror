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
package org.jboss.ide.eclipse.as.openshift.test.internal.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.jboss.ide.eclipse.as.openshift.core.ApplicationLogReader;
import org.jboss.ide.eclipse.as.openshift.core.IApplication;
import org.jboss.ide.eclipse.as.openshift.core.ICartridge;
import org.jboss.ide.eclipse.as.openshift.core.internal.Application;
import org.jboss.ide.eclipse.as.openshift.core.internal.IOpenshiftService;
import org.jboss.ide.eclipse.as.openshift.core.internal.OpenshiftService;
import org.jboss.ide.eclipse.as.openshift.test.internal.core.fakes.TestUser;
import org.jboss.ide.eclipse.as.openshift.test.internal.core.utils.ApplicationUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * @author André Dietisheim
 */
public class ApplicationLogReaderIntegrationTest {

	private IOpenshiftService service;
	private TestUser user;

	@Before
	public void setUp() {
		this.service = new OpenshiftService();
		this.user = new TestUser();
	}

	/**
	 * Asserts the service implementation: getStatus returns the same log if no
	 * new log entry is available
	 * 
	 * @throws Exception
	 */
	@Ignore
	@Test
	public void getStatusReturnsTheWholeLogIfNoNewLogEntryOnServer() throws Exception {
		String applicationName = ApplicationUtils.createRandomApplicationName();
		try {
			Application application = service.createApplication(applicationName, ICartridge.JBOSSAS_7, user);
			String applicationStatus = service.getStatus(application.getName(), application.getCartridge(), user);
			String applicationStatus2 = service.getStatus(application.getName(), application.getCartridge(), user);
			assertEquals(applicationStatus, applicationStatus2);
		} finally {
			ApplicationUtils.silentlyDestroyAS7Application(applicationName, user, service);
		}
	}

	/**
	 * Asserts the service implementation: getStatus returns the new entries
	 * (and a tailing-header) if new log entries are available
	 */
	@Ignore
	@Test
	public void getStatusReturnsNewEntriesIfNewLogEntriesOnServer() throws Exception {
		String applicationName = ApplicationUtils.createRandomApplicationName();
		try {
			Application application = service.createApplication(applicationName, ICartridge.JBOSSAS_7, user);
			String applicationStatus = service.getStatus(application.getName(), application.getCartridge(), user);
			application.restart();
			String applicationStatus2 = service.getStatus(application.getName(), application.getCartridge(), user);
			assertFalse(applicationStatus.equals(applicationStatus2));
		} finally {
			ApplicationUtils.silentlyDestroyAS7Application(applicationName, user, service);
		}
	}

	@Test
	public void logReaderReturnsNewEntriesAfterApplicationRestart() throws Exception {
		IApplication application = null;
		ExecutorService executor = null;
		try {
			application = user.createTestApplication();
			ApplicationLogReader logReader = application.getLogReader();
			LogReaderRunnable logReaderRunnable = new LogReaderRunnable(logReader, 10 * 1024);
			executor = Executors.newSingleThreadExecutor();
			executor.submit(logReaderRunnable);

			String log = logReaderRunnable.waitUntilNoNewLogentries();
			assertNotNull(log);
			assertTrue(log.length() > 0);
			
			application.restart();

			String newLog = logReaderRunnable.waitUntilNoNewLogentries();
			assertNotNull(newLog);
			assertTrue(newLog.length() > 0);
			assertFalse(log.equals(newLog));
		} finally {
			if (executor != null) {
				executor.shutdownNow();
			}
			if (application != null) {
				user.silentlyDestroyApplication(application);
			}
		}
	}
}
