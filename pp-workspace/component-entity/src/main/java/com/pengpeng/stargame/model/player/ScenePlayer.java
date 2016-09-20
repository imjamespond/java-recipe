package com.pengpeng.stargame.model.player;

import com.pengpeng.stargame.model.BaseEntity;

/**
 * 用于保存玩家所在场景的独立信息
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-5下午3:52
 */
public class ScenePlayer extends BaseEntity<String> {
    private String pid;
    private String sceneId;
    private int x;
    private int y;

    public ScenePlayer(){

    }
    public ScenePlayer(String pid, String sceneId) {
        this.pid = pid;
        this.sceneId = sceneId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setScene(String id,int x,int y){
        this.sceneId = id;
        this.x = x;
        this.y = y;
    }
    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
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

    public void setPoint(int x,int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public String getId() {
        return pid;
    }

    @Override
    public void setId(String id) {
        this.pid = id;
    }

    @Override
    public String getKey() {
        return pid;
    }
}
