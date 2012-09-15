package org.gson.nutblog.plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * 插件挂载点
 * 
 * @author gson
 * 
 */
public final class Hooks {

	private static HashMap<String, List<String>> hooks = new HashMap<String, List<String>>();
	private static HashMap<String, Plugin> plugins = new HashMap<String, Plugin>();

	private Hooks() {
	}

	public static void addPluginToHooks(Plugin plugin) {
		for (String hookName : plugin.getHookNames()) {
			if (hooks.get(hookName) == null) {
				hooks.put(hookName, new ArrayList<String>());
			}
			hooks.get(hookName).add(plugin.getPlugName());
			plugins.put(plugin.getPlugName(), plugin);
		}
	}

	public static List<Plugin> getHookPlugins(String hookName) {
		List<String> pnameList = hooks.get(hookName);
		if(pnameList == null || pnameList.isEmpty())
			return null;
		
		List<Plugin> pluginList = new ArrayList<Plugin>();
		for (String pname : pnameList) {
			if(plugins.get(pname) == null){
				pnameList.remove(pname);
				continue;
			}
			pluginList.add(plugins.get(pname));
		}
		return pluginList;
	}
	
	public static HashMap<String, List<Plugin>> getListHooks(){
		Set<String> keys = hooks.keySet();
		HashMap<String, List<Plugin>> lsp = new HashMap<String, List<Plugin>>();
		for (String key : keys) {
			List<String> p = hooks.get(key);
			List<Plugin> lps = new ArrayList<Plugin>();
			for (String pk : p) {
				lps.add(plugins.get(pk));
			}
			lsp.put(key, lps);
		}
		return lsp;
	}
	
	public static void delPluginInHook(String pluginName) {
		plugins.remove(pluginName);
	}
	
	public final class Hook{
		public static final String ADMIN_TOP = "HOOK_ADMIN_TOP";
		public static final String INDEX_TOP = "HOOK_INDEX_TOP";
	}
}
