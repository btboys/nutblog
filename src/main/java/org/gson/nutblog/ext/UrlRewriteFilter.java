package org.gson.nutblog.ext;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.gson.nutblog.util.Constant;
import org.gson.nutblog.util.Utils;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.RequestPath;

import freemarker.cache.WebappTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
/**
 * 
 * <dl><dt><b>%year%</b>&nbsp;</dt><dd> 文章发表的年份，四位数，如 <tt>2004</tt>
</dd><dt><b>%monthnum%</b>&nbsp;</dt><dd> 月份，如 <tt>05</tt>
</dd><dt><b>%day%</b>&nbsp;</dt><dd> 天，如 <tt>28</tt>
</dd><dt><b>%hour%</b>&nbsp;</dt><dd> 小时，如 <tt>15</tt>
</dd><dt><b>%minute%</b>&nbsp;</dt><dd> 分钟，如 <tt>43</tt>
</dd><dt><b>%second%</b>&nbsp;</dt><dd> 秒，如 <tt>33</tt>
</dd><dt><b>%postname%</b>&nbsp;</dt><dd> 文章标题的别名 (编辑文章/页面时的<i>别名栏</i>)。 
</dd><dt><b>%post_id%</b>&nbsp;</dt><dd> 文章的唯一ID，如 <tt>423</tt>
</dd><dt><b>%category%</b>&nbsp;</dt><dd> 分类的别名 (新建/编辑分类时的<i>别名栏</i>)。 有层级关系的类型在链接地址里就像有层级的目录。   <b>出于性能原因，强烈不建议使用%category%作为链接地址的开头</b>。
</dd><dt><b>%author%</b>&nbsp;</dt><dd> 作者的别名。
</dd></dl>
 * @author gson
 *
 */
public class UrlRewriteFilter implements Filter {
	public static final Log log = Logs.getLog(UrlRewriteFilter.class);
	private HttpServletResponse resp;
	private HttpServletRequest req;
	private RequestPath path;

	private static final String IGNORE = ".png|.gif|.jpg|.js|.css|.jpeg|.swf|.ico";

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		resp = (HttpServletResponse) response;
		req = (HttpServletRequest) request;
		path = Mvcs.getRequestPathObject(req);
		///%category%/%post_id%.html record/postname
		
		if(path.getPath().endsWith("/") || Strings.isEmpty(path.getSuffix())){
			String forward;
			if(path.getPath().endsWith("/")){
				forward = path.getPath()+"index.nut";
			}else{
				forward = path.getPath()+"/index.nut";
			}
			req.getRequestDispatcher(forward).forward(request, response);
		}else if(matcher(path.getSuffix().toLowerCase())){
			chain.doFilter(request, response);
		}else if(path.getSuffix().toLowerCase().equals("html")){
			String realPath = Mvcs.getServletContext().getRealPath(path.getUrl());
			if (Utils.fileExists(realPath)) {
				chain.doFilter(request, response);
			}else{
				path.getPath();
			}
		}else{
			outPut();
		}
	}

	@Override
	public void destroy() {

	}
	
	private Boolean matcher(String suffix){
		if(Strings.isEmpty(suffix))
			return false;
		return IGNORE.contains("."+suffix);
	}
	
	private void outPut() throws ServletException, IOException{
		Configuration cfg = new Configuration();
		ServletContext sc = Mvcs.getServletContext();
		String templateName = sc.getAttribute(Constant.KEY_TEMPLATENAME).toString();
		String templatePath = Constant.TMP_ROOT_PATH + templateName+"/";
		cfg.setTemplateLoader(new WebappTemplateLoader(sc, templatePath));
		cfg.setDefaultEncoding("UTF-8");
		Template t = null;
		StringWriter sw = new StringWriter();
		BufferedWriter wt = new BufferedWriter(sw);
		HashMap<String, Object> root = new HashMap<String, Object>();
		try {
			resp.setStatus(404);
			resp.setContentType("text/html; charset=UTF-8");
			root.put("path", path.getUrl());
			t = cfg.getTemplate("404.ftl");
			t.process(root, wt);
			resp.getWriter().write(sw.toString());
		} catch (TemplateException e) {
			log.info("渲染失败！TemplateException：" + e.getMessage());
			resp.setStatus(500);
			cfg.setTemplateLoader(new WebappTemplateLoader(sc, "/WEB-INF/public/"));
			t = cfg.getTemplate("error.ftl");
			try {
				t.process(root, wt);
				resp.getWriter().write(sw.toString());
			} catch (TemplateException e1) {
				log.info("渲染失败！TemplateException：" + e1.getMessage());
				resp.getWriter().write( e1.getMessage());
			}
		} catch (FileNotFoundException e) {
			log.info("404渲染失败！IOException：" + e.getMessage());
			resp.setStatus(404);
			cfg.setTemplateLoader(new WebappTemplateLoader(sc, "/WEB-INF/public/"));
			t = cfg.getTemplate("404.ftl");
			try {
				t.process(root, wt);
				resp.getWriter().write(sw.toString());
			} catch (TemplateException e1) {
				log.info("渲染失败！TemplateException：" + e1.getMessage());
				resp.getWriter().write( e1.getMessage());
			}
		}finally{
			sw.flush();
			wt.flush();
		}
	}

}
