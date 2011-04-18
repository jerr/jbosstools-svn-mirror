package org.jboss.ide.eclipse.as.core.server.internal.v7;

import java.io.File;

import org.eclipse.core.runtime.IProgressMonitor;

/**
 * 
 * This class is interim API and may change drastically 
 * as development on the application server continues. 
 * I expect credentials to be required eventually,
 * and the API will need to adjust to handle them. 
 * 
 */
public interface IJBoss7DeploymentManager {
	/**
	 * Asynchronously deploy a file to a server
	 * 
	 * @param host The host
	 * @param port The port
	 * @param name The deployment's name
	 * @param file The file to be deployed
	 * @param monitor The progress monitor
	 * 
	 * @return Not sure what to return yet
	 * @throws Exception 
	 */
	public Object deployAsync(String host, int port, 
					String deploymentName, File file, IProgressMonitor monitor) throws Exception;

	/**
	 * Synchronously deploy a file to a server
	 * 
	 * @param host The host
	 * @param port The port
	 * @param name The deployment's name
	 * @param file The file to be deployed
	 * @param monitor The progress monitor
	 * 
	 * @return Not sure what to return yet
	 * @throws Exception 
	 */
	public Object deploySync(String host, int port, 
			String deploymentName, File file, IProgressMonitor monitor) throws Exception;
	
	
	/**
	 * Asynchronously undeploy a file to a server
	 * 
	 * @param host The host
	 * @param port The port
	 * @param name The deployment's name
	 * @param file The file to be deployed
	 * @param monitor The progress monitor
	 * 
	 * @return Not sure what to return yet
	 * @throws Exception 
	 */
	public Object undeployAsync(String host, int port,
			String deploymentName, boolean removeFile, IProgressMonitor monitor) throws Exception;
	

	/**
	 * Synchronously undeploy a file to a server
	 * 
	 * @param host The host
	 * @param port The port
	 * @param name The deployment's name
	 * @param file The file to be deployed
	 * @param monitor The progress monitor
	 * 
	 * @return Not sure what to return yet
	 * @throws Exception 
	 */
	public Object syncUndeploy(String host, int port,
			String deploymentName, boolean removeFile, IProgressMonitor monitor) throws Exception;
	

}
