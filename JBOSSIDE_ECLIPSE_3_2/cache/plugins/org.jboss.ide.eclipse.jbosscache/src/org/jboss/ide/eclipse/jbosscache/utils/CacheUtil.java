/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.ide.eclipse.jbosscache.utils;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IMemento;
import org.eclipse.ui.XMLMemento;
import org.jboss.cache.Fqn;
import org.jboss.ide.eclipse.jbosscache.ICacheConstants;
import org.jboss.ide.eclipse.jbosscache.JBossCachePlugin;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheInstance;
import org.jboss.ide.eclipse.jbosscache.model.cache.ICacheRootInstance;
import org.jboss.ide.eclipse.jbosscache.model.config.CacheConfigurationModel;
import org.jboss.ide.eclipse.jbosscache.model.config.CacheConfigurationModel.CacheLoaderConfigInternal;
import org.jboss.ide.eclipse.jbosscache.model.config.CacheConfigurationModel.ClusterConfigInternal;
import org.jboss.ide.eclipse.jbosscache.model.factory.CacheInstanceFactory;

/**
 * This class is used for utility
 * @author Gurkaner
 *
 */
public class CacheUtil
{

   static Set immediates = new HashSet(Arrays.asList(new Object[]
   {String.class, Boolean.class, Double.class, Float.class, Integer.class, Long.class, Short.class, Character.class,
         Boolean.TYPE, Double.TYPE, Float.TYPE, Integer.TYPE, Long.TYPE, Short.TYPE, Character.TYPE, Class.class}));

   /**
    * Returns the path of the cache plug-in
    * @return IPath
    */
   public static IPath getPluginPathLocation()
   {
      return JBossCachePlugin.getDefault().getStateLocation();
   }//end of method

   /**
    * Create new File in the plug-in .metadata directory 
    * @param fileName: File name of this newly created file
    * @return
    */
   public static File createNewFileFromPluginLocation(String fileName)
   {
      return getPluginPathLocation().append(fileName).toFile();
   }

   /**
    * Save meta-data information
    * @param obj
    * @throws Exception
    */
   public static void saveMetaDataInformation(Object obj, IProgressMonitor monitor, File file) throws Exception
   {
      if (obj instanceof CacheConfigurationModel)
      {
         saveConfigurationMetaData((CacheConfigurationModel) obj, monitor, file);
      }
      else
      {

      }
   }

   /**
    * Saves the cache configuration model
    * @param confModel
    */
   private static void saveConfigurationMetaData(CacheConfigurationModel cacheConfigModel, IProgressMonitor monitor,
         File fileLoc) throws Exception
   {
      CacheLoaderConfigInternal cacheLoaderConfig = cacheConfigModel.getCacheLoaderConfig();
      ClusterConfigInternal clusterConfig = cacheConfigModel.getClusterConfig();

      File file = fileLoc;//createNewFile(cacheConfigModel.getCacheName()+ICacheConstants.XML_FILEEXTENSION);
      //		if(file.exists()){
      //			MessageDialog.openError(shell,getResourceBundleValue(ICacheConstants.CONFIGURATION_ERROR_MESSAGE_CACHEEXIST_TITLE),getResourceBundleValue(ICacheConstants.CONFIGURATION_ERROR_MESSAGE_CACHEEXIST));
      //			return;
      //		}

      FileWriter writer = null;

      try
      {
         IMemento childServerMemonto = null;
         writer = new FileWriter(file);
         XMLMemento memonto = XMLMemento.createWriteRoot(ICacheConstants.SERVER);
         childServerMemonto = memonto.createChild(ICacheConstants.CLASSPATH);
         childServerMemonto.putString(ICacheConstants.CODEBASE, "./lib");
         childServerMemonto.putString(ICacheConstants.ARCHIVES, "jboss-cache.jar, jgroups.jar");

         monitor.worked(2);

         childServerMemonto = memonto.createChild(ICacheConstants.MBEAN);
         
         if(cacheConfigModel.getCacheMode().equals(ICacheConstants.JBOSS_CACHE_TREE_CACHE))
         {
            childServerMemonto.putString(ICacheConstants.CODE, "org.jboss.cache.TreeCache");
            childServerMemonto.putString(ICacheConstants.NAME, "jboss.cache:service=TreeCache");            
         }
         else
         {
            childServerMemonto.putString(ICacheConstants.CODE, "org.jboss.cache.aop.TreeCacheAop");
            childServerMemonto.putString(ICacheConstants.NAME, "jboss.cache:service=TreeCacheAop");                        
         }
         
         IMemento childMBeanMemonto = null;
         childMBeanMemonto = childServerMemonto.createChild(ICacheConstants.DEPENDS);
         childMBeanMemonto.putTextData("jboss:service=Naming");

         childMBeanMemonto = childServerMemonto.createChild(ICacheConstants.DEPENDS);
         childMBeanMemonto.putTextData("jboss:service=TransactionManager");
         
         if(!cacheConfigModel.getTransManagerLookUpClass().equals(ICacheConstants.TRANSACTION_MANAGER_LOOKUP_CLASSES[0]))
         {
            childMBeanMemonto = childServerMemonto.createChild(ICacheConstants.ATTRIBUTE);
            childMBeanMemonto.putString(ICacheConstants.NAME, "TransactionManagerLookupClass");         
            childMBeanMemonto.putTextData(cacheConfigModel.getTransManagerLookUpClass());
         }
         monitor.worked(3);

         childMBeanMemonto = childServerMemonto.createChild(ICacheConstants.ATTRIBUTE);
         childMBeanMemonto.putString(ICacheConstants.NAME, "IsolationLevel");
         childMBeanMemonto.putTextData(cacheConfigModel.getIsolationLevel());

         childMBeanMemonto = childServerMemonto.createChild(ICacheConstants.ATTRIBUTE);
         childMBeanMemonto.putString(ICacheConstants.NAME, "CacheMode");
         childMBeanMemonto.putTextData(cacheConfigModel.getCacheMode());

         childMBeanMemonto = childServerMemonto.createChild(ICacheConstants.ATTRIBUTE);
         childMBeanMemonto.putString(ICacheConstants.NAME, "UseReplQueue");
         childMBeanMemonto.putTextData(Boolean.toString(cacheConfigModel.isUseReplQueue()));

         monitor.worked(4);

         childMBeanMemonto = childServerMemonto.createChild(ICacheConstants.ATTRIBUTE);
         childMBeanMemonto.putString(ICacheConstants.NAME, "ReplQueueInterval");
         childMBeanMemonto.putTextData(cacheConfigModel.getReplQueueInterval());

         childMBeanMemonto = childServerMemonto.createChild(ICacheConstants.ATTRIBUTE);
         childMBeanMemonto.putString(ICacheConstants.NAME, "ReplQueueMaxElements");
         childMBeanMemonto.putTextData(cacheConfigModel.getReplQueueMaxElements());

         childMBeanMemonto = childServerMemonto.createChild(ICacheConstants.ATTRIBUTE);
         childMBeanMemonto.putString(ICacheConstants.NAME, "ClusterName");
         childMBeanMemonto.putTextData(cacheConfigModel.getClusterName());

         monitor.worked(5);

         monitor.subTask(JBossCachePlugin.getResourceBundle().getString(
               ICacheConstants.CONFIGURATION_MONITOR_SUBTASK_CLUSTERCONFIG_NAME));

         childMBeanMemonto = childServerMemonto.createChild(ICacheConstants.ATTRIBUTE);
         childMBeanMemonto.putString(ICacheConstants.NAME, "ClusterConfig");

         childMBeanMemonto = childMBeanMemonto.createChild(ICacheConstants.CONFIG);
         IMemento childConfigMemento = null;
         childConfigMemento = childMBeanMemonto.createChild(ICacheConstants.UDP);
         childConfigMemento.putString(ICacheConstants.MCAST_ADDR, clusterConfig.getMcast_addrForUdp());
         childConfigMemento.putString(ICacheConstants.MCAST_PORT, clusterConfig.getMcast_portForUdp());
         childConfigMemento.putString(ICacheConstants.IP_TTL, clusterConfig.getIp_ttlForUdp());
         childConfigMemento.putString(ICacheConstants.IP_MCAST, Boolean.toString(clusterConfig.isIp_mcastForUdp()));
         childConfigMemento
               .putString(ICacheConstants.MCAST_SEND_BUF_SIZE, clusterConfig.getMcast_send_buf_sizeForUdp());
         childConfigMemento
               .putString(ICacheConstants.MCAST_RECV_BUF_SIZE, clusterConfig.getMcast_recv_buf_sizeForUdp());
         childConfigMemento
               .putString(ICacheConstants.UCAST_SEND_BUF_SIZE, clusterConfig.getUcast_send_buf_sizeForUdp());
         childConfigMemento
               .putString(ICacheConstants.UCAST_RECV_BUF_SIZE, clusterConfig.getUcast_recv_buf_sizeForUdp());
         childConfigMemento.putString(ICacheConstants.LOOPBACK, Boolean.toString(clusterConfig.isLoopbackForUdp()));

         childConfigMemento = childMBeanMemonto.createChild(ICacheConstants.PING);
         childConfigMemento.putString(ICacheConstants.TIMEOUT, clusterConfig.getTimeoutForPing());
         childConfigMemento.putString(ICacheConstants.NUM_INITIAL_MEMBERS, clusterConfig
               .getNum_initial_membersForPing());
         childConfigMemento.putString(ICacheConstants.UP_THREAD, Boolean.toString(clusterConfig.isUp_threadForPing()));
         childConfigMemento.putString(ICacheConstants.DOWN_THREAD, Boolean.toString(clusterConfig
               .isDown_threadForPing()));

         childConfigMemento = childMBeanMemonto.createChild(ICacheConstants.MERGE2);
         childConfigMemento.putString(ICacheConstants.MIN_INTERVAL, clusterConfig.getMin_intervalForMerge2());
         childConfigMemento.putString(ICacheConstants.MAX_INTERVAL, clusterConfig.getMax_intervalForMerge2());

         monitor.worked(6);

         childConfigMemento = childMBeanMemonto.createChild(ICacheConstants.FD);
         childConfigMemento.putString(ICacheConstants.SHUN, Boolean.toString(clusterConfig.isShunForFd()));
         childConfigMemento.putString(ICacheConstants.UP_THREAD, Boolean.toString(clusterConfig.isUp_threadForFd()));
         childConfigMemento
               .putString(ICacheConstants.DOWN_THREAD, Boolean.toString(clusterConfig.isDown_threadForFd()));

         childConfigMemento = childMBeanMemonto.createChild(ICacheConstants.FD_SOCK);

         childConfigMemento = childMBeanMemonto.createChild(ICacheConstants.VERIFY_SUSPECT);
         childConfigMemento.putString(ICacheConstants.TIMEOUT, clusterConfig.getSectimeoutForVerifySuspect());
         childConfigMemento.putString(ICacheConstants.UP_THREAD, Boolean.toString(clusterConfig
               .isSecup_threadForVerifySuspect()));
         childConfigMemento.putString(ICacheConstants.DOWN_THREAD, Boolean.toString(clusterConfig
               .isSecdown_threadForVerifySuspect()));

         childConfigMemento = childMBeanMemonto.createChild(ICacheConstants.NACKACK);
         childConfigMemento.putString(ICacheConstants.GC_LAG, clusterConfig.getSecgc_lagForPbCastNakAck());
         childConfigMemento.putString(ICacheConstants.RETRANSMIT_TIMEOUT, clusterConfig
               .getSecretransmit_timeoutForPbCastNakAck());
         childConfigMemento
               .putString(ICacheConstants.MAX_XMIT_SIZE, clusterConfig.getSecmax_xmit_sizeForPbCastNakAck());
         childConfigMemento.putString(ICacheConstants.UP_THREAD, Boolean.toString(clusterConfig
               .isSecup_threadForPbCastNakAck()));
         childConfigMemento.putString(ICacheConstants.DOWN_THREAD, Boolean.toString(clusterConfig
               .isSecdown_threadForPbCastNakAck()));

         childConfigMemento = childMBeanMemonto.createChild(ICacheConstants.UNICAST);
         childConfigMemento.putString(ICacheConstants.TIMEOUT, clusterConfig.getSectimeoutForUniCast());
         childConfigMemento.putString(ICacheConstants.WINDOW_SIZE, clusterConfig.getSecwindow_sizeForUniCast());
         childConfigMemento.putString(ICacheConstants.MIN_THRESHOLD, clusterConfig.getSecmin_thresholdForUniCast());
         childConfigMemento.putString(ICacheConstants.DOWN_THREAD, Boolean.toString(clusterConfig
               .isSecdown_threadForUniCast()));

         monitor.worked(7);
         childConfigMemento = childMBeanMemonto.createChild(ICacheConstants.STABLE);
         childConfigMemento.putString(ICacheConstants.DESIRED_AVERAGE_GOSSIP, clusterConfig
               .getSecdesired_avg_gossipForPbCastStable());
         childConfigMemento.putString(ICacheConstants.UP_THREAD, Boolean.toString(clusterConfig
               .isSecup_threadForPbCastStable()));
         childConfigMemento.putString(ICacheConstants.DOWN_THREAD, Boolean.toString(clusterConfig
               .isSecdown_threadForPbCastStable()));

         childConfigMemento = childMBeanMemonto.createChild(ICacheConstants.FRAG);
         childConfigMemento.putString(ICacheConstants.FRAG_SIZE, clusterConfig.getThirdfrag_sizeForFrag());
         childConfigMemento.putString(ICacheConstants.UP_THREAD, Boolean.toString(clusterConfig
               .isThirdup_threadForFrag()));
         childConfigMemento.putString(ICacheConstants.DOWN_THREAD, Boolean.toString(clusterConfig
               .isThirddown_threadForFrag()));

         childConfigMemento = childMBeanMemonto.createChild(ICacheConstants.GMS);
         childConfigMemento.putString(ICacheConstants.JOIN_TIMEOUT, clusterConfig.getThirdjoin_timeoutForPbCastGms());
         childConfigMemento.putString(ICacheConstants.JOIN_RETRY_TIMEOUT, clusterConfig
               .getThirdjoin_retry_timeoutForPbCastGms());
         childConfigMemento.putString(ICacheConstants.SHUN, Boolean.toString(clusterConfig.isThirdshunForPbCastGms()));
         childConfigMemento.putString(ICacheConstants.PRINT_LOCAL_ADDR, Boolean.toString(clusterConfig
               .isThirdprint_local_addrForPbCastGms()));

         childConfigMemento = childMBeanMemonto.createChild(ICacheConstants.STATE_TRANSFER);
         childConfigMemento.putString(ICacheConstants.UP_THREAD, Boolean.toString(clusterConfig
               .isThirdup_threadForPbCastStateTransfer()));
         childConfigMemento.putString(ICacheConstants.DOWN_THREAD, Boolean.toString(clusterConfig
               .isThirddown_threadForPbCastStateTransfer()));

         monitor.worked(8);
         monitor.subTask(ICacheConstants.CONFIGURATION_MONITOR_SUBTASK_CACHE_LOADER_CONFIG_NAME);

         childMBeanMemonto = childServerMemonto.createChild(ICacheConstants.ATTRIBUTE);
         childMBeanMemonto.putString(ICacheConstants.NAME, "FetchStateOnStartup");
         childMBeanMemonto.putTextData(Boolean.toString(cacheConfigModel.isFetchStateOnStartup()));

         childMBeanMemonto = childServerMemonto.createChild(ICacheConstants.ATTRIBUTE);
         childMBeanMemonto.putString(ICacheConstants.NAME, "InitialStateRetrievalTimeout");
         childMBeanMemonto.putTextData(cacheConfigModel.getInitialStateRetrievalTimeOut());

         childMBeanMemonto = childServerMemonto.createChild(ICacheConstants.ATTRIBUTE);
         childMBeanMemonto.putString(ICacheConstants.NAME, "SyncReplTimeout");
         childMBeanMemonto.putTextData(cacheConfigModel.getSyncReplicationTimeOut());

         childMBeanMemonto = childServerMemonto.createChild(ICacheConstants.ATTRIBUTE);
         childMBeanMemonto.putString(ICacheConstants.NAME, "LockAcquisitionTimeout");
         childMBeanMemonto.putTextData(cacheConfigModel.getLockAcquisitionTimeOut());

         monitor.worked(9);

         //			childMBeanMemonto = childServerMemonto.createChild(ICacheConstants.ATTRIBUTE);
         //			childMBeanMemonto.putString(ICacheConstants.NAME,"EvictionPolicyClass");
         //			childMBeanMemonto.putTextData(cacheConfigModel.getEvictionPolicyClass());

         childMBeanMemonto = childServerMemonto.createChild(ICacheConstants.ATTRIBUTE);
         childMBeanMemonto.putString(ICacheConstants.NAME, "UseMarshalling");
         childMBeanMemonto.putTextData(Boolean.toString(cacheConfigModel.isUseMarshalling()));

         if (!cacheConfigModel.getCacheLoaderConfig().getCacheLoaderClass().equals("None"))
         {
            childMBeanMemonto = childServerMemonto.createChild(ICacheConstants.ATTRIBUTE);
            childMBeanMemonto.putString(ICacheConstants.NAME, "CacheLoaderClass");
            childMBeanMemonto.putTextData(cacheConfigModel.getCacheLoaderConfig().getCacheLoaderClass());

            childMBeanMemonto = childServerMemonto.createChild(ICacheConstants.ATTRIBUTE);
            childMBeanMemonto.putString(ICacheConstants.NAME, "CacheLoaderConfig");

            if (cacheLoaderConfig.getCacheLoaderClass().equals(ICacheConstants.CACHE_LOADER_CLASSES[0]))
            {
               if (!cacheLoaderConfig.isUseDataSource())
               {
                  childMBeanMemonto.putTextData("\n\t" + "cache.jdbc.table.name=" + cacheLoaderConfig.getTableName()
                        + "\n" + "\t" + "cache.jdbc.table.create="
                        + Boolean.toString(cacheLoaderConfig.isTableCreate()) + "\n" + "\t" + "cache.jdbc.table.drop="
                        + Boolean.toString(cacheLoaderConfig.isTableDrop()) + "\n" + "\t" + "cache.jdbc.fqn.column="
                        + cacheLoaderConfig.getFqnColumn() + "\n" + "\t" + "cache.jdbc.fqn.type="
                        + cacheLoaderConfig.getFqnType() + "\n" + "\t" + "cache.jdbc.node.column="
                        + cacheLoaderConfig.getNodeColumn() + "\n" + "\t" + "cache.jdbc.node.type="
                        + cacheLoaderConfig.getNodeType() + "\n" + "\t" + "cache.jdbc.parent.column="
                        + cacheLoaderConfig.getParentColumn() + "\n" + "\t" + "cache.jdbc.driver="
                        + cacheLoaderConfig.getDriver() + "\n" + "\t" + "cache.jdbc.url="
                        + cacheLoaderConfig.getDriverUrl() + "\n" + "\t" + "cache.jdbc.user="
                        + cacheLoaderConfig.getUserName() + "\n" + "\t" + "cache.jdbc.password="
                        + cacheLoaderConfig.getPassword() + "\n");
               }
               else
               {
                  StringBuffer buffer = new StringBuffer("\n");
                  childMBeanMemonto.putTextData("cache.jdbc.datasource=" + cacheLoaderConfig.getDataSource());
               }

            }
            else if (cacheLoaderConfig.getCacheLoaderClass().equals(ICacheConstants.CACHE_LOADER_CLASSES[1]) || 
                     cacheLoaderConfig.getCacheLoaderClass().equals(ICacheConstants.CACHE_LOADER_CLASSES[2]))
            {
               childMBeanMemonto.putTextData("location=" + cacheLoaderConfig.getFileLocation());
            }
         }

         childMBeanMemonto = childServerMemonto.createChild(ICacheConstants.ATTRIBUTE);
         childMBeanMemonto.putString(ICacheConstants.NAME, "CacheLoaderShared");
         childMBeanMemonto.putTextData(Boolean.toString(cacheLoaderConfig.isCacheLoaderShared()));

         childMBeanMemonto = childServerMemonto.createChild(ICacheConstants.ATTRIBUTE);
         childMBeanMemonto.putString(ICacheConstants.NAME, "CacheLoaderPreload");
         childMBeanMemonto.putTextData(cacheLoaderConfig.getCacheLoaderPreload());

         childMBeanMemonto = childServerMemonto.createChild(ICacheConstants.ATTRIBUTE);
         childMBeanMemonto.putString(ICacheConstants.NAME, "CacheLoaderPassivation");
         childMBeanMemonto.putTextData(Boolean.toString(cacheLoaderConfig.isCacheLoaderPassivation()));

         childMBeanMemonto = childServerMemonto.createChild(ICacheConstants.ATTRIBUTE);
         childMBeanMemonto.putString(ICacheConstants.NAME, "CacheLoaderFetchPersistentState");
         childMBeanMemonto.putTextData(Boolean.toString(cacheLoaderConfig.isCacheLoaderFetchPersistentState()));

         childMBeanMemonto = childServerMemonto.createChild(ICacheConstants.ATTRIBUTE);
         childMBeanMemonto.putString(ICacheConstants.NAME, "CacheLoaderFetchTransientState");
         childMBeanMemonto.putTextData(Boolean.toString(cacheLoaderConfig.isCacheLoaderFetchTransientState()));

         childMBeanMemonto = childServerMemonto.createChild(ICacheConstants.ATTRIBUTE);
         childMBeanMemonto.putString(ICacheConstants.NAME, "CacheLoaderAsynchronous");
         childMBeanMemonto.putTextData(Boolean.toString(cacheLoaderConfig.isCacheLoaderAsynchronous()));

         memonto.save(writer);

         monitor.worked(10);

      }
      catch (Exception e)
      {
         IStatus status = new Status(IStatus.ERROR, ICacheConstants.CACHE_PLUGIN_UNIQUE_ID, IStatus.OK, e.getMessage(),
               e);
         JBossCachePlugin.getDefault().getLog().log(status);
      }
      finally
      {
         try
         {
            if (writer != null)
               writer.close();
         }
         catch (Exception e)
         {
            e.printStackTrace();
            throw e;
         }

      }
   }//end of method

   //	public static void createReat() throws Exception{
   //		FileReader reader = new FileReader(createNewFile("cemil.xml"));
   //		XMLMemento memonto = XMLMemento.createReadRoot(reader);
   //		IMemento [] memento = memonto.getChild("mbean").getChildren("attribute");
   //		for(int i=0;i<memento.length;i++)
   //			System.out.println(memento[i].getTextData());
   //		reader.close();
   //	}

   /**
    * Return the value according to the given key from the resource bundle
    * @param key
    * @return
    */
   public static String getResourceBundleValue(String key)
   {
      if (key != null)
      {
         ResourceBundle rb = JBossCachePlugin.getResourceBundle();
         return rb.getString(key);
      }
      else
         return null;
   }

   /**
    * Find the related ICacheInstance from the model
    * @param fqn
    * @return
    */
   public static Object findCacheInstanceWithFqn(Fqn fqn, ICacheRootInstance rootInstance)
   {

      StringTokenizer tokenize = null;
      String name = null;
      StringBuffer parentName = new StringBuffer();
      ICacheInstance cacheInstance = null;
      List instanceChilds = rootInstance.getRootChilds();
      tokenize = new StringTokenizer(fqn.toString(), ICacheConstants.SEPERATOR);
      if (tokenize != null)
      {
         int tokenizeNumber = tokenize.countTokens();
         /*This is root node*/
         if (tokenizeNumber == 1)
         {
            return rootInstance;
         }
         else
         {
            for (int i = 0; i < tokenizeNumber - 1; i++)
            {
               parentName.append(ICacheConstants.SEPERATOR).append(tokenize.nextToken());
            }

            name = tokenize.nextToken();
            Iterator it = instanceChilds.iterator();
            try
            {
               while (it.hasNext())
               {
                  ICacheInstance childInstance = (ICacheInstance) it.next();

                  if (CacheUtil.findParentOfChild(childInstance, parentName.toString()) == null)
                     continue;
                  else
                  {
                     cacheInstance = CacheUtil.findParentOfChild(childInstance, parentName.toString());
                     break;
                  }
               }
            }
            catch (Exception e)
            {
               IStatus status = new Status(Status.ERROR, ICacheConstants.CACHE_PLUGIN_UNIQUE_ID, Status.OK, e
                     .getMessage(), e);
               JBossCachePlugin.getDefault().getLog().log(status);
            }
         }//end of else						
      }//end of if tokenize != null									
      return cacheInstance;
   }//end of method

   /**
    * Return ICacheInstance with this fqn under the parentInstance(1. parentInstance is in search)
    * @param parentInstance
    * @param parentName
    * @param name
    * @return
    * @throws Exception
    */
   public static ICacheInstance findParentOfChild(ICacheInstance parentInstance, String parentName)
   {
      ICacheInstance searchChild = null;
      List insChildsList = parentInstance.getInstanceChilds();
      Iterator itChilds = null;
      if (insChildsList != null)
         itChilds = insChildsList.iterator();

      /*Found parent*/
      if (parentInstance.getFqnName().equals(parentName.toString()))
      {
         searchChild = parentInstance;
         return searchChild;
      }
      else
      {
         if (insChildsList != null)
         {
            while (itChilds.hasNext())
            {
               ICacheInstance childInstance = (ICacheInstance) itChilds.next();
               searchChild = findParentOfChild(childInstance, parentName);
               if (searchChild == null)
                  continue;
               else
                  break;
            }
         }
         else
            searchChild = null;
      }

      return searchChild;
   }//end of method

   /**
    * Object type control
    * @param clazz
    * @return
    */
   public static boolean isImmediate(Class clazz)
   {
      return immediates.contains(clazz);
   }

   public static boolean isCollection(Class clazz)
   {
      if (clazz.isAssignableFrom(Map.class) || clazz.isAssignableFrom(List.class) || clazz.isAssignableFrom(Set.class))
         return true;
      return false;
   }

   /**
    * Get all fields
    * @return
    */
   public static Map getAllFields(Class clazz)
   {
      Map fields = null;

      if (clazz != null)
      {
         Field[] clazzFieleds = clazz.getDeclaredFields();
         for (int i = 0; i < clazzFieleds.length; i++)
         {
            if (fields == null)
               fields = new HashMap();

            Field field = clazzFieleds[i];
            field.setAccessible(true);

            if (isNonReplicatable(field))
               continue;
            fields.put(field.getName(), field);
         }
      }

      return fields;
   }

   public static boolean isNonReplicatable(Field f)
   {
      int mods = f.getModifiers();
      /**
       * The following modifiers are ignored in the cache, i.e., they will not be stored in the cache.
       * Whenever, user trying to access these fields, it will be accessed from the in-memory version.
       */
      if (Modifier.isStatic(mods) || Modifier.isTransient(mods) || Modifier.isFinal(mods))
      {
         return true;
      }
      return false;
   }
   
   public static boolean isCollectionAssignable(Class clazz){
      if(clazz.isAssignableFrom(Map.class) || clazz.isAssignableFrom(List.class) || clazz.isAssignableFrom(Set.class) ||
            clazz.isAssignableFrom(HashMap.class) || clazz.isAssignableFrom(ArrayList.class) || clazz.isAssignableFrom(HashSet.class))
         return true;
      return false;
   }
   
   public static boolean checkCacheName(String cacheName)
   {
      ICacheRootInstance mainRootInstance = CacheInstanceFactory.getCacheRootMainInstance();
      if (mainRootInstance != null)
      {
         if (mainRootInstance.getRootInstanceChilds() != null)
         {
            List childList = mainRootInstance.getRootInstanceChilds();
            for (int i = 0, j = childList.size(); i < j; i++)
            {
               ICacheRootInstance rootInstance = (ICacheRootInstance) childList.get(i);
               if (rootInstance.getRootName().equals(cacheName))
               {
                  return false;
               }
            }
         }
      }
      return true;
   }


}//end of class