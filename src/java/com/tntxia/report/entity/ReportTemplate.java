package com.tntxia.report.entity;

import java.util.ArrayList;
import java.util.List;

public class ReportTemplate {
	
	private String name;
	
	private String displayName;
	
	private String serviceUrl;
	
	private List<ReportTemplateCol> cols = new ArrayList<ReportTemplateCol>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getServiceUrl() {
		return serviceUrl;
	}

	public void setServiceUrl(String serviceUrl) {
		this.serviceUrl = serviceUrl;
	}

	public List<ReportTemplateCol> getCols() {
		return cols;
	}

	public void setCols(List<ReportTemplateCol> cols) {
		this.cols = cols;
	}
	
}
