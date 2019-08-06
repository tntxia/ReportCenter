package com.tntxia.report.action;


import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.alibaba.fastjson.JSON;
import com.tntxia.dbmanager.DBManager;
import com.tntxia.httptrans.HttpTransfer;
import com.tntxia.httptrans.HttpTransferFactory;
import com.tntxia.report.entity.ReportTemplate;
import com.tntxia.report.entity.ReportTemplateCol;
import com.tntxia.report.service.ReportService;
import com.tntxia.report.service.TemplateService;
import com.tntxia.web.mvc.BaseAction;
import com.tntxia.web.mvc.PageBean;
import com.tntxia.web.mvc.WebRuntime;
import com.tntxia.web.mvc.entity.MultipartForm;
import com.tntxia.web.util.UUIDUtils;
import com.tntxia.xml.util.Dom4jUtil;

public class TemplateAction extends BaseAction {
	
	private DBManager dbManager = this.getDBManager();
	
	private TemplateService service = new TemplateService();
	
	private ReportService reportService = new ReportService();
	
	@SuppressWarnings("rawtypes")
	public Map<String,Object> list(WebRuntime runtime) throws Exception{
		PageBean pageBean = runtime.getPageBean();
		String sql = "select top "+pageBean.getTop()+" * from report_template";
		List list = dbManager.queryForList(sql, true);
		sql = "select count(*) from report_template";
		int count = dbManager.queryForInt(sql);
		return this.getPagingResult(list, pageBean, count);
		
		
	}
	
	/**
	 * 增加报表模板
	 * @param runtime
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> add(WebRuntime runtime) throws Exception{
		
		String name = runtime.getParam("name");
		String displayName = runtime.getParam("display_name"); 
		String serviceUrl = runtime.getParam("service_url"); 
		
		ReportTemplate template = new ReportTemplate();
		template.setName(name);
		template.setDisplayName(displayName);
		template.setServiceUrl(serviceUrl);
		service.add(template);
		
		return this.success();
		
	}
	
	public Map<String,Object> update(WebRuntime runtime) throws Exception{
		
		String id = runtime.getParam("id");
		String name = runtime.getParam("name");
		String display_name = runtime.getParam("display_name"); 
		String service_url = runtime.getParam("service_url"); 
		
		String sql = "update report_template set name=?,display_name=?,service_url=?,last_update=now() where id = ?";
		
		dbManager.update(sql, new Object[] {name,display_name,service_url,id});	
		return this.success();
		
	}
	
	public Map<String,Object> del(WebRuntime runtime) throws Exception{
		String id = runtime.getParam("id");
		String sql = "delete from report_template where id = ?";
		dbManager.executeUpdate(sql, new Object[] {id});
		sql = "delete from report_template_col where template_id = ?";
		dbManager.executeUpdate(sql, new Object[] {id});
		return this.success();
	}
	
	@SuppressWarnings("rawtypes")
	public Map<String,Object> export(WebRuntime runtime) throws Exception{
		String id = runtime.getParam("id");
		Map<String,Object> detail = service.getDetail(id);
		
		//创建一个xml文档
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("template");
		
		Element nameEl = root.addElement("name");
		nameEl.setText((String) detail.get("name"));
		Element displayNameEl = root.addElement("display-name");
		displayNameEl.setText((String) detail.get("display_name"));
		Element serviceUrlEl = root.addElement("service-url");
		serviceUrlEl.setText((String) detail.get("service_url"));
		
		Element colsEl = root.addElement("cols");
		List cols = (List) detail.get("cols");
		for(int i=0;i<cols.size();i++) {
			Map map = (Map) cols.get(i);
			Element colEl = colsEl.addElement("col");
			Element labelEl = colEl.addElement("label");
			labelEl.setText((String) map.get("name"));
			Element fieldEl = colEl.addElement("field");
			fieldEl.setText((String) map.get("field"));
		}
		
		
		long dateStamp = (new Date(System.currentTimeMillis())).getTime();
		
		String outPath = "D:\\oa_export_temp\\template_" + dateStamp + ".xml";
		
		FileWriter out = new FileWriter(outPath);
		OutputFormat format = OutputFormat.createPrettyPrint();  //转换成字符串
        format.setEncoding("UTF-8");
        XMLWriter writer = new XMLWriter( out, format );
        writer.write( doc );
        writer.close();
        
        HttpTransfer trans = HttpTransferFactory.generate("file_center");
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        Map<String,String> params = new HashMap<String,String>();
        String res = trans.uploadFile("file!upload", outPath, params);
        Map transResult = (Map) JSON.parse(res);
		return this.success("uuid", transResult.get("uuid"));
	}
	
	public Map<String,Object> detail(WebRuntime runtime) throws Exception{
		String id = runtime.getParam("id");
		Map<String,Object> detail = service.getDetail(id);
		return detail;
	}
	
	@SuppressWarnings("rawtypes")
	public List listCols(WebRuntime runtime) throws Exception{
		String id = runtime.getParam("id");
		return service.listCols(id);
		
	}
	
	/**
	 * 增加报表模板的列
	 * @param runtime
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> addCol(WebRuntime runtime) throws Exception{
		
		String template_id = runtime.getParam("template_id");
		String name = runtime.getParam("name");
		String field = runtime.getParam("field"); 
		
		String sql = "insert into report_template_col(id,name,field,template_id,create_time,last_update) values(?,?,?,?,now(),now())";
		String id = UUIDUtils.getUUID();
		dbManager.update(sql, new Object[] {id, name,field,template_id});	
		return this.success();
		
	}
	
	/**
	 * 增加报表模板的列
	 * @param runtime
	 * @return
	 * @throws Exception
	 */
	public Map<String,Object> updateCol(WebRuntime runtime) throws Exception{
		
		String id = runtime.getParam("id");
		String name = runtime.getParam("name");
		String field = runtime.getParam("field"); 
		
		String sql = "update report_template_col set name=?,field=?, last_update=now() where id = ?";
		
		dbManager.update(sql, new Object[] {name,field,id});	
		return this.success();
		
	}
	
	@SuppressWarnings("rawtypes")
	public Map<String,Object> generate(WebRuntime runtime) throws Exception{
		
		String id = runtime.getParam("id");
		Map<String,Object> detail = service.getDetail(id);
		List colList = service.listCols(id);
		String uuid = reportService.generate(detail, colList);
		return this.success("uuid",uuid);
	}
	
	public Map<String,Object> templateImport(WebRuntime runtime) throws Exception{
		MultipartForm form = runtime.getMultipartForm();
		String uuid = form.getString("uuid");
		List<String> saveRes = form.save("D:\\oa_upload");
		String fileSavePath = saveRes.get(0);
		Document doc = Dom4jUtil.getDoc(fileSavePath);
		Element root = doc.getRootElement();
		String name = Dom4jUtil.getProp(root, "name");
		String displayName = Dom4jUtil.getProp(root, "display-name");
		String serviceUrl = Dom4jUtil.getProp(root, "service-url");
		
		List<Element> list = Dom4jUtil.getElementList(root, "cols/col");
		
		List<ReportTemplateCol> cols = new ArrayList<ReportTemplateCol>();
		
		for(Element el : list) {
			String label = Dom4jUtil.getProp(el, "label");
			String field = Dom4jUtil.getProp(el, "field");
			ReportTemplateCol col = new ReportTemplateCol();
			col.setField(field);
			col.setLabel(label);
			cols.add(col);
		}
		
		ReportTemplate template = new ReportTemplate();
		template.setName(name);
		template.setDisplayName(displayName);
		template.setServiceUrl(serviceUrl);
		template.setCols(cols);
		
		service.add(template);
		
		return this.success("uuid",uuid);
	}

}
