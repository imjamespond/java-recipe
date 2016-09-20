package com.chitu.poker.model;

import cn.gecko.broadcast.api.ConnectorManager;

/**
 * 离线用户实体，继承TdPlayer，尽量保证对用户操作一致，<br>
 * 
 * @author ivan
 * 
 */
public class OffPokerPlayer extends PokerPlayer {

	public OffPokerPlayer(int sid, ConnectorManager connectorManager) {
		super(sid, connectorManager);
	}

	public OffPokerPlayer(long id) {
		super(0, null);
		init(id);
	}

}
