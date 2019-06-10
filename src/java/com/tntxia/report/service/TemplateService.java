package com.tntxia.report.service;

import java.util.List;
import java.util.Map;

import com.tntxia.dbmanager.DBManager;
import com.tntxia.report.entity.ReportTemplate;
import com.tntxia.report.entity.ReportTemplateCol;
import com.tntxia.web.mvc.service.CommonService;
import com.tntxia.web.util.UUIDUtils;

public class TemplateService extends CommonService {
	
	private DBManager dbManager = this.getDBManager();
	
	public void add(ReportTemplate template) throws Exception {
		String name = template.getName();
		String displayName = template.getDisplayName();
		String serviceUrl = template.getServiceUrl();
		String uuid = UUIDUtils.getUUID();
		String sql = "insert into report_template(id, name,display_name,service_url,create_time,last_update) values(?,?,?,?,now(),now())";
		dbManager.update(sql, new Object[] {uuid, name, displayName, serviceUrl});
		
		List<ReportTemplateCol> cols = template.getCols();
		if (cols != null && cols.size()> 0) {
			for(ReportTemplateCol col : cols) {
				sql = "insert into report_template_col(id, name, field, template_id) values(?,?,?,?)";
				dbManager.update(sql, new Object[] {UUIDUtils.getUUID(), col.getLabel(), col.getField(), uuid});
			}
		}
	}
	
	@SuppressWarnings("rawtypes")
	public Map<String,Object> getDetail(String id) throws Exception{
		String sql = "select * from report_template where id = ?";
		Map<String,Object> detail = dbManager.queryForMap(sql,new Object[] {id}, true);
		List cols = listCols(id);
		detail.put("cols", cols);
		return detail;
		
	}
	
	@SuppressWarnings("rawtypes")
	public List listCols(String id) throws Exception {
		String sql = "select * from report_template_col where template_id=?";
		List list = dbManager.queryForList(sql, new Object[] {id}, true);
		return list;
	}
	
	public Map<String,Object> getByName(String name) throws Exception{
		String sql = "select * from report_template where name = ?";
		return dbManager.queryForMap(sql,new Object[] {name}, true);
	}

}
