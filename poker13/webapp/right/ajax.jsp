<%@ page contentType="text/html;charset=utf-8" pageEncoding="utf-8"%>
<%@ page import="
java.util.Map,
java.util.Map.Entry,
java.util.HashMap,
java.util.Iterator,
java.util.List,
cn.gecko.commons.model.Pageable,
com.chitu.chess.data.StaticDistrictTypes,
com.chitu.chess.model.ChessPlayer,
com.chitu.chess.model.PersistChessPlayer,
com.chitu.chess.model.ChessRoom,
com.chitu.chess.model.ChessRoomPlayer,
com.chitu.chess.model.ChessDistrict,
com.chitu.chess.model.ChessUtils,
com.chitu.chess.model.ChessUtils4Web
" %>

<%
//out.println("dadadadadadada");
	String action = request.getParameter("action");
	if(null == action){
		return;
	}

	if(action.equals("ChessRoom")){
	
		out.println("{");
		
		ChessRoom room = ChessUtils4Web.getChessRoom(Integer.valueOf(request.getParameter("districtId")),request.getParameter("roomId"));
		
		out.println("roomState:\""+room.roomState+"\",");
		
		out.println("playerId:[");
		for (int i = 0; i < room.chessRoomPlayer.length; i++) {
			if(room.chessRoomPlayer[i].playerId == 0)
				continue;
			ChessPlayer playerTmp = ChessUtils4Web.getChessPlayer(room.chessRoomPlayer[i].playerId);
			out.println(  playerTmp.accountId+":"+room.chessRoomPlayer[i].playerState+":"+room.chessRoomPlayer[i].playerId+",");
		}
		out.println("0]");
		out.println("}");
	}

	if(action.equals("ADDRMB")){
	
		String field = request.getParameter("field");
		if(null == field){
			return;
		}
	
		long playerId = Long.valueOf(request.getParameter("playerId"));	
		if(field.equals("rmb")){
			int num = Integer.valueOf(request.getParameter("numInput"));
			if(num <= 0)
				return;
			num = ChessUtils4Web.add(playerId,0,0,num);
			out.println(num);		
		}
		if(field.equals("money")){
			int num = Integer.valueOf(request.getParameter("numInput"));
			num = ChessUtils4Web.add(playerId,num,0,0);			
			out.println(num);		
		}
		if(field.equals("npc")){
			ChessUtils4Web.createNpcRoom(request.getParameter("numInput"));	
		}
	}
	
	if(action.equals("cardSpecify")){
		String cardStr = request.getParameter("cardStr");	
		long playerId = Long.valueOf(request.getParameter("playerId"));	
		int trickNum = Integer.valueOf(request.getParameter("trickNum"));
		if(trickNum <= 0||null == cardStr||playerId<=0)
			return;
		else
			ChessUtils4Web.cardSpecify(trickNum, playerId, cardStr);
	}
	
	if(action.equals("createNpcRoom")){
		String roomNum = request.getParameter("roomNum");
		String districtId = request.getParameter("districtId");
		String playerNum = request.getParameter("playerNum");
		
		int num = 0;
		int num1 = 0;
		int num2 = 0;
		if(roomNum==null ){
			return;
		}else{
			num = Integer.valueOf(roomNum);
		}
		if(districtId==null ){
			return;
		}else{
			num1 = Integer.valueOf(districtId);
		}
		if(playerNum==null ){
			return;
		}else{
			num2 = Integer.valueOf(playerNum);
		}
		out.println(ChessUtils4Web.createNpcRooms(num1,num2,num));
		//ChessUtils4Web.matchNpcAdd(num1,num2,num);
		//out.println(num+"_"+num1+"_"+num2);
	}
	
	if(action.equals("createNpcMatch")){
		String districtId = request.getParameter("districtId");
		String playerNum = request.getParameter("playerNum");
		
		int num = 0;
		int num1 = 0;
		int num2 = 0;

		if(districtId==null ){
			return;
		}else{
			num1 = Integer.valueOf(districtId);
		}
		if(playerNum==null ){
			return;
		}else{
			num2 = Integer.valueOf(playerNum);
		}
		//out.println(ChessUtils4Web.createNpcRooms(num1,num2,num));
		ChessUtils4Web.matchNpcAdd(num1,num2,num);
		out.println(num+"_"+num1+"_"+num2);
	}

	if(action.equals("cleanNpc")){
		String districtId = request.getParameter("districtId");
		int num1 = 0;
		if(districtId==null ){
			return;
		}else{
			num1 = Integer.valueOf(districtId);
		}
		out.println(ChessUtils4Web.cleanNpc(num1));
	}	
	
	if(action.equals("importNpc")){	
		out.println(ChessUtils4Web.importNpc());
	}	
%>
