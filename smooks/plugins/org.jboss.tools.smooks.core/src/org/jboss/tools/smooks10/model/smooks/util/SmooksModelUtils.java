/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.smooks10.model.smooks.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.command.Command;
import org.eclipse.emf.common.command.CompoundCommand;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.xml.type.AnyType;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;
import org.eclipse.emf.edit.command.AddCommand;
import org.eclipse.emf.edit.command.CommandParameter;
import org.eclipse.emf.edit.command.RemoveCommand;
import org.eclipse.emf.edit.domain.EditingDomain;
import org.jboss.tools.smooks.model.freemarker.Freemarker;
import org.jboss.tools.smooks.model.freemarker.Template;
import org.jboss.tools.smooks.model.graphics.ext.GraphFactory;
import org.jboss.tools.smooks.model.graphics.ext.InputType;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.smooks.AbstractResourceConfig;
import org.jboss.tools.smooks.model.smooks.ConditionType;
import org.jboss.tools.smooks.model.smooks.ConditionsType;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;
import org.jboss.tools.smooks10.model.smooks.ParamType;
import org.jboss.tools.smooks10.model.smooks.ResourceConfigType;
import org.jboss.tools.smooks10.model.smooks.ResourceType;
import org.jboss.tools.smooks10.model.smooks.SmooksFactory;
import org.jboss.tools.smooks10.model.smooks.SmooksPackage;

/**
 * @author Dart Peng
 * 
 */

public class SmooksModelUtils {

	public static final String KEY_TEMPLATE_TYPE = "messageType";

	public static final String FREEMARKER_TEMPLATE_TYPE_CSV = "CSV";

	public static final String KEY_CSV_FIELDS = "csvFields";

	public static final String KEY_TASK_ID_REF = "idref";

	public static final String KEY_OBJECT_ID = "id";

	public static final String KEY_CSV_SEPERATOR = "seperator";

	public static final String KEY_CSV_QUOTE = "quote";

	public static final String INPUT_TYPE_JAVA = "java";

	public static final String INPUT_TYPE_CUSTOME = "custom";

	public static final String INPUT_TYPE_JSON_1_1 = "json";

	public static final String INPUT_TYPE_CSV = "csv";

	public static final String INPUT_TYPE_CSV_1_2 = "csv";

	public static final String PARAM_NAME_CLASS = "class";

	public static final String PARAM_NAME_PATH = "path";

	public static final String PARAM_NAME_ACTIVED = "actived";

	public static final String INPUT_TYPE_XML = "xml";

	public static final String INPUT_TYPE_XSD = "xsd";

	public static final String TYPE_XSL = "xsl";

	public static final String[] TEMPLATE_TYPES = new String[] { "xsl", "ftl" };

	public static final String BEAN_CLASS = "beanClass";

	public static final String BEAN_ID = "beanId";

	public static final String BINDINGS = "bindings";

	public static final String INPUT_TYPE_EDI_1_1 = "EDI";

	public static final String INPUT_TYPE_EDI_1_2 = "EDI";

	public static final String INPUT_TYPE_JSON_1_2 = "json";

	public static EStructuralFeature ATTRIBUTE_PROPERTY = ExtendedMetaData.INSTANCE.demandFeature(null, "property",
			false);

	public static EStructuralFeature ATTRIBUTE_SELECTOR = ExtendedMetaData.INSTANCE.demandFeature(null, "selector",
			false);

	public static EStructuralFeature ATTRIBUTE_TYPE = ExtendedMetaData.INSTANCE.demandFeature(null, "type", false);

	public static EStructuralFeature ELEMENT_BINDING = ExtendedMetaData.INSTANCE.demandFeature(
			"http://www.milyn.org/xsd/smooks-1.0.xsd", "binding", true);

	public static AnyType addBindingTypeToParamType(ParamType param, String property, String selector, String type,
			String uri) {
		AnyType binding = createBindingType(property, selector, type, uri);
		param.getMixed().add(ELEMENT_BINDING, binding);
		return binding;
	}

	public static List<Object> getBindingListFromResourceConfigType(ResourceConfigType resourceConfig) {
		List<ParamType> paramList = resourceConfig.getParam();
		for (Iterator<ParamType> iterator = paramList.iterator(); iterator.hasNext();) {
			ParamType param = iterator.next();
			if ("bindings".equals(param.getName())) {
				if (param.eContents().isEmpty())
					continue;
				List<Object> bindingList = (List<Object>) param.getMixed().list(SmooksModelUtils.ELEMENT_BINDING);
				return bindingList;
			}
		}
		return Collections.emptyList();
	}

	public static boolean isBeanPopulatorResource(ResourceConfigType type) {
		ResourceType resource = type.getResource();
		if (resource == null)
			return false;
		String value = resource.getStringValue();
		if (value != null)
			value = value.trim();
		if (SmooksModelConstants.BEAN_POPULATOR.equals(value)) {
			return true;
		}
		return false;
	}

	public static void setPropertyValueToAnyType(Object value, EStructuralFeature attribute, AnyType anyType) {
		anyType.getAnyAttribute().set(attribute, value);
	}

	public static AnyType getBindingViaProperty(ResourceConfigType resourceConfig, String property) {
		List bindingList = getBindingListFromResourceConfigType(resourceConfig);
		for (Iterator iterator = bindingList.iterator(); iterator.hasNext();) {
			AnyType binding = (AnyType) iterator.next();
			String pro = getAttributeValueFromAnyType(binding, ATTRIBUTE_PROPERTY);
			if (pro != null)
				pro = pro.trim();
			if (property.equals(pro)) {
				return binding;
			}
		}
		return null;
	}

	public static boolean isInnerFileContents(ResourceConfigType resourceConfig) {
		ResourceType resource = resourceConfig.getResource();
		if (resource == null)
			return false;
		String type = resource.getType();
		if (type != null)
			type = type.trim();
		for (int i = 0; i < TEMPLATE_TYPES.length; i++) {
			String type1 = TEMPLATE_TYPES[i];
			if (type1.equalsIgnoreCase(type))
				return true;
		}
		return false;
	}

	public static boolean isDateTypeSelector(ResourceConfigType type) {
		ResourceType resource = type.getResource();
		if (resource == null)
			return false;
		String value = resource.getStringValue();
		if (value != null)
			value = value.trim();
		for (int i = 0; i < SmooksModelConstants.DECODER_CLASSES.length; i++) {
			String decoderClass = SmooksModelConstants.DECODER_CLASSES[i];
			if (decoderClass.equals(value)) {
				return true;
			}
		}
		return false;
	}

	public static String getTransformType(ResourceConfigType resourceConfig) {
		ParamType typeParam = null;
		if (resourceConfig == null)
			return "";
		if (isTransformTypeResourceConfig(resourceConfig)) {
			List paramList = resourceConfig.getParam();
			for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
				ParamType param = (ParamType) iterator.next();
				String name = param.getName();
				if (name != null)
					name = name.trim();
				if (SmooksModelConstants.STREAM_FILTER_TYPE.equals(name)) {
					typeParam = param;
					break;
				}
			}
			if (typeParam != null) {
				return SmooksModelUtils.getAnyTypeText(typeParam);
			}
		}
		return "";
	}

	public static void setTransformType(ResourceConfigType resourceConfig, String type) {
		if (type == null)
			type = "";
		if (isTransformTypeResourceConfig(resourceConfig)) {
			List paramList = resourceConfig.getParam();
			for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
				ParamType param = (ParamType) iterator.next();
				if (SmooksModelConstants.STREAM_FILTER_TYPE.equals(param.getName())) {
					cleanTextToSmooksType(param);
					setTextToAnyType(param, type);
				}
			}
		}
	}

	public static boolean isFilePathResourceConfig(ResourceConfigType resourceConfig) {
		ResourceType resource = resourceConfig.getResource();
		if (resource != null) {
			String value = resource.getStringValue();
			if (value != null) {
				if (value.startsWith("\\")) {
					return true;
				}
				if (value.startsWith("/")) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isTransformTypeResourceConfig(ResourceConfigType resourceConfig) {
		String selector = resourceConfig.getSelector();
		if (selector != null)
			selector = selector.trim();
		if (!SmooksModelConstants.GLOBAL_PARAMETERS.equals(selector)) {
			return false;
		}

		if (resourceConfig.getParam().isEmpty()) {
			return false;
		} else {
			List paramList = resourceConfig.getParam();
			for (Iterator iterator = paramList.iterator(); iterator.hasNext();) {
				ParamType p = (ParamType) iterator.next();
				String paramName = p.getName();
				if (paramName != null)
					paramName = paramName.trim();
				if (SmooksModelConstants.STREAM_FILTER_TYPE.equals(paramName)) {
					return true;
				}
			}
			return false;
		}
	}

	public static void setParamText(String paramName, String value, ResourceConfigType resourceConfigType) {
		List<ParamType> list = resourceConfigType.getParam();
		ParamType param = null;
		for (Iterator<ParamType> iterator = list.iterator(); iterator.hasNext();) {
			ParamType paramType = (ParamType) iterator.next();
			String n = paramType.getName();
			if (n == null)
				continue;
			n = n.trim();
			if (n.equalsIgnoreCase(paramName)) {
				param = paramType;
				break;
			}
		}
		if (param == null) {
			param = SmooksFactory.eINSTANCE.createParamType();
			param.setName(paramName);
			resourceConfigType.getParam().add(param);
		}
		setTextToAnyType(param, value);
	}

	public static String getParmaText(String paramName, ResourceConfigType resourceConfigType) {
		List plist = resourceConfigType.getParam();
		for (Iterator iterator = plist.iterator(); iterator.hasNext();) {
			ParamType p = (ParamType) iterator.next();
			String n = p.getName();
			if (n == null)
				continue;
			n = n.trim();
			if (paramName.equalsIgnoreCase(n)) {
				return getAnyTypeText(p);
			}
		}
		return null;
	}

	public static String getAttributeValueFromAnyType(AnyType anyType, EStructuralFeature attribute) {
		String value = (String) anyType.getAnyAttribute().get(attribute, false);
		return value;
	}

	public static String getAnyTypeText(AnyType anyType) {
		Object value = anyType.getMixed().get(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__TEXT, true);
		if (value != null) {
			if (value instanceof List && !((List<?>) value).isEmpty()) {
				Object v = ((List<?>) value).get(0);
				if (v != null) {
					return v.toString().trim();
				}
			}
			// return value.toString();
		}
		return null;
	}

	public static String getAnyTypeCDATA(AnyType anyType) {
		Object value = anyType.getMixed().get(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__CDATA, true);
		if (value != null) {
			if (value instanceof List && !((List) value).isEmpty()) {
				Object v = ((List) value).get(0);
				if (v != null) {
					return v.toString().trim();
				}
			}
			// return value.toString();
		}
		return null;
	}

	public static String getAnyTypeComment(AnyType anyType) {
		EList<Object> value = anyType.getMixed().list(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__COMMENT);
		if (value != null && !value.isEmpty()) {
			Object v = ((List<?>) value).get(0);
			if (v != null) {
				return v.toString().trim();
			}
			// return value.toString();
		}
		return null;
	}

	public static AnyType createBindingType(String property, String selector, String type, String uri) {
		if (uri == null) {
			uri = SmooksPackage.eNS_URI;
		}

		AnyType binding = (AnyType) EcoreUtil.create(XMLTypePackage.Literals.ANY_TYPE);
		if (property != null) {
			binding.getAnyAttribute().add(ATTRIBUTE_PROPERTY, property);
		}

		if (selector != null) {
			binding.getAnyAttribute().add(ATTRIBUTE_SELECTOR, selector);
		}
		if (type != null) {
			binding.getAnyAttribute().add(ATTRIBUTE_TYPE, false);
		}
		return binding;
	}

	public static void appendTextToSmooksType(AnyType smooksModel, String text) {
		smooksModel.getMixed().add(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__TEXT, text);
	}

	public static void setTextToSmooksType(EditingDomain editingDomain, AnyType smooksModel, String text) {
		CompoundCommand ccommand = new CompoundCommand();
		Command addCommand = null;
		if (text != null) {
			addCommand = AddCommand.create(editingDomain, smooksModel, XMLTypePackage.Literals.ANY_TYPE__MIXED,
					FeatureMapUtil.createEntry(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__TEXT, text));
		}
		Object removeValue = (smooksModel.getMixed().get(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__TEXT, true));
		if (removeValue != null && removeValue instanceof Collection<?>) {
			List<Object> rList = new ArrayList<Object>();
			for (Iterator<?> iterator = ((Collection<?>) removeValue).iterator(); iterator.hasNext();) {
				Object string = (Object) iterator.next();
				rList.add(FeatureMapUtil.createEntry(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__TEXT, string));
			}
			Command cc = RemoveCommand.create(editingDomain, smooksModel, null, rList);
			if (cc != null && cc.canExecute()) {
				ccommand.append(cc);
			}
		}
		if (addCommand != null && addCommand.canExecute()) {
			ccommand.append(addCommand);
		}
		if (smooksModel.eContainer() == null) {
			ccommand.execute();
		} else {
			editingDomain.getCommandStack().execute(ccommand);
		}
	}

	public static void setCommentToSmooksType(EditingDomain editingDomain, AnyType smooksModel, String comment) {
		CompoundCommand ccommand = new CompoundCommand();
		Command addCommand = null;
		if (comment != null) {
			addCommand = AddCommand.create(editingDomain, smooksModel, XMLTypePackage.Literals.ANY_TYPE__MIXED,
					FeatureMapUtil.createEntry(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__COMMENT, comment));
		}
		Object removeValue = (smooksModel.getMixed().get(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__COMMENT, true));
		if (removeValue != null && removeValue instanceof Collection<?>) {
			List<Object> rList = new ArrayList<Object>();
			for (Iterator<?> iterator = ((Collection<?>) removeValue).iterator(); iterator.hasNext();) {
				Object string = (Object) iterator.next();
				rList.add(FeatureMapUtil.createEntry(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__COMMENT, string));
			}
			Command cc = RemoveCommand.create(editingDomain, smooksModel, null, rList);
			if (cc != null && cc.canExecute()) {
				ccommand.append(cc);
			}
		}
		if (addCommand != null && addCommand.canExecute()) {
			ccommand.append(addCommand);
		}
		if (smooksModel.eContainer() == null) {
			ccommand.execute();
		} else {
			editingDomain.getCommandStack().execute(ccommand);
		}
	}

	public static void setCDATAToSmooksType(EditingDomain editingDomain, AnyType smooksModel, String cdata) {
		CompoundCommand ccommand = new CompoundCommand();
		Command addCommand = null;
		if (cdata != null) {
			addCommand = AddCommand.create(editingDomain, smooksModel, XMLTypePackage.Literals.ANY_TYPE__MIXED,
					FeatureMapUtil.createEntry(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__CDATA, cdata));
		}
		Object removeValue = (smooksModel.getMixed().get(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__CDATA, true));
		if (removeValue != null && removeValue instanceof Collection<?>) {
			List<Object> rList = new ArrayList<Object>();
			for (Iterator<?> iterator = ((Collection<?>) removeValue).iterator(); iterator.hasNext();) {
				Object string = (Object) iterator.next();
				rList.add(FeatureMapUtil.createEntry(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__CDATA, string));
			}
			Command cc = RemoveCommand.create(editingDomain, smooksModel, null, rList);
			if (cc != null && cc.canExecute()) {
				ccommand.append(cc);
			}
		}
		if (addCommand != null && addCommand.canExecute()) {
			ccommand.append(addCommand);
		}
		if (smooksModel.eContainer() == null) {
			ccommand.execute();
		} else {
			editingDomain.getCommandStack().execute(ccommand);
		}
	}

	public static void appendCDATAToSmooksType(AnyType smooksModel, String text) {
		smooksModel.getMixed().add(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__CDATA, text);
	}

	/**
	 * @deprecated
	 * @param smooksModel
	 * @param text
	 */
	public static void setTextToAnyType(AnyType smooksModel, String text) {
		cleanTextToSmooksType(smooksModel);
		appendTextToSmooksType(smooksModel, text);
	}

	/**
	 * @deprecated
	 * @param smooksModel
	 * @param text
	 */
	public static void setCDATAToAnyType(AnyType smooksModel, String text) {
		cleanCDATAToSmooksType(smooksModel);
		appendCDATAToSmooksType(smooksModel, text);
	}

	public static void cleanTextToSmooksType(AnyType smooksModel) {
		Object obj = smooksModel.getMixed().get(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__TEXT, true);
		if (obj instanceof List) {
			((List) obj).clear();
		}
	}

	public static void cleanCDATAToSmooksType(AnyType smooksModel) {
		Object obj = smooksModel.getMixed().get(XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__CDATA, true);
		if (obj instanceof List) {
			((List) obj).clear();
		}
	}

	public static CommandParameter createTextCommandParamter(Object owner, String value) {
		return createChildParameter(owner, XMLTypePackage.Literals.ANY_TYPE__MIXED, FeatureMapUtil.createEntry(
				XMLTypePackage.Literals.XML_TYPE_DOCUMENT_ROOT__TEXT, value));
	}

	public static CommandParameter createChildParameter(Object owner, Object feature, Object child) {
		return new CommandParameter(owner, feature, child);
	}

	public static void addJavaInput(SmooksGraphicsExtType ext, String className) {
		InputType javaInput = GraphFactory.eINSTANCE.createInputType();
		javaInput.setType(INPUT_TYPE_JAVA);
		org.jboss.tools.smooks.model.graphics.ext.ParamType p = GraphFactory.eINSTANCE.createParamType();
		p.setName(PARAM_NAME_CLASS);
		p.setValue(className);
		javaInput.getParam().add(p);
		ext.getInput().add(javaInput);
	}

	public static String getInputPath(InputType input) {
		List<org.jboss.tools.smooks.model.graphics.ext.ParamType> list = input.getParam();
		if (INPUT_TYPE_JAVA.equals(input.getType()) || INPUT_TYPE_XML.equals(input.getType())
				|| INPUT_TYPE_XSD.equals(input.getType()) || INPUT_TYPE_JSON_1_1.equals(input.getType())
				|| INPUT_TYPE_JSON_1_2.equals(input.getType()) || INPUT_TYPE_CSV.equals(input.getType())
				|| INPUT_TYPE_EDI_1_1.equals(input.getType()) || INPUT_TYPE_EDI_1_2.equals(input.getType())
				|| INPUT_TYPE_CSV_1_2.equals(input.getType())) {
			for (Iterator<?> iterator = list.iterator(); iterator.hasNext();) {
				org.jboss.tools.smooks.model.graphics.ext.ParamType paramType = (org.jboss.tools.smooks.model.graphics.ext.ParamType) iterator
						.next();
				if ("path".equals(paramType.getName())) {
					String value = paramType.getValue();
					if (value != null)
						value = value.trim();
					return value;
				}
			}
		}
		return null;
	}

	public static List<ConditionType> collectConditionType(SmooksResourceListType resourceList) {
		ConditionsType conditions = resourceList.getConditions();
		if (conditions != null) {
			return conditions.getCondition();
		}
		return Collections.emptyList();
	}

	public static List<org.jboss.tools.smooks.model.smooks.ParamType> getParams(AnyType model) {
		if (model == null)
			return Collections.emptyList();
		List<org.jboss.tools.smooks.model.smooks.ParamType> obj = model.getMixed().list(
				org.jboss.tools.smooks.model.smooks.SmooksPackage.Literals.DOCUMENT_ROOT__PARAM);
		return obj;
	}

	public static void addParam(AnyType model, org.jboss.tools.smooks.model.smooks.ParamType param) {
		if (model == null)
			return;
		model.getMixed().add(
				XMLTypePackage.Literals.ANY_TYPE__MIXED,
				FeatureMapUtil.createEntry(
						org.jboss.tools.smooks.model.smooks.SmooksPackage.Literals.DOCUMENT_ROOT__PARAM, param));
	}

	public static char getFreemarkerCSVSeperator(Template template) {
		org.jboss.tools.smooks.model.smooks.ParamType typeParam = getParam(template, KEY_CSV_SEPERATOR);
		if (typeParam != null) {
			String value = typeParam.getStringValue();
			if (value != null && value.length() == 1) {
				return value.toCharArray()[0];
			}
		}
		return 0;
	}

	public static char getFreemarkerCSVQuote(Template template) {
		org.jboss.tools.smooks.model.smooks.ParamType typeParam = getParam(template, KEY_CSV_QUOTE);
		if (typeParam != null) {
			String value = typeParam.getStringValue();
			if (value != null && value.length() == 1) {
				return value.toCharArray()[0];
			}
		}
		return 0;
	}

	public static String getTemplateType(AnyType template) {
		if (template == null)
			return null;
		org.jboss.tools.smooks.model.smooks.ParamType typeParam = getParam(template, KEY_TEMPLATE_TYPE);
		if (typeParam != null) {
			return typeParam.getStringValue();
		}
		return null;
	}

	public static String[] getFreemarkerCSVFileds(Template template) {
		org.jboss.tools.smooks.model.smooks.ParamType typeParam = getParam(template, KEY_CSV_FIELDS);
		if (typeParam != null) {
			String value = typeParam.getStringValue();
			if (value != null) {
				value = value.trim();
				return value.split(",");
			}
		}
		return null;
	}

	public static org.jboss.tools.smooks.model.smooks.ParamType getParam(AnyType model, String paramName) {
		List<org.jboss.tools.smooks.model.smooks.ParamType> params = getParams(model);
		for (Iterator<?> iterator = params.iterator(); iterator.hasNext();) {
			org.jboss.tools.smooks.model.smooks.ParamType paramType = (org.jboss.tools.smooks.model.smooks.ParamType) iterator
					.next();
			if (paramName.equals(paramType.getName())) {
				return paramType;
			}
		}
		return null;
	}

	public static String getParamValue(AnyType model, String paramName) {
		List<org.jboss.tools.smooks.model.smooks.ParamType> params = getParams(model);
		for (Iterator<?> iterator = params.iterator(); iterator.hasNext();) {
			org.jboss.tools.smooks.model.smooks.ParamType paramType = (org.jboss.tools.smooks.model.smooks.ParamType) iterator
					.next();
			if (paramName.equals(paramType.getName())) {
				return paramType.getStringValue();
			}
		}
		return null;
	}

	public static String getParamValue(List<org.jboss.tools.smooks.model.smooks.ParamType> params, String paramName) {
		for (Iterator<?> iterator = params.iterator(); iterator.hasNext();) {
			org.jboss.tools.smooks.model.smooks.ParamType paramType = (org.jboss.tools.smooks.model.smooks.ParamType) iterator
					.next();
			if (paramName.equals(paramType.getName())) {
				return paramType.getStringValue();
			}
		}
		return null;
	}

	public static String generateTaskID(SmooksResourceListType resourceList, Class<?> modelClass, String baseID) {
		List<AbstractResourceConfig> configList = resourceList.getAbstractResourceConfig();
		int index = 0;
		List<AbstractResourceConfig> modelList = new ArrayList<AbstractResourceConfig>();
		for (Iterator<?> iterator = configList.iterator(); iterator.hasNext();) {
			AbstractResourceConfig abstractResourceConfig = (AbstractResourceConfig) iterator.next();
			if (modelClass.isInstance(abstractResourceConfig)) {
				modelList.add(abstractResourceConfig);
				// index++;
			}
		}
		String id = baseID + String.valueOf(index);
		int i = 0;
		for (i = 0; i < modelList.size(); i++) {
			AbstractResourceConfig abstractResourceConfig = modelList.get(i);
			String idref = null;
			if (abstractResourceConfig instanceof Freemarker) {
				idref = SmooksModelUtils.getParamValue(((Freemarker) abstractResourceConfig).getParam(),
						SmooksModelUtils.KEY_OBJECT_ID);
			}
			if (idref == null) {
				idref = SmooksModelUtils.getParamValue(abstractResourceConfig, SmooksModelUtils.KEY_OBJECT_ID);
			}
			if (id.equals(idref)) {
				index++;
				id = baseID + String.valueOf(index);
				i = 0;
				continue;
			}
		}
		return id;

	}

}
