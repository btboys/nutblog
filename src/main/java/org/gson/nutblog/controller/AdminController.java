package org.gson.nutblog.controller;

import org.gson.nutblog.util.Utils;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.entity.Record;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;

@IocBean
public class AdminController {

	@Inject
	private Dao dao;
	
	/**
	 * 用户认证
	 * @param userName
	 * @param password
	 * @return
	 */
	public Record checkUser(String userName,String password){
		Record rcd =dao.fetch(Utils.geDbtPrefix()+"user", Cnd.where("username","=",userName).and("password","=",Utils.md5(password)));
		return rcd;
	}
}
