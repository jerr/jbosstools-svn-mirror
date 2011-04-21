/*
 * Created on 27-09-2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.hibernate.console;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.engine.SessionImplementor;

import bsh.EvalError;
import bsh.Interpreter;

/**
 * @author MAX
 *
 * 
 */
public class JavaPage extends AbstractQueryPage {

    
    private String criteriaCode;    

    Criteria criteria = null;

    private Interpreter ip;
    
    /**
     * @param session2
     */
    public JavaPage(ConsoleConfiguration cfg, String criteriaCode) {
		super(cfg);
        this.criteriaCode =  criteriaCode;
    }

    public void setSession(Session s) {
		super.setSession(s);
        try {        
            ip = setupInterpreter(getSession() );
            Object o =  ip.eval(criteriaCode);
            // ugly! TODO: make un-ugly!
            if(o instanceof Criteria) {
                criteria = (Criteria) o;
            } if (o instanceof List) {
                list = (List) o;
            } 
            else {
                list = new ArrayList();
                list.add(o);   
            }                                                   
        } 
        catch (EvalError e) {
            addException(e);            
        } 
        catch (HibernateException e) {
        	addException(e);            
        }                    
	}
    
    private Interpreter setupInterpreter(Session session) throws EvalError, HibernateException {
        Interpreter interpreter = new Interpreter();
        interpreter.set("session", session);
        
        SessionImplementor si = (SessionImplementor)session;
        Map map = si.getFactory().getAllClassMetadata();
        
        Iterator iterator = map.keySet().iterator();
        
        String imports = new String();
        while (iterator.hasNext() ) {
            String element =  (String) iterator.next();
            imports += "import " + element + ";\n";
        }
        
        imports += "import org.hibernate.criterion.*;\n";
        imports += "import org.hibernate.*;\n";
        
        interpreter.eval(imports);
        
        return interpreter;
    }

    public List getList() {
        if(list!=null) return list;
        try {
            if(criteria!=null) {
                list = criteria.list();
            } 
            else {
                return Collections.EMPTY_LIST;
            }
        } 
        catch (HibernateException e) {
            addException(e);
        } 
        return list;
    }

    public List getPathNames() {
        List l = new ArrayList();
        l.add("<no info>");       
        return l;
    }

    public String getQueryString() {
        return criteriaCode;
    }

    public void release() {
        
        super.release();
    }
}
