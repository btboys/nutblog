package org.gson.nutblog.util;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import org.nutz.dao.Dao;
import org.nutz.ioc.impl.PropertiesProxy;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.Mvcs;

import freemarker.template.Configuration;

public final class Utils {
	private static final Log log = Logs.getLog(Utils.class);

	public static Configuration cfg;

	/**
	 * 判断文件是否存在
	 * 
	 * @param path
	 *            文件路径
	 * @return
	 */
	public static Boolean fileExists(String path) {
		File f = new File(path);
		return f.exists();
	}

	/**
	 * 获取option
	 * 
	 * @param optionName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static Object getOption(String optionName) {
		Cache cache = Cache.getInstance();
		HashMap<String, Object> options = cache.readCache(HashMap.class,
				"options");
		return options.get(optionName);
	}

	/**
	 * MD5 加密
	 * 
	 * @author gson
	 * @param str
	 *            加密字符串
	 * @return
	 */
	public static String md5(String str) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("MD5");
			messageDigest.reset();
			messageDigest.update(str.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			if (log.isErrorEnabled())
				log.error(e.getMessage());
		} catch (UnsupportedEncodingException e) {
			if (log.isErrorEnabled())
				log.error(e.getMessage());
		}
		byte[] byteArray = messageDigest.digest();
		StringBuffer md5StrBuff = new StringBuffer();
		for (int i = 0; i < byteArray.length; i++) {
			if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
				md5StrBuff.append("0").append(
						Integer.toHexString(0xFF & byteArray[i]));
			} else {
				md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
			}
		}
		return md5StrBuff.toString();
	}

	/**
	 * 生成Gravatar头像
	 * 
	 * @param email
	 * @param size
	 * @param d
	 * @param g
	 * @return
	 */
	public static String getGravatar(String email, String size, String d,
			String g) {
		String hash = md5(email);
		String avatar = "http://www.gravatar.com/avatar/";
		avatar += hash + "?s=" + (Strings.isEmpty(size) ? 40 : size) + "&d="
				+ (Strings.isEmpty(d) ? "mm" : d) + "&r="
				+ (Strings.isEmpty(g) ? "g" : g);
		return avatar;
	}

	/**
	 * 取数组最小值
	 * @param nums
	 * @return
	 */
	public static int min(int... nums) {
		if (null == nums || nums.length == 0)
			return 0;
		int re = nums[0];
		for (int i = 1; i < nums.length; i++) {
			if (nums[i] < re)
				re = nums[i];
		}
		return re;
	}
	
	/**
	 * 数据库前缀
	 * @return
	 */
	public static String geDbtPrefix(){
		String prefix = "nut_";
		if (Mvcs.getIoc().has("config")) {
			PropertiesProxy pp = Mvcs.getIoc().get(PropertiesProxy.class,"config");
			prefix = pp.get("db-prefix");
		}
		return prefix;
	}
	
	/**
	 * 获取dao
	 * @return
	 */
	public static Dao getDao(){
		Dao dao = null;
		if (Mvcs.getIoc().has("dao")) {
			dao = Mvcs.getIoc().get(Dao.class, "dao");
		}
		return dao;
	}
	
	/**
	 * 获取Controler
	 * @param clazz
	 * @return
	 */
	public static <T> T getControler(Class<T> clazz){
		return Mvcs.getIoc().get(clazz);
	}
	
	/**
	 * 判断是否是post请求
	 * @return
	 */
	public static Boolean isPost(){
		return Mvcs.getReq().getMethod().equalsIgnoreCase("post");
	}
	
	/**
	 * 设置标题
	 * @param title
	 */
	public static void setTopTitle(String title){
		Mvcs.getReq().setAttribute("topTitle", title);
	}

	private Utils() {
	}
	
	public static void main(String[] args) {
		System.out.println(Utils.md5("1234"));
	}
}
