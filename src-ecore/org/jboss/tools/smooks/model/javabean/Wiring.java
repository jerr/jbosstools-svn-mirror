/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package org.jboss.tools.smooks.model.javabean;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Wiring</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.Wiring#getProperty <em>Property</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.Wiring#getSetterMethod <em>Setter Method</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.Wiring#getBeanIdRef <em>Bean Id Ref</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.Wiring#getWireOnElement <em>Wire On Element</em>}</li>
 *   <li>{@link org.jboss.tools.smooks.model.javabean.Wiring#getWireOnElementNS <em>Wire On Element NS</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class Wiring extends EObjectImpl implements IWiring {
	/**
	 * The default value of the '{@link #getProperty() <em>Property</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProperty()
	 * @generated
	 * @ordered
	 */
	protected static final String PROPERTY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getProperty() <em>Property</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProperty()
	 * @generated
	 * @ordered
	 */
	protected String property = PROPERTY_EDEFAULT;

	/**
	 * The default value of the '{@link #getSetterMethod() <em>Setter Method</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSetterMethod()
	 * @generated
	 * @ordered
	 */
	protected static final String SETTER_METHOD_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSetterMethod() <em>Setter Method</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSetterMethod()
	 * @generated
	 * @ordered
	 */
	protected String setterMethod = SETTER_METHOD_EDEFAULT;

	/**
	 * The default value of the '{@link #getBeanIdRef() <em>Bean Id Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeanIdRef()
	 * @generated
	 * @ordered
	 */
	protected static final String BEAN_ID_REF_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getBeanIdRef() <em>Bean Id Ref</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBeanIdRef()
	 * @generated
	 * @ordered
	 */
	protected String beanIdRef = BEAN_ID_REF_EDEFAULT;

	/**
	 * The default value of the '{@link #getWireOnElement() <em>Wire On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWireOnElement()
	 * @generated
	 * @ordered
	 */
	protected static final String WIRE_ON_ELEMENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getWireOnElement() <em>Wire On Element</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWireOnElement()
	 * @generated
	 * @ordered
	 */
	protected String wireOnElement = WIRE_ON_ELEMENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getWireOnElementNS() <em>Wire On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWireOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected static final String WIRE_ON_ELEMENT_NS_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getWireOnElementNS() <em>Wire On Element NS</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWireOnElementNS()
	 * @generated
	 * @ordered
	 */
	protected String wireOnElementNS = WIRE_ON_ELEMENT_NS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Wiring() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return IJavaBeanPackage.Literals.WIRING;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setProperty(String newProperty) {
		String oldProperty = property;
		property = newProperty;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IJavaBeanPackage.WIRING__PROPERTY, oldProperty, property));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getSetterMethod() {
		return setterMethod;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSetterMethod(String newSetterMethod) {
		String oldSetterMethod = setterMethod;
		setterMethod = newSetterMethod;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IJavaBeanPackage.WIRING__SETTER_METHOD, oldSetterMethod, setterMethod));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getBeanIdRef() {
		return beanIdRef;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setBeanIdRef(String newBeanIdRef) {
		String oldBeanIdRef = beanIdRef;
		beanIdRef = newBeanIdRef;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IJavaBeanPackage.WIRING__BEAN_ID_REF, oldBeanIdRef, beanIdRef));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getWireOnElement() {
		return wireOnElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWireOnElement(String newWireOnElement) {
		String oldWireOnElement = wireOnElement;
		wireOnElement = newWireOnElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IJavaBeanPackage.WIRING__WIRE_ON_ELEMENT, oldWireOnElement, wireOnElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getWireOnElementNS() {
		return wireOnElementNS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setWireOnElementNS(String newWireOnElementNS) {
		String oldWireOnElementNS = wireOnElementNS;
		wireOnElementNS = newWireOnElementNS;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IJavaBeanPackage.WIRING__WIRE_ON_ELEMENT_NS, oldWireOnElementNS, wireOnElementNS));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case IJavaBeanPackage.WIRING__PROPERTY:
				return getProperty();
			case IJavaBeanPackage.WIRING__SETTER_METHOD:
				return getSetterMethod();
			case IJavaBeanPackage.WIRING__BEAN_ID_REF:
				return getBeanIdRef();
			case IJavaBeanPackage.WIRING__WIRE_ON_ELEMENT:
				return getWireOnElement();
			case IJavaBeanPackage.WIRING__WIRE_ON_ELEMENT_NS:
				return getWireOnElementNS();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case IJavaBeanPackage.WIRING__PROPERTY:
				setProperty((String)newValue);
				return;
			case IJavaBeanPackage.WIRING__SETTER_METHOD:
				setSetterMethod((String)newValue);
				return;
			case IJavaBeanPackage.WIRING__BEAN_ID_REF:
				setBeanIdRef((String)newValue);
				return;
			case IJavaBeanPackage.WIRING__WIRE_ON_ELEMENT:
				setWireOnElement((String)newValue);
				return;
			case IJavaBeanPackage.WIRING__WIRE_ON_ELEMENT_NS:
				setWireOnElementNS((String)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case IJavaBeanPackage.WIRING__PROPERTY:
				setProperty(PROPERTY_EDEFAULT);
				return;
			case IJavaBeanPackage.WIRING__SETTER_METHOD:
				setSetterMethod(SETTER_METHOD_EDEFAULT);
				return;
			case IJavaBeanPackage.WIRING__BEAN_ID_REF:
				setBeanIdRef(BEAN_ID_REF_EDEFAULT);
				return;
			case IJavaBeanPackage.WIRING__WIRE_ON_ELEMENT:
				setWireOnElement(WIRE_ON_ELEMENT_EDEFAULT);
				return;
			case IJavaBeanPackage.WIRING__WIRE_ON_ELEMENT_NS:
				setWireOnElementNS(WIRE_ON_ELEMENT_NS_EDEFAULT);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case IJavaBeanPackage.WIRING__PROPERTY:
				return PROPERTY_EDEFAULT == null ? property != null : !PROPERTY_EDEFAULT.equals(property);
			case IJavaBeanPackage.WIRING__SETTER_METHOD:
				return SETTER_METHOD_EDEFAULT == null ? setterMethod != null : !SETTER_METHOD_EDEFAULT.equals(setterMethod);
			case IJavaBeanPackage.WIRING__BEAN_ID_REF:
				return BEAN_ID_REF_EDEFAULT == null ? beanIdRef != null : !BEAN_ID_REF_EDEFAULT.equals(beanIdRef);
			case IJavaBeanPackage.WIRING__WIRE_ON_ELEMENT:
				return WIRE_ON_ELEMENT_EDEFAULT == null ? wireOnElement != null : !WIRE_ON_ELEMENT_EDEFAULT.equals(wireOnElement);
			case IJavaBeanPackage.WIRING__WIRE_ON_ELEMENT_NS:
				return WIRE_ON_ELEMENT_NS_EDEFAULT == null ? wireOnElementNS != null : !WIRE_ON_ELEMENT_NS_EDEFAULT.equals(wireOnElementNS);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (property: ");
		result.append(property);
		result.append(", setterMethod: ");
		result.append(setterMethod);
		result.append(", beanIdRef: ");
		result.append(beanIdRef);
		result.append(", wireOnElement: ");
		result.append(wireOnElement);
		result.append(", wireOnElementNS: ");
		result.append(wireOnElementNS);
		result.append(')');
		return result.toString();
	}

} //Wiring
