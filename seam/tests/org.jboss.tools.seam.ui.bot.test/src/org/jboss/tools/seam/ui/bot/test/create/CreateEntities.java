package org.jboss.tools.seam.ui.bot.test.create;

import java.util.Properties;

import org.jboss.tools.seam.ui.bot.test.AbstractSeamTestBase;
import org.jboss.tools.seam.ui.bot.test.EARTests;
import org.jboss.tools.seam.ui.bot.test.TestControl;
import org.jboss.tools.seam.ui.bot.test.WARTests;
import org.junit.Test;
import org.junit.experimental.categories.Category;

public class CreateEntities extends AbstractSeamTestBase {
	
	private static String TEST_TYPE = "Entity";
	
	public CreateEntities() {
	}
	
	@Test
	@Category(WARTests.class)	
	public void testCreateEntityFor12war(){
		createSeamUnit(TEST_TYPE, TestControl.TYPE_WAR);		
	}
	
	@Test
	@Category(EARTests.class)
	public void testCreateEntityFor12ear(){
		createSeamUnit(TEST_TYPE, TestControl.TYPE_EAR);		
	}

	
}
	