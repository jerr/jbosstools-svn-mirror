/**
 * <copyright>
 * 
 * Copyright (c) 2010 SAP AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Reiner Hille-Doering (SAP AG) - initial API and implementation and/or initial documentation
 * 
 * </copyright>
 *
 */
package org.eclipse.bpmn2.provider;

import java.util.Collection;
import java.util.List;

import org.eclipse.bpmn2.ErrorEventDefinition;
import org.eclipse.bpmn2.EventDefinition;
import org.eclipse.bpmn2.IntermediateCatchEvent;
import org.eclipse.bpmn2.MessageEventDefinition;
import org.eclipse.bpmn2.TimerEventDefinition;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;

/**
 * This is the item provider adapter for a {@link org.eclipse.bpmn2.IntermediateCatchEvent} object.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class IntermediateCatchEventItemProvider extends CatchEventItemProvider implements
        IEditingDomainItemProvider, IStructuredItemContentProvider, ITreeItemContentProvider,
        IItemLabelProvider, IItemPropertySource {
    /**
     * This constructs an instance from a factory and a notifier.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public IntermediateCatchEventItemProvider(AdapterFactory adapterFactory) {
        super(adapterFactory);
    }

    /**
     * This returns the property descriptors for the adapted class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public List<IItemPropertyDescriptor> getPropertyDescriptors(Object object) {
        if (itemPropertyDescriptors == null) {
            super.getPropertyDescriptors(object);

        }
        return itemPropertyDescriptors;
    }

    /**
     * This returns IntermediateCatchEvent.png.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated not
     */
    @Override
    public Object getImage(Object object) {
        try {
            return overlayImage(object,
                    getResourceLocator().getImage(getImagePath((IntermediateCatchEvent) object)));
        } catch (java.util.MissingResourceException ignore) {
        }
        try {
            return overlayImage(object,
                    getResourceLocator().getImage("full/obj16/IntermediateCatchEvent.png"));
        } catch (java.util.MissingResourceException e) {
            return overlayImage(object,
                    getResourceLocator().getImage("full/obj16/IntermediateCatchEvent.gif"));
        }
    }

    private boolean isMessageEvent(IntermediateCatchEvent intermediateCatchEvent) {
        List<EventDefinition> eventDefinitions = intermediateCatchEvent.getEventDefinitions();
        List<EventDefinition> eventDefinitionRefs = intermediateCatchEvent.getEventDefinitionRefs();
        if (eventDefinitions.size() + eventDefinitionRefs.size() != 1)
            return false;
        if (!eventDefinitions.isEmpty())
            return eventDefinitions.get(0) instanceof MessageEventDefinition;
        if (!eventDefinitionRefs.isEmpty())
            return eventDefinitionRefs.get(0) instanceof MessageEventDefinition;
        return false;
    }

    private boolean isTimerEvent(IntermediateCatchEvent intermediateCatchEvent) {
        List<EventDefinition> eventDefinitions = intermediateCatchEvent.getEventDefinitions();
        List<EventDefinition> eventDefinitionRefs = intermediateCatchEvent.getEventDefinitionRefs();
        if (eventDefinitions.size() + eventDefinitionRefs.size() != 1)
            return false;
        if (!eventDefinitions.isEmpty())
            return eventDefinitions.get(0) instanceof TimerEventDefinition;
        if (!eventDefinitionRefs.isEmpty())
            return eventDefinitionRefs.get(0) instanceof TimerEventDefinition;
        return false;
    }

    private boolean isErrorEvent(IntermediateCatchEvent intermediateCatchEvent) {
        List<EventDefinition> eventDefinitions = intermediateCatchEvent.getEventDefinitions();
        List<EventDefinition> eventDefinitionRefs = intermediateCatchEvent.getEventDefinitionRefs();
        if (eventDefinitions.size() + eventDefinitionRefs.size() != 1)
            return false;
        if (!eventDefinitions.isEmpty())
            return eventDefinitions.get(0) instanceof ErrorEventDefinition;
        if (!eventDefinitionRefs.isEmpty())
            return eventDefinitionRefs.get(0) instanceof ErrorEventDefinition;
        return false;
    }

    private String getImagePath(IntermediateCatchEvent intermediateCatchEvent) {
        if (isMessageEvent(intermediateCatchEvent)) {
            return "added/obj16/events_16px_intermediate_message_catch.png";
        } else if (isTimerEvent(intermediateCatchEvent)) {
            return "added/obj16/events_16px_intermediate_timer_catch.png";
        } else if (isErrorEvent(intermediateCatchEvent)) {
            return "added/obj16/events_16px_intermediate_error_catch.png";
        } else {
            return "added/obj16/events_16px_intermediate_message_none.png";
        }
    }

    /**
     * This returns the label text for the adapted class.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String getText(Object object) {
        String label = ((IntermediateCatchEvent) object).getName();
        return label == null || label.length() == 0 ? getString("_UI_IntermediateCatchEvent_type")
                : getString("_UI_IntermediateCatchEvent_type") + " " + label;
    }

    /**
     * This handles model notifications by calling {@link #updateChildren} to update any cached
     * children and by creating a viewer notification, which it passes to {@link #fireNotifyChanged}.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void notifyChanged(Notification notification) {
        updateChildren(notification);
        super.notifyChanged(notification);
    }

    /**
     * This adds {@link org.eclipse.emf.edit.command.CommandParameter}s describing the children
     * that can be created under this object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected void collectNewChildDescriptors(Collection<Object> newChildDescriptors, Object object) {
        super.collectNewChildDescriptors(newChildDescriptors, object);
    }

}
