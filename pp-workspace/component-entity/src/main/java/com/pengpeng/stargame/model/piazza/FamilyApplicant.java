package com.pengpeng.stargame.model.piazza;

import com.pengpeng.stargame.model.Indexable;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:38
 */
public class FamilyApplicant implements Indexable<String> {
    private String pid;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Override
    public String getKey() {
        return pid;
    }

    @Override
    public void setKey(String key) {
        pid = key;
    }


}
