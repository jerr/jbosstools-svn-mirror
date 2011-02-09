package org.jboss.tools.bpmn2.process.diagram.providers;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.bpmn2.Bpmn2Package;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.gmf.runtime.emf.type.core.ElementTypeRegistry;
import org.eclipse.gmf.runtime.emf.type.core.IElementType;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.AssociationEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.BusinessRuleTask2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.BusinessRuleTaskEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.DataObject2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.DataObjectEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.EndEvent2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.EndEvent3EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.EndEvent4EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.EndEvent5EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.EndEvent6EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.EndEventEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ExclusiveGateway2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ExclusiveGatewayEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.IntermediateCatchEvent2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.IntermediateCatchEvent3EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.IntermediateCatchEvent4EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.IntermediateCatchEvent5EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.IntermediateCatchEvent6EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.IntermediateCatchEventEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.IntermediateThrowEvent2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.IntermediateThrowEventEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ParallelGateway2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ParallelGatewayEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ProcessEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ScriptTask2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ScriptTaskEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.SequenceFlowEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ServiceTask2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.ServiceTaskEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.StartEvent2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.StartEvent3EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.StartEvent4EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.StartEventEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.SubProcess2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.SubProcessEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.TextAnnotation2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.TextAnnotationEditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.UserTask2EditPart;
import org.jboss.tools.bpmn2.process.diagram.edit.parts.UserTaskEditPart;
import org.jboss.tools.bpmn2.process.diagram.part.Bpmn2DiagramEditorPlugin;
import org.jboss.tools.bpmn2.process.diagram.part.Bpmn2ProcessDiagramEditorPlugin;

/**
 * @generated
 */
public class Bpmn2ElementTypes {

	/**
	 * @generated
	 */
	private Bpmn2ElementTypes() {
	}

	/**
	 * @generated
	 */
	private static Map<IElementType, ENamedElement> elements;

	/**
	 * @generated
	 */
	private static ImageRegistry imageRegistry;

	/**
	 * @generated
	 */
	private static Set<IElementType> KNOWN_ELEMENT_TYPES;

	/**
	 * @generated
	 */
	public static final IElementType Process_1000 = getElementType("org.jboss.tools.bpmn2.process.diagram.Process_1000"); //$NON-NLS-1$
	/**
	 * @generated
	 */
	public static final IElementType UserTask_2001 = getElementType("org.jboss.tools.bpmn2.process.diagram.UserTask_2001"); //$NON-NLS-1$
	/**
	 * @generated
	 */
	public static final IElementType BusinessRuleTask_2018 = getElementType("org.jboss.tools.bpmn2.process.diagram.BusinessRuleTask_2018"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType ServiceTask_2002 = getElementType("org.jboss.tools.bpmn2.process.diagram.ServiceTask_2002"); //$NON-NLS-1$
	/**
	 * @generated
	 */
	public static final IElementType StartEvent_2003 = getElementType("org.jboss.tools.bpmn2.process.diagram.StartEvent_2003"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType StartEvent_2007 = getElementType("org.jboss.tools.bpmn2.process.diagram.StartEvent_2007"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType EndEvent_2004 = getElementType("org.jboss.tools.bpmn2.process.diagram.EndEvent_2004"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType ExclusiveGateway_2005 = getElementType("org.jboss.tools.bpmn2.process.diagram.ExclusiveGateway_2005"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType ParallelGateway_2006 = getElementType("org.jboss.tools.bpmn2.process.diagram.ParallelGateway_2006"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType EndEvent_2008 = getElementType("org.jboss.tools.bpmn2.process.diagram.EndEvent_2008"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType EndEvent_2009 = getElementType("org.jboss.tools.bpmn2.process.diagram.EndEvent_2009"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType IntermediateCatchEvent_2010 = getElementType("org.jboss.tools.bpmn2.process.diagram.IntermediateCatchEvent_2010"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType IntermediateThrowEvent_2011 = getElementType("org.jboss.tools.bpmn2.process.diagram.IntermediateThrowEvent_2011"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType IntermediateCatchEvent_2012 = getElementType("org.jboss.tools.bpmn2.process.diagram.IntermediateCatchEvent_2012"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType IntermediateCatchEvent_2013 = getElementType("org.jboss.tools.bpmn2.process.diagram.IntermediateCatchEvent_2013"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType DataObject_2014 = getElementType("org.jboss.tools.bpmn2.process.diagram.DataObject_2014"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType TextAnnotation_2015 = getElementType("org.jboss.tools.bpmn2.process.diagram.TextAnnotation_2015"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType SubProcess_2016 = getElementType("org.jboss.tools.bpmn2.process.diagram.SubProcess_2016"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType ScriptTask_2017 = getElementType("org.jboss.tools.bpmn2.process.diagram.ScriptTask_2017"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType SubProcess_3001 = getElementType("org.jboss.tools.bpmn2.process.diagram.SubProcess_3001"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType UserTask_3002 = getElementType("org.jboss.tools.bpmn2.process.diagram.UserTask_3002"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType ServiceTask_3004 = getElementType("org.jboss.tools.bpmn2.process.diagram.ServiceTask_3004"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType BusinessRuleTask_3017 = getElementType("org.jboss.tools.bpmn2.process.diagram.BusinessRuleTask_3017"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType StartEvent_3003 = getElementType("org.jboss.tools.bpmn2.process.diagram.StartEvent_3003"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType StartEvent_3005 = getElementType("org.jboss.tools.bpmn2.process.diagram.StartEvent_3005"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType EndEvent_3006 = getElementType("org.jboss.tools.bpmn2.process.diagram.EndEvent_3006"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType ExclusiveGateway_3007 = getElementType("org.jboss.tools.bpmn2.process.diagram.ExclusiveGateway_3007"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType ParallelGateway_3008 = getElementType("org.jboss.tools.bpmn2.process.diagram.ParallelGateway_3008"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType EndEvent_3009 = getElementType("org.jboss.tools.bpmn2.process.diagram.EndEvent_3009"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType EndEvent_3010 = getElementType("org.jboss.tools.bpmn2.process.diagram.EndEvent_3010"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType IntermediateCatchEvent_3011 = getElementType("org.jboss.tools.bpmn2.process.diagram.IntermediateCatchEvent_3011"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType IntermediateThrowEvent_3012 = getElementType("org.jboss.tools.bpmn2.process.diagram.IntermediateThrowEvent_3012"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType IntermediateCatchEvent_3013 = getElementType("org.jboss.tools.bpmn2.process.diagram.IntermediateCatchEvent_3013"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType IntermediateCatchEvent_3018 = getElementType("org.jboss.tools.bpmn2.process.diagram.IntermediateCatchEvent_3018"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType DataObject_3014 = getElementType("org.jboss.tools.bpmn2.process.diagram.DataObject_3014"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType TextAnnotation_3015 = getElementType("org.jboss.tools.bpmn2.process.diagram.TextAnnotation_3015"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType ScriptTask_3016 = getElementType("org.jboss.tools.bpmn2.process.diagram.ScriptTask_3016"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType SequenceFlow_4001 = getElementType("org.jboss.tools.bpmn2.process.diagram.SequenceFlow_4001"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	public static final IElementType Association_4002 = getElementType("org.jboss.tools.bpmn2.process.diagram.Association_4002"); //$NON-NLS-1$

	/**
	 * @generated
	 */
	private static ImageRegistry getImageRegistry() {
		if (imageRegistry == null) {
			imageRegistry = new ImageRegistry();
		}
		return imageRegistry;
	}

	/**
	 * @generated
	 */
	private static String getImageRegistryKey(ENamedElement element) {
		return element.getName();
	}

	/**
	 * @generated
	 */
	private static ImageDescriptor getProvidedImageDescriptor(
			ENamedElement element) {
		if (element instanceof EStructuralFeature) {
			EStructuralFeature feature = ((EStructuralFeature) element);
			EClass eContainingClass = feature.getEContainingClass();
			EClassifier eType = feature.getEType();
			if (eContainingClass != null && !eContainingClass.isAbstract()) {
				element = eContainingClass;
			} else if (eType instanceof EClass
					&& !((EClass) eType).isAbstract()) {
				element = eType;
			}
		}
		if (element instanceof EClass) {
			EClass eClass = (EClass) element;
			if (!eClass.isAbstract()) {
				return Bpmn2ProcessDiagramEditorPlugin.getInstance()
						.getItemImageDescriptor(
								eClass.getEPackage().getEFactoryInstance()
										.create(eClass));
			}
		}
		// TODO : support structural features
		return null;
	}

	/**
	 * @generated
	 */
	public static ImageDescriptor getImageDescriptor(ENamedElement element) {
		String key = getImageRegistryKey(element);
		ImageDescriptor imageDescriptor = getImageRegistry().getDescriptor(key);
		if (imageDescriptor == null) {
			imageDescriptor = getProvidedImageDescriptor(element);
			if (imageDescriptor == null) {
				imageDescriptor = ImageDescriptor.getMissingImageDescriptor();
			}
			getImageRegistry().put(key, imageDescriptor);
		}
		return imageDescriptor;
	}

	/**
	 * @generated
	 */
	public static Image getImage(ENamedElement element) {
		String key = getImageRegistryKey(element);
		Image image = getImageRegistry().get(key);
		if (image == null) {
			ImageDescriptor imageDescriptor = getProvidedImageDescriptor(element);
			if (imageDescriptor == null) {
				imageDescriptor = ImageDescriptor.getMissingImageDescriptor();
			}
			getImageRegistry().put(key, imageDescriptor);
			image = getImageRegistry().get(key);
		}
		return image;
	}

	/**
	 * @generated
	 */
	public static ImageDescriptor getImageDescriptor(IAdaptable hint) {
		ENamedElement element = getElement(hint);
		if (element == null) {
			return null;
		}
		return getImageDescriptor(element);
	}

	/**
	 * @generated
	 */
	public static Image getImage(IAdaptable hint) {
		ENamedElement element = getElement(hint);
		if (element == null) {
			return null;
		}
		return getImage(element);
	}

	/**
	 * Returns 'type' of the ecore object associated with the hint.
	 * 
	 * @generated
	 */
	public static ENamedElement getElement(IAdaptable hint) {
		Object type = hint.getAdapter(IElementType.class);
		if (elements == null) {
			elements = new IdentityHashMap<IElementType, ENamedElement>();

			elements.put(Process_1000, Bpmn2Package.eINSTANCE.getProcess());

			elements.put(UserTask_2001, Bpmn2Package.eINSTANCE.getUserTask());

			elements.put(ScriptTask_2017,
					Bpmn2Package.eINSTANCE.getScriptTask());

			elements.put(ServiceTask_2002,
					Bpmn2Package.eINSTANCE.getServiceTask());

			elements.put(BusinessRuleTask_2018,
					Bpmn2Package.eINSTANCE.getBusinessRuleTask());

			elements.put(StartEvent_2003,
					Bpmn2Package.eINSTANCE.getStartEvent());

			elements.put(StartEvent_2007,
					Bpmn2Package.eINSTANCE.getStartEvent());

			elements.put(IntermediateCatchEvent_2010,
					Bpmn2Package.eINSTANCE.getIntermediateCatchEvent());

			elements.put(IntermediateCatchEvent_2012,
					Bpmn2Package.eINSTANCE.getIntermediateCatchEvent());

			elements.put(IntermediateCatchEvent_2013,
					Bpmn2Package.eINSTANCE.getIntermediateCatchEvent());

			elements.put(IntermediateThrowEvent_2011,
					Bpmn2Package.eINSTANCE.getIntermediateThrowEvent());

			elements.put(EndEvent_2004, Bpmn2Package.eINSTANCE.getEndEvent());

			elements.put(EndEvent_2008, Bpmn2Package.eINSTANCE.getEndEvent());

			elements.put(EndEvent_2009, Bpmn2Package.eINSTANCE.getEndEvent());

			elements.put(ExclusiveGateway_2005,
					Bpmn2Package.eINSTANCE.getExclusiveGateway());

			elements.put(ParallelGateway_2006,
					Bpmn2Package.eINSTANCE.getParallelGateway());

			elements.put(SubProcess_2016,
					Bpmn2Package.eINSTANCE.getSubProcess());

			elements.put(DataObject_2014,
					Bpmn2Package.eINSTANCE.getDataObject());

			elements.put(TextAnnotation_2015,
					Bpmn2Package.eINSTANCE.getTextAnnotation());

			elements.put(UserTask_3002, Bpmn2Package.eINSTANCE.getUserTask());

			elements.put(ScriptTask_3016,
					Bpmn2Package.eINSTANCE.getScriptTask());

			elements.put(ServiceTask_3004,
					Bpmn2Package.eINSTANCE.getServiceTask());

			elements.put(BusinessRuleTask_3017,
					Bpmn2Package.eINSTANCE.getBusinessRuleTask());

			elements.put(StartEvent_3003,
					Bpmn2Package.eINSTANCE.getStartEvent());

			elements.put(StartEvent_3005,
					Bpmn2Package.eINSTANCE.getStartEvent());

			elements.put(IntermediateCatchEvent_3011,
					Bpmn2Package.eINSTANCE.getIntermediateCatchEvent());

			elements.put(IntermediateCatchEvent_3013,
					Bpmn2Package.eINSTANCE.getIntermediateCatchEvent());

			elements.put(IntermediateCatchEvent_3018,
					Bpmn2Package.eINSTANCE.getIntermediateCatchEvent());

			elements.put(IntermediateThrowEvent_3012,
					Bpmn2Package.eINSTANCE.getIntermediateThrowEvent());

			elements.put(EndEvent_3006, Bpmn2Package.eINSTANCE.getEndEvent());

			elements.put(EndEvent_3009, Bpmn2Package.eINSTANCE.getEndEvent());

			elements.put(EndEvent_3010, Bpmn2Package.eINSTANCE.getEndEvent());

			elements.put(ExclusiveGateway_3007,
					Bpmn2Package.eINSTANCE.getExclusiveGateway());

			elements.put(ParallelGateway_3008,
					Bpmn2Package.eINSTANCE.getParallelGateway());

			elements.put(SubProcess_3001,
					Bpmn2Package.eINSTANCE.getSubProcess());

			elements.put(DataObject_3014,
					Bpmn2Package.eINSTANCE.getDataObject());

			elements.put(TextAnnotation_3015,
					Bpmn2Package.eINSTANCE.getTextAnnotation());

			elements.put(SequenceFlow_4001,
					Bpmn2Package.eINSTANCE.getSequenceFlow());

			elements.put(Association_4002,
					Bpmn2Package.eINSTANCE.getAssociation());
		}
		return (ENamedElement) elements.get(type);
	}

	/**
	 * @generated
	 */
	private static IElementType getElementType(String id) {
		return ElementTypeRegistry.getInstance().getType(id);
	}

	/**
	 * @generated
	 */
	public static boolean isKnownElementType(IElementType elementType) {
		if (KNOWN_ELEMENT_TYPES == null) {
			KNOWN_ELEMENT_TYPES = new HashSet<IElementType>();
			KNOWN_ELEMENT_TYPES.add(Process_1000);
			KNOWN_ELEMENT_TYPES.add(UserTask_2001);
			KNOWN_ELEMENT_TYPES.add(ScriptTask_2017);
			KNOWN_ELEMENT_TYPES.add(ServiceTask_2002);
			KNOWN_ELEMENT_TYPES.add(BusinessRuleTask_2018);
			KNOWN_ELEMENT_TYPES.add(StartEvent_2003);
			KNOWN_ELEMENT_TYPES.add(StartEvent_2007);
			KNOWN_ELEMENT_TYPES.add(IntermediateCatchEvent_2010);
			KNOWN_ELEMENT_TYPES.add(IntermediateCatchEvent_2012);
			KNOWN_ELEMENT_TYPES.add(IntermediateCatchEvent_2013);
			KNOWN_ELEMENT_TYPES.add(IntermediateThrowEvent_2011);
			KNOWN_ELEMENT_TYPES.add(EndEvent_2004);
			KNOWN_ELEMENT_TYPES.add(EndEvent_2008);
			KNOWN_ELEMENT_TYPES.add(EndEvent_2009);
			KNOWN_ELEMENT_TYPES.add(ExclusiveGateway_2005);
			KNOWN_ELEMENT_TYPES.add(ParallelGateway_2006);
			KNOWN_ELEMENT_TYPES.add(SubProcess_2016);
			KNOWN_ELEMENT_TYPES.add(DataObject_2014);
			KNOWN_ELEMENT_TYPES.add(TextAnnotation_2015);
			KNOWN_ELEMENT_TYPES.add(UserTask_3002);
			KNOWN_ELEMENT_TYPES.add(ScriptTask_3016);
			KNOWN_ELEMENT_TYPES.add(ServiceTask_3004);
			KNOWN_ELEMENT_TYPES.add(BusinessRuleTask_3017);
			KNOWN_ELEMENT_TYPES.add(StartEvent_3003);
			KNOWN_ELEMENT_TYPES.add(StartEvent_3005);
			KNOWN_ELEMENT_TYPES.add(IntermediateCatchEvent_3011);
			KNOWN_ELEMENT_TYPES.add(IntermediateCatchEvent_3013);
			KNOWN_ELEMENT_TYPES.add(IntermediateCatchEvent_3018);
			KNOWN_ELEMENT_TYPES.add(IntermediateThrowEvent_3012);
			KNOWN_ELEMENT_TYPES.add(EndEvent_3006);
			KNOWN_ELEMENT_TYPES.add(EndEvent_3009);
			KNOWN_ELEMENT_TYPES.add(EndEvent_3010);
			KNOWN_ELEMENT_TYPES.add(ExclusiveGateway_3007);
			KNOWN_ELEMENT_TYPES.add(ParallelGateway_3008);
			KNOWN_ELEMENT_TYPES.add(SubProcess_3001);
			KNOWN_ELEMENT_TYPES.add(DataObject_3014);
			KNOWN_ELEMENT_TYPES.add(TextAnnotation_3015);
			KNOWN_ELEMENT_TYPES.add(SequenceFlow_4001);
			KNOWN_ELEMENT_TYPES.add(Association_4002);
		}
		return KNOWN_ELEMENT_TYPES.contains(elementType);
	}

	/**
	 * @generated
	 */
	public static IElementType getElementType(int visualID) {
		switch (visualID) {
		case ProcessEditPart.VISUAL_ID:
			return Process_1000;
		case UserTaskEditPart.VISUAL_ID:
			return UserTask_2001;
		case ScriptTaskEditPart.VISUAL_ID:
			return ScriptTask_2017;
		case ServiceTaskEditPart.VISUAL_ID:
			return ServiceTask_2002;
		case BusinessRuleTaskEditPart.VISUAL_ID:
			return BusinessRuleTask_2018;
		case StartEventEditPart.VISUAL_ID:
			return StartEvent_2003;
		case StartEvent2EditPart.VISUAL_ID:
			return StartEvent_2007;
		case IntermediateCatchEventEditPart.VISUAL_ID:
			return IntermediateCatchEvent_2010;
		case IntermediateCatchEvent2EditPart.VISUAL_ID:
			return IntermediateCatchEvent_2012;
		case IntermediateCatchEvent3EditPart.VISUAL_ID:
			return IntermediateCatchEvent_2013;
		case IntermediateThrowEventEditPart.VISUAL_ID:
			return IntermediateThrowEvent_2011;
		case EndEventEditPart.VISUAL_ID:
			return EndEvent_2004;
		case EndEvent2EditPart.VISUAL_ID:
			return EndEvent_2008;
		case EndEvent3EditPart.VISUAL_ID:
			return EndEvent_2009;
		case ExclusiveGatewayEditPart.VISUAL_ID:
			return ExclusiveGateway_2005;
		case ParallelGatewayEditPart.VISUAL_ID:
			return ParallelGateway_2006;
		case SubProcessEditPart.VISUAL_ID:
			return SubProcess_2016;
		case DataObjectEditPart.VISUAL_ID:
			return DataObject_2014;
		case TextAnnotationEditPart.VISUAL_ID:
			return TextAnnotation_2015;
		case UserTask2EditPart.VISUAL_ID:
			return UserTask_3002;
		case ScriptTask2EditPart.VISUAL_ID:
			return ScriptTask_3016;
		case ServiceTask2EditPart.VISUAL_ID:
			return ServiceTask_3004;
		case BusinessRuleTask2EditPart.VISUAL_ID:
			return BusinessRuleTask_3017;
		case StartEvent3EditPart.VISUAL_ID:
			return StartEvent_3003;
		case StartEvent4EditPart.VISUAL_ID:
			return StartEvent_3005;
		case IntermediateCatchEvent4EditPart.VISUAL_ID:
			return IntermediateCatchEvent_3011;
		case IntermediateCatchEvent5EditPart.VISUAL_ID:
			return IntermediateCatchEvent_3013;
		case IntermediateCatchEvent6EditPart.VISUAL_ID:
			return IntermediateCatchEvent_3018;
		case IntermediateThrowEvent2EditPart.VISUAL_ID:
			return IntermediateThrowEvent_3012;
		case EndEvent4EditPart.VISUAL_ID:
			return EndEvent_3006;
		case EndEvent5EditPart.VISUAL_ID:
			return EndEvent_3009;
		case EndEvent6EditPart.VISUAL_ID:
			return EndEvent_3010;
		case ExclusiveGateway2EditPart.VISUAL_ID:
			return ExclusiveGateway_3007;
		case ParallelGateway2EditPart.VISUAL_ID:
			return ParallelGateway_3008;
		case SubProcess2EditPart.VISUAL_ID:
			return SubProcess_3001;
		case DataObject2EditPart.VISUAL_ID:
			return DataObject_3014;
		case TextAnnotation2EditPart.VISUAL_ID:
			return TextAnnotation_3015;
		case SequenceFlowEditPart.VISUAL_ID:
			return SequenceFlow_4001;
		case AssociationEditPart.VISUAL_ID:
			return Association_4002;
		}
		return null;
	}

}
