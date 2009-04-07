/*******************************************************************************
 * Copyright (c) 2007 Red Hat, Inc.
 * Distributed under license by Red Hat, Inc. All rights reserved.
 * This program is made available under the terms of the
 * Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributor:
 *     Red Hat, Inc. - initial API and implementation
 ******************************************************************************/
package org.jboss.tools.hibernate.ui.view.views;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.cfg.Environment;
import org.hibernate.console.ConsoleConfiguration;
import org.hibernate.console.execution.ExecutionContext.Command;
import org.hibernate.dialect.Dialect;
import org.hibernate.eclipse.console.workbench.TypeNameValueVisitor;
import org.hibernate.engine.Mapping;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.DependantValue;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.RootClass;
import org.hibernate.mapping.SimpleValue;
import org.hibernate.mapping.Subclass;
import org.hibernate.mapping.Table;
import org.hibernate.mapping.Value;
import org.jboss.tools.hibernate.ui.view.ViewPlugin;

public class OrmModelNameVisitor /*implements IOrmModelVisitor*/ {
	
	static private String SPACE = " "; //$NON-NLS-1$
	static private String POINTER = " -> "; //$NON-NLS-1$
	
	public OrmModelNameVisitor() {
		super();
	}
	
	private Map mappings = new HashMap();
	private Map dialects = new HashMap();

	public Object visitDatabaseColumn(Column column, Object argument) {

		String type = getColumnSqlType(column, argument);

		StringBuffer name = new StringBuffer();
		name.append(column.getName());

		if (type != null) {
			name.append(" ["); //$NON-NLS-1$
			name.append(type.toUpperCase());
			name.append(column.isNullable() ? " Nullable" : ""); //$NON-NLS-1$ //$NON-NLS-2$
			name.append(HibernateUtils.getTable(column) != null
					&& HibernateUtils.isPrimaryKey(column) ? " PK" : ""); //$NON-NLS-1$ //$NON-NLS-2$
			name.append(HibernateUtils.getTable(column) != null
					&& HibernateUtils.isForeignKey(column) ? " FK" : ""); //$NON-NLS-1$ //$NON-NLS-2$
			name.append("]"); //$NON-NLS-1$
		}

		return name.toString();

	}
	
	public String getColumnSqlType(final Column column, Object argument) {
		
		ConsoleConfiguration cfg = null;
		Mapping mapping = null;
		Dialect dialect = null;
		
		String type = null;

		if (argument instanceof ConsoleConfiguration) {
			
			cfg = (ConsoleConfiguration) argument;
			
			if (mappings.containsKey(cfg.getConfiguration())) {
				mapping = (Mapping) mappings.get(cfg);
			} else {
				mapping = cfg.getConfiguration().buildMapping();
				mappings.put(cfg, mapping);
			}

			try {
				String dialectName = cfg.getConfiguration().getProperty(Environment.DIALECT);
				if (dialectName != null) {
					if (dialects.containsKey(dialectName)) {
						dialect = (Dialect) dialects.get(dialectName);
					} else {
						dialect = (Dialect) Class.forName(dialectName).newInstance();
						dialects.put(dialectName, dialect);
					}
				}
			} catch (HibernateException e) {
				ViewPlugin.getDefault().logError(e);
			} catch (InstantiationException e) {
				ViewPlugin.getDefault().logError(e);
			} catch (IllegalAccessException e) {
				ViewPlugin.getDefault().logError(e);
			} catch (ClassNotFoundException e) {
				ViewPlugin.getDefault().logError(e);
			}
			
			final Mapping fMapping = mapping;
			final Dialect fDialect = dialect;
			
			try {	
			type = (String)cfg.execute( new Command() {
				public Object execute() {
					return column.getSqlType(fDialect, fMapping);
				}});
			} catch(Exception e){
			}
		}
		
		//if (type != null) {
			return type;
		/*} else {
			if (column.getValue() instanceof SimpleValue) {
				SimpleValue sValue = (SimpleValue) column.getValue();
				Properties p = sValue.getTypeParameters();
				if (p == null)
					return null;
				String propType = p.getProperty( "type" );
				if ( propType != null ) {
					int sqlType = Integer.decode( propType ).intValue();
					return dialect.getTypeName( sqlType, column.getLength(), column.getPrecision(), column.getScale() );
				}
			} 
			return null;
		}	*/	
	}

	public Object visitPersistentClass(RootClass clazz, Object argument) {

		StringBuffer name = new StringBuffer();
		name.append(clazz.getEntityName() != null ? clazz.getEntityName() : clazz.getClassName());

		Table table = clazz.getTable(); // upd tau 06.06.2005
		if (table != null) {
			String tableName = HibernateUtils.getTableName(table);
			if (tableName != null) {
				name.append(POINTER);
				name.append(tableName);
			}
		}

		return name.toString();
	}

	public Object visitTable(Table table, Object argument) {
		StringBuffer name = new StringBuffer();
		name.append(HibernateUtils.getTableName(table));
		return name.toString();
	}

	public Object visitPersistentClass(Subclass clazz, Object argument) {

		StringBuffer name = new StringBuffer();
		name.append(clazz.getEntityName());

		Table table = clazz.getTable();
		if (table != null) {
			String tableName = HibernateUtils.getTableName(table);
			if (tableName != null) {
				name.append(POINTER);
				name.append(tableName);
			}
		}

		return name.toString();
	}

	public Object visitPersistentField(Property field, Object argument) {
		StringBuffer name = new StringBuffer();
		name.append(field.getName());
		name.append(" "); //$NON-NLS-1$
		name.append(Messages.getString("OrmModelNameVisitor.Colon")); //$NON-NLS-1$
		String typeString = null;
		
		try {
			typeString = field.getType().getReturnedClass().getName();
		} catch (Exception e) {
			if (field.getValue() instanceof Component) {
				typeString = ((Component)field.getValue()).getComponentClassName();
			} else if (field.getValue()!= null && field.getValue().isSimpleValue()) {
				typeString = ((SimpleValue)field.getValue()).getTypeName();
			}
		}
				
		if (typeString != null) {
			typeString = correctTypeString(typeString);
			name.append(SPACE);
			name.append(typeString);
			return name.toString();
		}
		
		Value value = field.getValue();
		String typeName = null;
		if (value != null){
			typeName = (String) value.accept(new TypeNameValueVisitor(false));
			if (typeName!=null) {
				return field.getName() + " : " + typeName; //$NON-NLS-1$
			}	
		}			
		return field.getName(); 
	}

	private String correctTypeString(String str) {
		String ret = str;
		while (ret.startsWith("[")) { //$NON-NLS-1$
			ret = ret.substring(1).concat("[]"); //$NON-NLS-1$
		}
		switch (ret.toCharArray()[0]) {
		case 'Z': ret = "boolean".concat(ret.substring(1));break; //$NON-NLS-1$
		case 'B': ret = "byte".concat(ret.substring(1));break; //$NON-NLS-1$
		case 'C': ret = "char".concat(ret.substring(1));break; //$NON-NLS-1$
		case 'L': ret = ret.substring(1);break;
		case 'D': ret = "double".concat(ret.substring(1));break; //$NON-NLS-1$
		case 'F': ret = "float".concat(ret.substring(1));break; //$NON-NLS-1$
		case 'I': ret = "int".concat(ret.substring(1));break; //$NON-NLS-1$
		case 'J': ret = "long".concat(ret.substring(1));break; //$NON-NLS-1$
		case 'S': ret = "short".concat(ret.substring(1));break; //$NON-NLS-1$
		}
		return ret;
	}

	public Object visitCollectionKeyMapping(DependantValue mapping,	Object argument) {
		return "key"; //$NON-NLS-1$
	}

	public Object visitComponentMapping(Component mapping,	Object argument) {
		return "element"; //$NON-NLS-1$
	}
}