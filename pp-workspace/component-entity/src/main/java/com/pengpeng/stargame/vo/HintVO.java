package com.pengpeng.stargame.vo;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;

/**
 * User: mql
 * Date: 14-3-18
 * Time: 下午12:25
 */
@Desc("右下角提示信息VO")
@EventAnnotation(name="event.hint",desc="右下角提示信息 Vo")
public class HintVO {
    private String content;

    public HintVO(){

    }
    public HintVO(String content){
        this.content=content;
    }
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
