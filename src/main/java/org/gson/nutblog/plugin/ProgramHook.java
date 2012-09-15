package org.gson.nutblog.plugin;

import java.util.List;

import org.nutz.log.Log;
import org.nutz.log.Logs;

public final class ProgramHook {
	private static final Log log = Logs.getLog(ProgramHook.class);

	private ProgramHook() {}

	public synchronized static void exec(String hookName, Object... object) {
		List<Plugin> hook = Hooks.getHookPlugins(hookName);
		if (hook != null) {
			for (Plugin plugin : hook) {
				try {
//					plugin.exec(new PluginContext(hookName), object);
				} catch (Exception e) {
					log.info("NutError：" + e.getMessage() + "。hookName："+ hookName);
					continue;
				}
			}
		}
	}
}
