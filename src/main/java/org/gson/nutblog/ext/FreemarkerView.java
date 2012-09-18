package org.gson.nutblog.ext;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gson.nutblog.plugin.TemplateHook;
import org.gson.nutblog.util.Cache;
import org.gson.nutblog.util.Constant;
import org.gson.nutblog.util.Utils;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.View;

import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreemarkerView implements View {
	private static final Log log = Logs.getLog(FreemarkerView.class);
	
	private static final String SESSION = "session";
	private static final String APPLICATION = "application";
	private static final String PARAMS = "params";
	public static final String REQUEST = "request";
	
	private String tpNames;
	private String tplName;
	private ServletContext sc;
	private String templateName;
	private String templatePath;
	
	public static final String OBJ = "obj";
	public static final String BASE = "BLOG_URL";
	public static final String TMP_PATH = "TEM_URL";
	private static final String TMP_REAL_PATH = "TMP_REAL_PATH";

	

	public FreemarkerView(String tpName) {
		this.tpNames = tpName+".ftl";
		 sc = Mvcs.getServletContext();
	}

	public void render(HttpServletRequest request, HttpServletResponse response, Object value) throws Throwable {
		templateName = sc.getAttribute(Constant.KEY_TEMPLATENAME).toString();
		templatePath = Constant.TMP_ROOT_PATH + templateName+"/";
		
		tplName = this.tpNames;
		if(tplName.startsWith("admin:")){
			tplName = tplName.replace("admin:", "");
			templatePath = Constant.ADMIN_TMP_ROOT_PATH;
		}
		
		String realPath = sc.getRealPath("/WEB-INF/"+templatePath);
		
		if(Utils.fileExists(realPath) && Utils.fileExists(realPath+"\\"+tplName)){
			outPut(request, response, value, sc);
		}else{
			log.info(templateName+"主题或者主题文件<"+tplName+">不存在！");
			response.setStatus(500);
			request.getRequestDispatcher("/WEB-INF/public/error.jsp").forward(request, response);
		}
	}

	private void outPut(HttpServletRequest request, HttpServletResponse response, Object value, ServletContext sc) throws TemplateException, IOException, ServletException {
		Mvcs.setLocalizationKey(request.getLocale().toString());
		// 添加数据模型
		HashMap<String, Object> root = new HashMap<String, Object>();
		HashMap<String, Object> requestParms = new HashMap<String, Object>();
		HashMap<String, Object> sessionParms = new HashMap<String, Object>();
		HashMap<String, Object> applicationParms = new HashMap<String, Object>();
		
		root.put(OBJ, value);
		
		String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
		root.put(BASE, basePath);
		root.put(TMP_PATH, basePath+"templates/"+templateName+"/");
		root.put(TMP_REAL_PATH, templatePath);
		
		Enumeration<?> reqs = request.getAttributeNames();
		while (reqs.hasMoreElements()) {
			String strKey = (String) reqs.nextElement();
			requestParms.put(strKey, request.getAttribute(strKey));
		}
		root.put(REQUEST, requestParms);
		
		Enumeration<?> sess = request.getSession().getAttributeNames();
		while (sess.hasMoreElements()) {
			String strKey = (String) sess.nextElement();
			sessionParms.put(strKey, request.getSession().getAttribute(strKey));
		}
		root.put(SESSION, sessionParms);
		
		Enumeration<?> scs = sc.getAttributeNames();
		while (scs.hasMoreElements()) {
			String strKey = (String) scs.nextElement();
			applicationParms.put(strKey, sc.getAttribute(strKey));
		}
		root.put(APPLICATION, applicationParms);
		root.put(PARAMS, request.getParameterMap());
		
		root.put("options",Cache.getInstance().readCache(Map.class, Cache.OPTIONS));
		root.put("execHook", TemplateHook.getInstance(root)); 
		root.put("locale", request.getLocale().toString());
		
		Template t = Utils.cfg.getTemplate(templatePath+tplName);
		
		response.setContentType("text/html; charset=" + t.getEncoding());
		try {
			StringWriter sw = new StringWriter();
			BufferedWriter wt = new BufferedWriter(sw);
			t.process(root, wt);
			response.getWriter().write(sw.toString());
			response.getWriter().flush();
			sw.flush();
			wt.flush();
		} catch (TemplateException e) {
			log.info(tplName+"渲染失败！"+e.getMessage());
			response.setStatus(500);
			request.getRequestDispatcher("/WEB-INF/public/error.jsp").forward(request, response);
		}
	}
	
}
