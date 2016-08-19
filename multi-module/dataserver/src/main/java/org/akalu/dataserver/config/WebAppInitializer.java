package org.akalu.dataserver.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

//import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;  
/**
 * In this application all the web setting is done in a class which implements
 * WebApplicationInitializer.
 * <p>
 * NB: to run a such application the container with support of servlet 3.0 is
 * needed
 * 
 * @author Alex Kalutov
 * @since Version 1.0
 */

public class WebAppInitializer implements WebApplicationInitializer {

	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.register(org.akalu.dataserver.config.ApplicationContextConfig.class);
		ctx.setServletContext(servletContext);
		servletContext.addListener(new ContextLoaderListener(ctx));
		Dynamic dynamic = servletContext.addServlet("dispatcher", new DispatcherServlet(ctx));
		dynamic.addMapping("/");
		dynamic.setLoadOnStartup(1);
	}

	/*
	 * @Override protected Class<?>[] getRootConfigClasses() { // TODO
	 * Auto-generated method stub return new Class[]
	 * {org.testtask.dataserver.config.ApplicationContextConfig.class}; }
	 * 
	 * @Override protected Class<?>[] getServletConfigClasses() { // TODO
	 * Auto-generated method stub return new Class[] {
	 * org.testtask.dataserver.config.ApplicationContextConfig.class}; }
	 * 
	 * @Override protected String[] getServletMappings() { // TODO
	 * Auto-generated method stub return new String[] { "/" }; }
	 */
}
