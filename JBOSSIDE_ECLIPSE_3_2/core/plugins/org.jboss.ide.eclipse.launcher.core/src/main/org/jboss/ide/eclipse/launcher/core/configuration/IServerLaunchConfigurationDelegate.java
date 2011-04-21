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
package org.jboss.ide.eclipse.launcher.core.configuration;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;

/**
 * @author    Hans Dockter
 * @version   $Revision$
 * @created   17 mai 2003
 */
public interface IServerLaunchConfigurationDelegate extends ILaunchConfigurationDelegate
{
   /**
    * Description of the Method
    *
    * @return   Description of the Return Value
    */
   public boolean hasShutdown();

   /**
    * Description of the Method
    *
    * @param configuration      Description of the Parameter
    * @param monitor            Description of the Parameter
    * @exception CoreException  Description of the Exception
    */
   public void shutdown(ILaunchConfiguration configuration, IProgressMonitor monitor) throws CoreException;
}
