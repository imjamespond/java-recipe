package com.pengpeng.stargame.fashion.rule;

import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.util.DateUtil;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

/**
 * User: mql
 * Date: 13-5-28
 * Time: 下午4:12
 */
@Entity
@Table(name = "sg_rule_item_dress")
@PrimaryKeyJoinColumn(name="itemsId")
public class FashionItemRule extends BaseItemRule {
    @Column
    private int sex;
    @Column
    private String image;
    @Column
    private int fashionIndex;
    @Column
    private int level;
    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getFashionIndex() {
        return fashionIndex;
    }

    public void setFashionIndex(int fashionIndex) {
        this.fashionIndex = fashionIndex;
    }

    public void init(){
        if(this.itemValidDate==null||this.itemValidDate.equals("")||this.itemValidDate.equals("0")){
            return ;
        }
         DateUtil.convertStringToDate(null, this.getItemValidDate());
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
