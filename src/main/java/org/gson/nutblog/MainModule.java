package org.gson.nutblog;

import javax.servlet.http.HttpServletRequest;

import org.gson.nutblog.controller.AdminController;
import org.gson.nutblog.ext.FreemarkerView;
import org.gson.nutblog.ext.FreemarkerViewMaker;
import org.gson.nutblog.ext.MvcSetup;
import org.gson.nutblog.util.Utils;
import org.nutz.dao.entity.Record;
import org.nutz.mvc.View;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Encoding;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Localization;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.Param;
import org.nutz.mvc.annotation.SetupBy;
import org.nutz.mvc.annotation.Views;
import org.nutz.mvc.ioc.provider.ComboIocProvider;
import org.nutz.mvc.view.ServerRedirectView;


@SetupBy(value=MvcSetup.class)
@IocBy(type = ComboIocProvider.class, args = {
	"*org.nutz.ioc.loader.json.JsonLoader", 
	"ioc/",
	"*org.nutz.ioc.loader.annotation.AnnotationIocLoader", 
	"org.gson.nutblog"
	})
@Encoding(input = "utf8", output = "utf8")
@Modules(scanPackage = true, packages={"org.gson.nutblog"})
@Localization(value="locale",defaultLocalizationKey="zh_CN")
@Views({FreemarkerViewMaker.class})
public class MainModule {

	@At
	@Ok("tp:index")
	public void index(){
		
	}
	
	@At
	public View login(HttpServletRequest req){
		if(req.getSession().getAttribute("user") != null)
			return new ServerRedirectView("/admin");
		return new FreemarkerView("admin:login");
	}
	
	@At
	@Ok("ioc:json")
	public Boolean doLogin(HttpServletRequest req,@Param("userName")  String userName,@Param("password")  String password,@Param("remenber") Boolean remenber){
		AdminController ct = Utils.getControler(AdminController.class);
		Record rcd =ct.checkUser(userName, password);
		if(rcd==null){
			return false;
		}
		req.getSession().setAttribute("user", rcd);
		return true;
	}
	
	@At
	@Ok(">>:/")
	public void doLogout(HttpServletRequest req){
		req.getSession().removeAttribute("user");
	}
}