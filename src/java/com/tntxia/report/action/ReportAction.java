package com.tntxia.report.action;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.tntxia.dbmanager.DBManager;
import com.tntxia.report.service.ReportService;
import com.tntxia.report.service.TemplateService;
import com.tntxia.web.mvc.BaseAction;
import com.tntxia.web.mvc.WebRuntime;
import com.tntxia.web.mvc.view.FileView;

public class ReportAction extends BaseAction {
	
	private DBManager dbManager = this.getDBManager();
	
	private TemplateService templateService = new TemplateService();
	
	private ReportService service = new ReportService();
	
	@SuppressWarnings("rawtypes")
	public Map<String,Object> generate(WebRuntime runtime) throws Exception{
		String templateName = runtime.getParam("template");
		Map<String,Object> template = templateService.getByName(templateName);
		Integer id = (Integer) template.get("id");
		List colList = templateService.listCols(String.valueOf(id));
		String uuid = service.generate(template, colList);
		return this.success("uuid", uuid);
	}
	
	public Map<String,Object> add(WebRuntime runtime) throws Exception{
		
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		String name = runtime.getParam("name");
		
		String sql = "insert into report(id,name,progress,status,create_time,last_update) values(?,?,0,'init',now(),now())";
		
		dbManager.update(sql, new Object[] {uuid,name});	
		return this.success();
		
	}
	
	public Map<String,Object> detail(WebRuntime runtime) throws Exception{
		String uuid = runtime.getParam("id");
		String sql = "select * from report where id = ?";
		return dbManager.queryForMap(sql, new Object[] {uuid},true);	
	}
	
	public FileView download(WebRuntime runtime) throws Exception{
		String uuid = runtime.getParam("id");
		String sql = "select file_path from report where id = ?";
		String filePath = dbManager.getString(sql, new Object[] {uuid});
		FileView fileView = new FileView();
		fileView.setFilePath(filePath);
		return fileView;
	}

}
