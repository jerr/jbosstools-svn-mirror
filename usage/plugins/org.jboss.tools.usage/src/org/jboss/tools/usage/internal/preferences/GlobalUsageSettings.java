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
package org.jboss.tools.usage.internal.preferences;

import java.util.Map;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Plugin;
import org.jboss.tools.usage.branding.IUsageBranding;
import org.jboss.tools.usage.http.HttpRemotePropertiesProvider;
import org.jboss.tools.usage.http.IPropertiesProvider;
import org.jboss.tools.usage.internal.JBossToolsUsageActivator;
import org.jboss.tools.usage.util.BundleUtils;
import org.jboss.tools.usage.util.LoggingUtils;
import org.jboss.tools.usage.util.StatusUtils;

/**
 * A class that implements a global reporting enablement setting. The current
 * implementation queries a given url and extracts the enablement value out of
 * the response.
 * 
 * @author Andre Dietisheim
 */
public class GlobalUsageSettings {

	/**
	 * system property that enables/disables reporting for current eclipse
	 * instance
	 */
	public static final String USAGE_REPORTING_ENABLED_KEY = "usage_reporting_enabled";

	public static final String REMOTEPROPS_USAGE_REPORTING_ENABLED_KEY = USAGE_REPORTING_ENABLED_KEY + "="; //$NON-NLS-1$

	/** the enablement default for the local instance */
	private static final boolean INSTANCE_USAGE_REPORTING_ENABLED_DEFAULT = true;

	/** the enablement default for all instances */
	private static final boolean ALLINSTANCES_USAGE_REPORTING_ENABLED_DEFAULT = false;

	/** the delimiter that delimits the key/value-pairs */
	private static final char VALUE_DELIMITER = '\n';

	private IPropertiesProvider remoteMap;

	public GlobalUsageSettings(Plugin plugin) {
		try {
			IUsageBranding brandingService = BundleUtils.getHighestRankedService(IUsageBranding.class.getName(),
					JBossToolsUsageActivator.getDefault().getBundle());
			remoteMap = createRemoteMap(
					brandingService.getGlobalRemotePropertiesUrl()
					, VALUE_DELIMITER
					, plugin
					, REMOTEPROPS_USAGE_REPORTING_ENABLED_KEY);
		} catch (Exception e) {
			IStatus status = StatusUtils.getErrorStatus(JBossToolsUsageActivator.PLUGIN_ID, "could not ", e);
			LoggingUtils.log(status, JBossToolsUsageActivator.getDefault());
		}
	}

	/**
	 * Returns <code>true</code> if usage reporting is turned on.
	 * 
	 * @return true, if is reporting enabled
	 */
	public boolean isReportingEnabled() {
		return isInstanceReportingEnabled() && isAllInstancesReportingEnabled();
	}

	/**
	 * Returns <code>true</code> if reporting is enabled for all instances. The
	 * appropriate setting is queried in a remote properties file at
	 * {@link #REMOTEPROPS_URL}. The key is
	 * {@link #REMOTEPROPS_ALLINSTANCES_ENABLED_DEFAULT}
	 * 
	 * @return <code>true, if the remote peer is set to enabled
	 * 
	 * @see #REMOTEPROPS_URL
	 * @see #REMOTEPROPS_ALLINSTANCES_ENABLED_KEY
	 */
	private boolean isAllInstancesReportingEnabled() {
		try {
			Map<String, String> valueMap = remoteMap.getMap();
			String isEnabled = valueMap.get(REMOTEPROPS_USAGE_REPORTING_ENABLED_KEY);
			if (isEnabled == null) {
				return ALLINSTANCES_USAGE_REPORTING_ENABLED_DEFAULT;
			}

			return Boolean.valueOf(isEnabled);
		} catch (Exception e) {
			return ALLINSTANCES_USAGE_REPORTING_ENABLED_DEFAULT;
		}
	}

	/**
	 * Returns <code>true</code> if is reporting is enabled for this eclipse
	 * instance.
	 * 
	 * @return true, if this instance shall report usage
	 * 
	 * @see #SYSPROPS_INSTANCE_ENABLED_KEY
	 */
	private boolean isInstanceReportingEnabled() {
		return Boolean.valueOf(
				System.getProperty(USAGE_REPORTING_ENABLED_KEY,
						String.valueOf(INSTANCE_USAGE_REPORTING_ENABLED_DEFAULT)));
	}

	protected IPropertiesProvider createRemoteMap(String url, char valueDelimiter, Plugin plugin,
			String... keys) {
		return new HttpRemotePropertiesProvider(
				url,
				valueDelimiter,
				plugin,
				keys);
	}
}
