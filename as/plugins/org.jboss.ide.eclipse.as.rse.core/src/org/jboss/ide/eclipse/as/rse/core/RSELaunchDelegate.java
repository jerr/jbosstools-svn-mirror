/******************************************************************************* 
 * Copyright (c) 2010 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 * 
 * TODO: Logging and Progress Monitors
 ******************************************************************************/ 
package org.jboss.ide.eclipse.as.rse.core;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.rse.core.RSECorePlugin;
import org.eclipse.rse.services.clientserver.messages.SystemMessageException;
import org.eclipse.rse.services.shells.IHostOutput;
import org.eclipse.rse.services.shells.IHostShell;
import org.eclipse.rse.services.shells.IHostShellChangeEvent;
import org.eclipse.rse.services.shells.IHostShellOutputListener;
import org.eclipse.wst.server.core.IServer;
import org.jboss.ide.eclipse.as.core.extensions.events.ServerLogger;
import org.jboss.ide.eclipse.as.core.extensions.polling.WebPortPoller;
import org.jboss.ide.eclipse.as.core.server.internal.DeployableServerBehavior;
import org.jboss.ide.eclipse.as.core.server.internal.JBossServer;
import org.jboss.ide.eclipse.as.core.server.internal.JBossServerBehavior;
import org.jboss.ide.eclipse.as.core.server.internal.launch.AbstractJBossLaunchConfigType;
import org.jboss.ide.eclipse.as.core.server.internal.launch.JBossServerStartupLaunchConfiguration;
import org.jboss.ide.eclipse.as.core.server.internal.launch.JBossServerStartupLaunchConfiguration.IStartLaunchSetupParticipant;
import org.jboss.ide.eclipse.as.core.server.internal.launch.JBossServerStartupLaunchConfiguration.StartLaunchDelegate;
import org.jboss.ide.eclipse.as.core.server.internal.launch.LocalJBossServerStartupLaunchUtil;
import org.jboss.ide.eclipse.as.core.server.internal.launch.StopLaunchConfiguration;
import org.jboss.ide.eclipse.as.core.util.ArgsUtil;
import org.jboss.ide.eclipse.as.core.util.IJBossRuntimeConstants;
import org.jboss.ide.eclipse.as.core.util.IJBossRuntimeResourceConstants;
import org.jboss.ide.eclipse.as.core.util.ServerConverter;
import org.jboss.ide.eclipse.as.rse.core.RSEHostShellModel.ServerShellModel;

public class RSELaunchDelegate implements StartLaunchDelegate, IStartLaunchSetupParticipant {

	public static final String RSE_STARTUP_COMMAND = "org.jboss.ide.eclipse.as.rse.core.RSELaunchDelegate.STARTUP_COMMAND";
	public static final String RSE_SHUTDOWN_COMMAND = "org.jboss.ide.eclipse.as.rse.core.RSELaunchDelegate.SHUTDOWN_COMMAND";
	public static final String DETECT_STARTUP_COMMAND = "org.jboss.ide.eclipse.as.rse.core.RSELaunchDelegate.DETECT_STARTUP_COMMAND";
	public static final String DETECT_SHUTDOWN_COMMAND = "org.jboss.ide.eclipse.as.rse.core.RSELaunchDelegate.DETECT_SHUTDOWN_COMMAND";
	
	
	public void actualLaunch(
			JBossServerStartupLaunchConfiguration launchConfig,
			ILaunchConfiguration configuration, String mode, ILaunch launch,
			IProgressMonitor monitor) throws CoreException {
		JBossServerBehavior beh = LocalJBossServerStartupLaunchUtil.getServerBehavior(configuration);
		beh.setServerStarting();
		String command = configuration.getAttribute(RSE_STARTUP_COMMAND, (String)null);
		try {
			ServerShellModel model = RSEHostShellModel.getInstance().getModel(beh.getServer());
			IHostShell shell = model.createStartupShell("/", command, new String[]{}, new NullProgressMonitor());
			addShellOutputListener(shell);
			launchPingThread(beh);
		} catch(SystemMessageException sme) {
			beh.setServerStopped(); // Not sure when this comes, but we should try to keep track
			throw new CoreException(new Status(IStatus.ERROR, org.jboss.ide.eclipse.as.rse.core.RSECorePlugin.PLUGIN_ID, 
									sme.getMessage(), sme));
		} 
	}
	
	private void launchPingThread(DeployableServerBehavior beh) {
		// TODO do it properly here
		RSEHostShellModel.delay(30000);
		beh.setServerStarted();
	}
	
	
	// Only for debugging
	private void addShellOutputListener(IHostShell shell) {
		IHostShellOutputListener listener = null;
		listener = new IHostShellOutputListener(){
			public void shellOutputChanged(IHostShellChangeEvent event) {
				IHostOutput[] out = event.getLines();
				for(int i = 0; i < out.length; i++ ) {
					// TODO listen here for obvious exceptions or failures
					// System.out.println(out[i]);
				}
			}
		};
		//shell.addOutputListener(listener);
	}
	
	public static void launchCommandNoResult(JBossServerBehavior behaviour, int delay, String command) {
		try {
			ServerShellModel model = RSEHostShellModel.getInstance().getModel(behaviour.getServer());
			model.executeRemoteCommand("/", command, new String[]{}, new NullProgressMonitor(), delay, true);
		} catch( CoreException ce ) {
			ServerLogger.getDefault().log(behaviour.getServer(), ce.getStatus());
		}
	}
	
	public static void launchStopServerCommand(JBossServerBehavior behaviour) {
		ILaunchConfiguration config = null;
		String command2 = "";
		try {
			config = behaviour.getServer().getLaunchConfiguration(false, new NullProgressMonitor());
			String defaultCmd = getDefaultStopCommand(behaviour.getServer(), true);
			command2 = config == null ? defaultCmd :
				config.getAttribute(RSE_SHUTDOWN_COMMAND, defaultCmd);
			behaviour.setServerStopping();
			ServerShellModel model = RSEHostShellModel.getInstance().getModel(behaviour.getServer());
			model.executeRemoteCommand("/", command2, new String[]{}, new NullProgressMonitor(), 10000, true);
			if( model.getStartupShell() != null && model.getStartupShell().isActive())
				model.getStartupShell().writeToShell("exit");
			behaviour.setServerStopped();
		} catch(CoreException ce) {
			behaviour.setServerStarted();
			ServerLogger.getDefault().log(behaviour.getServer(), ce.getStatus());
		}
	}
	
	
	public boolean preLaunchCheck(ILaunchConfiguration configuration,
			String mode, IProgressMonitor monitor) throws CoreException {
		// ping if up 
		final JBossServerBehavior beh = LocalJBossServerStartupLaunchUtil.getServerBehavior(configuration);
		boolean started = new WebPortPoller().onePing(beh.getServer());
		if( started ) {
			beh.setServerStarting();
			beh.setServerStarted();
			return false;
		}
		return true;
	}

	public void preLaunch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
	}

	public void postLaunch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
	}

	public void setupLaunchConfiguration(
			ILaunchConfigurationWorkingCopy workingCopy, IServer server)
			throws CoreException {
		boolean detectStartupCommand, detectShutdownCommand;
		detectStartupCommand = workingCopy.getAttribute(DETECT_STARTUP_COMMAND, true);
		detectShutdownCommand = workingCopy.getAttribute(DETECT_SHUTDOWN_COMMAND, true);
		
		String currentStartupCmd = workingCopy.getAttribute(RSELaunchDelegate.RSE_STARTUP_COMMAND, (String)null);
		if( detectStartupCommand || currentStartupCmd == null || "".equals(currentStartupCmd)) {
			workingCopy.setAttribute(RSELaunchDelegate.RSE_STARTUP_COMMAND, getDefaultLaunchCommand(workingCopy));
		}

		String currentStopCmd = workingCopy.getAttribute(RSELaunchDelegate.RSE_SHUTDOWN_COMMAND, (String)null);
		if( detectShutdownCommand || currentStopCmd == null || "".equals(currentStopCmd)) {
			workingCopy.setAttribute(RSELaunchDelegate.RSE_SHUTDOWN_COMMAND, getDefaultStopCommand(server));
		}
		/*
		 *   /usr/lib/jvm/jre/bin/java -Dprogram.name=run.sh -server -Xms1530M -Xmx1530M 
		 *   -XX:PermSize=425M -XX:MaxPermSize=425M -Dorg.jboss.resolver.warning=true 
		 *   -Dsun.rmi.dgc.client.gcInterval=3600000 -Dsun.rmi.dgc.server.gcInterval=3600000 
		 *   -Djboss.partition.udpGroup=228.1.2.3 -Djboss.webpartition.mcast_port=45577 
		 *   -Djboss.hapartition.mcast_port=45566 -Djboss.ejb3entitypartition.mcast_port=43333 
		 *   -Djboss.ejb3sfsbpartition.mcast_port=45551 -Djboss.jvmRoute=node-10.209.183.100 
		 *   -Djboss.gossip_port=12001 -Djboss.gossip_refresh=5000 -Djava.awt.headless=true 
		 *   -Djava.net.preferIPv4Stack=true 
		 *   -Djava.endorsed.dirs=/opt/jboss-eap-5.1.0.Beta/jboss-as/lib/endorsed 
		 *   -classpath /opt/jboss-eap-5.1.0.Beta/jboss-as/bin/run.jar org.jboss.Main 
		 *   -c default -b 10.209.183.100
		 */
	}
	
	public static String getDefaultStopCommand(IServer server) {
		try {
			return getDefaultStopCommand(server, false);
		} catch(CoreException ce) {/* ignore, INTENTIONAL */}
		return null;
	}
	
	public static String getDefaultStopCommand(IServer server, boolean errorOnFail) throws CoreException {
		String rseHome = server.getAttribute(RSEUtils.RSE_SERVER_HOME_DIR, (String)null);
		if( errorOnFail && rseHome == null ) {
			IStatus s = new Status(IStatus.ERROR, RSECorePlugin.PLUGIN_ID, 
							"Remote Server Home not set.");
			throw new CoreException(s);
		}
		rseHome = rseHome == null ? "" : rseHome;
		
		JBossServer jbs = ServerConverter.getJBossServer(server);
		
		IJBossRuntimeResourceConstants c = new IJBossRuntimeResourceConstants(){};
		String stop = new Path(rseHome).append(c.BIN).append(c.SHUTDOWN_SH).toString() + IJBossRuntimeConstants.SPACE;
		
		// Pull args from single utility method
		stop += StopLaunchConfiguration.getDefaultArgs(jbs);
		return stop;
	}
	
	public static IServer findServer(ILaunchConfiguration config) throws CoreException {
		String serverId = config.getAttribute("server-id", (String)null);
		JBossServer jbs = AbstractJBossLaunchConfigType.findJBossServer(serverId);
		return jbs.getServer();
	}
	
	public static String getDefaultLaunchCommand(ILaunchConfiguration config) throws CoreException {
		IServer server = findServer(config);
		String rseHome = server.getAttribute(RSEUtils.RSE_SERVER_HOME_DIR, "");
		// initialize startup command to something reasonable
		String currentArgs = config.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, ""); //$NON-NLS-1$
		String currentVMArgs = config.getAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS, ""); //$NON-NLS-1$
		
		currentVMArgs= ArgsUtil.setArg(currentVMArgs, null,
				IJBossRuntimeConstants.SYSPROP + IJBossRuntimeConstants.ENDORSED_DIRS,
				new Path(rseHome).append(
						IJBossRuntimeResourceConstants.LIB).append(
								IJBossRuntimeResourceConstants.ENDORSED).toOSString(), true);

		String libPath = new Path(rseHome).append(IJBossRuntimeResourceConstants.BIN)
				.append(IJBossRuntimeResourceConstants.NATIVE).toOSString();
		currentVMArgs= ArgsUtil.setArg(currentVMArgs, null,
				IJBossRuntimeConstants.SYSPROP + IJBossRuntimeConstants.JAVA_LIB_PATH,
				libPath, true);

		
		String cmd = "java " + currentVMArgs + " -classpath " + 
			new Path(rseHome).append(IJBossRuntimeResourceConstants.BIN).append(
					IJBossRuntimeResourceConstants.START_JAR).toString() + IJBossRuntimeConstants.SPACE + 
					IJBossRuntimeConstants.START_MAIN_TYPE + IJBossRuntimeConstants.SPACE + currentArgs + "&";
		return cmd;
	}
}