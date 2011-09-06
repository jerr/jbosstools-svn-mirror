/******************************************************************************* 
 * Copyright (c) 2007 Red Hat, Inc. 
 * Distributed under license by Red Hat, Inc. All rights reserved. 
 * This program is made available under the terms of the 
 * Eclipse Public License v1.0 which accompanies this distribution, 
 * and is available at http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Red Hat, Inc. - initial API and implementation 
 ******************************************************************************/ 
package org.jboss.ide.eclipse.as.openshift.core;

import org.jboss.ide.eclipse.as.openshift.internal.core.Cartridge;

public class Application implements IOpenshiftObject {

	private String name;
	private Cartridge cartridge;

	public Application(String name, Cartridge cartridge) {
		this.name = name;
		this.cartridge = cartridge;
	}

	public String getName() {
		return name;
	}

	public Cartridge getCartridge() {
		return cartridge;
	}

}
