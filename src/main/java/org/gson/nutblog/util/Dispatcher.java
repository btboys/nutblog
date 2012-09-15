/**
 * @copyright (c) NutBlog All Rights Reserved
 */
package org.gson.nutblog.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.nutz.json.Json;

/**
 * 路由分发器
 * <dl><dt><b>%year%</b>&nbsp;</dt><dd> 文章发表的年份，四位数，如 <tt>2004</tt>
 * </dd><dt><b>%monthnum%</b>&nbsp;</dt><dd> 月份，如 <tt>05</tt>
 * </dd><dt><b>%day%</b>&nbsp;</dt><dd> 天，如 <tt>28</tt>
 * </dd><dt><b>%postname%</b>&nbsp;</dt><dd> 文章标题的别名 (编辑文章/页面时的<i>别名栏</i>)。 
 * </dd><dt><b>%post_id%</b>&nbsp;</dt><dd> 文章的唯一ID，如 <tt>423</tt>
 * </dd><dt><b>%category%</b>&nbsp;</dt><dd> 分类的别名 (新建/编辑分类时的<i>别名栏</i>)。 有层级关系的类型在链接地址里就像有层级的目录。   <b>出于性能原因，强烈不建议使用%category%作为链接地址的开头</b>。
 * </dd><dt><b>%author%</b>&nbsp;</dt><dd> 作者的别名。
 * </dd></dl>
 * @author gson
 *
 */
public final class Dispatcher {
 
	private static List<String> alias = Arrays.asList(new String[]{"%year%","%monthnum%","%day%","%postname%","%post_id%","%category%","%author%"});
	
	public static String reverse(String uri){
		String rewrite = Utils.getOption("urlRewrite").toString();
		String split = rewrite.replace("%.+?%", " ");
		
		/*String[] urlTrunk = uri.split("/");
		String[] rewriteTrunk = rewrite.split("/");
		if(urlTrunk.length != rewriteTrunk.length)
			return null;
		for (String trunk : rewriteTrunk) {
			if(alias.contains(trunk)){
				
			}
		}*/
		
		return null;
	}
	
	public static void main(String[] args) {
		String rewrite = "mytest/%year%-%monthnum%-%postname%/%post_id%";
		String uri = "mytest/2012-09-hello-world/1";
		String split = rewrite.replaceAll("%.+?%", " ");
		System.out.println(Json.toJson(split.split(" ")));
		List<String> temp = new ArrayList<String>();
		for (String string : split.split(" ")) {
			String[] sp = uri.split(string);
			temp.add(sp[0]);
			uri = sp[1];
		}
		
		System.out.println(Json.toJson(temp));
	}
}
