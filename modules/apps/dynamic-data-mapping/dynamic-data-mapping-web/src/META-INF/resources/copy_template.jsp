<%--
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
--%>

<%@ include file="/init.jsp" %>

<%
String closeRedirect = ParamUtil.getString(request, "closeRedirect");

DDMTemplate template = (DDMTemplate)request.getAttribute(WebKeys.DYNAMIC_DATA_MAPPING_TEMPLATE);

long templateId = BeanParamUtil.getLong(template, request, "templateId");

DDMTemplateVersion templateVersion = template.getTemplateVersion();
%>

<portlet:actionURL name="copyTemplate" var="copyTemplateURL">
	<portlet:param name="mvcPath" value="/copy_template.jsp" />
</portlet:actionURL>

<aui:form action="<%= copyTemplateURL %>" method="post" name="fm">
	<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />
	<aui:input name="closeRedirect" type="hidden" value="<%= closeRedirect %>" />
	<aui:input name="templateId" type="hidden" value="<%= String.valueOf(templateId) %>" />
	<aui:input name="status" type="hidden" value="<%= templateVersion.getStatus() %>" />
	<aui:input name="saveAndContinue" type="hidden" value="<%= true %>" />

	<liferay-ui:error exception="<%= TemplateNameException.class %>" message="please-enter-a-valid-name" />

	<aui:model-context bean="<%= template %>" model="<%= DDMTemplate.class %>" />

	<aui:fieldset>
		<aui:input name="name" />

		<aui:input name="description" />
	</aui:fieldset>

	<aui:button-row>
		<aui:button type="submit" value="copy" />

		<aui:button onClick="Liferay.Util.getWindow().hide();" value="close" />
	</aui:button-row>
</aui:form>