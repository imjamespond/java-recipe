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
com.chitu.chess.model.ChessRoom,
com.chitu.chess.model.ChessDistrict,
com.chitu.chess.model.ChessUtils,
com.chitu.chess.model.ChessUtils4Web
" %>


<script type="text/javascript" language="javascript">
window.onload=function(){
document.getElementById("list_player_header").innerHTML				=list_player_header;
document.getElementById("list_player_interpret").innerHTML			=list_player_interpret;

document.getElementById("money").innerHTML							=money;
document.getElementById("point").innerHTML							=point;
document.getElementById("stime").innerHTML							=stime;
document.getElementById("etime").innerHTML							=etime;

}


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
			<th  class="tc"><span id='newid'>Name</span></th>
			<th  class="tc"><span id='newid'>min</span></th>
			<th  class="tc"><span id='newid'>max</span></th>
			<th  class="tc"><span id='newid'>rate</span></th>
			<th  class="tc"><span id='newid'>hehe</span></th>
        </tr>
    </thead>
    <tbody>
	<%
		//ChessDistrict d = ChessUtils4Web.getDistrict(1007);
		//if(d == null){
			//return;
		//}
			
		for (Entry<Integer, StaticDistrictTypes> entry : StaticDistrictTypes.getAll().entrySet()) {
			int mid = entry.getKey();
			StaticDistrictTypes sdt = entry.getValue();
			
    %>
    	<tr>
			<td class="tbold" align="center"><a class="chessRoom" href="./right.jsp?page=list_room.jsp?dist=<%= mid %>"><%= mid %></td>
            <td class="tbold" align="center"><a class="chessRoom" roomId="<%= mid %>"><%= sdt.getName() %></td>
			<td class="tbold" align="center"><a class="chessRoom" roomId="<%= mid %>"><%= sdt.getMoneyLimitation() %></td>
			<td class="tbold" align="center"><a class="chessRoom" roomId="<%= mid %>"><%= sdt.getMoneyMaxLimitation() %></td>
			<td class="tbold" align="center"><a class="chessRoom" roomId="<%= mid %>"><%= sdt.getRateOfExchange() %></td>
			<td class="tbold" align="center"><a class="chessRoom" roomId="<%= mid %>"><%= ChessUtils4Web.getDistPlayerNum(mid) %></td>
        </tr>
	<%
		}
    %>
    </tbody>
</table>


</div>

