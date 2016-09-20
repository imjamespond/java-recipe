package com.pengpeng.stargame.vo.farm;

import com.pengpeng.stargame.annotation.Desc;

/**
 * 统一需要显示  物品的地方 用到
 * User: mql
 * Date: 13-5-6
 * Time: 上午11:02
 */
@Desc("物品VO")
public class GoodsVO {
    @Desc("物品的Id")
    private String itemId;
    @Desc("物品名字")
    private String name;
    @Desc("自己拥有的数量")
    private  int  myNum;
    @Desc("需要的数量")
    private int needNum;
    @Desc("物品图标")
    private  String icon;

    public GoodsVO(){

    }
    public GoodsVO(String itemId,String name,int myNum,int needNum,String icon){
        this.itemId=itemId;
        this.name=name     ;
        this.myNum=myNum  ;
        this.needNum=needNum;
        this.icon=icon;

    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMyNum() {
        return myNum;
    }

    public void setMyNum(int myNum) {
        this.myNum = myNum;
    }

    public int getNeedNum() {
        return needNum;
    }

    public void setNeedNum(int needNum) {
        this.needNum = needNum;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
