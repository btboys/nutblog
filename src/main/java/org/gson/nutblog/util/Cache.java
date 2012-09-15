package org.gson.nutblog.util;

import java.io.BufferedWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.entity.Record;
import org.nutz.dao.sql.Sql;
import org.nutz.dao.sql.SqlCallback;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.json.Json;
import org.nutz.json.JsonFormat;
import org.nutz.lang.Files;
import org.nutz.lang.Maths;
import org.nutz.lang.Strings;
import org.nutz.lang.Times;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;
public final class Cache {
	private static final Log log = Logs.getLog(Cache.class);
	private static String cacheDir;
	private static Dao dao;
	private static String prefix = "nut_";
	
	public static final String OPTIONS = "options";
	public static final String USER = "user";
	public static final String STATE = "state";
	public static final String TAGS = "tags";
	public static final String LOGALIAS = "logalias";
	public static final String LOGTAGS = "logtags";
	public static final String RECORD = "record";
	public static final String SORT = "sort";
	private static final String LINK = "link";
	

	private Cache() {
	}

	private static Cache instance;

	public synchronized static Cache getInstance() {
		if (instance != null) {
			return instance;
		}
		if (Mvcs.getIoc().has("dao")) {
			dao = Mvcs.getIoc().get(Dao.class, "dao");
		}
		if (Mvcs.getIoc().has("config")) {
			PropertiesProxy pp = Mvcs.getIoc().get(PropertiesProxy.class,
					"config");
			prefix = pp.get("db-prefix");
		}
		cacheDir = Mvcs.getServletContext().getRealPath(
				"/WEB-INF/content/cache/");
		instance = new Cache();
		return instance;
	}

	/**
	 * 更新缓存
	 * 
	 * @param cacheNames
	 */
	public void updateCache(String... cacheNames) {
		if (dao == null) {
			return;
		}

		if (cacheNames == null || cacheNames.length == 0) {
			Method[] methods = this.getClass().getDeclaredMethods();
			for (Method method : methods) {
				if (method.getName().startsWith("c_")) {
					method.setAccessible(true);
					try {
						log.info("开始生成"+method.getName()+"缓存！");
						method.invoke(this);
						log.info(method.getName()+"缓存已经生成！");
					} catch (IllegalArgumentException e) {
						log.info("NutBlog Cache IllegalArgumentException:"
								+ e.getMessage());
					} catch (IllegalAccessException e) {
						log.info("NutBlog Cache IllegalAccessException:"
								+ e.getMessage());
					} catch (InvocationTargetException e) {
						log.info("NutBlog Cache InvocationTargetException:"
								+ e.getMessage());
					}
				}
			}
		} else {
			for (String cacheName : cacheNames) {
				try {
					Method method = this.getClass().getDeclaredMethod("c_" + cacheName);
					log.info("开始生成"+method.getName()+"缓存！");
					method.invoke(this);
					log.info(method.getName()+"缓存已经生成！");
				} catch (SecurityException e) {
					log.info("NutBlog Cache SecurityException:"
							+ e.getMessage());
				} catch (NoSuchMethodException e) {
					log.info("NutBlog Cache NoSuchMethodException:"
							+ e.getMessage());
				} catch (IllegalArgumentException e) {
					log.info("NutBlog Cache IllegalArgumentException:"
							+ e.getMessage());
				} catch (IllegalAccessException e) {
					log.info("NutBlog Cache IllegalAccessException:"
							+ e.getMessage());
				} catch (InvocationTargetException e) {
					log.info("NutBlog Cache InvocationTargetException:"
							+ e.getMessage());
				}
			}
		}
	}

	public <T> T readCache(Class<T> clazz, String cacheName) {
		String c = Files.read(cacheDir + "/" + cacheName);
		return Json.fromJson(clazz, c);
	}

	void c_options() {
		List<Record> r = query("options",null);
		HashMap<String, String> op = new HashMap<String, String>();
		for (Record record : r) {
			String key = record.getString("option_name");
			String value = record.getString("option_value");
			op.put(key, value);
		}
		cacheWrite(op, Cache.OPTIONS);
	}
	
	void c_user(){
		List<Record> r = query("user",null);
		HashMap<Integer, Record> us = new HashMap<Integer, Record>();
		for (Record record : r) {
			record.remove("password");
			record.remove("role");
			if(Strings.isEmpty(record.getString("nickname"))){
				record.set("nickname", record.get("username"));
			}
			us.put(record.getInt("uid"), record);
		}
		cacheWrite(us, Cache.USER);
	}
	
	/**
	 * 站点信息统计
	 */
	void c_state(){
		String sql = "SELECT 'lognum' AS types, count(*) num FROM " + prefix + "blog WHERE type = 'blog' AND hide = 'n' UNION SELECT 'draftnum' AS types, count(*) FROM " + prefix + "blog WHERE type = 'blog' AND hide = 'y' UNION SELECT 'comnum' AS types, count(*) FROM " + prefix + "comment WHERE hide = 'n' UNION SELECT 'hidecom' AS types, count(*) FROM " + prefix + "comment WHERE hide = 'y' UNION SELECT 'tbnum' AS types, count(*) FROM " + prefix + "trackback";
		List<Record> globalr = query(sql);
		HashMap<String, Integer> global = new HashMap<String, Integer>();
		for (Record record : globalr) {
			global.put(record.getString("types"), record.getInt("num"));
		}
		
		HashMap<String, HashMap<String, Integer>> state = new HashMap<String, HashMap<String, Integer>>();
		
		state.put("global", global);
		List<Record> r = query("user",null);
		for (Record record : r) {
			int uid = record.getInt("uid");
			sql = "SELECT 'lognum' AS types, count(*) AS num FROM " + prefix + "blog WHERE type = 'blog' AND hide = 'n' AND author = "+uid+" UNION SELECT 'draftnum' AS types, count(*) AS num FROM " + prefix + "comment AS COMMENT, " + prefix + "blog AS blog WHERE COMMENT .gid = blog.gid AND blog.type = 'blog' AND blog.hide = 'y' AND blog.author = "+uid+" UNION SELECT 'comnum' AS types, count(*) AS num FROM " + prefix + "comment AS COMMENT, " + prefix + "blog AS blog WHERE COMMENT .gid = blog.gid AND blog.author = "+uid+" UNION SELECT 'hidecom' AS types, count(*) AS num FROM " + prefix + "comment AS COMMENT, " + prefix + "blog AS blog WHERE COMMENT .gid = blog.gid AND COMMENT .hide = 'y' AND blog.author = "+uid+" UNION SELECT 'tbnum' AS types, count(*) AS num FROM " + prefix + "trackback AS trackback, " + prefix + "blog AS blog WHERE trackback.gid = blog.gid AND blog.author = "+uid;
			globalr = query(sql);
			for (Record temp : globalr) {
				global.put(temp.getString("types"), temp.getInt("num"));
			}
			state.put(record.get("uid").toString(), global);
		}
		cacheWrite(state, Cache.STATE);
	}
	
	void c_tags(){
		String sql = "SELECT count(1) num,tag.tagname from " + prefix + "tag as tag ," + prefix + "blog as blog where tag.gid = blog.gid and blog.hide = 'n' GROUP BY tagname";
		List<Record> r = query(sql);
		int[] nums = new int[r.size()];
		for (int i = 0; i < r.size(); i++) {
			nums[i] = r.get(i).getInt("num");
		}
		int min = Utils.min(nums);
		int spread = (r.size() > 12?12:r.size());
		int rank = Maths.max(nums) - min;
		rank = (rank == 0?1:rank);
		rank = spread / rank;
		List<Record> ntag = new ArrayList<Record>();
		for (Record record : r) {
			try {
				record.put("tagurl", URLEncoder.encode(record.getString("tagname"), "UTF-8"));
				record.put("fontsize", 10+(r.size()-min)*rank);
				ntag.add(record);
			} catch (UnsupportedEncodingException e) {
				log.info(record.getString("tagname")+"转换失败！UnsupportedEncodingException:"+e.getMessage());
				continue;
			}
		}
		cacheWrite(ntag, Cache.TAGS);
	}
	
	void c_link(){
		String sql = "SELECT siteurl,sitename,description FROM " + prefix + "link WHERE hide='n' ORDER BY taxis ASC";
		List<Record> r = query(sql);
		cacheWrite(r, Cache.LINK);
	}
	
	void c_logalias(){
		String sql = "SELECT gid,alias FROM " + prefix + "blog where alias!=''";
		HashMap<String, HashMap<String, String>> cacheData = new HashMap<String, HashMap<String, String>>();
		HashMap<String, String> keyAlias = new HashMap<String, String>();
		HashMap<String, String> aliasKey = new HashMap<String, String>();
		List<Record> r = query(sql);
		for (Record record : r) {
			keyAlias.put(record.getString("gid"), record.getString("alias"));
			aliasKey.put(record.getString("alias"), record.getString("gid"));
		}
		cacheData.put("keyAlias", keyAlias);
		cacheData.put("aliasKey", aliasKey);
		cacheWrite(cacheData, Cache.LOGALIAS);
	}
	
	void c_logtags(){
		List<Record> r = query("tag",null);
		HashMap<Integer, List<String>> cacheData = new HashMap<Integer,  List<String>>();
		for (Record record : r) {
			if(cacheData.containsKey(record.get("gid"))){
				cacheData.get(record.get("gid")).add(record.getString("tagname"));
			}else{
				 List<String> tags = new ArrayList<String>();
				 tags.add(record.getString("tagname"));
				 cacheData.put(record.getInt("gid"), tags);
			}
		}
		cacheWrite(cacheData,Cache.TAGS);
	}
	
	void c_record(){
		String sql = "select date from " + prefix + "blog WHERE hide='n' and type='blog' ORDER BY date DESC";
		List<Record> r = query(sql);
		HashMap<Integer, HashMap<Integer, Integer>> years = new HashMap<Integer,  HashMap<Integer, Integer>>();
		
		for (Record record : r) {
			Calendar cd = Times.C(Long.parseLong(record.getString("date")));
			int year = cd.get(Calendar.YEAR);
			int month = cd.get(Calendar.MONTH);
			if(years.containsKey(year)){
				 HashMap<Integer, Integer> months = years.get(year);
				if(months.containsKey(month)){
					int tm = months.get(month);
					months.put(month, tm+1);
				}else{
					months.put(month, 1);
				}
			}else{
				 HashMap<Integer, Integer> months = new HashMap<Integer, Integer>();
				 months.put(month, 1);
				 years.put(year, months);
			}
		}
		cacheWrite(years, Cache.RECORD);
	}
	
	void c_sort(){
		String sql = "SELECT count(1) num,sort.* FROM "+prefix+"blog blog,"+prefix+"sort sort where blog.sortid = sort.sid GROUP BY sort.sortname ORDER BY sort.taxis";
		List<Record> r = query(sql);
		cacheWrite(r, Cache.SORT);
	}

	
	private List<Record> query(String table,Cnd cnd){
		List<Record> r = dao.query(prefix + table, cnd);
		if(r == null)
			return new ArrayList<Record>();
		return r;
	}
	
	private List<Record> query(String sql){
		Sql sqls = Sqls.create(sql);
		sqls.setCallback(new SqlCallback() {
	            public Object invoke(Connection conn, ResultSet rs, Sql sql) throws SQLException {
	                    List<Record> list = new LinkedList<Record>();
	                    while (rs.next()){
	                    	Record r = new Record();
	                    	int count = rs.getMetaData().getColumnCount();
	                    	for (int i = 1; i <= count; i++) {
	                    		String cname = rs.getMetaData().getColumnName(i);
	                    		r.put(cname, rs.getObject(i));
							}
	                    	list.add(r);
	                    }
	                    return list;
	            }
	    });
		dao.execute(sqls);
		return sqls.getList(Record.class);
	}


	private void cacheWrite(Object cacheData, String cacheName) {
		StringWriter sw = new StringWriter();
		BufferedWriter bfw = new BufferedWriter(sw);
		Json.toJson(bfw, cacheData, JsonFormat.compact());
		Files.write(cacheDir + "/" + cacheName, sw.toString());
	}
}
