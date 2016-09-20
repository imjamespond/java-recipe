package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.net.annotation.EventAnno;

/**
 * @author james
 * 
 */
@DescAnno("重生事件")
@EventAnno(desc = "", name = "event.pk.rebirth")
public class PkRebirthVO {

	@DescAnno("重生玩家 ")
	private PkPlayerVO player;

	
	public PkRebirthVO() {
		super();
	}


	public PkPlayerVO getPlayer() {
		return player;
	}


	public void setPlayer(PkPlayerVO player) {
		this.player = player;
	}


}
