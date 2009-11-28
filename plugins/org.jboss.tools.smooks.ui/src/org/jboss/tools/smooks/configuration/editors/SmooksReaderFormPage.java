/*******************************************************************************
 * Copyright (c) 2008 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks.configuration.editors;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.eclipse.core.filesystem.EFS;
import org.eclipse.core.filesystem.IFileInfo;
import org.eclipse.core.filesystem.IFileStore;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.command.SetCommand;
import org.eclipse.emf.edit.domain.AdapterFactoryEditingDomain;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.ScrolledPageBook;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.ide.IDE;
import org.jboss.tools.smooks.configuration.editors.input.InputType;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.editors.wizard.IStructuredDataSelectionWizard;
import org.jboss.tools.smooks.configuration.editors.wizard.StructuredDataSelectionWizard;
import org.jboss.tools.smooks.configuration.editors.wizard.ViewerInitorStore;
import org.jboss.tools.smooks.configuration.validate.ISmooksModelValidateListener;
import org.jboss.tools.smooks.editor.ISmooksModelProvider;
import org.jboss.tools.smooks.editor.ISourceSynchronizeListener;
import org.jboss.tools.smooks.model.csv.CsvPackage;
import org.jboss.tools.smooks.model.csv.CsvReader;
import org.jboss.tools.smooks.model.csv12.CSV12Reader;
import org.jboss.tools.smooks.model.csv12.Csv12Package;
import org.jboss.tools.smooks.model.edi.EDIReader;
import org.jboss.tools.smooks.model.edi.EdiPackage;
import org.jboss.tools.smooks.model.edi12.EDI12Reader;
import org.jboss.tools.smooks.model.edi12.Edi12Package;
import org.jboss.tools.smooks.model.json.JsonPackage;
import org.jboss.tools.smooks.model.json.JsonReader;
import org.jboss.tools.smooks.model.json12.Json12Package;
import org.jboss.tools.smooks.model.json12.Json12Reader;
import org.jboss.tools.smooks.model.smooks.AbstractReader;
import org.jboss.tools.smooks.model.smooks.DocumentRoot;
import org.jboss.tools.smooks.model.smooks.ParamType;
import org.jboss.tools.smooks.model.smooks.ParamsType;
import org.jboss.tools.smooks.model.smooks.ReaderType;
import org.jboss.tools.smooks.model.smooks.SmooksFactory;
import org.jboss.tools.smooks.model.smooks.SmooksPackage;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;
import org.jboss.tools.smooks10.model.smooks.util.SmooksModelUtils;

/**
 * @author Dart
 * 
 */
public class SmooksReaderFormPage extends FormPage implements ISmooksModelValidateListener, ISourceSynchronizeListener {

	private CheckboxTableViewer inputDataViewer;
	private TreeViewer inputModelViewer;
	private Combo readerCombo;
	private List<Object> readerTypeList = new ArrayList<Object>();
	private Composite readerConfigComposite;
	private ModelPanelCreator modelPanelCreator;
	protected boolean lockCheck = false;
	private Button removeInputDataButton;
	private Button addInputDataButton;

	public SmooksReaderFormPage(FormEditor editor, String id, String title) {
		super(editor, id, title);
	}

	public SmooksReaderFormPage(String id, String title) {
		super(id, title);
		IResource resource;
	}

	@Override
	protected void createFormContent(IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		FormToolkit toolkit = managedForm.getToolkit();
		// toolkit.decorateFormHeading(form.getForm());
		// form.setText("Input");
		// // create master details UI
		// createMasterDetailBlock(managedForm);
		Composite leftComposite = toolkit.createComposite(form.getBody());
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.widthHint = 300;
		leftComposite.setLayoutData(gd);

		Composite rightComposite = toolkit.createComposite(form.getBody());
		gd = new GridData(GridData.FILL_BOTH);
		rightComposite.setLayoutData(gd);

		GridLayout lgl = new GridLayout();
		lgl.marginWidth = 0;
		lgl.marginHeight = 0;
		leftComposite.setLayout(lgl);

		GridLayout rgl = new GridLayout();
		rgl.marginWidth = 0;
		rgl.marginHeight = 0;
		rightComposite.setLayout(rgl);

		createReaderSection(toolkit, leftComposite);
		createInputDataSection(toolkit, rightComposite);
		createReaderConfigSection(toolkit, leftComposite);
		createInputModelViewerSection(toolkit, rightComposite);

		handleReaderCombo(readerCombo);

		GridLayout gridLayout = new GridLayout();
		gridLayout.marginHeight = 13;
		gridLayout.numColumns = 2;
		gridLayout.horizontalSpacing = 20;
		gridLayout.makeColumnsEqualWidth = true;
		form.getBody().setLayout(gridLayout);

		refreshInputDataButtons();
	}

	private void createInputModelViewerSection(FormToolkit toolkit, Composite parent) {
		Section section = toolkit.createSection(parent, Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
		GridData gd = new GridData(GridData.FILL_BOTH);
		// gd.verticalAlignment = GridData.BEGINNING;
		section.setLayoutData(gd);
		section.setText("Input Model View");
		// section.setDescription("View the XML structure model of the input data");
		FillLayout flayout = new FillLayout();
		section.setLayout(flayout);

		Composite mainContainer = toolkit.createComposite(section);
		GridLayout gl = new GridLayout();
		mainContainer.setLayout(gl);
		section.setClient(mainContainer);

		Hyperlink refreshLink = toolkit.createHyperlink(mainContainer, "Refresh", SWT.NONE);
		refreshLink.addHyperlinkListener(new IHyperlinkListener() {

			public void linkExited(HyperlinkEvent e) {
				// TODO Auto-generated method stub

			}

			public void linkEntered(HyperlinkEvent e) {
				// TODO Auto-generated method stub

			}

			public void linkActivated(HyperlinkEvent e) {
				refreshInputModelView();
			}
		});

		Composite viewerContainer = toolkit.createComposite(mainContainer);
		gd = new GridData(GridData.FILL_BOTH);
		viewerContainer.setLayoutData(gd);

		FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 1;
		fillLayout.marginWidth = 1;
		viewerContainer.setBackground(GraphicsConstants.BORDER_CORLOR);
		viewerContainer.setLayout(fillLayout);
		inputModelViewer = new TreeViewer(viewerContainer, SWT.NONE);
		inputModelViewer.setContentProvider(new CompoundStructuredDataContentProvider());
		inputModelViewer.setLabelProvider(new CompoundStructuredDataLabelProvider());
		List<Object> inputList = generateInputData();
		inputModelViewer.setInput(inputList);
		inputModelViewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
			}
		});
		inputModelViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				// currentSelection = ((IStructuredSelection)
				// event.getSelection()).getFirstElement();
			}
		});
		SmooksUIUtils.expandSelectorViewer(inputList, inputModelViewer);

	}

	protected List<Object> generateInputData() {
		Object obj = ((SmooksMultiFormEditor) getEditor()).getSmooksModel();
		SmooksResourceListType resourceList = null;
		if (obj instanceof DocumentRoot) {
			resourceList = ((DocumentRoot) obj).getSmooksResourceList();
		}
		return SelectorCreationDialog.generateInputData(resourceList);
	}

	private void createReaderConfigSection(FormToolkit toolkit, Composite parent) {
		Section section = toolkit.createSection(parent, Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
		GridData gd = new GridData(GridData.FILL_BOTH);
		section.setLayoutData(gd);
		section.setText("Input Configuration");
		section.setDescription("Specify reader details");
		FillLayout flayout = new FillLayout();
		section.setLayout(flayout);

		ScrolledPageBook pageBook = new ScrolledPageBook(section);
		pageBook.setBackground(toolkit.getColors().getBackground());
		section.setClient(pageBook);

		readerConfigComposite = pageBook.createPage(pageBook);
		pageBook.showPage(pageBook);

		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		readerConfigComposite.setLayout(gl);

		initReaderConfigSection();
	}

	private void createReaderSection(FormToolkit toolkit, Composite parent) {
		Section section = toolkit.createSection(parent, Section.TITLE_BAR | Section.TWISTIE | Section.EXPANDED);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.verticalAlignment = GridData.BEGINNING;
		section.setLayoutData(gd);
		section.setText("Input Type");
		// section.setDescription("Select the input type");
		FillLayout flayout = new FillLayout();
		section.setLayout(flayout);

		Composite mainComposite = toolkit.createComposite(section);
		section.setClient(mainComposite);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		mainComposite.setLayout(gl);

		toolkit.createLabel(mainComposite, "Input Type:");

		readerCombo = new Combo(mainComposite, SWT.NONE | SWT.READ_ONLY);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		readerCombo.setLayoutData(gd);

		toolkit.createLabel(mainComposite, " ");

		bindingReaderCombo();

		initReaderCombo();

		gd = new GridData(GridData.FILL_HORIZONTAL);
		readerCombo.setLayoutData(gd);

		toolkit.paintBordersFor(mainComposite);
	}

	private void initReaderConfigSection() {
		Object reader = getCurrentReaderModel();
		ISmooksModelProvider provider = getSmooksModelProvider();
		if (provider == null)
			return;
		String type = provider.getInputType();
		if (reader instanceof EObject && type != null) {
			SmooksResourceListType list = getSmooksConfigResourceList();
			createReaderPanel((EObject) list.getAbstractReader().get(0));
		} else {
			disposeCompositeControls(readerConfigComposite, null);
		}
	}

	private void selectCorrectReaderItem(Object reader) {
		for (int i = 0; i < readerTypeList.size(); i++) {
			Object r = readerTypeList.get(i);
			if (r instanceof EObject) {
				if (r.getClass() == reader.getClass()) {
					readerCombo.select(i);
					break;
				}
			}
		}
	}

	private String getCurrentReaderType() {
		Object reader = getCurrentReaderModel();
		return getReaderType(reader);
	}

	private void initReaderCombo() {
		if (readerCombo == null)
			return;
		SmooksResourceListType rlist = getSmooksConfigResourceList();
		if (rlist == null) {
			readerCombo.select(-1);
			return;
		}

		ISmooksModelProvider modelProvider = getSmooksModelProvider();

		String inputType = modelProvider.getInputType();

		if (inputType == null) {
			// for the first time to open the file.
			if (rlist.getAbstractReader().isEmpty()) {
				readerCombo.select(0);
				return;
			} else {
			}
		}
		if (SmooksModelUtils.INPUT_TYPE_XML.equals(inputType)) {
			readerCombo.select(1);
		}
		if (SmooksModelUtils.INPUT_TYPE_JAVA.equals(inputType)) {
			readerCombo.select(2);
		}
		if (SmooksModelUtils.INPUT_TYPE_XSD.equals(inputType)) {
			readerCombo.select(3);
		}

		if (SmooksModelUtils.INPUT_TYPE_CSV.equals(inputType)) {
			if (!rlist.getAbstractReader().isEmpty()) {
				AbstractReader reader = rlist.getAbstractReader().get(0);
				if (CsvReader.class.isInstance(reader) || CSV12Reader.class.isInstance(reader)) {
					selectCorrectReaderItem(reader);
				}
			}
		}
		if (SmooksModelUtils.INPUT_TYPE_EDI_1_1.equals(inputType)) {
			if (!rlist.getAbstractReader().isEmpty()) {
				AbstractReader reader = rlist.getAbstractReader().get(0);
				if (EDIReader.class.isInstance(reader) || EDI12Reader.class.isInstance(reader)) {
					selectCorrectReaderItem(reader);
				}
			}
		}
		if (SmooksModelUtils.INPUT_TYPE_JSON_1_1.equals(inputType)) {
			if (!rlist.getAbstractReader().isEmpty()) {
				AbstractReader reader = rlist.getAbstractReader().get(0);
				if (JsonReader.class.isInstance(reader) || Json12Reader.class.isInstance(reader)) {
					selectCorrectReaderItem(reader);
				}
			}
		}
		if (SmooksModelUtils.INPUT_TYPE_CUSTOME.equals(inputType)) {
			if (!rlist.getAbstractReader().isEmpty()) {
				AbstractReader reader = rlist.getAbstractReader().get(0);
				if (ReaderType.class.isInstance(reader)) {
					selectCorrectReaderItem(reader);
				}
			}
		}
		return;
	}

	private void handleReaderCombo(final Combo combo) {
		combo.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				Object newreader = getCurrentReaderModel();
				if (newreader == null)
					return;
				// String type = getCurrentReaderType();
				// if (type == null) {
				// getSmooksGraphicsExtType().eUnset(GraphPackage.Literals.SMOOKS_GRAPHICS_EXT_TYPE__INPUT_TYPE);
				// } else {
				// getSmooksGraphicsExtType().setInputType(type);
				// }
				readerChanged(newreader);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub

			}
		});
	}

	private Object createReaderEntry(Object reader, boolean clone) {
		if (clone) {
			reader = EcoreUtil.copy((EObject) reader);
		}
		if (reader instanceof CsvReader) {
			return FeatureMapUtil.createEntry(CsvPackage.Literals.CSV_DOCUMENT_ROOT__READER, reader);
		}
		if (reader instanceof CSV12Reader) {
			return FeatureMapUtil.createEntry(Csv12Package.Literals.CSV12_DOCUMENT_ROOT__READER, reader);
		}
		if (reader instanceof EDIReader) {
			return FeatureMapUtil.createEntry(EdiPackage.Literals.EDI_DOCUMENT_ROOT__READER, reader);
		}
		if (reader instanceof EDI12Reader) {
			return FeatureMapUtil.createEntry(Edi12Package.Literals.EDI12_DOCUMENT_ROOT__READER, reader);
		}
		if (reader instanceof JsonReader) {
			return FeatureMapUtil.createEntry(JsonPackage.Literals.JSON_DOCUMENT_ROOT__READER, reader);
		}
		if (reader instanceof Json12Reader) {
			return FeatureMapUtil.createEntry(Json12Package.Literals.JSON12_DOCUMENT_ROOT__READER, reader);
		}
		if (reader instanceof ReaderType) {
			return FeatureMapUtil.createEntry(SmooksPackage.Literals.DOCUMENT_ROOT__READER, reader);
		}
		return null;
	}

	private Command createRemoveReaderCommand() {
		SmooksResourceListType rlist = getSmooksConfigResourceList();
		List<AbstractReader> readerList = rlist.getAbstractReader();
		CompoundCommand compoundCommand = new CompoundCommand();
		for (Iterator<?> iterator = readerList.iterator(); iterator.hasNext();) {
			AbstractReader abstractReader = (AbstractReader) iterator.next();
			Object readerEntry = createReaderEntry(abstractReader, false);
			if (readerEntry == null)
				continue;
			Command removeCommand = RemoveCommand.create(getEditingDomain(), rlist,
					SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_READER_GROUP, readerEntry);
			if (removeCommand.canExecute()) {
				compoundCommand.append(removeCommand);
			}
		}
		if (compoundCommand.isEmpty()) {
			return null;
		}
		return compoundCommand;
	}

	private String getReaderType(Object reader) {
		if (reader instanceof XMLReader) {
			return SmooksModelUtils.INPUT_TYPE_XML;
		}
		if (reader instanceof JavaReader) {
			return SmooksModelUtils.INPUT_TYPE_JAVA;
		}
		if (reader instanceof XSDReader) {
			return SmooksModelUtils.INPUT_TYPE_XSD;
		}
		if (reader instanceof EObject) {
			Object obj = ((EObject) reader);

			if (obj instanceof CsvReader || obj instanceof CSV12Reader) {
				return SmooksModelUtils.INPUT_TYPE_CSV;
			}
			if (obj instanceof EDIReader || obj instanceof EDI12Reader) {
				return SmooksModelUtils.INPUT_TYPE_EDI_1_1;
			}
			if (obj instanceof JsonReader || obj instanceof Json12Reader) {
				return SmooksModelUtils.INPUT_TYPE_JSON_1_1;
			}
			if (obj instanceof ReaderType) {
				return SmooksModelUtils.INPUT_TYPE_CUSTOME;
			}
		}
		return null;
	}

	private void refreshInputDataButtons() {
		this.addInputDataButton.setEnabled(true);
		this.removeInputDataButton.setEnabled(true);

		String inputType = getSmooksModelProvider().getInputType();
		if (inputType == null || inputType.trim().equals("")) {
			this.addInputDataButton.setEnabled(false);
			this.removeInputDataButton.setEnabled(false);
		}
	}

	private void readerChanged(Object reader) {

		String type = getCurrentReaderType();
		String oldType = this.getSmooksModelProvider().getInputType();

		if (type == null && oldType == null) {
			return;
		}
		if (type != null && type.equals(oldType)) {
			return;
		}
		if (oldType != null && oldType.equals(type)) {
			return;
		}

		CompoundCommand compoundCommand = new CompoundCommand();

		ParamsType params = getSmooksConfigResourceList().getParams();
		if (params == null) {
			params = SmooksFactory.eINSTANCE.createParamsType();
			Command addparamsCommand = SetCommand.create(getEditingDomain(), getSmooksConfigResourceList(),
					SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__PARAMS, params);
			if (addparamsCommand.canExecute())
				compoundCommand.append(addparamsCommand);
		}

		org.jboss.tools.smooks.model.smooks.ParamType param = SmooksUIUtils
				.getInputTypeParam(getSmooksConfigResourceList());
		if (param == null) {
			// add new one
			param = SmooksFactory.eINSTANCE.createParamType();
			param.setName(SmooksModelUtils.INPUT_TYPE);
			param.setStringValue(type);
			Command addparamc = AddCommand.create(getEditingDomain(), params,
					SmooksPackage.Literals.PARAMS_TYPE__PARAM, param);
			if (addparamc.canExecute())
				compoundCommand.append(addparamc);
		} else {

			Command addCommand = null;
			if (type != null) {
				addCommand = AddCommand.create(getEditingDomain(), param, XMLTypePackage.Literals.ANY_TYPE__MIXED,
						FeatureMapUtil.createEntry(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__TEXT, type));
			}
			Object removeValue = (param.getMixed().get(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__TEXT, true));
			if (removeValue != null && removeValue instanceof Collection<?>) {
				List<Object> rList = new ArrayList<Object>();
				for (Iterator<?> iterator = ((Collection<?>) removeValue).iterator(); iterator.hasNext();) {
					Object string = (Object) iterator.next();
					rList.add(FeatureMapUtil.createEntry(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__TEXT, string));
				}
				Command cc = RemoveCommand.create(getEditingDomain(), param, null, rList);
				if (cc != null && cc.canExecute()) {
					compoundCommand.append(cc);
				}
			}
			if (addCommand != null && addCommand.canExecute()) {
				compoundCommand.append(addCommand);
			}

		}
		Command removeCommand = createRemoveReaderCommand();
		if (removeCommand != null && removeCommand.canExecute()) {
			compoundCommand.append(removeCommand);
		}
		if (readerConfigComposite != null) {
			disposeCompositeControls(readerConfigComposite, null);
		}
		if (reader instanceof EObject) {
			Object obj = ((EObject) reader);
			obj = AdapterFactoryEditingDomain.unwrap(obj);
			Command command = AddCommand.create(getEditingDomain(), getSmooksConfigResourceList(),
					SmooksPackage.Literals.SMOOKS_RESOURCE_LIST_TYPE__ABSTRACT_READER_GROUP, createReaderEntry(obj,
							false));
			if (command.canExecute()) {
				compoundCommand.append(command);
			}

		}
		deactiveAllInputFile(compoundCommand);
		if (!compoundCommand.isEmpty() && compoundCommand.canExecute()) {
			getEditingDomain().getCommandStack().execute(compoundCommand);
			if (reader != null && reader instanceof EObject) {
				createReaderPanel(((EObject) reader));
			}
		}

		if (inputDataViewer != null) {
			inputDataViewer.refresh();
		}
		refreshInputModelView();
		refreshInputDataButtons();
	}

	private void deactiveAllInputFile(CompoundCommand command) {
		Object viewerInput = this.inputDataViewer.getInput();
		if (viewerInput != null && viewerInput instanceof List<?>) {
			List<InputType> inputList = (List) viewerInput;
			for (Iterator<?> iterator = inputList.iterator(); iterator.hasNext();) {
				InputType inputType = (InputType) iterator.next();
				setInputDataActiveStatus(false, inputType, command);
			}
		}
	}

	private void createReaderPanel(EObject reader) {
		disposeCompositeControls(readerConfigComposite, null);
		try {
			ModelPanelCreator modelPanelCreator = getModelPanelCreator();
			IItemPropertySource ps = (IItemPropertySource) getEditingDomain().getAdapterFactory().adapt(reader,
					IItemPropertySource.class);
			modelPanelCreator.createModelPanel(reader, getManagedForm().getToolkit(), readerConfigComposite, ps,
					(ISmooksModelProvider) getEditor(), getEditor());
			readerConfigComposite.getParent().layout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ModelPanelCreator getModelPanelCreator() {
		if (modelPanelCreator == null) {
			modelPanelCreator = new ModelPanelCreator();
		}
		return modelPanelCreator;
	}

	private void bindingReaderCombo() {
		if (readerCombo == null)
			return;

		readerCombo.removeAll();
		readerTypeList.clear();

		readerCombo.add("No Input");
		readerTypeList.add(new NullReader());
		readerCombo.add("XML");
		readerTypeList.add(new XMLReader());
		readerCombo.add("Java");
		readerTypeList.add(new JavaReader());
		readerCombo.add("XSD/WSDL");
		readerTypeList.add(new XSDReader());

		SmooksResourceListType resourceList = getSmooksConfigResourceList();

		if (resourceList == null) {
			return;
		}

		AdapterFactoryEditingDomain editDomain = getEditingDomain();
		Object obj = editDomain.getAdapterFactory().adapt(resourceList, IEditingDomainItemProvider.class);
		IEditingDomainItemProvider provider = (IEditingDomainItemProvider) editDomain.getAdapterFactory().adapt(
				resourceList, IEditingDomainItemProvider.class);
		Collection<?> collections = provider.getNewChildDescriptors(resourceList, editDomain, null);

		ISmooksModelProvider modelProvider = getSmooksModelProvider();
		if (modelProvider != null) {
			String version = modelProvider.getPlatformVersion();
			OnlyReaderViewerFilter filter = new OnlyReaderViewerFilter();
			for (Iterator<?> iterator = collections.iterator(); iterator.hasNext();) {
				Object object = (Object) iterator.next();
				if (object instanceof CommandParameter) {
					Object value = ((CommandParameter) object).getValue();

					value = AdapterFactoryEditingDomain.unwrap(value);

					if (filter.select(null, null, value)) {
						if (SmooksUIUtils.isUnSupportElement(version, (EObject) value)) {
							continue;
						}

						IItemLabelProvider lp = (IItemLabelProvider) editDomain.getAdapterFactory().adapt(value,
								IItemLabelProvider.class);
						String text = lp.getText(value);
						readerCombo.add(text);
						readerTypeList.add(value);
					}
				}
			}
		}
	}

	private void setInputDataActiveStatus(boolean active, InputType inputType, final CompoundCommand command) {
		inputType.setActived(active);
		ParamType param = SmooksUIUtils.getInputTypeAssociatedParamType(inputType, getSmooksConfigResourceList());
		if (param != null) {
			String value = SmooksModelUtils.INPUT_ACTIVE_TYPE;
			if (!active) {
				value = SmooksModelUtils.INPUT_DEACTIVE_TYPE;
			}
			Command c = SetCommand.create(this.getEditingDomain(), param, SmooksPackage.Literals.PARAM_TYPE__TYPE,
					value);
			if (command != null) {
				command.append(c);
			} else {
				getEditingDomain().getCommandStack().execute(c);
			}
		}
		this.inputTypeChanged();
		// SmooksGraphicsExtType ext = getSmooksGraphicsExtType();
		// if (ext != null) {
		// List<ISmooksGraphChangeListener> listeners = ((SmooksGraphicsExtType)
		// ext).getChangeListeners();
		// for (Iterator<?> iterator = listeners.iterator();
		// iterator.hasNext();) {
		// ISmooksGraphChangeListener smooksGraphChangeListener =
		// (ISmooksGraphChangeListener) iterator.next();
		// smooksGraphChangeListener.inputTypeChanged((SmooksGraphicsExtType)
		// ext);
		// }
		// }
	}

	protected void createInputDataSection(FormToolkit toolkit, Composite parent) {
		Section section = toolkit.createSection(parent, Section.TITLE_BAR | Section.DESCRIPTION | Section.TWISTIE
				| Section.EXPANDED);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		section.setLayoutData(gd);
		section.setText("Input Data");
		section.setDescription("Select a sample data file");
		FillLayout flayout = new FillLayout();
		section.setLayout(flayout);

		Composite mainComposite = toolkit.createComposite(section, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		mainComposite.setLayout(gl);
		section.setClient(mainComposite);

		Composite tableComposite = toolkit.createComposite(mainComposite, SWT.NONE);
		FillLayout fillLayout = new FillLayout();
		fillLayout.marginHeight = 1;
		fillLayout.marginWidth = 1;
		gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 70;
		tableComposite.setLayoutData(gd);
		tableComposite.setBackground(GraphicsConstants.BORDER_CORLOR);
		tableComposite.setLayout(fillLayout);

		inputDataViewer = CheckboxTableViewer.newCheckList(tableComposite, SWT.MULTI | SWT.FULL_SELECTION);
		// inputDataViewer.set
		inputDataViewer.setCheckStateProvider(new ICheckStateProvider() {

			public boolean isGrayed(Object element) {
				return isIncorrectInputType((InputType) element);
			}

			public boolean isChecked(Object element) {
				if (element instanceof InputType) {
					return ((InputType) element).isActived();
				}
				return false;
			}
		});
		inputDataViewer.addCheckStateListener(new ICheckStateListener() {

			public void checkStateChanged(CheckStateChangedEvent event) {
				if (lockCheck)
					return;
				boolean checked = event.getChecked();
				InputType inputType = (InputType) event.getElement();
				if (isIncorrectInputType(inputType)) {
					lockCheck = true;
					inputDataViewer.setChecked(inputType, false);
					lockCheck = false;
					return;
				}
				CompoundCommand compoundCommand = new CompoundCommand();
				if (checked) {
					ParamType param = SmooksUIUtils.getInputTypeAssociatedParamType(inputType,
							getSmooksConfigResourceList());
					if (param != null) {
						inputType.setActived(checked);
						String value = SmooksModelUtils.INPUT_ACTIVE_TYPE;
						Command c = SetCommand.create(getEditingDomain(), param,
								SmooksPackage.Literals.PARAM_TYPE__TYPE, value);
						if (c.canExecute())
							compoundCommand.append(c);
					}
					// getEditingDomain().getCommandStack().execute(setCommand);
					// command.append(c);
					// boolean newOne = true;
					// for (Iterator<?> iterator = params.iterator();
					// iterator.hasNext();) {
					// ParamType paramType = (ParamType) iterator.next();
					// if
					// (SmooksModelUtils.PARAM_NAME_ACTIVED.equals(paramType.getName()))
					// {
					// Command setCommand =
					// SetCommand.create(getEditingDomain(), paramType,
					// GraphPackage.Literals.PARAM_TYPE__VALUE,
					// String.valueOf(checked));
					// getEditingDomain().getCommandStack().execute(setCommand);
					// newOne = false;
					// break;
					// }
					// }
					// if (newOne) {
					// ParamType p = GraphFactory.eINSTANCE.createParamType();
					// p.setName(SmooksModelUtils.PARAM_NAME_ACTIVED);
					// p.setValue(String.valueOf(checked));
					// Command addCommand =
					// AddCommand.create(getEditingDomain(), inputType,
					// GraphPackage.Literals.INPUT_TYPE__PARAM, p);
					// getEditingDomain().getCommandStack().execute(addCommand);
					// inputType.getParam().add(p);
					// }

					Object[] checkedObjects = inputDataViewer.getCheckedElements();
					for (int i = 0; i < checkedObjects.length; i++) {
						InputType type = (InputType) checkedObjects[i];
						type.setActived(!checked);
						if (type == inputType) {
							continue;
						}
						ParamType param1 = SmooksUIUtils.getInputTypeAssociatedParamType(type,
								getSmooksConfigResourceList());
						if (param1 != null) {
							String value1 = SmooksModelUtils.INPUT_DEACTIVE_TYPE;
							Command c1 = SetCommand.create(getEditingDomain(), param1,
									SmooksPackage.Literals.PARAM_TYPE__TYPE, value1);
							compoundCommand.append(c1);
						}

						lockCheck = true;
						inputDataViewer.setChecked(type, false);
						lockCheck = false;
					}

				} else {
					ParamType param = SmooksUIUtils.getInputTypeAssociatedParamType(inputType,
							getSmooksConfigResourceList());
					if (param != null) {
						String value = SmooksModelUtils.INPUT_DEACTIVE_TYPE;
						Command c = SetCommand.create(getEditingDomain(), param,
								SmooksPackage.Literals.PARAM_TYPE__TYPE, value);
						compoundCommand.append(c);
					}
				}

				getEditingDomain().getCommandStack().execute(compoundCommand);
				// inputTypeChanged();
				refreshInputModelView();

			}
		});
		inputDataViewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				Object element = selection.getFirstElement();
				if (element instanceof InputType) {
					String type = ((InputType) element).getType();
					String filePath = ((InputType) element).getPath();
					if (type != null && filePath != null) {
						if (SmooksModelUtils.INPUT_TYPE_JAVA.equals(type)) {
							IFile file = ((IFileEditorInput) getEditorInput()).getFile();
							IJavaProject javaProject = JavaCore.create(file.getProject());
							if (javaProject != null) {
								try {
									if (filePath.endsWith("[]")) {
										filePath = filePath.substring(0, filePath.length() - 2);
									}
									IJavaElement result = javaProject.findType(filePath);
									if (result != null)
										JavaUI.openInEditor(result);
									else {
										MessageDialog.openError(getSite().getWorkbenchWindow().getShell(),
												"Can't find type", "Can't find type \"" + filePath + "\" in \""
														+ javaProject.getProject().getName() + "\" project.");
									}
								} catch (Exception e) {

								}
							}
						} else {
							try {
								filePath = SmooksUIUtils.parseFilePath(filePath);
								if (filePath != null) {
									IFileStore fileStore = EFS.getLocalFileSystem().getStore(new Path(filePath));
									IFileInfo fetchInfo = fileStore.fetchInfo();
									if (!fetchInfo.isDirectory() && fetchInfo.exists()) {
										IWorkbenchWindow window = getSite().getWorkbenchWindow();
										IWorkbenchPage page = window.getActivePage();
										try {
											IDE.openEditorOnFileStore(page, fileStore);
										} catch (PartInitException e) {
											MessageDialog.open(MessageDialog.ERROR, window.getShell(), "Open File",
													"Can't open the file : '" + filePath + "'", SWT.SHEET);
										}
									} else {
									}
								}
							} catch (Exception e) {
								MessageDialog.open(MessageDialog.ERROR, getSite().getWorkbenchWindow().getShell(),
										"Open File", "Can't open the file : '" + filePath + "'", SWT.SHEET);
							}
						}
					}
				}
			}
		});
		TableColumn header = new TableColumn(inputDataViewer.getTable(), SWT.NONE);
		header.setText("Type");
		header.setWidth(100);
		TableColumn pathColumn = new TableColumn(inputDataViewer.getTable(), SWT.NONE);
		pathColumn.setText("Path");
		pathColumn.setWidth(300);

		// TableColumn extColumn = new TableColumn(inputDataViewer.getTable(),
		// SWT.NONE);
		// extColumn.setText("Extension Paramers");
		// extColumn.setWidth(400);
		inputDataViewer.setContentProvider(new ExtentionInputContentProvider());
		inputDataViewer.setLabelProvider(new InputDataViewerLabelProvider());
		inputDataViewer.getTable().setHeaderVisible(true);
		inputDataViewer.getTable().setLinesVisible(true);
		ISmooksModelProvider provider = getSmooksModelProvider();
		if (provider != null) {
			inputDataViewer.setInput(SmooksUIUtils.getInputTypeList(getSmooksConfigResourceList()));
		}
		Composite buttonComposite = toolkit.createComposite(mainComposite, SWT.NONE);
		gd = new GridData(GridData.FILL_VERTICAL);
		buttonComposite.setLayoutData(gd);
		GridLayout l = new GridLayout();
		buttonComposite.setLayout(l);

		addInputDataButton = toolkit.createButton(buttonComposite, "Add", SWT.FLAT);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		addInputDataButton.setLayoutData(gd);
		addInputDataButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				showInputDataWizard();
			}

		});

		removeInputDataButton = toolkit.createButton(buttonComposite, "Delete", SWT.FLAT);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		removeInputDataButton.setLayoutData(gd);
		removeInputDataButton.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) inputDataViewer.getSelection();
				if (selection != null) {
					List<?> inputs = selection.toList();
					ISmooksModelProvider smooksModelProvider = getSmooksModelProvider();
					for (Iterator<?> iterator = inputs.iterator(); iterator.hasNext();) {
						InputType input = (InputType) iterator.next();
						SmooksUIUtils.removeInputType(input, smooksModelProvider);
					}
					if (!inputs.isEmpty()) {
						List<?> viewerInput = (List<?>) inputDataViewer.getInput();
						viewerInput.removeAll(inputs);
						inputTypeChanged();
					}
				}
			}
		});
	}

	// protected SmooksGraphicsExtType getSmooksGraphicsExtType() {
	// SmooksGraphicsExtType extType = ((SmooksMultiFormEditor)
	// getEditor()).getSmooksGraphicsExt();
	// return extType;
	// }

	protected ISmooksModelProvider getSmooksModelProvider() {
		return (ISmooksModelProvider) getEditor();
	}

	protected AdapterFactoryEditingDomain getEditingDomain() {
		AdapterFactoryEditingDomain editDomain = (AdapterFactoryEditingDomain) ((SmooksMultiFormEditor) this
				.getEditor()).getEditingDomain();
		return editDomain;
	}

	protected SmooksResourceListType getSmooksConfigResourceList() {
		EObject doc = ((SmooksMultiFormEditor) this.getEditor()).getSmooksModel();
		if (doc instanceof DocumentRoot) {
			return ((DocumentRoot) doc).getSmooksResourceList();
		}
		return null;
	}

	protected void showInputDataWizard() {

		// SmooksGraphicsExtType extType = getSmooksGraphicsExtType();
		String inputType = getSmooksModelProvider().getInputType();
		List<InputType> inputTypes = null;
		if (inputType == null || SmooksModelUtils.INPUT_TYPE_CUSTOME.equals(inputType) || inputType.trim().equals("")) {
			StructuredDataSelectionWizard wizard = new StructuredDataSelectionWizard();
			wizard.setInput(getEditorInput());
			wizard.setSite(getEditorSite());
			wizard.setForcePreviousAndNextButtons(true);
			StructuredDataSelectionWizardDailog dialog = new StructuredDataSelectionWizardDailog(getEditorSite()
					.getShell(), wizard);

			if (dialog.show() == Dialog.OK) {
				String type = dialog.getType();
				String path = dialog.getPath();
				Properties pros = dialog.getProperties();
				inputTypes = SmooksUIUtils.recordInputDataInfomation(getEditingDomain(), getSmooksConfigResourceList()
						.getParams(), type, path, pros);
			}
		} else {
			IStructuredDataSelectionWizard wizard = ViewerInitorStore.getInstance().getStructuredDataCreationWizard(
					inputType);
			WizardDialog dialog = new WizardDialog(getEditorSite().getShell(), wizard);
			wizard.init(getEditorSite(), getEditorInput());
			if (dialog.open() == Dialog.OK) {
				String path = wizard.getStructuredDataSourcePath();
				Properties pros = wizard.getProperties();
				inputTypes = SmooksUIUtils.recordInputDataInfomation(getEditingDomain(), getSmooksConfigResourceList()
						.getParams(), inputType, path, pros);
			}
		}

		if (inputTypes != null && !inputTypes.isEmpty()) {
			InputType addedInputType = inputTypes.get(0);
			Object obj = this.inputDataViewer.getInput();
			if (obj != null && obj instanceof List) {
				((List) obj).add(addedInputType);
			}

			deactiveAllInputFile(null);
			if (inputType.equals(SmooksModelUtils.INPUT_TYPE_CUSTOME)) {
				// don't active the input file
			} else {
				setInputDataActiveStatus(true, addedInputType, null);
			}
			inputTypeChanged();
		}
	}

	private void refreshInputModelView() {
		if (inputModelViewer != null) {
			List<Object> input = generateInputData();
			inputModelViewer.setInput(input);
			SmooksUIUtils.expandSelectorViewer(input, inputModelViewer);
		}
	}

	public void inputTypeChanged() {
		if (inputDataViewer != null)
			inputDataViewer.refresh();
		refreshInputModelView();
	}

	protected void disposeCompositeControls(Composite composite, Control[] ignoreControl) {
		if (composite != null) {
			Control[] children = composite.getChildren();
			for (int i = 0; i < children.length; i++) {
				Control child = children[i];
				if (ignoreControl != null) {
					for (int j = 0; j < ignoreControl.length; j++) {
						if (child == ignoreControl[j]) {
							continue;
						}
					}
				}
				child.dispose();
				child = null;
			}
		}
	}

	private Object getCurrentReaderModel() {
		if (readerCombo == null || readerCombo.isDisposed())
			return null;
		int index = readerCombo.getSelectionIndex();
		if (index < 0)
			return null;
		return readerTypeList.get(index);
	}

	public void sourceChange(Object model) {
		bindingReaderCombo();
		initReaderCombo();
		initReaderConfigSection();
		if (inputDataViewer != null) {
			inputDataViewer.setInput(SmooksUIUtils.getInputTypeList(getSmooksConfigResourceList()));
			inputDataViewer.refresh();
		}
		refreshInputModelView();
	}

	// public void graphChanged(SmooksGraphicsExtType extType) {
	// // TODO Auto-generated method stub
	//
	// }

	public void graphPropertyChange(EStructuralFeature featre, Object value) {
		// TODO Auto-generated method stub

	}

	public void validateEnd(List<Diagnostic> diagnosticResult) {
		Object model = getCurrentReaderModel();
		if (model == null)
			return;
		if (model instanceof EObject) {
			this.getModelPanelCreator().markPropertyUI(diagnosticResult, (EObject) model);
		}
	}

	public void validateStart() {

	}

	protected boolean isIncorrectInputType(InputType element) {
		if (element == null)
			return false;
		if (element instanceof InputType) {
			String type = ((InputType) element).getType();
			int index = readerCombo.getSelectionIndex();
			if (index == -1)
				return true;

			Object reader = readerTypeList.get(index);
			if (reader instanceof NullReader) {
				return true;
			}
			if (reader instanceof XMLReader || reader instanceof XSDReader || reader instanceof JavaReader) {

			}

			if (reader instanceof XMLReader) {
				if (!SmooksModelUtils.INPUT_TYPE_XML.equals(type)) {
					return true;
				}
			}
			if (reader instanceof XSDReader) {
				if (!SmooksModelUtils.INPUT_TYPE_XSD.equals(type)) {
					return true;
				}
			}
			if (reader instanceof JavaReader) {
				if (!SmooksModelUtils.INPUT_TYPE_JAVA.equals(type)) {
					return true;
				}
			}

			if (reader instanceof EObject) {
				Object obj = ((EObject) reader);
				obj = AdapterFactoryEditingDomain.unwrap(obj);
				if (obj instanceof EDIReader || obj instanceof EDI12Reader) {
					if (!SmooksModelUtils.INPUT_TYPE_EDI_1_1.equals(type)) {
						return true;
					}
				}
				if (obj instanceof CsvReader || obj instanceof CSV12Reader) {
					if (!SmooksModelUtils.INPUT_TYPE_CSV.equals(type)) {
						return true;
					}
				}
				if (obj instanceof JsonReader || obj instanceof Json12Reader) {
					if (!SmooksModelUtils.INPUT_TYPE_JSON_1_1.equals(type)) {
						return true;
					}
				}
				if (obj instanceof ReaderType) {
					if (!SmooksModelUtils.INPUT_TYPE_CUSTOME.equals(type)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private class NullReader {

	}

	private class XMLReader {

	}

	private class XSDReader {

	}

	private class JavaReader {

	}

	private class InputDataViewerLabelProvider extends ExtentionInputLabelProvider implements ITableColorProvider {

		public Color getBackground(Object element, int columnIndex) {
			if (isIncorrectInputType((InputType) element)) {
				// return ColorConstants.darkGray;
			}
			return null;
		}

		public Color getForeground(Object element, int columnIndex) {
			if (isIncorrectInputType((InputType) element)) {
				return ColorConstants.lightGray;
			}
			return null;
		}
	}
}
