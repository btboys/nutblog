package org.gson.nutblog.plugin.internal;

import java.util.List;

import org.gson.nutblog.plugin.AbstractPlugin;
import org.gson.nutblog.plugin.Hooks.Hook;

import freemarker.template.TemplateModelException;

public class IndexPlugin extends AbstractPlugin {

	@Override
	public String[] getHookNames() {
		return new String[]{Hook.INDEX_TOP};
	}
	
	@Override
	public String getPlugName() {
		return "index";
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Object exec(List arguments) throws TemplateModelException {
		return "wirte测试";
	}
}
