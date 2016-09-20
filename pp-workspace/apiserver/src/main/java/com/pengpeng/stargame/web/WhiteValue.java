package com.pengpeng.stargame.web;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mql
 * Date: 13-10-24
 * Time: 下午3:51
 */
public class WhiteValue {
    private int environment;
    private List<String> whiteList;

    public List<String> getWhiteList() {
        if(environment==0){
            return new ArrayList<String>();
        }
        return whiteList;
    }

    public void setWhiteList(List<String> whiteList) {
        this.whiteList = whiteList;
    }

    public int getEnvironment() {
        return environment;
    }

    public void setEnvironment(int environment) {
        this.environment = environment;
    }
}
