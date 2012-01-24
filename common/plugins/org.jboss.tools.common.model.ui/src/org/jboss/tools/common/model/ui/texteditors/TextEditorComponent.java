/*******************************************************************************
 * Copyright (c) 2007 Exadel, Inc. and Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Exadel, Inc. and Red Hat, Inc. - initial API and implementation
 ******************************************************************************/ 
package org.jboss.tools.common.model.ui.texteditors;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.internal.ui.preferences.OverlayPreferenceStore;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.ITextListener;
import org.eclipse.jface.text.TextEvent;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ST;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.IStatusField;
import org.eclipse.ui.texteditor.ITextEditorActionConstants;
import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;
import org.eclipse.ui.texteditor.ITextEditorExtension;
import org.eclipse.ui.texteditor.TextNavigationAction;
import org.jboss.tools.common.model.XModelException;
import org.jboss.tools.common.model.XModelObject;
import org.jboss.tools.common.model.filesystems.impl.FileAnyImpl;
import org.jboss.tools.common.model.ui.ModelUIPlugin;
import org.jboss.tools.common.model.util.XModelObjectCache;

public class TextEditorComponent implements ITextListener, ITextEditorExtension {
	protected XModelObjectCache object = null;
	protected IDocument document = null;
	protected IDocumentPartitioner partitioner = null;
	protected SourceViewer preview = null;
	protected boolean lock = false;
	protected IEditorPart editorPart = null;
	
	public TextEditorComponent() {
		init();
	}
	
	protected void init() {}
	
	public void setEditorPart(IEditorPart editorPart) {
		this.editorPart = editorPart;
	}

	public Control createControl(Composite container) {
		return createPreviewer(container);
	}
	
	public Control getControl() {
		return preview.getControl();
	}
	
	public void setObject (XModelObject object) {
		this.object = new XModelObjectCache(object);
		update();
	}
	
	public XModelObject getModelObject() {
		return (object == null) ? null : object.getObject();
	}
	
	private boolean modified = false;
	
	public void setModified(boolean set) {
		modified = set;
	}
	
	public boolean isModified() {
		return modified;
	}
	
	protected String loadContent() {
		XModelObject o = getModelObject();
		String content = (o == null? "": o.get("body")); //$NON-NLS-1$ //$NON-NLS-2$
		content = (content == null ? "" : content); //$NON-NLS-1$
		return content;
	}
	
	public void update() {
		if(lock) return;
		lock = true;
		String content = loadContent();
		if(document != null && content.equals(getText())) {
			lock = false;
			return;
		} 

		if (partitioner != null) partitioner.disconnect();
		
		document = new Document(content);
		IDocumentPartitioner partitioner = createDocumentPartitioner(); 

		partitioner.connect(document);
		document.setDocumentPartitioner(partitioner);

		preview.setDocument(document);
		setModified(false);
		lock = false;
	}
	
	protected IDocumentPartitioner createDocumentPartitioner() {
		return null; 
	}
	
	protected SourceViewerConfiguration createSourceViewerConfiguration() {
		return null;
	}
	
	private Control createPreviewer(Composite parent) {
		preview = new SourceViewer(parent, null, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		preview.configure(createSourceViewerConfiguration());
		preview.getTextWidget().setFont(JFaceResources.getFontRegistry().get(JFaceResources.TEXT_FONT));
//		preview.setEditable(false);
		initializeViewerColors(preview);
		update();
		preview.addTextListener((ITextListener) this);
		preview.getControl().addMouseListener(new M());
		preview.getControl().addKeyListener(new K());
		createNavigationActions();
		return preview.getControl();
	}
	
	/**
	 * Initializes the given viewer's colors.
	 * 
	 * @param viewer the viewer to be initialized
	 */
	private OverlayPreferenceStore overlay = null;
	
	private void initializeViewerColors(ISourceViewer viewer) {
		if (overlay != null) {
			StyledText styledText = viewer.getTextWidget();
			Color color = null;
			if ( !overlay.getBoolean(AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND_SYSTEM_DEFAULT) ) {
				color = createColor(
					overlay,
					AbstractTextEditor.PREFERENCE_COLOR_BACKGROUND,
					styledText.getDisplay());
			}
			styledText.setBackground(color);
		}
	}

	/**
	 * Creates a color from the information stored in the given preference store.
	 * Returns <code>null</code> if there is no such information available.
	 */
	private Color createColor(IPreferenceStore store, String key, Display display) {
		RGB rgb = null;
		if (store.contains(key)) {
			if (store.isDefault(key)) {
				rgb = PreferenceConverter.getDefaultColor(store, key);
			} else {
				rgb = PreferenceConverter.getColor(store, key);
			}
		}
		return (rgb != null) ? new Color(display, rgb) : null;
	}

	public String getText() {
		String text = null;
		if (document != null) text = document.get();
		return (text == null) ? "" : text; //$NON-NLS-1$
	}

	public void textChanged(TextEvent event) {
		this.modified = true;
	}
	
	public void save() {
		if(lock || !modified) return;		
		lock = true;
		try {
			FileAnyImpl f = (FileAnyImpl)getModelObject();
			if(f != null) f.edit(getText()); 
		} catch (XModelException e) {
			ModelUIPlugin.getPluginLog().logError(e);
		} finally {
			lock = false;
			modified = false;
		}
	}
	
	public void setCursor(int line, int position) {
		try {
			int i = document.getLineOffset(line - 1) + position -1;
			preview.setSelectedRange(i, 0);
		} catch (BadLocationException e) {
			//ignore
		}
	}
	
	//AbstractTextEditor
	
	protected void createNavigationActions() {
		IAction action = new ToggleInsertModeAction(preview.getTextWidget());
		action.setActionDefinitionId(ITextEditorActionDefinitionIds.TOGGLE_OVERWRITE);
		setAction(ITextEditorActionDefinitionIds.TOGGLE_OVERWRITE, action);
	}

	public void setAction(String actionID, IAction action) {
		if(editorPart == null) return;
		editorPart.getSite().getKeyBindingService().registerAction(action);
	}
	
	private Map<String,IStatusField> fStatusFields = new HashMap<String,IStatusField>(); 
	
	protected IStatusField getStatusField(String category) {
		if (category != null && fStatusFields != null)
			return fStatusFields.get(category);
		return null;
	}
	
	public void setStatusField(IStatusField field, String category) {
		if(field == null) {
			fStatusFields.remove(category);
		} else {
			fStatusFields.put(category, field);
			updateStatusField(category);
		}
	}

	public boolean isEditorInputReadOnly() {
		XModelObject o = getModelObject();
		return o != null && !o.isObjectEditable();
	}

	public void addRulerContextMenuListener(IMenuListener listener) {}

	public void removeRulerContextMenuListener(IMenuListener listener) {}

	protected void handleCursorPositionChanged() {
		updateStatusField(ITextEditorActionConstants.STATUS_CATEGORY_INPUT_POSITION);
		updateStatusField(ITextEditorActionConstants.STATUS_CATEGORY_ELEMENT_STATE);
	}
	
	protected void updateStatusField(String category) {
		if (category == null) return;			
		IStatusField field = getStatusField(category);
		if (field != null) {
			String text = null;			
			if (ITextEditorActionConstants.STATUS_CATEGORY_INPUT_POSITION.equals(category))
				text = getCursorPosition();
			else if (ITextEditorActionConstants.STATUS_CATEGORY_ELEMENT_STATE.equals(category))
				text = isEditorInputReadOnly() ? "Read Only" : "Writable";
			else if (ITextEditorActionConstants.STATUS_CATEGORY_INPUT_MODE.equals(category))
				text = (!fOverwriting) ? "Insert" : "Overwrite";			
			field.setText(text == null ? "" : text); //$NON-NLS-1$
		}
	}
	
	protected String getCursorPosition() {		
		if (preview == null) return "";		 //$NON-NLS-1$
		StyledText styledText = preview.getTextWidget();
		int caret = styledText.getCaretOffset();
		IDocument document= preview.getDocument();
		if (document == null) return "";	 //$NON-NLS-1$
		try {			
			int line = document.getLineOfOffset(caret);
			int lineOffset= document.getLineOffset(line);
			int tabWidth= styledText.getTabs();
			int column= 0;
			for (int i= lineOffset; i < caret; i++)
				if ('\t' == document.getChar(i))
					column += tabWidth - (column % tabWidth);
				else
					column++;
			return "" + (++line) + " : " + (++column);  //$NON-NLS-1$ //$NON-NLS-2$
			
		} catch (BadLocationException x) {
			return ""; //$NON-NLS-1$
		}
	}
	
	class K extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			handleCursorPositionChanged();
		}
	}
	class M extends MouseAdapter {
		public void mouseUp(MouseEvent e) {
			handleCursorPositionChanged();
		}
	}
	
	private boolean fOverwriting = false;
	class ToggleInsertModeAction extends TextNavigationAction {		
		public ToggleInsertModeAction(StyledText textWidget) {
			super(textWidget, ST.TOGGLE_OVERWRITE);
		}
		public void run() {
			super.run();
			fOverwriting = !fOverwriting;
			handleInsertModeChanged();
		}
	}
	
	protected void handleInsertModeChanged() {
		updateStatusField(ITextEditorActionConstants.STATUS_CATEGORY_INPUT_MODE);
	}
	
}