/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.openshift.express.internal.ui.wizard;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.util.NLS;
import org.jboss.tools.common.ui.databinding.ObservableUIPojo;
import org.jboss.tools.common.ui.preferencevalue.StringPreferenceValue;
import org.jboss.tools.openshift.express.internal.core.console.UserDelegate;
import org.jboss.tools.openshift.express.internal.core.console.UserModel;
import org.jboss.tools.openshift.express.internal.ui.OpenShiftUIActivator;
import org.jboss.tools.openshift.express.internal.ui.utils.Logger;
import org.jboss.tools.openshift.express.internal.ui.utils.StringUtils;

import com.openshift.client.NotFoundOpenShiftException;
import com.openshift.client.configuration.OpenShiftConfiguration;

/**
 * @author Andre Dietisheim
 * @author Xavier Coulon
 */
public class CredentialsWizardPageModel extends ObservableUIPojo {

	private static final String RHLOGIN_PREFS_KEY = "org.jboss.tools.openshift.express.internal.ui.wizard.CredentialsWizardModel_RHLOGIN";

	public static final String PROPERTY_SERVER_URL = "serverUrl";
	public static final String PROPERTY_RHLOGIN = "rhLogin";
	public static final String PROPERTY_PASSWORD = "password";
	public static final String PROPERTY_CREDENTIALS_STATUS = "credentialsStatus";
	public static final String PROPERTY_REMEMBER_PASSWORD = "rememberPassword";

	private String rhLogin;
	private String password;
	private boolean rememberPassword = false;
	private IStatus credentialsStatus;
	private StringPreferenceValue rhLoginPreferenceValue;
	private final String libraServer;

	private IUserAwareModel wizardModel;

	public CredentialsWizardPageModel(IUserAwareModel wizardModel) {
		this.wizardModel = wizardModel;
		this.rhLoginPreferenceValue = new StringPreferenceValue(RHLOGIN_PREFS_KEY, OpenShiftUIActivator.PLUGIN_ID);
		this.libraServer = initLibraServer();
		this.rhLogin = initRhLogin();
		this.password = initPassword();
		resetCredentialsStatus();
	}

	private String initLibraServer() {
		try {
			return new OpenShiftConfiguration().getLibraServer();
		} catch (Exception e) {
			Logger.error("Failed to load OpenShift configuration from client library", e);
		}
		return null;
	}

	protected String initRhLogin() {
		String rhLogin = null;

		UserDelegate user = wizardModel.getUser();
		if (user == null) {
			user = UserModel.getDefault().getRecentUser();
		}
		if (user != null) {
			rhLogin = user.getUsername();
		} else {
			rhLogin = rhLoginPreferenceValue.get();
			if (rhLogin == null 
					|| rhLogin.length() == 0) {
				rhLogin = getConfiguredUserName();
			}
		}
		
		return rhLogin;
	}
	
	protected String initPassword() {
		String password = null;
		if (!StringUtils.isEmpty(libraServer) 
				&& !StringUtils.isEmpty(rhLogin)) {
			password = UserModel.getDefault().getPasswordFromSecureStorage(rhLogin);
			setRememberPassword(!StringUtils.isEmpty(password));
		}
		return password;
	}

	private void storePassword(UserDelegate user) {
		UserModel.getDefault().setPasswordInSecureStorage(user.getUsername(), password);
	}

	private void erasePasswordStore() {
		UserModel.getDefault().clearPasswordInSecureStorage(rhLogin);
	}

	protected String getConfiguredUserName() {
		String configuredUsername = null;
		try {
			// retrieved from the local 'express.conf' configuration file
			configuredUsername = new OpenShiftConfiguration().getRhlogin();
		} catch (Exception e) {
			Logger.error("Cound not retrieve rhlogin from express configuration");
		}
		return configuredUsername;
	}

	public String getRhLogin() {
		return rhLogin;
	}

	public void setRhLogin(String rhLogin) {
		if (rhLogin != null && !rhLogin.equals(this.rhLogin)) {
			rhLoginPreferenceValue.store(rhLogin);
			firePropertyChange(PROPERTY_RHLOGIN, this.rhLogin, this.rhLogin = rhLogin);
			resetCredentialsStatus();
		}
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		if (password != null && !password.equals(this.password)) {
			firePropertyChange(PROPERTY_PASSWORD, this.password, this.password = password);
			resetCredentialsStatus();
		}
	}

	/**
	 * @return the rememberPassword
	 */
	public boolean isRememberPassword() {
		return rememberPassword;
	}

	/**
	 * @param rememberPassword
	 *            the rememberPassword to set
	 */
	public void setRememberPassword(boolean rememberPassword) {
		firePropertyChange(PROPERTY_REMEMBER_PASSWORD, this.rememberPassword, this.rememberPassword = rememberPassword);
	}

	private void resetCredentialsStatus() {
		setCredentialsStatus(null);
	}

	private void setCredentialsStatus(IStatus status) {
		firePropertyChange(PROPERTY_CREDENTIALS_STATUS, this.credentialsStatus, this.credentialsStatus = status);
	}

	public IStatus getCredentialsStatus() {
		return credentialsStatus;
	}

	public boolean areCredentialsValid() {
		IStatus validationStatus = getCredentialsStatus();
		return validationStatus != null && validationStatus.isOK();
	}

	public boolean areCredentialsValidated() {
		return credentialsStatus != null;
	}

	public IStatus validateCredentials() {
		IStatus status = getValidityStatus(getRhLogin(), getPassword());
		setCredentialsStatus(status);
		return status;
	}
	
	private IStatus getValidityStatus(String rhLogin, String password) {
		IStatus status = Status.OK_STATUS;
		UserDelegate user = null;
		try {
			// reuse previous user if it was properly logged in
			user = UserModel.getDefault().findUser(rhLogin);
			// check user credentials if not logged before or if input password changed
			if(user != null && user.isConnected() && user.getPassword().equals(password)) {
				user.setRememberPassword(rememberPassword);
				wizardModel.setUser(user);
				storeUser(user);
			} else {
				user = new UserDelegate(UserModel.getDefault().createUser(rhLogin, password), rememberPassword);
				if (user.isValid()) {
					storeUser(user);
				} else {
					status = OpenShiftUIActivator.createErrorStatus(NLS.bind(
							"The credentials for user {0} are not valid", user.getUsername()));
				}
			}
		} catch (NotFoundOpenShiftException e) {
			// valid user without domain
			storeUser(user);
		} catch (Exception e) {
			status = OpenShiftUIActivator.createErrorStatus(NLS.bind(
					"Could not check user credentials: {0}.", e.getMessage()));
		}
		return status;
	}

	private void storeUser(UserDelegate user) {
		wizardModel.setUser(user);
		if (rememberPassword) {
			storePassword(user);
		} else {
			erasePasswordStore();
		}
	}
	
	public UserDelegate getUser() {
		return wizardModel.getUser();
	}

}
