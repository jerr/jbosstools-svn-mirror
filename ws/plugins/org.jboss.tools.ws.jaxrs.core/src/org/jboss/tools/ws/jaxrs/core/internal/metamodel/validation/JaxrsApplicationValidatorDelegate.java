package org.jboss.tools.ws.jaxrs.core.internal.metamodel.validation;

import org.eclipse.core.runtime.CoreException;
import org.jboss.tools.common.validation.TempMarkerManager;
import org.jboss.tools.ws.jaxrs.core.internal.metamodel.domain.JaxrsJavaApplication;
import org.jboss.tools.ws.jaxrs.core.internal.metamodel.domain.JaxrsResource;
import org.jboss.tools.ws.jaxrs.core.internal.metamodel.domain.JaxrsResourceMethod;
import org.jboss.tools.ws.jaxrs.core.metamodel.IJaxrsResourceMethod;

public class JaxrsApplicationValidatorDelegate extends AbstractJaxrsElementValidatorDelegate<JaxrsJavaApplication> {

	public JaxrsApplicationValidatorDelegate(TempMarkerManager markerManager, JaxrsJavaApplication element) {
		super(markerManager, element);
	}

	@Override
	public void validate() throws CoreException {
		final JaxrsJavaApplication resource = getElement();
		MarkerUtils.clearMarkers(resource.getResource());
	}
	
	

}
