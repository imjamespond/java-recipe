package com.chitu.chess.data;

import cn.gecko.commons.data.BillType;
import cn.gecko.commons.data.ItemOpType;
import cn.gecko.commons.utils.SpringUtils;
import cn.gecko.player.model.Backpack;

import com.chitu.chess.model.ChessPlayer;
import com.chitu.chess.service.ChessPlayerManager;

/**
 * 虚拟物品
 * @author open
 *
 */
public class VirtualItem extends ChessItem {

	public VirtualItem(){
		setItemType(ItemType.Virtual.ordinal());
	}

	public void beOverlayOwned(Backpack backpack, int count, ItemOpType itemOpType, String desc, Object... options) {
		ChessPlayerManager playerManager = SpringUtils.getBeanOfType(ChessPlayerManager.class);
		ChessPlayer player = playerManager.getAnyPlayerById(backpack.playerId);
		switch (getId()) {
		// 银币
		case 1:
			player.wealthHolder.increaseMoney(count,0,0,BillType.get(itemOpType.getId()), desc);
			break;
		// 金币
		case 2:
			player.wealthHolder.increaseMoney(0,count,0, BillType.get(itemOpType.getId()), desc);
			break;
		//威望
		case 3:
			player.wealthHolder.increaseMoney(0,0,count, BillType.get(itemOpType.getId()), desc);
			break;
		}
	}
}
