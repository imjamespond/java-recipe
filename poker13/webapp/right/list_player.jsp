<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page
	import="java.util.Map,java.util.Map.Entry,java.util.HashMap,java.util.Iterator,java.util.List,cn.gecko.commons.model.Pageable,com.chitu.chess.data.StaticDistrictTypes,com.chitu.chess.model.PersistChessPlayer,com.chitu.chess.model.ChessUtils,com.chitu.chess.model.ChessUtils4Web"%>

<%

	String params = "&page="+request.getParameter("page");
	
	String pageNum_ = request.getParameter("pageNum");
	int pageNum = pageNum_==null?0:Integer.valueOf(pageNum_);
	int pageSize = 20;

	String where = "";
	
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
	window.onload = function() {
		document.getElementById("list_player_header").innerHTML = list_player_header;
		document.getElementById("list_player_interpret").innerHTML = list_player_interpret;

		document.getElementById("money").innerHTML = money;
		document.getElementById("point").innerHTML = point;
		document.getElementById("stime").innerHTML = stime;
		document.getElementById("etime").innerHTML = etime;

	}

	$(function() {
		$('.mission').click(function() {
			var val = $(this).attr("val");
			alert(val);
			//$.post('right/ajax.jsp',{"action":"mission","id":id,},function(data){
			//alert(data);
			//location.reload();
			//});
		});
		$('.item').click(function() {
			var val = $(this).attr("val");
			alert(val);
		});

		//編輯input信息
		var inputStatus = true;
		var numInput = '';
		var curInput = null;
		$('.numInput').click(
				function() {
					if (inputStatus) {
						rowId = $(this).attr("val");
						numInput = $(this).html();
						curInput = $(this);
						$(this).empty();
						$(this).append(
								'<input id="numInput" value="'+numInput+'" /><p><span id="updateInput">'
										+ add
										+ '</span><span id="closeInput" >'
										+ close + '</span></p>');
						inputStatus = false;
					}
				});
		//關閉input
		$('#closeInput').live('click', function() {//alert(prizeInfo);
			curInput.empty();
			curInput.append(numInput);
			inputStatus = true;
		});
		//更新input
		$('#updateInput').live('click', function() {
			if (!inputStatus) {
				numInput = $('#numInput').val();
				var field = curInput.attr("field");
				var reg = /[^-\d]+/;
				if (reg.test(numInput)) {
					alert("请输入数字!");
				} else {
					$.post('right/ajax.jsp', {
						"action" : "ADDRMB",
						"playerId" : rowId,
						"numInput" : numInput,
						"field" : field
					}, function(data) {
						//alert(data);
						//top.frames['center'].location.reload();
						curInput.empty();
						curInput.append(data);
						inputStatus = true;
					});
				}
			}
		});

		//cardSpecify
		var playerId = "";
		var ckedit = $('#edit');
		$('.edit').click(function() {
			playerId = $(this).attr("val");
			$('#playerId').html(playerId);
			setCenter(ckedit);
		});
		$("#submitBtn").click(function() {
			$.post('right/ajax.jsp', {
				"action" : "cardSpecify",
				"playerId" : playerId,
				"cardStr" : $('#cardStr').val().replace(/\s*/g,""),
				"trickNum" : $('#trickNum').val()
			}, function(data) {
				alert(data);
				//top.frames['center'].location.reload();
			});
			ckedit.hide();
		});
		$("#close").click(function() {
			ckedit.hide();
		});

		function setCenter(div) {
			var bodyW = $(document).width();
			var bodyH = $(document).height();

			var winW = $(window).width();
			var winH = $(window).height();

			var scrollTop = $(window).scrollTop();
			var scrollL = $(window).scrollLeft();

			var divW = div.width();
			var divH = div.height();

			var left = winW / 2 - divW / 2 + scrollL;
			var top = winH / 2 - divH / 2 + scrollTop;

			div.css({
				"left" : left,
				"top" : top
			}).show();
		}
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
				<td><span id='user'>賬號搜索：</span>&nbsp;&nbsp;&nbsp; <input
					name="keyword" size="10" value="<%=keyword%>" />&nbsp; <span
					id='role'>角色搜索：</span>&nbsp;&nbsp;&nbsp; <input name="keyword1"
					size="10" value="<%=keyword1%>" />&nbsp; <span id='stime'>開始時間：</span>
					<input name="bdate" type="text" class="Wdate"
					onFocus="WdatePicker()" value="<%=bdate%>" size="20"> <span
					id='etime'>結束時間：</span> <input name="edate" type="text"
					class="Wdate" onFocus="WdatePicker()" value="<%=edate%>" size="20">&nbsp;&nbsp;
					<input id='btnConfirm' type="submit" value="Submit" />
					&nbsp;&nbsp;</td>
				</td>
			</tr>
		</table>
	</form>

	<table class="tb tblist">
		<thead>
			<tr class="tbcap nowarp">
				<th class="tc"><span id='newid'>id</span></th>
				<th class="tc"><span id='username'>player account</span></th>
				<th class="tc"><span id='rolename'>player name</span></th>
				
				<th class="tc"><span id='onlineTime'>online time</span></th>
				<th class="tc"><span id='loginCount'>login count</span></th>
				
				<th class="tc"><span id='money'>money</span></th>
				<th class="tc"><span id='point'>point</span></th>
				<th class="tc"><span id='rmb'>rmb</span></th>

				<th class="tc"><span id='generalincome'>victory</span></th>
				<th class="tc"><span id='gameAmount'>gameAmount</span></th>
				<th class="tc"><span id='continioulyVictory'>continuosly
						victory</span></th>

				<th class="tc"><span id='mission'>mission</span></th>

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
				<td class="tbold" align="center"><div class="edit"
						val="<%= p.getId() %>"><%= p.getId() %></td>
				<td class="tbold" align="center"><%= p.getAccountId() %></td>
				<td class="tbold" align="center"><%= p.getNickname() %></td>
				<td class="tbold" align="center"><%= p.getOnlineTime() %></td>
				<td class="tbold" align="center"><%= p.getLoginCount() %></td>
				
				<td class="tbold" align="center"><div class="numInput"
						val="<%= p.getId() %>" field='money'><%= p.getMoney() %></td>
				<td class="tbold" align="center"><%= p.getPoint() %></td>
				<td class="tbold" align="center"><div class="numInput"
						val="<%= p.getId() %>" field='rmb'><%= p.getRmb() %></td>

				<td class="tbold" align="center"><%= p.getVictoryAmount() %></td>
				<td class="tbold" align="center"><%= p.getGameAmount() %></td>
				<td class="tbold" align="center"><%= p.getContinuousVictory() %></td>

				<td class="tbold" align="center"><span class="mission"
					val="
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

	<table align="center">
		<tr>
			<td>第 <%
	for(int i=1;i<=mypage.getPageCount();i++){
		out.println("<a title='"+i+"' href='?pageNum="+i+params+"'>"+i+"</a>,");
	}
	//out.println(mypage.getPageSize()+"_"+mypage.getOffset()+"_"+mypage.getPageCount() +"_"+ pageNum +"_"+ pageSize);
%> 页
			</td>
		</tr>
	</table>

</div>


<div id="edit"
	style="width: 600px; background: #EEEEEE; z-index: 100; display: none; border: 2px gray solid; position: absolute">
	<DIV style="padding: 10px;">
		<div style="border: 1px #CCCCCC dashed; padding: 10px;">
			玩家ID： <span id='playerId'></span>
			<hr>
			玩家牌ID：<input type="text"
				id="cardStr" style="width: 200px; border: 1px #999999 solid;"
				maxlength="100">
			<hr>
			次数： <input type="text" value="1" 
				id="trickNum" style="width: 20px; border: 1px #999999 solid;"
				maxlength="100">
			<hr>
			<input type="button" id="submitBtn" value="Submit" class="button">&nbsp;
			<input type="button" id="close" value="Close" class="button">
		</div>
	</DIV>
</div>




