package com.pengpeng.stargame.gameevent.rule;

import com.pengpeng.stargame.farm.rule.DropItem;
import com.pengpeng.stargame.model.BaseEntity;
import com.pengpeng.stargame.util.RandomUtil;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mql
 * Date: 13-12-20
 * Time: 上午10:00
 */
@Entity
@Table(name = "sg_rule_dropgift")
public class DropGiftRule extends BaseEntity<String> {
    @Id
    private String id;
    //类型
    @Column
    private int type;
    //名字
    @Column
    private String  name;
    //掉落编辑
    @Column
    private String dropEdit;
    @Column
    private String icon;
    //掉落
    @Transient
    List<DropItem> dropItemList;
    public  void init(){
        /**
         * 初始化 掉落 信息
         */
        if(dropEdit==null){
            return;
        }
        String [] line1=this.dropEdit.split(";");

        List<DropItem> list1=new ArrayList<DropItem>();
        for (int i=0;i<line1.length;i++){
            DropItem dropItem=new DropItem();
            String[] items=line1[i].split(",");
            if(items.length!=3){
                continue;
            }
            dropItem.setItemId(items[0]);
            dropItem.setNum(Integer.parseInt(items[1]));
            dropItem.setProbability(Integer.parseInt(items[2]));
            list1.add(dropItem);
        }
        dropItemList = list1;
    }

    public DropItem getReward() {
        if(dropItemList.size()>0){
            int total=0;
            for(DropItem dropItem:dropItemList){
                total+=dropItem.getProbability();
            }
            int randomNum = RandomUtil.range(0, total);
            int temp=0;
            for(DropItem dropItem:dropItemList){
                temp+=dropItem.getProbability();
                if(randomNum<temp){
                    return dropItem;
                }
            }

        }
        return null;
    }
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String  getName() {
        return name;
    }

    public void setName(String  name) {
        this.name = name;
    }

    public String getDropEdit() {
        return dropEdit;
    }

    public void setDropEdit(String dropEdit) {
        this.dropEdit = dropEdit;
    }

    public List<DropItem> getDropItemList() {
        return dropItemList;
    }

    public void setDropItemList(List<DropItem> dropItemList) {
        this.dropItemList = dropItemList;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getKey() {
        return id;
    }
}
