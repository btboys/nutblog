<!DOCTYPE html>
<html>
  <head>
  	<base href="${BLOG_URL}">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta http-equiv="Content-Language" content="${locale}" />
	<meta name="author" content="nutblog" />
	<meta name="robots" content="noindex, nofollow">
	<meta http-equiv="X-UA-Compatible" content="IE=8" />
	<title>NutBlog登陆</title>
	<link href="public/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<link href="public/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet">
	<link href="public/style/admin.css" rel="stylesheet">
	
    <script type="text/javascript" src="public/javascript/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="public/bootstrap/js/bootstrap.min.js"></script>
    
    <script type="text/javascript">
    	$(function(){
    		var loging = false;
    		$("#doLogin").on("click",function(){
    			if(loging) return;
    			var userName = $("#userName").val();
    			var password = $("#password").val();
    			if(!$.trim(userName)){
    				$("#msg").text("请输入账号！");
    				$("#userName").focus();
    				return;
    			}
    			if(!$.trim(password)){
    				$("#msg").text("请输入密码！");
    				$("#password").focus();
    				return;
    			}
    			
    			loging = true;
    			$("#msg").text("认证中...");
    			$.post("doLogin.nut",{userName:userName,password:password},function(resp){
    				if(resp){
    					window.location.replace("admin");
    				}else{
    					$("#msg").text("认证失败！用户名密码不匹配！");
    					$("#userName").focus().select();
    					loging = false;
    				}
    			},"JSON");
    		});
    		$("#userName").focus();
    		$("input").keydown(function(e){
    			if(e.keyCode == 13){
    				$("#doLogin").click();
    			}
    		});
    	});
    </script>
  </head>
  <body style="background-color: whiteSmoke;">
 	<div class="modal"  role="dialog">
	  <div class="modal-header">
	    <h3>NutBlog登陆</h3>
	  </div>
	  <div class="modal-body">
	  	<form class="form-horizontal">
		  <div class="control-group">
		    <label class="control-label" for="userName">账	号</label>
		    <div class="controls">
		      <input id="userName" name="userName"  type="text" placeholder="请输入账号.."/>
		    </div>
		  </div>
		  <div class="control-group">
		    <label class="control-label" for="password">密	码</label>
		    <div class="controls">
		     <input id="password" name="password" type="password" placeholder="请输入密码.."/>
		    </div>
		  </div>
		  <div class="control-group">
		    <div class="controls">
		      <label class="checkbox">
		        <input type="checkbox" name="remenber" value="1"> 记住登陆
		      </label>
		    </div>
		  </div>
		</form>
	  </div>
	  <div class="modal-footer">
	  	<b id="msg"></b>
	    <a href="javascript:void(0)" class="btn btn-primary" id="doLogin">登	陆</a>
	  </div>
	</div>
  </body>
  </html>