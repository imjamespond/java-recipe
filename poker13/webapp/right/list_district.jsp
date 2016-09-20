<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="
java.util.Map,
java.util.Map.Entry,
java.util.HashMap,
java.util.Iterator,
java.util.List,
cn.gecko.commons.model.Pageable,
com.chitu.chess.data.StaticDistrictTypes,
com.chitu.chess.model.PersistChessPlayer,
com.chitu.chess.model.ChessUtils,
com.chitu.chess.model.ChessUtils4Web
" %>

<%
	
	String pageNum_ = request.getParameter("pageNum");
	int pageNum = pageNum_==null?1:Integer.valueOf(pageNum_);
	int pageSize = 20;

	String where = "";
	String params = "&page="+request.getParameter("page");
	
	String bdate = request.getParameter("bdate");
	bdate = bdate==null?"":bdate;
	String edate = request.getParameter("edate");
	edate = edate==null?"":edate;
	String keyword = request.getParameter("keyword");
	if(keyword==null ){
		keyword = "";
	}else{
		if(keyword.length() > 0 ){
			where += " and accountId like '%" + keyword + "%'";
			params += "&keyword="+keyword;		
		}
	}
	String keyword1 = request.getParameter("keyword1");
	if(keyword1==null ){
		keyword1 = "";
	}else{
		if(keyword1.length() > 0 ){
			where += " and nickname like '%" + keyword1 + "%'";
			params += "&keyword1="+keyword1;		
		}
	}
	

	
	
%>

<script type="text/javascript" language="javascript">
window.onload=function(){
document.getElementById("list_player_header").innerHTML				=list_player_header;
document.getElementById("list_player_interpret").innerHTML			=list_player_interpret;

document.getElementById("money").innerHTML							=money;
document.getElementById("point").innerHTML							=point;
document.getElementById("stime").innerHTML							=stime;
document.getElementById("etime").innerHTML							=etime;

}

$(function(){
	$('.mission').click(function(){
		var val	= $(this).attr("val");
		alert(val);
		//$.post('right/ajax.jsp',{"action":"mission","id":id,},function(data){
			//alert(data);
			//location.reload();
		//});
	});
	$('.item').click(function(){
		var val	= $(this).attr("val");
		alert(val);
	});
});
</script>
<div class="itemTitle">
	<h1 id='list_player_header'>遊戲統計 - 遊戲貝幣充值記錄列表</h1>
</div>
<div class="container">
<p id='list_player_interpret'>說明：遊戲貝幣充值記錄列表</p>
<form action="" name="player_form1" id="player_form1" method="post">
<table class="tb tblist">
	<tr>
	<td>
	<td>

	</td>
	</td>
	</tr>
</table>
</form>

<table class="tb tblist">
    <thead>
        <tr class="tbcap nowarp">
            <th  class="tc"><span id='newid'>id</span></th>
            <th class="tc"><span id='username'>name</span></th>
			<th class="tc"><span id='rolename'></span></th>  
        </tr>
    </thead>
    <tbody>
	<%
		for (Entry<Integer, StaticDistrictTypes> entry : StaticDistrictTypes.getAll().entrySet()) {
			int mid = entry.getKey();
			StaticDistrictTypes StaticMission1 = entry.getValue();
			String name = StaticMission1.getName();

    %>
    	<tr>
            <td class="tbold" align="center"><%= mid %></td>      				
            <td class="tbold" align="center"><a herf="<%= mid%>"><%= name %></a></td>
			<td class="tbold" align="center"><%= name %></td>
        </tr>
	<%
		}
    %>
    </tbody>
</table>

<table align = "center">
<tr>
<td>
第

页
</td>
</tr>
</table>

</div>

