<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>英拓備案管理系統</title>
<link href="css/left.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" href="js/zTreeStyle/zTreeStyle.css" type="text/css" /> 
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/jquery.ztree.core-3.5.min.js"></script> 
<script type="text/javascript">
	var setting = {
		data:{
			key:{
				url:"url"
			},
			simpleData:{
				enable:true,
				idKey:"id",
				pidKey:"pId"
			}
		}
	};
	var zNodes =[
		{ id:1, pId:0, name:"基本资讯"},
		{ id:2, pId:1, name:"玩家列表","url":"./right.jsp?page=list_player.jsp","target":"right", },
		{ id:2, pId:1, name:"npc玩家列表","url":"./right.jsp?page=list_npcplayer.jsp","target":"right", },
		{ id:3, pId:1, name:"游戏区列表","url":"./right.jsp?page=list_districts.jsp","target":"right", },
		{ id:3, pId:1, name:"统计数据","url":"./right.jsp?page=list_statistic.jsp","target":"right", },
		//{ id:3, pId:1, name:"父节点3 - 没有子节点", isParent:true}
	];

	$(document).ready(function(){
		$.fn.zTree.init($("#tree"), setting, zNodes);
	});
</script>
</head>
<body>

<div class="subNav">
	<div class="title"><img src="images/subNav.jpg" /></div>   
	<div class="zTreeDemoBackground left"><ul id="tree" class="ztree"></ul></div>	   
</div>
</body>
</html>