package org.gson.nutblog;

import org.gson.nutblog.ext.FreemarkerViewMaker;
import org.gson.nutblog.ext.MvcSetup;
import org.nutz.mvc.annotation.At;
import org.nutz.mvc.annotation.Encoding;
import org.nutz.mvc.annotation.IocBy;
import org.nutz.mvc.annotation.Localization;
import org.nutz.mvc.annotation.Modules;
import org.nutz.mvc.annotation.Ok;
import org.nutz.mvc.annotation.SetupBy;
import org.nutz.mvc.annotation.Views;
import org.nutz.mvc.ioc.provider.ComboIocProvider;


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
	
	public static void main(String[] args) {
		/*Matcher m = Pattern.compile("^/admin/?$",Pattern.CASE_INSENSITIVE).matcher("/admin");
		System.out.println(m.find());*/
	}
}