package com.pengpeng.stargame.vo.map;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;
import org.springframework.stereotype.Component;

/**
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-1-14上午11:18
 */
@Desc("怪物或人物移动信息")
@EventAnnotation(name="event.move",desc="移动推送消息")
public class MoveVO {
	// 暂时只记录了playerId
    @Desc("怪物,或人物id")
    private String id;
    @Desc("坐标x")
    private int x;
    @Desc("坐标y")
    private int y;
    @Desc("0移动,1出现,2消失")
    private int type;

    public MoveVO(){

    }
    public MoveVO(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }
    public MoveVO(String id, int x, int y,int type) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
