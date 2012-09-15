package org.gson.nutblog.plugin;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.gson.nutblog.ext.FreemarkerView;

public class PluginContext {
	
	private String hookName;
	
	private String basePath;
	
	private String templatesPath;
	
	private HttpServletRequest request;
	
	private Object returnValue;
	
	public PluginContext(HashMap<String, Object> params) {
		this.request = (HttpServletRequest) params.get(FreemarkerView.REQUEST);
		this.templatesPath = params.get(FreemarkerView.TMP_PATH).toString();
		this.basePath = params.get(FreemarkerView.BASE).toString();
		this.returnValue = params.get(FreemarkerView.OBJ);
		this.hookName = params.get("hookName").toString();
	}

	public PluginContext(String hookName) {
		this.hookName = hookName;
	}

	public String getBasePath() {
		return basePath;
	}

	public String getTemplatesPath() {
		return templatesPath;
	}


	public HttpServletRequest getRequest() {
		return request;
	}


	public Object getReturnValue() {
		return returnValue;
	}
	
	public String getHookName() {
		return hookName;
	}
}
