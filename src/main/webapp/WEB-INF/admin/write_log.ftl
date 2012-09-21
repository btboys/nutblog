<@INCLUDE path="header.ftl"/>
<script type="text/javascript" charset="utf-8">
	    window.UEDITOR_HOME_URL = "${BLOG_URL}public/ueditor/";
	    $(function(){
	    	var content=new UE.ui.Editor();
   			content.render('content');
   			var excerpt=new UE.ui.Editor();
   			excerpt.render('excerpt');
	    });
</script>
<script type="text/javascript" src="public/ueditor/editor_config.js"></script>
<script type="text/javascript" src="public/ueditor/editor_all_min.js"></script>
<link rel="stylesheet" type="text/css" href="public/ueditor/themes/default/ueditor.css"/>
<div class="postbtn">
<button type="button" class="btn">保存草稿</button>
<button type="button" class="btn btn-primary">发布</button>
</div>
<form> 
   <div class="input-prepend"> 
    <span class="add-on">文章标题</span>
    <input id="title" name="title" class="span11" type="text" placeholder="请输入标题.." /> 
   </div> 
   <div class="input-prepend" style="overflow: hidden;white-space: normal;"> 
    <span class="add-on" style="float: left;">文章内容</span> 
    <textarea rows="3" style="margin-left:0;" placeholder="请输入内容.." class="span11" name="content" id="content"></textarea> 
   </div> 
   <div class="row"> 
    <div class="span6"> 
     <div class="input-prepend"> 
      <span class="add-on">文章分类</span> 
      <select class="span2" placeholder="请选择分类"> <option value="0">请选择分类</option> </select> 
     </div> 
    </div> 
    <div class="span6"> 
     <div class="input-prepend"> 
      <span class="add-on">访问密码</span>
      <input type="text" id="password" name="password" class="span2" /> 
      <label class="checkbox inline" style="margin-left: 10px;"> <input type="checkbox" id="top" name="top" value="y" /> 日志置顶 </label> 
      <label class="checkbox inline"> <input type="checkbox" id="allowcomment" name="allowcomment" value="y" /> 允许评论 </label> 
     </div> 
    </div> 
   </div> 
   <div class="input-prepend"> 
    <span class="add-on">文章标签</span> 
    <input id="tag" name="tag" class="span11" type="text" placeholder="逗号或空格分隔" /> 
   </div> 
   <div class="input-prepend"> 
    <span class="add-on">链接别名</span>
    <input type="text" id="alias" name="alias" class="span11" placeholder="用于自定义日志链接。需要启用链接别名"/> 
   </div> 
   <div class="input-prepend"> 
    <span class="add-on">引用通告</span>
    <textarea rows="3" placeholder="每行一条引用地址" class="span11" name="pingurl" id="pingurl"></textarea> 
   </div> 
   <div class="input-prepend" style="overflow: hidden;white-space: normal;"> 
    <span class="add-on" style="float: left;">文章摘要</span> 
    <textarea rows="3" style="margin-left:0;" placeholder="请输入内容.." class="span11" name="excerpt" id="excerpt"></textarea> 
   </div> 
  </form>
<@INCLUDE path="footer.ftl"/>