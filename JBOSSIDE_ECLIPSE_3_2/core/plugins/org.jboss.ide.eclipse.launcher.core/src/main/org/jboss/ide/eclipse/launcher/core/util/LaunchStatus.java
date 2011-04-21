/*
 * JBoss, Home of Professional Open Source
 * Copyright 2005, JBoss Inc., and individual contributors as indicated
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.jboss.ide.eclipse.launcher.core.util;

import org.jboss.ide.eclipse.core.util.NamedType;
import org.jboss.ide.eclipse.launcher.core.LauncherCoreMessages;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public class LaunchStatus extends NamedType
{
   /** Description of the Field */
   public final static LaunchStatus CONFIGURE_ERROR = new LaunchStatus(LauncherCoreMessages
         .getString("LaunchStatusconfigure_error_3"));//$NON-NLS-1$

   /** Description of the Field */
   public final static LaunchStatus NOT_RUNNING = new LaunchStatus(LauncherCoreMessages
         .getString("LaunchStatusnot_running_2"));//$NON-NLS-1$

   /** Description of the Field */
   public final static LaunchStatus RUNNING = new LaunchStatus(LauncherCoreMessages.getString("LaunchStatusrunning_1"));//$NON-NLS-1$

   /**
    *Constructor for the LaunchStatus object
    *
    * @param name  Description of the Parameter
    */
   private LaunchStatus(String name)
   {
      super(name);
   }
}
