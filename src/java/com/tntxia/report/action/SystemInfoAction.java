package com.tntxia.report.action;

import java.util.HashMap;
import java.util.Map;

import com.tntxia.report.cache.SystemCache;
import com.tntxia.web.mvc.BaseAction;
import com.tntxia.web.mvc.WebRuntime;

public class SystemInfoAction extends BaseAction  {
	
	public Map<String,Object> getSystemInfo(WebRuntime runtime) throws Exception{
		
		Map<String,Object> res = new HashMap<String,Object>();
		res.put("filecenter", SystemCache.filecenter);
		return res;
	}

}
