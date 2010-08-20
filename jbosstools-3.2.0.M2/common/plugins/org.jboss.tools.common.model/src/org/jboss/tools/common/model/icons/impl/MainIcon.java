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
package org.jboss.tools.common.model.icons.impl;

import org.eclipse.swt.graphics.Image;
import org.jboss.tools.common.model.*;

public class MainIcon implements ImageComponent {

    public MainIcon() {}

    public int getHash(XModelObject obj) {
        return obj.getMainIconName().hashCode();
    }

    public Image getImage(XModelObject obj) {
        String s = obj.getMainIconName();
        return obj.getModelEntity().getMetaModel().getIconList().getImage(s);
    }

}

