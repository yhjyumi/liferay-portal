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

package com.liferay.portlet.usersadmin.lar;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.xml.Element;
import com.liferay.portal.model.Group;
import com.liferay.portal.model.Website;
import com.liferay.portal.service.GroupLocalServiceUtil;
import com.liferay.portal.service.ServiceContext;
import com.liferay.portal.service.WebsiteLocalServiceUtil;
import com.liferay.portlet.exportimport.lar.BaseStagedModelDataHandler;
import com.liferay.portlet.exportimport.lar.ExportImportPathUtil;
import com.liferay.portlet.exportimport.lar.PortletDataContext;

import java.util.ArrayList;
import java.util.List;

/**
 * @author David Mendez Gonzalez
 */
public class WebsiteStagedModelDataHandler
	extends BaseStagedModelDataHandler<Website> {

	public static final String[] CLASS_NAMES = {Website.class.getName()};

	@Override
	public void deleteStagedModel(
			String uuid, long groupId, String className, String extraData)
		throws PortalException {

		Group group = GroupLocalServiceUtil.getGroup(groupId);

		Website website =
			WebsiteLocalServiceUtil.fetchWebsiteByUuidAndCompanyId(
				uuid, group.getCompanyId());

		if (website != null) {
			deleteStagedModel(website);
		}
	}

	@Override
	public void deleteStagedModel(Website website) {
		WebsiteLocalServiceUtil.deleteWebsite(website);
	}

	@Override
	public List<Website> fetchStagedModelsByUuidAndCompanyId(
		String uuid, long companyId) {

		List<Website> websites = new ArrayList<>();

		websites.add(
			WebsiteLocalServiceUtil.fetchWebsiteByUuidAndCompanyId(
				uuid, companyId));

		return websites;
	}

	@Override
	public String[] getClassNames() {
		return CLASS_NAMES;
	}

	@Override
	protected void doExportStagedModel(
			PortletDataContext portletDataContext, Website website)
		throws Exception {

		Element websiteElement = portletDataContext.getExportDataElement(
			website);

		portletDataContext.addClassedModel(
			websiteElement, ExportImportPathUtil.getModelPath(website),
			website);
	}

	@Override
	protected void doImportStagedModel(
			PortletDataContext portletDataContext, Website website)
		throws Exception {

		long userId = portletDataContext.getUserId(website.getUserUuid());

		ServiceContext serviceContext = portletDataContext.createServiceContext(
			website);

		Website existingWebsite =
			WebsiteLocalServiceUtil.fetchWebsiteByUuidAndCompanyId(
				website.getUuid(), portletDataContext.getCompanyGroupId());

		Website importedWebsite = null;

		if (existingWebsite == null) {
			serviceContext.setUuid(website.getUuid());

			importedWebsite = WebsiteLocalServiceUtil.addWebsite(
				userId, website.getClassName(), website.getClassPK(),
				website.getUrl(), website.getTypeId(), website.isPrimary(),
				serviceContext);
		}
		else {
			importedWebsite = WebsiteLocalServiceUtil.updateWebsite(
				existingWebsite.getWebsiteId(), website.getUrl(),
				website.getTypeId(), website.isPrimary());
		}

		portletDataContext.importClassedModel(website, importedWebsite);
	}

}