/**
 * 
 */
package org.jboss.tools.flow.jpdl4.io;

import java.util.List;

import org.jboss.tools.flow.common.model.Element;
import org.jboss.tools.flow.common.wrapper.Wrapper;
import org.jboss.tools.flow.jpdl4.model.JavaTask;

class JavaTaskSerializer extends ProcessNodeSerializer {
	protected List<String> getAttributesToSave() {
		List<String> result = super.getAttributesToSave();
		result.add("class");
		result.add("method");
		result.add("var");
		result.add("expr");
		return result;
	}
	protected String getPropertyName(String attributeName) {
		if ("class".equals(attributeName)) {
			return JavaTask.CLASS;
		} else if ("method".equals(attributeName)) {
			return JavaTask.METHOD;
		} else if ("var".equals(attributeName)) {
			return JavaTask.VAR;
		} else if ("expr".equals(attributeName)) {
			return JavaTask.EXPR;
		}
		return super.getPropertyName(attributeName);
	}
	public void appendBody(StringBuffer buffer, Wrapper wrapper, int level) {

		List<Element> arguments = wrapper.getChildren(JavaTask.ARGS);
		if (arguments != null) {
			for (Element argument : arguments) {
				if (argument instanceof Wrapper) {
					JpdlSerializer.serialize((Wrapper)argument, buffer, level+1);
				}
			}
		}
		super.appendBody(buffer, wrapper, level);
	}
}