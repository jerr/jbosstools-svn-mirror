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
package org.hibernate.console;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

import org.hibernate.Hibernate;
import org.hibernate.mapping.Table;
import org.hibernate.type.NullableType;


public class ConsoleQueryParameter {

	static public final Object NULL_MARKER = new Object() { public String toString() { return "[null]"; } };
	
	static final Map typeFormats = new HashMap();
	static {
		addTypeFormat(Hibernate.BOOLEAN, Boolean.TRUE );
		addTypeFormat(Hibernate.BYTE, new Byte((byte) 42));
		addTypeFormat(Hibernate.BIG_INTEGER, BigInteger.valueOf(42));
		addTypeFormat(Hibernate.SHORT, new Short((short) 42));
		addTypeFormat(Hibernate.CALENDAR, new GregorianCalendar());
		addTypeFormat(Hibernate.CALENDAR_DATE, new GregorianCalendar());
		addTypeFormat(Hibernate.INTEGER, new Integer(42));
		addTypeFormat(Hibernate.INTEGER, new Integer(42));
		addTypeFormat(Hibernate.BIG_DECIMAL, new BigDecimal(42.0));
		addTypeFormat(Hibernate.CHARACTER, new Character('h'));
		addTypeFormat(Hibernate.CLASS, Table.class);
		addTypeFormat(Hibernate.CURRENCY, Currency.getInstance(Locale.getDefault()));
		addTypeFormat(Hibernate.DATE, new Date());
		addTypeFormat(Hibernate.DOUBLE, new Double(42.42));
		addTypeFormat(Hibernate.FLOAT, new Float(42.42));
		addTypeFormat(Hibernate.LOCALE, Locale.getDefault());
		addTypeFormat(Hibernate.LONG, new Long(42));
		addTypeFormat(Hibernate.STRING, "a string");
		addTypeFormat(Hibernate.TEXT, "a text");
		addTypeFormat(Hibernate.TIME, new Date());
		addTypeFormat(Hibernate.TIMESTAMP, new Date());
		addTypeFormat(Hibernate.TIMEZONE, TimeZone.getDefault());
		addTypeFormat(Hibernate.TRUE_FALSE, Boolean.TRUE);
		addTypeFormat(Hibernate.YES_NO, Boolean.TRUE);
	}


	private static void addTypeFormat(NullableType nullableType, Object value) {
		typeFormats.put(nullableType, nullableType.toString(value));
	}
	String name;
	NullableType type;
	Object value;
	
	public ConsoleQueryParameter(ConsoleQueryParameter cqp) {
		name = cqp.name;
		type = cqp.type;
		value = cqp.value;
	}

	public ConsoleQueryParameter() {
		
	}

	public ConsoleQueryParameter(String name, NullableType type, Object value) {
		this.name = name;
		this.type = type;
		this.value = value;
	}

	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public NullableType getType() {
		return type;
	}
	
	public void setType(NullableType type) {
		this.type = type;
	}
	
	public Object getValue() {
		return value;
	}
	
	public void setValue(Object value) {
		if(value == null) { throw new IllegalArgumentException("Value must not be set to null"); }
		this.value = value;
	}
	
	public String getValueAsString() {
		if(getValue()==NULL_MARKER) return "";
		return type.toString(getValue());
	}
	
	public void setValueFromString(String value) {
		try {
			Object object = type.fromStringValue(value);
			setValue(object);
		} catch(Exception he) {
			setValue(NULL_MARKER);
		}
	}

	
	public String getDefaultFormat() {
		if(type!=null) {
			Object object = typeFormats.get(type);
			if(object!=null) {
				return object.toString();
			}
		}
		return "<unknown>";				
	}

	public static Set getPossibleTypes() {
		return typeFormats.keySet();
	}
}
