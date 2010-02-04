/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, JBoss Inc., and others contributors as indicated
 * by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 * This copyrighted material is made available to anyone wishing to use,
 * modify, copy, or redistribute it subject to the terms and conditions
 * of the GNU Lesser General Public License, v. 2.1.
 * This program is distributed in the hope that it will be useful, but WITHOUT A
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more details.
 * You should have received a copy of the GNU Lesser General Public License,
 * v.2.1 along with this distribution; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA  02110-1301, USA.
 *
 * (C) 2005-2006, JBoss Inc.
 */
package org.jboss.tools.smooks.templating.template.exception;

import org.w3c.dom.Element;
import org.milyn.xml.DomUtils;

/**
 * Unmapped collection node exception.
 * @author <a href="mailto:tom.fennelly@jboss.com">tom.fennelly@jboss.com</a>
 */
public class UnmappedCollectionNodeException extends InvalidMappingException {

    private Element unmappedCollectionNode;

    public UnmappedCollectionNodeException(Element unmappedCollectionNode) {
        super("Unmapped collection node '" + DomUtils.getName(unmappedCollectionNode) + Messages.UnmappedCollectionNodeException_0); //$NON-NLS-1$
        this.unmappedCollectionNode = unmappedCollectionNode;
    }

    public Element getUnmappedCollectionNode() {
        return unmappedCollectionNode;
    }
}
