package com.pengpeng.stargame.vo.farm;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;

/**
 * User: mql
 * Date: 13-5-20
 * Time: 下午3:35
 */
@Desc("农田")
@EventAnnotation(name="event.farmField.add",desc="增加农田 事件")
public class FieldAddVO {
    private int  id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
