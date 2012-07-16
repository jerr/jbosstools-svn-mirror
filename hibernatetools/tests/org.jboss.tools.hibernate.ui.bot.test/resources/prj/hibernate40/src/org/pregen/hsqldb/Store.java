package org.pregen.hsqldb;

// Generated Jul 16, 2012 4:51:41 PM by Hibernate Tools 4.0.0

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Store generated by hbm2java
 */
public class Store implements java.io.Serializable {

	private byte storeId;
	private Staff staff;
	private Address address;
	private Date lastUpdate;
	private Set customers = new HashSet(0);
	private Set staffs = new HashSet(0);
	private Set inventories = new HashSet(0);

	public Store() {
	}

	public Store(byte storeId, Staff staff, Address address, Date lastUpdate) {
		this.storeId = storeId;
		this.staff = staff;
		this.address = address;
		this.lastUpdate = lastUpdate;
	}

	public Store(byte storeId, Staff staff, Address address, Date lastUpdate,
			Set customers, Set staffs, Set inventories) {
		this.storeId = storeId;
		this.staff = staff;
		this.address = address;
		this.lastUpdate = lastUpdate;
		this.customers = customers;
		this.staffs = staffs;
		this.inventories = inventories;
	}

	public byte getStoreId() {
		return this.storeId;
	}

	public void setStoreId(byte storeId) {
		this.storeId = storeId;
	}

	public Staff getStaff() {
		return this.staff;
	}

	public void setStaff(Staff staff) {
		this.staff = staff;
	}

	public Address getAddress() {
		return this.address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Date getLastUpdate() {
		return this.lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	public Set getCustomers() {
		return this.customers;
	}

	public void setCustomers(Set customers) {
		this.customers = customers;
	}

	public Set getStaffs() {
		return this.staffs;
	}

	public void setStaffs(Set staffs) {
		this.staffs = staffs;
	}

	public Set getInventories() {
		return this.inventories;
	}

	public void setInventories(Set inventories) {
		this.inventories = inventories;
	}

}
