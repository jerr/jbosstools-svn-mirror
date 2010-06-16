/*******************************************************************************
 * Copyright (c) 20010 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.cdi.ui.test.wizard;

import junit.framework.TestCase;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.PlatformUI;
import org.jboss.tools.cdi.ui.CDIUIPlugin;
import org.jboss.tools.cdi.ui.wizard.NewCDIAnnotationCreationWizard;
import org.jboss.tools.cdi.ui.wizard.NewCDIAnnotationWizardPage;
import org.jboss.tools.cdi.ui.wizard.NewInterceptorBindingWizardPage;
import org.jboss.tools.cdi.ui.wizard.NewQualifierWizardPage;
import org.jboss.tools.cdi.ui.wizard.NewScopeWizardPage;
import org.jboss.tools.cdi.ui.wizard.NewStereotypeWizardPage;
import org.jboss.tools.common.EclipseUtil;
import org.jboss.tools.common.util.FileUtil;
import org.jboss.tools.test.util.JUnitUtils;
import org.jboss.tools.test.util.WorkbenchUtils;

/**
 * @author Viacheslav Kabanovich
 *
 */
public class NewCDIWizardTest extends TestCase {
	
	static String PACK_NAME = "test";
	static String QUALIFIER_NAME = "MyQualifier";
	static String STEREOTYPE_NAME = "MyStereotype";
	static String SCOPE_NAME = "MyScope";
	static String INTERCEPTOR_BINDING_NAME = "MyInterceptorBinding";
	
	static class WizardContext {
		NewCDIAnnotationCreationWizard wizard;
		IProject tck;
		IJavaProject jp;
		WizardDialog dialog;
		NewCDIAnnotationWizardPage page;
		String packName;
		String typeName;
		

		public void init(String wizardId, String packName, String typeName) {
			this.packName = packName;
			this.typeName = typeName;
			wizard = (NewCDIAnnotationCreationWizard)WorkbenchUtils.findWizardByDefId(wizardId);
			tck = ResourcesPlugin.getWorkspace().getRoot().getProject("tck");
			jp = EclipseUtil.getJavaProject(tck);
			wizard.init(CDIUIPlugin.getDefault().getWorkbench(), new StructuredSelection(jp));
			dialog = new WizardDialog(
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
					wizard);
			dialog.setBlockOnOpen(false);
			dialog.open();

			page = (NewCDIAnnotationWizardPage)dialog.getSelectedPage();

			page.setTypeName(typeName, true);
			IPackageFragment pack = page.getPackageFragmentRoot().getPackageFragment(PACK_NAME);
			page.setPackageFragment(pack, true);
		}

		public String getNewTypeContent() {
			IType type = null;
			try {
				type = jp.findType(packName + "." + typeName);
			} catch (JavaModelException e) {
				JUnitUtils.fail("Cannot find type " + typeName, e);
			}
			
			IFile file = (IFile)type.getResource();
			assertNotNull(file);
			String text = null;
			try {
				text = FileUtil.readStream(file.getContents());
			} catch (CoreException e) {
				JUnitUtils.fail("Cannot read from " + file, e);
			}
			return text;
		}

		public void close() {
			dialog.close();
		}
		
	}

	public void testNewQualifierWizard() {
		WizardContext context = new WizardContext();
		context.init("org.jboss.tools.cdi.ui.wizard.NewQualifierCreationWizard",
				PACK_NAME, QUALIFIER_NAME);

		try {
			NewQualifierWizardPage page = (NewQualifierWizardPage)context.page;
			page.setInherited(true);
			
			context.wizard.performFinish();
			
			String text = context.getNewTypeContent();
			
			assertTrue(text.contains("@Qualifier"));
			assertTrue(text.contains("@Inherited"));
			assertTrue(text.contains("@Target( { TYPE, METHOD, PARAMETER, FIELD })"));
			assertTrue(text.contains("@Retention(RUNTIME)"));
			
		} finally {
			context.close();
		}
	}

	public void testNewStereotypeWizard() {
		WizardContext context = new WizardContext();
		context.init("org.jboss.tools.cdi.ui.wizard.NewStereotypeCreationWizard",
				PACK_NAME, STEREOTYPE_NAME);

		try {
			NewStereotypeWizardPage page = (NewStereotypeWizardPage)context.page;
			page.setInherited(true);
			page.setTarget("METHOD,FIELD");
			page.setNamed(true);
			
			context.wizard.performFinish();
			
			String text = context.getNewTypeContent();
			
			assertTrue(text.contains("@Stereotype"));
			assertTrue(text.contains("@Inherited"));
			assertTrue(text.contains("@Named"));
			assertTrue(text.contains("@Target( { METHOD, FIELD })"));
			assertTrue(text.contains("@Retention(RUNTIME)"));
			
		} finally {
			context.close();
		}

	}

	public void testNewScopeWizard() {
		WizardContext context = new WizardContext();
		context.init("org.jboss.tools.cdi.ui.wizard.NewScopeCreationWizard",
				PACK_NAME, SCOPE_NAME);

		try {
			NewScopeWizardPage page = (NewScopeWizardPage)context.page;
			
			context.wizard.performFinish();
			
			String text = context.getNewTypeContent();
			
			assertTrue(text.contains("@NormalScope"));
			assertTrue(text.contains("@Inherited"));
			assertTrue(text.contains("@Target( { TYPE, METHOD, FIELD })"));
			assertTrue(text.contains("@Retention(RUNTIME)"));
			
		} finally {
			context.close();
		}
	}

	public void testNewInterceptorBindingWizard() {
		WizardContext context = new WizardContext();
		context.init("org.jboss.tools.cdi.ui.wizard.NewInterceptorBindingCreationWizard",
				PACK_NAME, INTERCEPTOR_BINDING_NAME);

		try {
			NewInterceptorBindingWizardPage page = (NewInterceptorBindingWizardPage)context.page;
			
			context.wizard.performFinish();
			
			String text = context.getNewTypeContent();
			
			assertTrue(text.contains("@InterceptorBinding"));
			assertTrue(text.contains("@Inherited"));
			assertTrue(text.contains("@Target( { TYPE, METHOD })"));
			assertTrue(text.contains("@Retention(RUNTIME)"));
			
		} finally {
			context.close();
		}
	}

}