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
package org.jboss.tools.usage.internal.reporting;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.window.IShellProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.jboss.tools.usage.branding.IUsageBranding;
import org.jboss.tools.usage.internal.JBossToolsUsageActivator;
import org.jboss.tools.usage.util.BrowserUtil;
import org.jboss.tools.usage.util.BundleUtils;
import org.jboss.tools.usage.util.StatusUtils;
import org.osgi.framework.InvalidSyntaxException;

/**
 * @author Andre Dietisheim
 */
public class UsageReportEnablementDialog extends Dialog {

	private Button checkBox;
	private boolean reportEnabled;

	public UsageReportEnablementDialog(boolean reportEnabled, IShellProvider parentShell) {
		super(parentShell);
		this.reportEnabled = reportEnabled;
	}

	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID) {
			this.reportEnabled = checkBox.getSelection();
		} else if (buttonId == IDialogConstants.CANCEL_ID) {
			this.reportEnabled = false;
		}
		super.buttonPressed(buttonId);
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		try {
			IUsageBranding branding = BundleUtils.getHighestRankedService(IUsageBranding.class.getName(),
					JBossToolsUsageActivator
							.getDefault().getBundle());
			shell.setText(branding.getStartupAllowReportingTitle());
		} catch (InvalidSyntaxException e) {
			catchBrandingError(e);
		}
	}

	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
		checkBox.setFocus();
		checkBox.setSelection(reportEnabled);
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite) super.createDialogArea(parent);
		createUsageReportingWidgets(parent, composite);
		applyDialogFont(composite);

		return composite;
	}

	private void createUsageReportingWidgets(Composite parent, Composite composite) {
		try {
			// message
			Link link = new Link(composite, SWT.WRAP);
			link.setFont(parent.getFont());

			final IUsageBranding branding = BundleUtils.getHighestRankedService(IUsageBranding.class.getName(),
					JBossToolsUsageActivator.getDefault().getBundle());
			link.setText(branding.getStartupAllowReportingMessage());
			link.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					BrowserUtil.checkedCreateExternalBrowser(
							branding.getStartupAllowReportingDetailLink(),
							JBossToolsUsageActivator.PLUGIN_ID,
							JBossToolsUsageActivator.getDefault().getLog());
				}
			});
			GridDataFactory.fillDefaults()
					.align(SWT.FILL, SWT.CENTER)
					.grab(true, false)
					.hint(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH, SWT.DEFAULT)
					.applyTo(link);

			// checkbox
			checkBox = new Button(composite, SWT.CHECK);
			checkBox.setText(branding.getStartupAllowReportingCheckboxLabel());
			GridDataFactory.fillDefaults().grab(true, false).align(SWT.LEFT, SWT.CENTER).applyTo(checkBox);
		} catch (Exception e) {
			catchBrandingError(e);
		}
	}

	private void catchBrandingError(Exception e) {
		IStatus status = StatusUtils.getErrorStatus(JBossToolsUsageActivator.PLUGIN_ID, "Could not find branding.", e);
		ErrorDialog.openError(getShell(), "Branding Error", "Could not ask to allow usage reporting", status);
		close();
	}

	public boolean isReportEnabled() {
		return reportEnabled;
	}
}
