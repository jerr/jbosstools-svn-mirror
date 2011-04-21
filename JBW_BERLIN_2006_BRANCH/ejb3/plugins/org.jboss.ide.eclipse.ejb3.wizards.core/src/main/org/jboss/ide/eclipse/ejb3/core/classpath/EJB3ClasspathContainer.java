/*
 * JBoss, a division of Red Hat
 * Copyright 2006, Red Hat Middleware, LLC, and individual contributors as indicated
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
package org.jboss.ide.eclipse.ejb3.core.classpath;

import java.util.ArrayList;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.wst.server.core.IRuntime;
import org.eclipse.wst.server.core.ServerCore;
import org.jboss.ide.eclipse.as.core.server.runtime.JBossServerRuntime;
import org.jboss.ide.eclipse.jdt.aop.core.classpath.AopClasspathContainer;

/**
 * @author Marshall
 */
public class EJB3ClasspathContainer extends AopClasspathContainer
{
   public static final String CONTAINER_ID = "org.jboss.ide.eclipse.jdt.ejb3.wizards.core.classpath.EJB3_CONTAINER";

   public static final String DESCRIPTION = "JBoss EJB3 Libraries";

   public static final QualifiedName JBOSS_EJB3_CONFIGURATION = new QualifiedName(
         "org.jboss.ide.eclipse.ejb3.wizards.core.classpath", "jboss-ejb3-configuration");

   private static IPath clientPath, libPath, serverConfigLibPath, ejb3DeployerPath;
   static {
      clientPath = new Path("client");
      libPath = new Path("lib");
      serverConfigLibPath = new Path("lib");
      ejb3DeployerPath = new Path("deploy/ejb3.deployer");
   }

   public static final IPath[] jbossJarPaths = new IPath[]
   {clientPath.append("jnp-client.jar"), clientPath.append("jbosssx-client.jar"), clientPath.append("jboss-j2ee.jar"),
         clientPath.append("jboss-transaction-client.jar"), clientPath.append("jboss-client.jar"), libPath.append("commons-logging.jar")};

   public static final IPath[] jbossConfigRelativeJarPaths = new IPath[]
   {ejb3DeployerPath.append("jboss-ejb3.jar"), ejb3DeployerPath.append("jboss-ejb3x.jar"),
	   ejb3DeployerPath.append("jboss-annotations-ejb3.jar"),
	   serverConfigLibPath.append("hibernate3.jar"), serverConfigLibPath.append("ejb3-persistence.jar"),
	   serverConfigLibPath.append("hibernate-annotations.jar"), serverConfigLibPath.append("jboss-remoting.jar")};


   
   
   
   protected IJavaProject javaProject;
   protected JBossServerRuntime jbsRuntime;
   
   public EJB3ClasspathContainer(IPath path, IJavaProject project) {
      super(path);

      this.javaProject = project;
      String runtimeName = path.segment(1);
      IRuntime runtime = ServerCore.findRuntime(runtimeName);
      if( runtime != null ) 
    	  jbsRuntime = (JBossServerRuntime)runtime.loadAdapter(JBossServerRuntime.class, new NullProgressMonitor());
   }

   public IPath[] getAopJarPaths() {
      if (jbsRuntime == null)
         return new IPath[0];

      String jbossBaseDir = jbsRuntime.getRuntime().getLocation().toOSString();
      String jbossConfigDir = jbsRuntime.getConfigName();

      ArrayList paths = new ArrayList();

      if (jbossBaseDir != null) {
         for (int i = 0; i < jbossJarPaths.length; i++) {
            IPath jar = jbossJarPaths[i];
            IPath entryPath = new Path(jbossBaseDir).append(jar);
            paths.add(entryPath);
         }

         if (jbossConfigDir != null) {
            IPath jbossServerConfigPath = new Path(jbossBaseDir).append("server").append(jbossConfigDir);
            for (int i = 0; i < jbossConfigRelativeJarPaths.length; i++) {
               IPath jar = jbossConfigRelativeJarPaths[i];
               IPath entryPath = jbossServerConfigPath.append(jar);
               paths.add(entryPath);
            }
         }
      }

      return (IPath[]) paths.toArray(new IPath[paths.size()]);
   }

   protected IPath[] getAopJarRelativePaths() {
      return new IPath[0];
   }

   public String getContainerId() {
      return CONTAINER_ID;
   }

   public String getDescription() {
      return DESCRIPTION + " [" + (jbsRuntime == null ? "error" : jbsRuntime.getRuntime().getName()) + "]";
   }
}
