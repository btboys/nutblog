package org.gson.nutblog.plugin;

import org.nutz.log.Log;
import org.nutz.log.Logs;

import freemarker.template.TemplateMethodModel;

/**
 * 插件接口
 * @author gson
 *
 */
public interface Plugin extends TemplateMethodModel{
	
	public static final Log log = Logs.get();

	public void install();
	
	public void uninstall();
	
	public void start();
	
	public void stop();
	
	public String getPlugName();
	
	public String[] getHookNames();
}
