package org.gson.nutblog.plugin;


public abstract class AbstractPlugin implements Plugin{
	
	public AbstractPlugin() {
		this.install();
	}

	public void install() {
		log.info(this.getPlugName()+"已经加载！");
	}

	@Override
	public void uninstall() {
		log.info(this.getPlugName()+"已经卸载！");
	}

	@Override
	public void start() {
		log.info(this.getPlugName()+"已经启动！");
	}

	@Override
	public void stop() {
		log.info(this.getPlugName()+"已经停止！");
	}
}
