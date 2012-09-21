package org.gson.nutblog;

import org.gson.nutblog.ext.FreemarkerView;
import org.gson.nutblog.util.Utils;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.filter.CheckSession;

@At("/admin")
@Filters(value={@By(type=CheckSession.class,args={"user","/login.nut"})})
public class AdminModule {

	@At
	public Object index(){
		Utils.setTopTitle("NutBlog管理");
		return new FreemarkerView("admin:admin:index");
	}
	
	@At
	public Object writeLog(){
		if(!Utils.isPost()){
			Utils.setTopTitle("撰写新文章");
			return new FreemarkerView("admin:write_log");
		}
		return true;
	}
}
