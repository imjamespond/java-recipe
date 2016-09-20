package com.pengpeng.stargame.model.antiaddiction;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: james
 * Date: 13-9-12
 * Time: 下午7:07
 */
public class PlayerAntiAddiction extends BaseEntity<String> {
    private String pid;
    private String indentity;
    private String name;
    private long birth;

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getIndentity(){
        return indentity;
    }

    public void setIndentity(String indentity) {
        this.indentity = indentity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getBirth() {
        return birth;
    }

    public void setBirth(long birth) {
        this.birth = birth;
    }

    @Override
    public String getId() {
        return pid;
    }

    @Override
    public void setId(String id) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String getKey() {
        return pid;
    }

}
