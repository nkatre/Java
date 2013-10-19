package edu.sjsu.cmpe295b.planhercareer.ws;

import java.util.HashSet;
import java.util.Set;

import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;
import org.codehaus.jackson.map.AnnotationIntrospector;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.introspect.JacksonAnnotationIntrospector;
import org.codehaus.jackson.xc.JaxbAnnotationIntrospector;
import javax.ws.rs.core.Application;

/** 
 * Defines the components of a JAX-RS application and supplies additional metadata
 * extends javax.ws.rs.core.Application
 * 
 * Re-used the implementation as provided by Pr. John Gash in ws-wink-simple
 * 
 * @author Team 5
 */
public class BlogApplication extends Application {

	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> serviceClasses = new HashSet<Class<?>>();
		serviceClasses.add(BlogResource.class);
		return serviceClasses;
	}

	@Override
	public Set<Object> getSingletons() {
		Set<Object> s = new HashSet<Object>();

		// Register the Jackson provider for JSON

		// Make (de)serializer use a subset of JAXB and (afterwards) Jackson
		// annotations
		// See http://wiki.fasterxml.com/JacksonJAXBAnnotations for more
		// information
		ObjectMapper mapper = new ObjectMapper();
		AnnotationIntrospector primary = new JaxbAnnotationIntrospector();
		AnnotationIntrospector secondary = new JacksonAnnotationIntrospector();
		AnnotationIntrospector pair = new AnnotationIntrospector.Pair(primary, secondary);
		mapper.getDeserializationConfig().setAnnotationIntrospector(pair);
		mapper.getSerializationConfig().setAnnotationIntrospector(pair);

		// Set up the provider
		JacksonJaxbJsonProvider jaxbProvider = new JacksonJaxbJsonProvider();
		jaxbProvider.setMapper(mapper);

		s.add(jaxbProvider);
		return s;
	}
}
