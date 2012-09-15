package org.gson.nutblog;

import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.By;
import org.nutz.mvc.annotation.Filters;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.filter.CheckSession;

@At("/admin")
/*@Filters(value={@By(type=CheckSession.class,args={"user","public/404.jsp"})})*/
public class AdminModule {

	@At
	@Ok("tp:admin:index")
	public void index(){
		
	}
}
