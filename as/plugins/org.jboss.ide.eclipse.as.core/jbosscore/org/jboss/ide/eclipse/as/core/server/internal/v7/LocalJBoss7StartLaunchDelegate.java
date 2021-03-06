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
package org.jboss.ide.eclipse.as.core.server.internal.v7;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.wst.server.core.IServer;
import org.jboss.ide.eclipse.as.core.server.IJBossLaunchDelegate;
import org.jboss.ide.eclipse.as.core.server.internal.DelegatingServerBehavior;
import org.jboss.ide.eclipse.as.core.server.internal.LocalJBossBehaviorDelegate;
import org.jboss.ide.eclipse.as.core.server.internal.launch.LocalJBossStartLaunchDelegate;
import org.jboss.ide.eclipse.as.core.util.JBossServerBehaviorUtils;

/**
 * @author Rob Stryker
 */
public class LocalJBoss7StartLaunchDelegate extends LocalJBossStartLaunchDelegate 
	implements IJBossLaunchDelegate {

	public String[] getJavaLibraryPath(ILaunchConfiguration configuration) throws CoreException {
		return new String[] {};
	}

	public boolean preLaunchCheck(ILaunchConfiguration configuration, String mode, IProgressMonitor monitor)
			throws CoreException {
		return super.preLaunchCheck(configuration, mode, monitor);
	}

	public void preLaunch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		DelegatingServerBehavior jbsBehavior = JBossServerBehaviorUtils.getServerBehavior(configuration);
		if( jbsBehavior != null ) {
			jbsBehavior.setRunMode(mode);
			jbsBehavior.setServerStarting();
		}
	}

	public void postLaunch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
			throws CoreException {
		DelegatingJBoss7ServerBehavior behavior = JBossServerBehaviorUtils.getJBoss7ServerBehavior(configuration);
		if( behavior != null ) {
			IProcess[] processes = launch.getProcesses();
			if (processes != null && processes.length >= 1) {
				behavior.setProcess(processes[0]);
				((LocalJBossBehaviorDelegate) (behavior.getDelegate())).setProcess(processes[0]);
			}
			behavior.setRunMode(mode);
		}
	}
	
	@Override
	public void setupLaunchConfiguration(
			ILaunchConfigurationWorkingCopy workingCopy, IServer server) throws CoreException {
		new LocalJBoss7StartConfigurator(server).configure(workingCopy);
	}

}
