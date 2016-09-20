<%@page contentType="text/html;charset=utf-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
<head>
<title>千寻游戏-初始化游戏</title>
</head>
<body>
<div class="wrapper">
	<div style="overflow:hidden">
		<input type="hidden" value="${type}" id="gameType">
		<input type="hidden" value="${rank}" id="gameRank">
		<select multiple="multiple" id="select1"  class="form-control" style="width: 170px;height:320px;float: left;">
			<c:forEach items="${rankList}" var="rank">
				<option value="${rank.gid}">${rank.gname}</option>
			</c:forEach>
		</select>
		<div class="operate" style="width:90px;margin:0 10px;float:left;text-align:center;">
			<button type="button" id="add" class="btn btn-default" style="width:74px;padding: 2px 5px;font-size:12px;margin-bottom: 3px;">添加》</button>
			<button type="button" id="add_all" class="btn btn-default" style="width:74px;padding: 2px 5px;font-size:12px;margin-bottom: 3px;">全部添加》</button>
			<button type="button" id="remove" class="btn btn-default" style="width:74px;padding: 2px 5px;font-size:12px;margin-bottom: 3px;">《删除</button>
			<button type="button" id="remove_all" class="btn btn-default" style="width:74px;padding: 2px 5px;font-size:12px;">《全部删除</button>
		</div>
		<select multiple="multiple" id="select2" class="form-control" style="width: 170px;height:320px;float: left;">
		</select>
	</div>
	<button class="btn btn-default" style="margin-top:10px;" onclick="sendResult()">提交</button>
</div>
<script type="text/javascript">
$(document).ready(function(){
	bindSel();
});
function sendResult(){
	var list = $('#select2 option'),
		str = "",
		type = $("#gameType").val(),
		rank = $("#gameRank").val();
	for(i=0;i<list.length;i++){
		str += list.eq(i).val()+",";
	}
	var rankList = str.substring(0,str.lastIndexOf(','));
	$.ajax({
		url : "/game/changeRank",
		type : "post",
		data : {
			type : type,
			rank : rank,
			rankList : rankList
		},
		success : function(data){
			if(data == "1"){
				location.href="/game/rank"
			}
		},
		error : function(){
			alert("服务器错误！请稍后再试!");
		}
	});
}
function bindSel(){
	//移到右边
    $('#add').click(function() {
        $('#select1 option:selected').appendTo('#select2');
    });
    //移到左边
    $('#remove').click(function() {
        $('#select2 option:selected').appendTo('#select1');
    });
    //全部移到右边
    $('#add_all').click(function() {
        $('#select1 option').appendTo('#select2');
    });
    //全部移到左边
    $('#remove_all').click(function() {
        $('#select2 option').appendTo('#select1');
    });
    //双击选项
    $('#select1').dblclick(function(){
        $("option:selected",this).appendTo('#select2'); 
    });
    //双击选项
    $('#select2').dblclick(function(){
       $("option:selected",this).appendTo('#select1');
    });
}
</script>
</body>
</html>