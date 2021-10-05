
package com.ibm.poc.report;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BirtConfig {

	private ApplicationContext context;

	public BirtConfig(ApplicationContext context) {
		super();
		this.context = context;
	}

	@Bean
	public IReportEngine initialize() throws BirtException {
		EngineConfig config = new EngineConfig();
		config.getAppContext().put("spring", this.context);
		Platform.startup(config);
		IReportEngineFactory factory = (IReportEngineFactory) Platform
				.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
		return factory.createReportEngine(config);
	}

}
