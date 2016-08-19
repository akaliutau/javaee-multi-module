package org.akalu.webclient.config;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration.Dynamic;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
//import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;  

public class WebAppInitializer implements WebApplicationInitializer {

	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
		ctx.register(org.akalu.webclient.config.AppConfig.class);
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
