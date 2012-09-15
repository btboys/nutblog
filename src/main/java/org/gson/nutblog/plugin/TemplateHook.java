package org.gson.nutblog.plugin;

import java.util.HashMap;
import java.util.List;

import org.nutz.log.Log;
import org.nutz.log.Logs;

import freemarker.template.TemplateMethodModel;
import freemarker.template.TemplateModelException;

public class TemplateHook implements TemplateMethodModel {
	private static final Log log = Logs.getLog(TemplateHook.class);
	
	private static TemplateHook instance;
	private HashMap<String, Object> context = new HashMap<String, Object>();
	
	private TemplateHook() {}

	private TemplateHook(HashMap<String, Object> context) {
		this.context = context;
	}
	
	public synchronized static TemplateHook getInstance(HashMap<String, Object> context) {
		if(instance != null){
			instance.context = context;
			return instance;
		}
		instance = new TemplateHook(context);
		return instance;
	}

	@SuppressWarnings({ "rawtypes" })
	public Object exec(List arguments) throws TemplateModelException{
		if(arguments != null && !arguments.isEmpty()){
			List<Plugin> plugins = Hooks.getHookPlugins(arguments.get(0).toString());
			if(plugins != null){
				StringBuffer bf = new StringBuffer();
				for (Plugin plugin : plugins) {
					try {
						context.put("hookName", arguments.get(0).toString());
//						Object rt = plugin.exec(new PluginContext(context),arguments);
//						if(rt instanceof String || rt instanceof StringBuffer){
//							bf.append(rt.toString());
//						}
					} catch (Exception e) {
						log.info("NutError："+e.getMessage()+"。hookName："+arguments.get(0));
						continue;
					}
				}
				return bf.toString();
			}
		}
		return null;
	}
}
