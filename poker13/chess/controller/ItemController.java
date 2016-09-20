package com.chitu.chess.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import cn.gecko.broadcast.MultiGeneralController;
import cn.gecko.commons.data.BillType;
import cn.gecko.commons.model.GeneralException;

import com.chitu.chess.data.StaticItem;
import com.chitu.chess.model.ChessBillTypes;
import com.chitu.chess.model.ChessErrorCodes;
import com.chitu.chess.model.ChessPlayer;
import com.chitu.chess.msg.BuyItemDto;
import com.chitu.chess.service.ChessPlayerManager;

/**
 * @author Administrator 游戏分区=>游戏房间
 * 
 */
@Controller
public class ItemController extends MultiGeneralController {

	@Autowired
	private ChessPlayerManager chessPlayerManager;

	public BuyItemDto buyItem(int itemId,int itemNum){
		ChessPlayer player = chessPlayerManager.getRequestPlayer();
		StaticItem.get(itemId);
		
		StaticItem staticItem = StaticItem.get(itemId);

		BuyItemDto buyItemDto = new BuyItemDto();
		
		int cost = staticItem.getCost() * itemNum;
		int money = staticItem.getMoneyGiving() * itemNum;
		
		if(player.wealthHolder.getRmb() < cost){
			throw new GeneralException(ChessErrorCodes.RMB_NOT_ENOUGH);
		}
		
		if (staticItem.getMoneyGiving() > 0) {
			player.wealthHolder.increaseMoney(money ,0, 0, BillType.get(ChessBillTypes.BUY), "");
			player.wealthHolder.decreaseMoney(0, 0, cost, BillType.get(ChessBillTypes.PAY), "");
			buyItemDto.setMoney(money);
		}		
		if (staticItem.getBuffExpression() > 0) {

		}
		if (staticItem.getBuffLoginMoneyGiving() > 0) {

		}
		if (staticItem.getBuffMultiplePoint() > 0) {

		}
		if (staticItem.getBuffSafebox() > 0) {

		}
		if (staticItem.getBuffVip() > 0) {

		}
		if (staticItem.getBuffVipExpression() > 0) {

		}
		//ChessUtils.chessLog.info("itemId:"+itemId);
		//ChessUtils.chessLog.info("itemNum:"+itemNum);
		//ChessUtils.chessLog.info("getMoneyGiving:"+staticItem.getMoneyGiving());
		//ChessUtils.chessLog.info("chessPlayer getMoney:"+chessPlayer.wealthHolder.getMoney());

		return buyItemDto;
	}

}
