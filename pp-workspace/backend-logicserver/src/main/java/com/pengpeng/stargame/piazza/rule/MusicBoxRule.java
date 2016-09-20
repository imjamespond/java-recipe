package com.pengpeng.stargame.piazza.rule;

import com.pengpeng.stargame.model.BaseEntity;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-12-5上午11:04
 */
public class MusicBoxRule extends BaseEntity<String> {
    private String id = "music_zan";
    private int maxZan = 5;

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

    public boolean checkZan(int num){
        return num<maxZan;
    }
}
