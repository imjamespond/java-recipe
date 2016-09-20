package com.pengpeng.stargame.model.room;

/**
 * User: mql
 * Date: 13-5-20
 * Time: 下午2:25
 */
public class DecoratePosition {

    private String id;

    private String itemId;

    private String position;

    public DecoratePosition() {

    }
    public DecoratePosition(String id, String itemId, String position) {
        this.id = id;
        this.itemId = itemId;
        this.position = position;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
