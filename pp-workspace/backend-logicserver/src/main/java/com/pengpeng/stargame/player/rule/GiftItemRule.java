package com.pengpeng.stargame.player.rule;

import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-3下午5:00
 */
@Deprecated
@Entity
@Table(name="sg_rule_gift_item")
public class GiftItemRule extends BaseEntity<String>{
    //物品id
    @Id
    private String itemId;
    //物品名称
    @Column
    private String name;
    //礼物数量
    @Column
    private int num;
    //有效时间(天)
    @Column
    private long validityTime;

    @Override
    public String getId() {
        return itemId;
    }

    @Override
    public void setId(String id) {
        this.itemId = id;
    }

    @Override
    public String getKey() {
        return itemId;
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

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public long getValidityTime() {
        return validityTime;
    }

    public void setValidityTime(long validityTime) {
        this.validityTime = validityTime;
    }
}
