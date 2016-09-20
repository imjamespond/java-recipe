package com.chitu.chess.data;

import cn.gecko.broadcast.annotation.IncludeEnum;
import cn.gecko.broadcast.annotation.IncludeEnums;
import cn.gecko.commons.data.StaticDataManager;
import cn.gecko.player.data.GeneralItem;
import cn.gecko.player.model.BindType;
import cn.gecko.player.model.PackItem;
import cn.gecko.player.msg.PackItemDto;

import com.chitu.chess.model.ChessPackItem;
import com.chitu.chess.msg.ChessPackItemDto;

@IncludeEnums({ @IncludeEnum(BindType.class) })
public class ChessItem extends GeneralItem {

	public enum ItemType {
		/**未知**/
		Unknown,
		/**虚拟物品类型**/
		Virtual
	}
	
	public static ChessItem get(int id) {
		return StaticDataManager.getInstance().get(ChessItem.class, id);
	}

	public PackItemDto toPackItemDto(PackItem packItem) {
		ChessPackItemDto dto = new ChessPackItemDto((ChessPackItem) packItem);
		dto.setExtra(initPackItemExtra(packItem));
		return dto;
	}
}
