package org.gson.nutblog.plugin.internal;

import java.util.List;

import org.gson.nutblog.plugin.AbstractPlugin;
import org.gson.nutblog.plugin.Hooks.Hook;

import freemarker.template.TemplateModelException;

public class TipPlugin extends AbstractPlugin {

	@Override
	public String getPlugName() {
		return "tip插件";
	}

	@Override
	public String[] getHookNames() {
		return new String[]{Hook.ADMIN_TOP};
	}

	@Override
	public Object exec(List arguments) throws TemplateModelException {
		
		return null;
	}
}
