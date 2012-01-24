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
package org.jboss.tools.cdi.ui.marker;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.ILocalVariable;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.refactoring.CompilationUnitChange;
import org.eclipse.ltk.core.refactoring.TextChange;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Image;
import org.eclipse.text.edits.MultiTextEdit;
import org.eclipse.ui.IMarkerResolution2;
import org.jboss.tools.cdi.core.CDIImages;
import org.jboss.tools.cdi.internal.core.refactoring.CDIMarkerResolutionUtils;
import org.jboss.tools.cdi.ui.CDIUIMessages;
import org.jboss.tools.cdi.ui.CDIUIPlugin;
import org.jboss.tools.common.refactoring.MarkerResolutionUtils;
import org.jboss.tools.common.ui.CommonUIPlugin;

public class DeleteAnnotationMarkerResolution implements
		IMarkerResolution2 {
	private IJavaElement element;
	private String qualifiedName;
	private String label;
	private String description;
	
	public DeleteAnnotationMarkerResolution(IJavaElement element, String qualifiedName){
		this.element = element;
		this.qualifiedName = qualifiedName;
		String shortName = CDIMarkerResolutionUtils.getShortName(qualifiedName);
		String type = "";
		if(element instanceof IType){
			try {
				if(((IType) element).isAnnotation())
					type = CDIUIMessages.CDI_QUICK_FIXES_ANNOTATION;
				else if(((IType) element).isInterface())
					type = CDIUIMessages.CDI_QUICK_FIXES_INTERFACE;
				else if(((IType) element).isClass())
					type = CDIUIMessages.CDI_QUICK_FIXES_CLASS;
				else
					type = CDIUIMessages.CDI_QUICK_FIXES_TYPE;
			} catch (JavaModelException ex) {
				CDIUIPlugin.getDefault().logError(ex);
			}
		}else if(element instanceof IMethod){
			type = CDIUIMessages.CDI_QUICK_FIXES_METHOD;
		}else if(element instanceof IField){
			type = CDIUIMessages.CDI_QUICK_FIXES_FIELD;
		}else if(element instanceof ILocalVariable && ((ILocalVariable) element).isParameter()){
			type = NLS.bind(CDIUIMessages.CDI_QUICK_FIXES_PARAMETER, element.getParent().getElementName());
		}
			
		label = NLS.bind(CDIUIMessages.DELETE_ANNOTATION_MARKER_RESOLUTION_TITLE, new String[]{shortName, element.getElementName(), type});
		description = getPreview();
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void run(IMarker marker) {
		try{
			ICompilationUnit original = CDIMarkerResolutionUtils.getJavaMember(element).getCompilationUnit();
			ICompilationUnit compilationUnit = original.getWorkingCopy(new NullProgressMonitor());

			CompilationUnitChange change = getChange(compilationUnit);
			
			if(change.getEdit().hasChildren()){
				change.perform(new NullProgressMonitor());
				original.reconcile(ICompilationUnit.NO_AST, false, null, new NullProgressMonitor());
			}
			compilationUnit.discardWorkingCopy();
		}catch(CoreException ex){
			CDIUIPlugin.getDefault().logError(ex);
		}
	}
	
	private CompilationUnitChange getChange(ICompilationUnit compilationUnit) throws JavaModelException{
		CompilationUnitChange change = new CompilationUnitChange("", compilationUnit);
		
		MultiTextEdit edit = new MultiTextEdit();
		
		change.setEdit(edit);
		
		CDIMarkerResolutionUtils.deleteAnnotation(qualifiedName, compilationUnit, element, edit);
		
		return change;
	}
	
	private CompilationUnitChange getPreviewChange(){
		try{
			ICompilationUnit original = CDIMarkerResolutionUtils.getJavaMember(element).getCompilationUnit();
			
			return getChange(original);
		}catch(CoreException ex){
			CDIUIPlugin.getDefault().logError(ex);
		}
		return null;
	}
	
	protected String getPreview(){
		TextChange previewChange = getPreviewChange();
		
		try {
			return MarkerResolutionUtils.getPreview(previewChange);
		} catch (CoreException e) {
			CommonUIPlugin.getDefault().logError(e);
		}
		return label;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public Image getImage() {
		return CDIImages.QUICKFIX_REMOVE;
	}
}