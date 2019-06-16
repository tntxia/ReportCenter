package com.tntxia.report.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.tntxia.dbmanager.DBManager;
import com.tntxia.excel.ExcelData;
import com.tntxia.excel.ExcelRow;
import com.tntxia.excel.ExcelUtils;
import com.tntxia.httptrans.HttpTrans;
import com.tntxia.web.mvc.service.CommonService;

public class ReportService extends CommonService{
	
	private DBManager dbManager = this.getDBManager();
	
	public String add(String name) throws Exception {
		
		String uuid = UUID.randomUUID().toString().replaceAll("-", "");
		
		
		String sql = "insert into report(id,name,progress,status,create_time,last_update) values(?,?,0,'init',now(),now())";
		
		dbManager.update(sql, new Object[] {uuid,name});
		return uuid;
		
	}
	
	public void updateReportStatus(String uuid,String status) throws Exception {
		String sql = "update report set status=? where id = ?";
		dbManager.update(sql,new Object[] {status,uuid});
	}
	
	public void updateReportFilePath(String reportId,String filePath) throws Exception {
		String sql = "update report set file_path=? where id = ?";
		dbManager.update(sql,new Object[] {filePath,reportId});
	}
	
	public String generate(Map<String,Object> template,List<?> colList) throws Exception {
		
		String reportName = (String) template.get("display_name");
		
		String uuid = add(reportName);
		
		Thread t = new Thread() {
			
			@SuppressWarnings("rawtypes")
			public void run() {
				
				try {
					String service_url = (String) template.get("service_url");
					System.out.println("service_url,,,,,"+service_url);
					updateReportStatus(uuid, "data");
					Map serviceRes = HttpTrans.getMap(service_url);
					
					List<String> cols = new ArrayList<String>();
					for(int i=0;i<colList.size();i++) {
						Map map = (Map) colList.get(i);
						String name = (String) map.get("name");
						cols.add(name);
					}
					
					ExcelData data = new ExcelData();
					
					List list = (List) serviceRes.get("data");

					for (int i = 0; i < list.size(); i++) {
						Map map = (Map) list.get(i);
						ExcelRow row = new ExcelRow();
						for(int j=0;j<colList.size();j++) {
							Map col = (Map) colList.get(j);
							String field = (String) col.get("field");
							row.add(map.get(field));
						}
						data.add(row);
					}
					
					updateReportStatus(uuid, "file");
					String excelPath;
					excelPath = ExcelUtils.makeCommonExcel(reportName, cols, data);
					updateReportFilePath(uuid, excelPath);
					updateReportStatus(uuid, "finish");
				}catch(Exception ex) {
					ex.printStackTrace();
					try {
						updateReportStatus(uuid, "exception");
					}catch(Exception e) {
						ex.printStackTrace();
					}
					
				}finally {
					
				}
				
			}
		};
		t.start();
		return uuid;
	}

}
