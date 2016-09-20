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


	int pageNum = 0;
	int pageSize = 500;

	String where = " and npc = 1 ";
	
	String keyword = request.getParameter("keyword");
	String keyword1 = request.getParameter("keyword1");
	String keyword2 = request.getParameter("keyword2");
	
	int num = 0;
	int num1 = 0;
	int num2 = 0;
	if(keyword==null ){
		keyword = "";
	}else{
		num = Integer.valueOf(keyword);
	}
	if(keyword1==null ){
		keyword1 = "";
	}else{
		num1 = Integer.valueOf(keyword1);
	}
	if(keyword2==null ){
		keyword2 = "";
	}else{
		num2 = Integer.valueOf(keyword2);
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
	var reg = /[^-\d]+/;

	$('#createNpcRoom').click(function(){
		var val	= $("#keyword").val();
		var val1 = $("#keyword1").val();
		var val2 = $("#keyword2").val();
		
		if(reg.test(val)){
			alert("请输入数字!");return;
		}
		if(reg.test(val1)){
			alert("请输入数字!");return;
		}
		if(reg.test(val2)){
			alert("请输入数字!");return;
		}	
		
		$.post('right/ajax.jsp',{"action":"createNpcRoom","roomNum":val,"districtId":val1,"playerNum":val2},function(data){
			alert(data);
			location.reload();
		});
	});

	$('#createNpcMatch').click(function(){
		var val	= $("#keyword").val();
		var val1 = $("#keyword1").val();
		var val2 = $("#keyword2").val();
		
		if(reg.test(val)){
			alert("请输入数字!");return;
		}
		if(reg.test(val1)){
			alert("请输入数字!");return;
		}
		if(reg.test(val2)){
			alert("请输入数字!");return;
		}	
		
		$.post('right/ajax.jsp',{"action":"createNpcMatch","districtId":val1,"playerNum":val2},function(data){
			alert(data);
			//location.reload();
		});
	});	
	
	$('#importNpc').click(function(){
	
		$.post('right/ajax.jsp',{"action":"importNpc"},function(data){
			alert(data);
			//location.reload();
		});	
	});
	
	$('#cleanNpc').click(function(){
		var val1 = $("#keyword1").val();
		if(reg.test(val1)){
			alert("请输入数字!");return;
		}	
		$.post('right/ajax.jsp',{"action":"cleanNpc","districtId":val1},function(data){
			alert(data);
			//location.reload();
		});	
	});	
	
	
	
});
</script>
<div class="itemTitle">
	<h1 id='list_player_header'>遊戲統計 - 遊戲貝幣充值記錄列表</h1>
</div>
<div class="container">
<p id='list_player_interpret'>說明：遊戲貝幣充值記錄列表</p>
<table class="tb tblist">
	<tr>
	<td>
	<td>
	<span >添加房间数量：</span>&nbsp;&nbsp;&nbsp;
	<input id="keyword" size="10" value=""/>&nbsp
	<span >游戏区id：</span>&nbsp;&nbsp;&nbsp;
	<input id="keyword1" size="10" value=""/>&nbsp;
	<span >每间房人数：</span>&nbsp;&nbsp;&nbsp;
	<input id="keyword2" size="10" value=""/>&nbsp;

	<input id='createNpcRoom' type="button" value="创建Npc"/> &nbsp;|&nbsp;
	<input id='createNpcMatch' type="button" value="创建比赛Npc"/> &nbsp;|&nbsp;
	<input id='cleanNpc' type="button" value="清除"/>  &nbsp;|&nbsp;
	<input id='importNpc' type="button" value="导入"/> 
	</td>
	</td>
	</tr>
</table>

<table class="tb tblist">
    <thead>
        <tr class="tbcap nowarp">
            <th  class="tc"><span id='newid'>id</span></th>
            <th class="tc"><span id='username'>player account</span></th>
			<th class="tc"><span id='rolename'>player name</span></th>
            <th class="tc"><span id='money'>money</span></th>
			<th class="tc"><span id='point'>point</span></th>
			<th class="tc"><span id='rmb'>rmb</span></th>
			
			<th class="tc"><span id='generalincome'>victory</span></th>
            <th  class="tc"><span id='gameAmount'>gameAmount</span></th>
			<th  class="tc"><span id='continioulyVictory'>continiouly victory</span></th>
			
			<th  class="tc"><span id='mission'>mission</span></th>
            
        </tr>
    </thead>
    <tbody>
	<%
	Pageable<PersistChessPlayer> mypage = PersistChessPlayer.find(pageNum,pageSize,where);//var page is unavailable!
	List<PersistChessPlayer> list = mypage.getElements();
	Iterator<PersistChessPlayer> itr = list.iterator();
	while (itr.hasNext()) {
		PersistChessPlayer p = itr.next();
    %>
    	<tr>
            <td class="tbold" align="center"><div class="numInput" val="<%= p.getId() %>" field='npc'><%= p.getId() %></td>     				
            <td class="tbold" align="center"><%= p.getAccountId() %></td>
			<td class="tbold" align="center"><%= p.getNickname() %></td>
            <td class="tbold" align="center"><div class="numInput" val="<%= p.getId() %>" field='money'><%= p.getMoney() %></td>		
            <td class="tbold" align="center"><%= p.getPoint() %></td>
			<td class="tbold" align="center"><div class="numInput" val="<%= p.getId() %>" field='rmb'><%= p.getRmb() %></td>
			
			<td class="tbold" align="center"><%= p.getVictoryAmount() %></td>
            <td class="tbold" align="center"><%= p.getGameAmount() %></td>
			<td class="tbold" align="center"><%= p.getContinuousVictory() %></td>
			
			<td class="tbold" align="center"><span class="mission" val="
			<%
			Map<Integer, Integer> map = new HashMap<Integer, Integer>();
			p.initMission(map);
			for (Entry<Integer, Integer> entry : map.entrySet()) {
				out.println(entry.getKey() + "_" + entry.getValue() + "\n");
			}
			%>">查看</span>

        </tr>
	<%
    }
    %>
    </tbody>
</table>


</div>

