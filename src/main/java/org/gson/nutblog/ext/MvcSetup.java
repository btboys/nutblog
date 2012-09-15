package org.gson.nutblog.ext;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.gson.nutblog.plugin.Hooks;
import org.gson.nutblog.plugin.internal.Index2Plugin;
import org.gson.nutblog.plugin.internal.IndexPlugin;
import org.gson.nutblog.plugin.internal.TipPlugin;
import org.gson.nutblog.util.Cache;
import org.gson.nutblog.util.Constant;
import org.gson.nutblog.util.Utils;
import org.nutz.lang.Files;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
import org.nutz.mvc.NutConfig;
import org.nutz.mvc.Setup;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateModelException;

public class MvcSetup implements Setup {
	private static final Log log = Logs.getLog(MvcSetup.class);
	private static final String CONFIG_SERVLET_CONTEXT_KEY = "freemarker.Configuration";
	
	@Override
	public void destroy(NutConfig nc) {
		log.info("系统已停止！");
	}

	@Override
	public void init(NutConfig nc) {
		//获取系统版本号
		nc.getServletContext().setAttribute(Constant.KEY_VERSION, Constant.VERSION);
		nc.getServletContext().setAttribute(Constant.KEY_TEMPLATENAME, "default");
		
		Hooks.addPluginToHooks(new TipPlugin());
		Hooks.addPluginToHooks(new IndexPlugin());
		Hooks.addPluginToHooks(new Index2Plugin());
		//更新缓存
		Cache c = Cache.getInstance();
		c.updateCache();
		
		setConfiguration();
		if(Utils.cfg != null){
			Utils.cfg.setAutoIncludes(Arrays.asList("nut_macro.ftl"));
			try {
				Utils.cfg.setSharedVariable("hooks", Hooks.getListHooks());
			} catch (TemplateModelException e) {
				log.error("插件挂载点初始化失败！TemplateModelException:"+e.getMessage());
			}
		}
	}
	
	private void setConfiguration() {
		Configuration config = (Configuration) Mvcs.getServletContext().getAttribute(CONFIG_SERVLET_CONTEXT_KEY);
		if (config == null) {
			config = new Configuration();
			config.setServletContextForTemplateLoading(Mvcs.getServletContext(), "/");
			config.setDefaultEncoding("UTF-8");
			config.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
			// 读取freemarker配置文件
			loadSettings(Mvcs.getServletContext(), config);

			Mvcs.getServletContext().setAttribute(CONFIG_SERVLET_CONTEXT_KEY, config);
		}
		config.setWhitespaceStripping(true);
		Utils.cfg = config;
	}

	private void loadSettings(ServletContext servletContext, Configuration config) {
		log.debug("开始读取："+CONFIG_SERVLET_CONTEXT_KEY);
		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(Files.findFile("freemarker.properties")));
			if (in != null) {
				Properties p = new Properties();
				p.load(in);
				config.setSettings(p);
			}
		} catch (IOException e) {
			log.error("NutBlog freemarker配置文件读取失败！IOException："+e.getMessage());
		} catch (TemplateException e) {
			log.error("NutBlog freemarker配置文件读取失败！IOException："+e.getMessage());
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					log.error("NutBlog freemarker配置文件读取失败！IOException："+e.getMessage());
				}
			}
		}
	}
}
