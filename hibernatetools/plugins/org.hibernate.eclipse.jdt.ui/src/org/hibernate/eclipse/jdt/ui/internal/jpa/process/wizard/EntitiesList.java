/*******************************************************************************
  * Copyright (c) 2007-2009 Red Hat, Inc.
  * Distributed under license by Red Hat, Inc. All rights reserved.
  * This program is made available under the terms of the
  * Eclipse Public License v1.0 which accompanies this distribution,
  * and is available at http://www.eclipse.org/legal/epl-v10.html
  *
  * Contributor:
  *     Red Hat, Inc. - initial API and implementation
  ******************************************************************************/
package org.hibernate.eclipse.jdt.ui.internal.jpa.process.wizard;

import org.eclipse.jdt.internal.ui.viewsupport.JavaElementImageProvider;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ltk.ui.refactoring.UserInputWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.hibernate.eclipse.jdt.ui.internal.JdtUiMessages;
import org.hibernate.eclipse.jdt.ui.internal.jpa.common.EntityInfo;
import org.hibernate.eclipse.jdt.ui.internal.jpa.process.AnnotStyle;

/**
 * Entities list wizard page
 *
 * @author Vitali
 */
public class EntitiesList extends UserInputWizardPage {
	
	protected IHibernateJPAWizardData data;

	protected IHibernateJPAWizardParams params;

	public EntitiesList(String name, IHibernateJPAWizardData data, IHibernateJPAWizardParams params) {
		super(name);
		this.data = data;
		this.params = params;
	}
	
	public void createControl(Composite parent) {
	    initializeDialogUnits(parent);
		Composite container = new Composite(parent, SWT.NULL);
        GridLayout layout = new GridLayout();
        container.setLayout(layout);
        layout.numColumns = 1;
        Label label = new Label(container, SWT.NULL);
        label.setText(JdtUiMessages.AllEntitiesProcessor_message);

        TableViewer listViewer = new TableViewer(container, SWT.SINGLE | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.BORDER);
		//listViewer.setComparator(getViewerComparator());
		Control control = listViewer.getControl();
		GridData gridData = new GridData(GridData.FILL_BOTH
				| GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL);
		gridData.heightHint = convertHeightInCharsToPixels(10);
		control.setLayoutData(gridData);
		listViewer.setContentProvider(new IStructuredContentProvider() {
			public Object[] getElements(Object inputElement) {
				return data.getEntities().values().toArray();
			}

			public void dispose() {

			}

			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {

			}
		});

		listViewer.setLabelProvider(new LabelProvider() {

			private Image classImage;

			{
				classImage = JavaElementImageProvider.getTypeImageDescriptor(false, false, 0, false).createImage();

			}
			@Override
			public String getText(Object element) {
				EntityInfo info = (EntityInfo) element;
				return info.getFullyQualifiedName();
			}

			@Override
			public Image getImage(Object element) {
				return classImage;
			}

			@Override
			public void dispose() {
				classImage.dispose();
				super.dispose();
			}
		});

		listViewer.setInput(data.getEntities());
		GridDataFactory.fillDefaults().align(SWT.FILL, SWT.FILL)
			.grab(true, true)
			.hint(convertHorizontalDLUsToPixels(IDialogConstants.MINIMUM_MESSAGE_AREA_WIDTH),
				convertHorizontalDLUsToPixels(2 * IDialogConstants.BUTTON_BAR_HEIGHT)).applyTo(listViewer.getControl());
		//Button generateChoice = new Button(container, SWT.CHECK);
		//generateChoice.setText("fdwsdfv");
		Composite combolabel = new Composite(container, SWT.NULL);
        layout = new GridLayout();
        combolabel.setLayout(layout);
        layout.numColumns = 2;
		Label labelChoice = new Label(combolabel, SWT.NULL);
		labelChoice.setText(JdtUiMessages.AllEntitiesProcessor_setup_annotation_generation_preference);
		Combo generateChoice = new Combo(combolabel, SWT.READ_ONLY);
		generateChoice.add(JdtUiMessages.AllEntitiesProcessor_annotate_fields);
		generateChoice.add(JdtUiMessages.AllEntitiesProcessor_annotate_getters);
		generateChoice.add(JdtUiMessages.AllEntitiesProcessor_auto_select_from_class_preference);
		int idx = 0;
		if (params.getAnnotationStyle().equals(AnnotStyle.FIELDS)) {
			idx = 0;
		}
		else if (params.getAnnotationStyle().equals(AnnotStyle.GETTERS)) {
			idx = 1;
		}
		else if (params.getAnnotationStyle().equals(AnnotStyle.AUTO)) {
			idx = 2;
		}
		generateChoice.select(idx);
		final ModifyListener ml = new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				int idx = ((Combo)e.getSource()).getSelectionIndex();
				if (idx == 0 && !params.getAnnotationStyle().equals(AnnotStyle.FIELDS)) {
					params.setAnnotationStyle(AnnotStyle.FIELDS);
					params.reCollectModification(data.getBufferManager(), data.getEntities());
				}
				else if (idx == 1 && !params.getAnnotationStyle().equals(AnnotStyle.GETTERS)) {
					params.setAnnotationStyle(AnnotStyle.GETTERS);
					params.reCollectModification(data.getBufferManager(), data.getEntities());
				}
				else if (idx == 2 && !params.getAnnotationStyle().equals(AnnotStyle.AUTO)) {
					params.setAnnotationStyle(params.getAnnotationStylePreference());
					params.reCollectModification(data.getBufferManager(), data.getEntities());
					params.setAnnotationStyle(AnnotStyle.AUTO);
				}
			}
			
		};
		generateChoice.addModifyListener(ml);
		setControl(container);
	}
}
