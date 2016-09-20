package com.pengpeng.stargame.common;

/**
 * User: mql
 * Date: 13-8-2
 * Time: 下午12:25
 */
public class ItemData {
    //物品id
    public String itemId;
    //物品数量
    public int num;

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public static ItemData newTaskItem(String[] items){
        if (items.length>=2){
            ItemData item = new ItemData();
            item.itemId = items[0];
            item.num = Integer.parseInt(items[1]);
            return item;
        }
        return null;
    }
}
