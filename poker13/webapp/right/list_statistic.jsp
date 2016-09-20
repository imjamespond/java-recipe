<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page
	import="java.util.Map,java.util.Map.Entry,java.util.HashMap,java.util.Iterator,java.util.List,cn.gecko.commons.model.Pageable,com.chitu.chess.data.StaticDistrictTypes,com.chitu.chess.model.PersistChessPlayer,com.chitu.chess.model.ChessUtils,com.chitu.chess.model.ChessUtils4Web"%>

<%


	
%>

<script type="text/javascript" language="javascript">
	window.onload = function() {
		document.getElementById("list_player_header").innerHTML = list_player_header;
		document.getElementById("list_player_interpret").innerHTML = list_player_interpret;

		document.getElementById("money").innerHTML = money;
		document.getElementById("point").innerHTML = point;
		document.getElementById("stime").innerHTML = stime;
		document.getElementById("etime").innerHTML = etime;

	}

	$(function() {
		var info = <%=ChessUtils4Web.getStatistic()%>;
		var registry = "";
		for (key in info.registry){
			registry = registry + key+":"+info.registry[key]+"|";
		}
		var launch = new Date(parseInt(info.launch));
	    var date = launch.toLocaleString();
	    $("#date").html( date);
		$("#amount").html( info.amount);
		$("#registry").html(registry);
		$("#online").html( info.online);
	});
</script>
<div class="itemTitle">
	<h1 id='list_player_header'>遊戲統計 - </h1>
</div>
<div class="container">
	<p id='list_player_interpret'>說明：</p>

	<table class="tb tblist">
		<thead>
			<tr class="tbcap nowarp">
				<th class="tc"><span id='newid'>field</span></th>
				<th class="tc"><span id='username'>value</span></th>

			</tr>
		</thead>
		<tbody>

			<tr>
				<td class="tbold" align="center">总玩家人数</td>
				<td class="tbold" align="center"><span id="amount"></span></td>
			</tr>

			<tr>
				<td class="tbold" align="center">启动日期</td>
				<td class="tbold" align="center"><span id="date"></span></td>
			</tr>
			
			<tr>
				<td class="tbold" align="center">每日注册人数</td>
				<td class="tbold" align="center"><span id="registry"></span></td>
			</tr>
			
			<tr>
				<td class="tbold" align="center">在线人数</td>
				<td class="tbold" align="center"><span id="online"></span></td>
			</tr>
			
		</tbody>
	</table>


</div>







