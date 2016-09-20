package com.chitu.chess.model;

import cn.gecko.broadcast.api.ConnectorManager;

/**
 * 离线用户实体，继承ChessPlayer，尽量保证对用户操作一致，<br>
 * 初始化会加载用户基本数据，<br>
 * 按需加载背包数据，礼物数据，<br>
 * 
 * @author ivan
 * 
 */
public class OffChessPlayer extends ChessPlayer {

	public OffChessPlayer(int sid, ConnectorManager connectorManager) {
		super(sid, connectorManager);
	}

	public OffChessPlayer(long id) {
		super(0, null);
		init(id);
	}

}
