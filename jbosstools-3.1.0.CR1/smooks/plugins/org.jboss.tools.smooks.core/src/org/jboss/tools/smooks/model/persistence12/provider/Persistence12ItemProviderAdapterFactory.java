/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.persistence12.provider;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import org.jboss.tools.smooks.model.persistence12.util.Persistence12AdapterFactory;


/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class Persistence12ItemProviderAdapterFactory extends Persistence12AdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {
	/**
	 * This keeps track of the root adapter factory that delegates to this adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComposedAdapterFactory parentAdapterFactory;

	/**
	 * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IChangeNotifier changeNotifier = new ChangeNotifier();

	/**
	 * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Collection supportedTypes = new ArrayList();

	/**
	 * This constructs an instance.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Persistence12ItemProviderAdapterFactory() {
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
		supportedTypes.add(IItemPropertySource.class);
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.jboss.tools.smooks.model.persistence12.DecoderParameter} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DecoderParameterItemProvider decoderParameterItemProvider;

	/**
	 * This creates an adapter for a {@link org.jboss.tools.smooks.model.persistence12.DecoderParameter}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createDecoderParameterAdapter() {
		if (decoderParameterItemProvider == null) {
			decoderParameterItemProvider = new DecoderParameterItemProvider(this);
		}

		return decoderParameterItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.jboss.tools.smooks.model.persistence12.Deleter} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DeleterItemProvider deleterItemProvider;

	/**
	 * This creates an adapter for a {@link org.jboss.tools.smooks.model.persistence12.Deleter}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createDeleterAdapter() {
		if (deleterItemProvider == null) {
			deleterItemProvider = new DeleterItemProvider(this);
		}

		return deleterItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Persistence12DocumentRootItemProvider persistence12DocumentRootItemProvider;

	/**
	 * This creates an adapter for a {@link org.jboss.tools.smooks.model.persistence12.Persistence12DocumentRoot}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createPersistence12DocumentRootAdapter() {
		if (persistence12DocumentRootItemProvider == null) {
			persistence12DocumentRootItemProvider = new Persistence12DocumentRootItemProvider(this);
		}

		return persistence12DocumentRootItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.jboss.tools.smooks.model.persistence12.ExpressionParameter} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ExpressionParameterItemProvider expressionParameterItemProvider;

	/**
	 * This creates an adapter for a {@link org.jboss.tools.smooks.model.persistence12.ExpressionParameter}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createExpressionParameterAdapter() {
		if (expressionParameterItemProvider == null) {
			expressionParameterItemProvider = new ExpressionParameterItemProvider(this);
		}

		return expressionParameterItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.jboss.tools.smooks.model.persistence12.Flusher} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FlusherItemProvider flusherItemProvider;

	/**
	 * This creates an adapter for a {@link org.jboss.tools.smooks.model.persistence12.Flusher}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createFlusherAdapter() {
		if (flusherItemProvider == null) {
			flusherItemProvider = new FlusherItemProvider(this);
		}

		return flusherItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.jboss.tools.smooks.model.persistence12.Inserter} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InserterItemProvider inserterItemProvider;

	/**
	 * This creates an adapter for a {@link org.jboss.tools.smooks.model.persistence12.Inserter}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createInserterAdapter() {
		if (inserterItemProvider == null) {
			inserterItemProvider = new InserterItemProvider(this);
		}

		return inserterItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.jboss.tools.smooks.model.persistence12.Locator} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected LocatorItemProvider locatorItemProvider;

	/**
	 * This creates an adapter for a {@link org.jboss.tools.smooks.model.persistence12.Locator}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createLocatorAdapter() {
		if (locatorItemProvider == null) {
			locatorItemProvider = new LocatorItemProvider(this);
		}

		return locatorItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.jboss.tools.smooks.model.persistence12.Parameters} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ParametersItemProvider parametersItemProvider;

	/**
	 * This creates an adapter for a {@link org.jboss.tools.smooks.model.persistence12.Parameters}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createParametersAdapter() {
		if (parametersItemProvider == null) {
			parametersItemProvider = new ParametersItemProvider(this);
		}

		return parametersItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.jboss.tools.smooks.model.persistence12.Updater} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected UpdaterItemProvider updaterItemProvider;

	/**
	 * This creates an adapter for a {@link org.jboss.tools.smooks.model.persistence12.Updater}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createUpdaterAdapter() {
		if (updaterItemProvider == null) {
			updaterItemProvider = new UpdaterItemProvider(this);
		}

		return updaterItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.jboss.tools.smooks.model.persistence12.ValueParameter} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ValueParameterItemProvider valueParameterItemProvider;

	/**
	 * This creates an adapter for a {@link org.jboss.tools.smooks.model.persistence12.ValueParameter}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createValueParameterAdapter() {
		if (valueParameterItemProvider == null) {
			valueParameterItemProvider = new ValueParameterItemProvider(this);
		}

		return valueParameterItemProvider;
	}

	/**
	 * This keeps track of the one adapter used for all {@link org.jboss.tools.smooks.model.persistence12.WiringParameter} instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected WiringParameterItemProvider wiringParameterItemProvider;

	/**
	 * This creates an adapter for a {@link org.jboss.tools.smooks.model.persistence12.WiringParameter}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter createWiringParameterAdapter() {
		if (wiringParameterItemProvider == null) {
			wiringParameterItemProvider = new WiringParameterItemProvider(this);
		}

		return wiringParameterItemProvider;
	}

	/**
	 * This returns the root adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComposeableAdapterFactory getRootAdapterFactory() {
		return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
	}

	/**
	 * This sets the composed adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
		this.parentAdapterFactory = parentAdapterFactory;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public boolean isFactoryForType(Object type) {
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}

	/**
	 * This implementation substitutes the factory itself as the key for the adapter.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Adapter adapt(Notifier notifier, Object type) {
		return super.adapt(notifier, this);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Object adapt(Object object, Object type) {
		if (isFactoryForType(type)) {
			Object adapter = super.adapt(object, type);
			if (!(type instanceof Class) || (((Class)type).isInstance(adapter))) {
				return adapter;
			}
		}

		return null;
	}

	/**
	 * This adds a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void addListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.addListener(notifyChangedListener);
	}

	/**
	 * This removes a listener.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void removeListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.removeListener(notifyChangedListener);
	}

	/**
	 * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void fireNotifyChanged(Notification notification) {
		changeNotifier.fireNotifyChanged(notification);

		if (parentAdapterFactory != null) {
			parentAdapterFactory.fireNotifyChanged(notification);
		}
	}

	/**
	 * This disposes all of the item providers created by this factory. 
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void dispose() {
		if (decoderParameterItemProvider != null) decoderParameterItemProvider.dispose();
		if (deleterItemProvider != null) deleterItemProvider.dispose();
		if (persistence12DocumentRootItemProvider != null) persistence12DocumentRootItemProvider.dispose();
		if (expressionParameterItemProvider != null) expressionParameterItemProvider.dispose();
		if (flusherItemProvider != null) flusherItemProvider.dispose();
		if (inserterItemProvider != null) inserterItemProvider.dispose();
		if (locatorItemProvider != null) locatorItemProvider.dispose();
		if (parametersItemProvider != null) parametersItemProvider.dispose();
		if (updaterItemProvider != null) updaterItemProvider.dispose();
		if (valueParameterItemProvider != null) valueParameterItemProvider.dispose();
		if (wiringParameterItemProvider != null) wiringParameterItemProvider.dispose();
	}

}
