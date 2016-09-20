package com.pengpeng.stargame.farm.rule;

import com.pengpeng.stargame.common.ItemData;
import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * User: mql
 * Date: 13-11-13
 * Time: 上午11:42
 */
@Entity
@Table(name = "sg_rule_farm_process")
public class FarmProcessRule extends BaseEntity<String>{
    @Id
    private String proceId;//	编号，唯一
    @Column
    private int type;//	1表示食品，2表示工艺，3表示其他
    @Column
    private String items;//	加工的物品编号
    @Column
    private int time; //	完成加工所需要的时间,单位秒
    @Column
    private String materialEdit;//	所需要的材料编辑，格式：道具编号1,数量1;道具编号2,数量2;……

    @Column  //领取加工生成的 物品的时候 掉落信息
    private String dropEditor;
    @Transient
    private List<ItemData> itemDataList; //夹功能需要的材料
    @Transient
    // 掉落 东西编辑
     List<DropItem> dropItemList;

    public void init(){
        itemDataList=new ArrayList<ItemData>();
        /*
        初始化需要的物品信息物品
         */
        StringTokenizer itemToken = new StringTokenizer(materialEdit,";");
        while(itemToken.hasMoreElements()){
            String item = itemToken.nextToken();
            String[] its = item.split(",");
            ItemData it = ItemData.newTaskItem(its);
            if (it!=null){
                itemDataList.add(it);
            }
        }
       /**
        * 初始化 掉落 信息
         */
        if(dropEditor!=null&&!dropEditor.equals("")){
            String[] line1 = this.dropEditor.split(";");

            List<DropItem> list1 = new ArrayList<DropItem>();
            for (int i = 0; i < line1.length; i++) {
                DropItem dropItem = new DropItem();
                String[] items = line1[i].split(",");
                if (items.length != 3) {
                    continue;
                }
                dropItem.setItemId(items[0]);
                dropItem.setNum(Integer.parseInt(items[1]));
                dropItem.setProbability(Integer.parseInt(items[2]));
                list1.add(dropItem);
            }
            dropItemList = list1;
        }
        if(dropItemList==null){
            dropItemList=new ArrayList<DropItem>();
        }

    }

    public String getDropEditor() {
        return dropEditor;
    }

    public void setDropEditor(String dropEditor) {
        this.dropEditor = dropEditor;
    }

    public List<DropItem> getDropItemList() {
        return dropItemList;
    }

    public void setDropItemList(List<DropItem> dropItemList) {
        this.dropItemList = dropItemList;
    }

    public List<ItemData> getItemDataList() {
        return itemDataList;
    }

    public void setItemDataList(List<ItemData> itemDataList) {
        this.itemDataList = itemDataList;
    }

    public String getProceId() {
        return proceId;
    }

    public void setProceId(String proceId) {
        this.proceId = proceId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getMaterialEdit() {
        return materialEdit;
    }

    public void setMaterialEdit(String materialEdit) {
        this.materialEdit = materialEdit;
    }

    @Override
    public String getId() {
        return proceId;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getKey() {
        return proceId;
    }
}
