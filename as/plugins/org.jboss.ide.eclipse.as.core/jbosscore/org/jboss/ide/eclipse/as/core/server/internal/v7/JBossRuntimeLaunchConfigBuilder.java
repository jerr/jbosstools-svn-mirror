package org.jboss.ide.eclipse.as.core.server.internal.v7;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.internal.launching.RuntimeClasspathEntry;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.IServer;
import org.jboss.ide.eclipse.as.core.server.IJBossServerRuntime;
import org.jboss.ide.eclipse.as.core.server.internal.launch.AbstractJBossLaunchConfigType;
import org.jboss.ide.eclipse.as.core.util.IJBossRuntimeResourceConstants;

public class JBossRuntimeLaunchConfigBuilder {

	private ILaunchConfigurationWorkingCopy launchConfig;
	private IJBossServerRuntime jbossRuntime;

	public JBossRuntimeLaunchConfigBuilder(ILaunchConfigurationWorkingCopy launchConfig, IJBossServerRuntime runtime) {
		this.launchConfig = launchConfig;
		this.jbossRuntime = runtime;
	}

	public JBossRuntimeLaunchConfigBuilder setVmContainer() {
		IVMInstall vmInstall = jbossRuntime.getVM();
		if (vmInstall != null) {
			launchConfig.setAttribute(IJavaLaunchConfigurationConstants.ATTR_JRE_CONTAINER_PATH,
					JavaRuntime.newJREContainerPath(vmInstall).toPortableString());
		}
		return this;
	}

	public JBossRuntimeLaunchConfigBuilder setDefaultArguments() {
		launchConfig.setAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS,
				jbossRuntime.getDefaultRunArgs());
		launchConfig.setAttribute(IJavaLaunchConfigurationConstants.ATTR_VM_ARGUMENTS,
				jbossRuntime.getDefaultRunVMArgs());
		return this;
	}

	public JBossRuntimeLaunchConfigBuilder setClassPath(IServer server) throws CoreException {
		return setClassPath(JBossRuntimeClasspathUtil.getClasspath(server, jbossRuntime.getVM()));
	}

	public JBossRuntimeLaunchConfigBuilder setClassPath(List<String> entries) throws CoreException {
		launchConfig.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH_PROVIDER,
				JBoss7ServerBehavior.DEFAULT_CP_PROVIDER_ID);
		launchConfig.setAttribute(IJavaLaunchConfigurationConstants.ATTR_CLASSPATH, entries);
		launchConfig.setAttribute(IJavaLaunchConfigurationConstants.ATTR_DEFAULT_CLASSPATH, false);
		return this;
	}

	public JBossRuntimeLaunchConfigBuilder setMainType(String mainType) {
		launchConfig.setAttribute(
				IJavaLaunchConfigurationConstants.ATTR_MAIN_TYPE_NAME, mainType);
		return this;
	}

	public JBossRuntimeLaunchConfigBuilder setWorkingDirectory(IRuntime runtime) {
		IPath location = runtime.getLocation();
		launchConfig.setAttribute(IJavaLaunchConfigurationConstants.ATTR_WORKING_DIRECTORY,
				location.append(IJBossRuntimeResourceConstants.BIN).toString());
		return this;
	}

	public void setServerId(IServer server) {
		launchConfig.setAttribute(AbstractJBossLaunchConfigType.SERVER_ID, server.getId());
	}
}
