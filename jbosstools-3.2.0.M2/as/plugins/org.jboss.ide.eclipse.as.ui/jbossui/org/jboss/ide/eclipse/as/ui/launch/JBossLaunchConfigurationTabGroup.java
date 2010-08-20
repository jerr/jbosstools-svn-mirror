/**
 * JBoss by Red Hat
 * Copyright 2006-2009, Red Hat Middleware, LLC, and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
* This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.as.ui.launch;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.sourcelookup.SourceLookupTab;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaArgumentsTab;
import org.eclipse.jdt.debug.ui.launchConfigurations.JavaClasspathTab;
import org.eclipse.jdt.internal.debug.ui.IJavaDebugHelpContextIds;
import org.eclipse.jdt.internal.debug.ui.launcher.LauncherMessages;
import org.eclipse.jdt.launching.IJavaLaunchConfigurationConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.jboss.ide.eclipse.as.core.util.ArgsUtil;
import org.jboss.ide.eclipse.as.ui.Messages;
import org.jboss.ide.eclipse.as.ui.xpl.JavaMainTabClone;

/**
 * 
 * @author Rob Stryker <rob.stryker@redhat.com>
 *
 */
public class JBossLaunchConfigurationTabGroup extends
		AbstractLaunchConfigurationTabGroup {

	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
				new JavaArgumentsTabExtension(),
				new JavaMainTabExtension(),
				new JavaClasspathTab(),
				new SourceLookupTab(),
				new EnvironmentTab(),
				new CommonTab()
		};

		for( int i = 0; i < tabs.length; i++ )
			tabs[i].setLaunchConfigurationDialog(dialog);
		setTabs(tabs);
	}
	
	public static Composite createComposite(Composite parent, Font font, int columns, int hspan, int fill) {
    	Composite g = new Composite(parent, SWT.NONE);
    	g.setLayout(new GridLayout(columns, false));
    	g.setFont(font);
    	GridData gd = new GridData(fill);
		gd.horizontalSpan = hspan;
    	g.setLayoutData(gd);
    	return g;
    }

	
	public class JavaMainTabExtension extends JavaMainTabClone {
		public void createControl(Composite parent) {
			Composite comp = createComposite(parent, parent.getFont(), 1, 1, GridData.FILL_BOTH);
			((GridLayout)comp.getLayout()).verticalSpacing = 0;
			//createProjectEditor(comp);
			//createVerticalSpacer(comp, 1);
			createMainTypeEditor(comp, LauncherMessages.JavaMainTab_Main_cla_ss__4);
			setControl(comp);
			PlatformUI.getWorkbench().getHelpSystem().setHelp(getControl(), IJavaDebugHelpContextIds.LAUNCH_CONFIGURATION_DIALOG_MAIN_TAB);
		}
		public void initializeFrom(ILaunchConfiguration config) {
			//super.initializeFrom(config);
			//updateProjectFromConfig(config);
			setCurrentLaunchConfiguration(config);
			updateMainTypeFromConfig(config);
			updateStopInMainFromConfig(config);
			updateInheritedMainsFromConfig(config);
			updateExternalJars(config);		
		}
		public boolean isValid(ILaunchConfiguration config) {
			setErrorMessage(null);
			setMessage(null);
			String name = fMainText.getText().trim();
			if (name.length() == 0) {
				setErrorMessage(LauncherMessages.JavaMainTab_Main_type_not_specified_16); 
				return false;
			}
			return true;
		}
		protected IJavaProject getJavaProject() {
			return null;
		}

	}
	
	public class JavaArgumentsTabExtension extends JavaArgumentsTab {
		private String originalHost=null;
		private String originalConf=null;
		public void initializeFrom(ILaunchConfiguration configuration) {
			super.initializeFrom(configuration);
			try {
				String startArgs = configuration.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, (String)null);
				originalHost = ArgsUtil.getValue(startArgs, "-b", "--host"); //$NON-NLS-1$ //$NON-NLS-2$
				originalConf = ArgsUtil.getValue(startArgs, "-c", "--configuration"); //$NON-NLS-1$ //$NON-NLS-2$
			} catch( CoreException ce ) { }
		}
		public boolean isValid(ILaunchConfiguration config) {
			if( !super.isValid(config)) 
				return false;
			try {
				String startArgs = config.getAttribute(IJavaLaunchConfigurationConstants.ATTR_PROGRAM_ARGUMENTS, (String)null);
				String newHost = ArgsUtil.getValue(startArgs, "-b", "--host"); //$NON-NLS-1$ //$NON-NLS-2$
				String newConf = ArgsUtil.getValue(startArgs, "-c", "--configuration"); //$NON-NLS-1$ //$NON-NLS-2$
				if( newConf == null || !newConf.equals(originalConf)) 
					return false;
				if( newHost == null || !newHost.equals(originalHost)) 
					return false;
			} catch( CoreException ce ) {}
			return true;
		}
		public String getErrorMessage() {
			String m = super.getErrorMessage();
			if (m == null) {
				String startArgs = getAttributeValueFrom(fPrgmArgumentsText);
				String newHost = ArgsUtil.getValue(startArgs, "-b", "--host"); //$NON-NLS-1$ //$NON-NLS-2$
				String newConf = ArgsUtil.getValue(startArgs, "-c", "--configuration"); //$NON-NLS-1$ //$NON-NLS-2$
				if( newConf == null || !newConf.equals(originalConf)) 
					return Messages.LaunchInvalidConfigChanged;
				if( newHost == null || !newHost.equals(originalHost)) 
					return Messages.LaunchInvalidHostChanged;
			}
			return m;
		}
	}
}
