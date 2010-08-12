package org.jboss.tools.smooks.configuration.editors.input;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	
	public static String InputSourceType_Warning_Specify_Input_Type;
	public static String InputSourceType_Warning_Specify_Sample_Data;

	static {
		// initialize resource bundle
		NLS.initializeMessages(Messages.class.getPackage().getName() + ".messages", Messages.class); //$NON-NLS-1$
	}

	private Messages() {
	}
}
