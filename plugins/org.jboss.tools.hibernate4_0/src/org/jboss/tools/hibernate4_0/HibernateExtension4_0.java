/*******************************************************************************
 * Copyright (c) 2011 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.hibernate4_0;

import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.osgi.util.NLS;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.console.ConsoleConfigClassLoader;
import org.hibernate.console.ConsoleMessages;
import org.hibernate.console.QueryInputModel;
import org.hibernate.console.execution.DefaultExecutionContext;
import org.hibernate.console.execution.ExecutionContext;
import org.hibernate.console.execution.ExecutionContext.Command;
import org.hibernate.console.ext.CompletionProposalsResult;
import org.hibernate.console.ext.HibernateException;
import org.hibernate.console.ext.HibernateExtension;
import org.hibernate.console.ext.QueryResult;
import org.hibernate.console.ext.QueryResultImpl;
import org.hibernate.console.preferences.ConsoleConfigurationPreferences;
import org.hibernate.console.preferences.PreferencesClassPathUtils;
import org.hibernate.service.BasicServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.hibernate.service.internal.BasicServiceRegistryImpl;
import org.hibernate.tool.ide.completion.HQLCodeAssist;
import org.hibernate.tool.ide.completion.IHQLCodeAssist;

/**
 * 
 * @author Dmitry Geraskov
 *
 */
public class HibernateExtension4_0 implements HibernateExtension {
	
	private ConsoleConfigClassLoader classLoader = null;

	private ExecutionContext executionContext;
	
	private ConsoleConfigurationPreferences prefs;
	
	private Configuration configuration;
	
	private SessionFactory sessionFactory;
	
	private BasicServiceRegistry serviceRegistry;
	
	private Map<String, FakeDelegatingDriver> fakeDrivers = new HashMap<String, FakeDelegatingDriver>();

	public HibernateExtension4_0() {
	}

	@Override
	public String getHibernateVersion() {
		return "4.0";
	}
	
	@Override
	public QueryResult executeHQLQuery(String hql,
			QueryInputModel queryParameters) {
		Session session = null;
		try {
			try {
				session = sessionFactory.openSession();
				return QueryExecutor.executeHQLQuery(session, hql, queryParameters);
			} catch (Throwable e){
				//Incompatible library versions could throw subclasses of Error, like  AbstractMethodError
				//may be there is a sense to say to user that the reason is probably a wrong CC version
				//(when catch a subclass of Error)
				return new QueryResultImpl(e);
			}
		} finally {
			if (session != null && session.isOpen()){
				try {
					session.close();
				} catch (HibernateException e) {
					return new QueryResultImpl(e);
	    		}
			}
		}
	}

	@Override
	public QueryResult executeCriteriaQuery(String criteriaCode,
			QueryInputModel model) {
		Session session = null;
		try {
			try {
				session = sessionFactory.openSession();
				return QueryExecutor.executeCriteriaQuery(session, criteriaCode, model);
			} catch (Throwable e){
				//Incompatible library versions could throw subclasses of Error, like  AbstractMethodError
				//may be there is a sense to say to user that the reason is probably a wrong CC version
				//(when catch a subclass of Error)
				return new QueryResultImpl(e);
			}
		} finally {
			if (session != null && session.isOpen()){
				try {
					session.close();
				} catch (HibernateException e) {
					return new QueryResultImpl(e);
	    		}
			}
		}
	}

	/**
	 * @param ConsoleConfigurationPreferences the prefs to set
	 */
	public void setConsoleConfigurationPreferences(ConsoleConfigurationPreferences prefs) {
		this.prefs = prefs;
	}
	
	public void build() {
		configuration = buildWith(null, true);
	}

	@Override
	public void buildSessionFactory() {
		execute(new Command() {
			public Object execute() {
				if (sessionFactory != null) {
					throw new HibernateException("Factory was not closed before attempt to build a new Factory");
				}
				serviceRegistry =  new ServiceRegistryBuilder()
					.applySettings( configuration.getProperties())
					.buildServiceRegistry();
				sessionFactory = configuration.buildSessionFactory(serviceRegistry);
				return null;
			}
		});
	}

	@Override
	public boolean closeSessionFactory() {
		boolean res = false;
		if (sessionFactory != null) {
			sessionFactory.close();
			sessionFactory = null;
			( (BasicServiceRegistryImpl) serviceRegistry ).destroy();
			serviceRegistry = null;
			res = true;
		}
		return res;
	}
	
	public Configuration buildWith(final Configuration cfg, final boolean includeMappings) {
		reinitClassLoader();
		//TODO handle user libraries here
		executionContext = new DefaultExecutionContext(prefs.getName(), classLoader);
		Configuration result = (Configuration)execute(new Command() {
			public Object execute() {
				ConfigurationFactory cf = new ConfigurationFactory(prefs, fakeDrivers);
				return cf.createConfiguration(cfg, includeMappings);
			}
		});
		return result;
	}
	
	/**
	 * Create class loader - so it uses the original urls list from preferences. 
	 */
	protected void reinitClassLoader() {
		boolean recreateFlag = true;
		final URL[] customClassPathURLs = PreferencesClassPathUtils.getCustomClassPathURLs(prefs);
		if (classLoader != null) {
			// check -> do not recreate class loader in case if urls list is the same
			final URL[] oldURLS = classLoader.getURLs();
			if (customClassPathURLs.length == oldURLS.length) {
				int i = 0;
				for (; i < oldURLS.length; i++) {
					if (!customClassPathURLs[i].sameFile(oldURLS[i])) {
						break;
					}
				}
				if (i == oldURLS.length) {
					recreateFlag = false;
				}
			}
		}
		if (recreateFlag) {
			reset();
			classLoader = createClassLoader(customClassPathURLs);
		}
	}
	
	protected ConsoleConfigClassLoader createClassLoader(final URL[] customClassPathURLs) {
		ConsoleConfigClassLoader classLoader = AccessController.doPrivileged(new PrivilegedAction<ConsoleConfigClassLoader>() {
			public ConsoleConfigClassLoader run() {
				return new ConsoleConfigClassLoader(customClassPathURLs, Thread.currentThread().getContextClassLoader()) {
					protected Class<?> findClass(String name) throws ClassNotFoundException {
						try {
							return super.findClass(name);
						} catch (ClassNotFoundException cnfe) {
							throw cnfe;
						} catch (IllegalStateException e){
							e.printStackTrace();
							throw e;
						}
					}
		
					protected synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
						try {
							return super.loadClass(name, resolve);
						} catch (ClassNotFoundException cnfe) {
							throw cnfe;
						}
					}
		
					public Class<?> loadClass(String name) throws ClassNotFoundException {
						try {
							return super.loadClass(name);
						} catch (ClassNotFoundException cnfe) {
							throw cnfe;
						}
					}
					
					public URL getResource(String name) {
					      return super.getResource(name);
					}
				};
			}
		});
		return classLoader;
	}

	public String getName() {
		return prefs.getName();
	}
	
	public Object execute(Command c) {
		if (executionContext != null) {
			return executionContext.execute(c);
		}
		final String msg = NLS.bind(ConsoleMessages.ConsoleConfiguration_null_execution_context, getName());
		throw new HibernateException(msg);
	}

	@Override
	public boolean reset() {
		boolean res = false;
		// reseting state
		if (configuration != null) {
			configuration = null;
			res = true;
		}
		
		boolean tmp = closeSessionFactory();
		res = res || tmp;
		if (executionContext != null) {
			executionContext.execute(new Command() {
				public Object execute() {
					Iterator<FakeDelegatingDriver> it = fakeDrivers.values().iterator();
					while (it.hasNext()) {
						try {
							DriverManager.deregisterDriver(it.next());
						} catch (SQLException e) {
							// ignore
						}
					}
					return null;
				}
			});
		}
		if (fakeDrivers.size() > 0) {
			fakeDrivers.clear();
			res = true;
		}
		tmp = cleanUpClassLoader();
		res = res || tmp;
		executionContext = null;
		return res;
	}
	
	protected boolean cleanUpClassLoader() {
		boolean res = false;
		ClassLoader classLoaderTmp = classLoader;
		while (classLoaderTmp != null) {
			if (classLoaderTmp instanceof ConsoleConfigClassLoader) {
				((ConsoleConfigClassLoader)classLoaderTmp).close();
				res = true;
			}
			classLoaderTmp = classLoaderTmp.getParent();
		}
		if (classLoader != null) {
			classLoader = null;
			res = true;
		}
		return res;
	}

	@Override
	public boolean hasConfiguration() {
		return configuration != null;
	}

	@Override
	public CompletionProposalsResult hqlCodeComplete(String query, int currentOffset) {
		EclipseHQLCompletionRequestor requestor = new EclipseHQLCompletionRequestor();
		if (!hasConfiguration()){
			try {
				build();
			 	execute( new ExecutionContext.Command() {
			 		public Object execute() {
			 			if(hasConfiguration()) {
				 			configuration.buildMappings();
				 		}
			 			return null;
			 		}
				});
			} catch (HibernateException e){
				//FIXME
				//String mess = NLS.bind(HibernateConsoleMessages.CompletionHelper_error_could_not_build_cc, consoleConfiguration.getName());
				//HibernateConsolePlugin.getDefault().logErrorMessage(mess, e);
			}
		}
		IHQLCodeAssist hqlEval = new HQLCodeAssist(configuration);
		query = query.replace('\t', ' ');
		hqlEval.codeComplete(query, currentOffset, requestor);
		return new CompletionProposalsResult(requestor.getCompletionProposals(), requestor.getLastErrorMessage());
	}

}