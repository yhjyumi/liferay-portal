/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.microblogs.service.configuration.configurator;

import com.liferay.portal.service.configuration.ServiceComponentConfiguration;
import com.liferay.portal.service.configuration.configurator.ServiceConfigurator;
import com.liferay.portal.spring.extender.loader.ModuleResourceLoader;

import javax.servlet.ServletContext;

import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Ryan Park
 */
@Component(immediate = true, service = MicroblogsServiceConfigurator.class)
public class MicroblogsServiceConfigurator {

	@Activate
	protected void activate() throws Exception {
		_serviceConfigurator.initServices(getConfiguration(), getClassLoader());
	}

	@Deactivate
	protected void deactivate() throws Exception {
		_serviceConfigurator.destroyServices(
			getConfiguration(), getClassLoader());
	}

	protected ClassLoader getClassLoader() {
		Class<? extends MicroblogsServiceConfigurator> clazz = getClass();

		return clazz.getClassLoader();
	}

	protected ServiceComponentConfiguration getConfiguration() {
		Bundle bundle = FrameworkUtil.getBundle(getClass());

		return new ModuleResourceLoader(bundle);
	}

	@Reference(unbind = "-")
	protected void setServiceConfigurator(
		ServiceConfigurator serviceConfigurator) {

		_serviceConfigurator = serviceConfigurator;
	}

	@Reference(target = "(original.bean=*)", unbind = "-")
	protected void setServletContext(ServletContext servletContext) {
	}

	private ServiceConfigurator _serviceConfigurator;

}