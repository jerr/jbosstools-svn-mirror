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
package org.jboss.tools.smooks.test.csvparser;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.DocumentException;
import org.eclipse.emf.ecore.resource.Resource;
import org.jboss.tools.smooks.configuration.editors.IXMLStructuredObject;
import org.jboss.tools.smooks.configuration.editors.csv.CSVDataParser;
import org.jboss.tools.smooks.configuration.editors.uitls.SmooksUIUtils;
import org.jboss.tools.smooks.configuration.editors.xml.TagList;
import org.jboss.tools.smooks.model.graphics.ext.DocumentRoot;
import org.jboss.tools.smooks.model.graphics.ext.InputType;
import org.jboss.tools.smooks.model.graphics.ext.SmooksGraphicsExtType;
import org.jboss.tools.smooks.model.graphics.ext.util.SmooksGraphicsExtResourceFactoryImpl;
import org.jboss.tools.smooks.model.smooks.SmooksResourceListType;
import org.jboss.tools.smooks.model.smooks.util.SmooksResourceFactoryImpl;
import org.jboss.tools.smooks.test.model11.BaseTestCase;

/**
 * @author Dart (dpeng@redhat.com)
 * 
 */
public class CsvParserTest extends BaseTestCase {

	public void testCsvParser() throws IOException, DocumentException, ParserConfigurationException {
		Resource extResource = new SmooksGraphicsExtResourceFactoryImpl().createResource(null);
		extResource.load(CsvParserTest.class.getResourceAsStream("smooks-config.xml.ext"), null);
		SmooksGraphicsExtType extType = ((DocumentRoot) extResource.getContents().get(0)).getSmooksGraphicsExt();
		Resource smooksResource = new SmooksResourceFactoryImpl().createResource(null);

		assertNotNull(extType);
		InputType inputType = null;
		List<?> ilist = extType.getInput();
		for (Iterator<?> iterator = ilist.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof InputType) {
				if ("csv".equalsIgnoreCase(((InputType) object).getType())) {
					inputType = (InputType) object;
					break;
				}
			}
		}
		assertNotNull(inputType);

		smooksResource.load(CsvParserTest.class.getResourceAsStream("smooks-config.xml"), null);

		SmooksResourceListType resourceList = ((org.jboss.tools.smooks.model.smooks.DocumentRoot) smooksResource
				.getContents().get(0)).getSmooksResourceList();
		assertNotNull(resourceList);

		CSVDataParser parser = new CSVDataParser();
		TagList tagList = parser.parseCSV(CsvParserTest.class.getResourceAsStream("input-message.csv"), inputType,
				resourceList);
		assertNotNull(tagList);
		checkTagList1(tagList);
		
		for (Iterator<?> iterator = ilist.iterator(); iterator.hasNext();) {
			Object object = (Object) iterator.next();
			if (object instanceof InputType) {
				if ("csv".equalsIgnoreCase(((InputType) object).getType()) && object != inputType) {
					inputType = (InputType) object;
					break;
				}
			}
		}
		
		parser = new CSVDataParser();
		tagList = parser.parseCSV(CsvParserTest.class.getResourceAsStream("input-message.csv"), inputType,
				resourceList);
		assertNotNull(tagList);
		checkTagList2(tagList);
	}
	
	private void checkNode(String nodeName , IXMLStructuredObject tag){
		IXMLStructuredObject node1 = SmooksUIUtils.localXMLNodeWithNodeName(nodeName, tag);
		assertNotNull(node1);
		String selector = SmooksUIUtils.generateFullPath(node1, "/");
		assertEquals("/csv-set/csv-record/"+nodeName, selector);
		
	}
	
	private void checkTagList2(TagList tagList){
		assertEquals(1, tagList.getChildren().size());
		IXMLStructuredObject tag = tagList.getChildren().get(0);
		assertEquals("csv-set",tag.getNodeName());
		
		IXMLStructuredObject child = tag.getChildren().get(0);
		assertEquals("csv-record", child.getNodeName());
		
		checkNode("a",tag);
		checkNode("b",tag);
		checkNode("c",tag);
		checkNode("d",tag);
		checkNode("e",tag);
	}
	
	private void checkTagList1(TagList tagList){
		assertEquals(1, tagList.getChildren().size());
		IXMLStructuredObject tag = tagList.getChildren().get(0);
		assertEquals("csv-set",tag.getNodeName());
		
		IXMLStructuredObject child = tag.getChildren().get(0);
		assertEquals("csv-record", child.getNodeName());
		
		checkNode("firstname",tag);
		checkNode("lastname",tag);
		checkNode("gender",tag);
		checkNode("age",tag);
		checkNode("country",tag);
	}
}
