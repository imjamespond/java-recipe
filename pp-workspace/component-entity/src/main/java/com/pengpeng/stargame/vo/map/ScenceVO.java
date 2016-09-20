package com.pengpeng.stargame.vo.map;

import com.pengpeng.stargame.annotation.Desc;

/**
 * 地图(场景数据)
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-1-9下午4:36
 */
@Desc("地图(场景)信息")
public class ScenceVO {
    @Desc("id")
    private String id;//地图(场景id)
    @Desc("地图名称")
    private String name;//地图名称
    @Desc("地图等级")
    private int level;//地图限制等级
    @Desc("传送口")
    private TransferVO[] transfers;//传送点

    @Desc("玩家坐标信息")
    private MoveVO[] players;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

	public TransferVO[] getTransfers() {
		return transfers;
	}

	public void setTransfers(TransferVO[] transfers) {
		this.transfers = transfers;
	}

    public MoveVO[] getPlayers() {
        return players;
    }

    public void setPlayers(MoveVO[] players) {
        this.players = players;
    }
}
