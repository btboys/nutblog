<!DOCTYPE html>
<html>
  <head>
  	<base href="${BLOG_URL}">
    <meta http-equiv="content-type" content="text/html; charset=UTF-8">
    <meta http-equiv="Content-Language" content="${locale}" />
	<meta name="author" content="nutblog" />
	<meta name="robots" content="noindex, nofollow">
	<meta http-equiv="X-UA-Compatible" content="IE=8" />
	<title>NutBlog管理</title>
	<link href="public/bootstrap/css/bootstrap.min.css" rel="stylesheet">
	<link href="public/bootstrap/css/bootstrap-responsive.min.css" rel="stylesheet">
	<link href="public/style/admin.css" rel="stylesheet">
	
    <script type="text/javascript" src="public/javascript/jquery-1.7.2.min.js"></script>
    <script type="text/javascript" src="public/bootstrap/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="public/javascript/jquery.hashable.js"></script>
    <script type="text/javascript">
	    $(function(){
	    	$(window).hashChange(function(newHash,oldHash){
	    		$(".container").html(newHash+oldHash);
	    	});
	    });
    </script>
  </head>
  <body>
  <div class="navbar navbar-inverse navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container-fluid">
          <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </a>
          <a class="brand" href="admin">NutBlog管理</a>
          <div class="nav-collapse collapse">
            <ul class="nav pull-right">
              	<li class="divider-vertical"></li>
              	<li><a href="admin#setting" class="navbar-link">设置</a></li>
              	<li class="divider-vertical"></li>
              	<li><a href="doLogout.nut" class="navbar-link">退出</a></li>
            </ul>
            <p class="navbar-text pull-right">
           		您好,<a href="javascript:void(0)" class="navbar-link">${session.user.username}</a>
            </p>
            <ul class="nav">
              <li class="dropdown">
                <a href="javascript:void(0)" class="dropdown-toggle" data-toggle="dropdown">文章 <b class="caret"></b></a>
                <ul class="dropdown-menu">
                  <li><a href="admin#wirte">写文章</a></li>
                  <li><a href="admin#draf">草稿</a></li>
                  <li class="divider"></li>
                  <li><a href="admin#sort">分类</a></li>
                  <li><a href="admin#tag">标签</a></li>
                  <li><a href="admin#comment">评论</a></li>
                </ul>
              </li>
              <li class="dropdown">
                <a href="javascript:void(0)" class="dropdown-toggle" data-toggle="dropdown">资源 <b class="caret"></b></a>
                <ul class="dropdown-menu">
                   <li><a href="admin#upload">上传</a></li>
                  <li class="divider"></li>
                  <li><a href="admin#pic">图库</a></li>
                  <li><a href="admin#attr">附件</a></li>
                </ul>
              </li>
              <li class="dropdown">
                <a href="javascript:void(0)" class="dropdown-toggle" data-toggle="dropdown">站点 <b class="caret"></b></a>
                <ul class="dropdown-menu">
                  <li><a href="admin#link">链接</a></li>
                  <li><a href="admin#page">页面</a></li>
                  <li><a href="admin#user">用户</a></li>
                </ul>
              </li>
              <li class="dropdown">
                <a href="javascript:void(0)" class="dropdown-toggle" data-toggle="dropdown">外观 <b class="caret"></b></a>
                <ul class="dropdown-menu">
                  <li><a href="admin#theme">主题</a></li>
                  <li><a href="admin#silder">边栏组件</a></li>
                  <li><a href="admin#menuedit">菜单编辑</a></li>
                  <li><a href="admin#themeoption">主题选项</a></li>
                  <li><a href="admin#themeedit">主题编辑</a></li>
                   <li><a href="admin#bgedit">背景编辑</a></li>
                </ul>
              </li>
               <li class="dropdown">
                <a href="javascript:void(0)" class="dropdown-toggle" data-toggle="dropdown">插件 <b class="caret"></b></a>
                <ul class="dropdown-menu">
                  <li><a href="admin#install">安装插件</a></li>
                  <li><a href="admin#pluginlist">插件列表</a></li>
                </ul>
              </li>
            </ul>
          </div><!--/.nav-collapse -->
        </div>
      </div>
    </div>
    
    <div class="container">