package org.gson.nutblog.ext;

import org.nutz.ioc.Ioc;
import org.nutz.mvc.View;
import org.nutz.mvc.ViewMaker;

public class FreemarkerViewMaker implements ViewMaker {
	
	@Override
	public View make(Ioc ioc, String type, String value) {
		if("tp".equalsIgnoreCase(type)){
			return new FreemarkerView(value);
		}
		return null;
	}
}
