package org.akalu.test.webclient;

import static org.junit.Assert.assertNotNull;

import org.akalu.webclient.config.AppConfig;

import org.junit.Test;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

public class AppConfigTest {
	
	@Test
	public void testConfig(){
		AppConfig appConfig = new AppConfig();
		
		MessageSource ms = appConfig.getMessageSource();

		SessionLocaleResolver slr = appConfig.getLocaleResolver();
		
		assertNotNull(ms);
		assertNotNull(slr);
	}

}
