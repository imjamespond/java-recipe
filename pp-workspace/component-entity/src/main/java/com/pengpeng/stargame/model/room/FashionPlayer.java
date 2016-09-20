package com.pengpeng.stargame.model.room;

import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.Entity;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * User: mql
 * Date: 13-5-28
 * Time: 下午5:48
 */
@Entity
public class FashionPlayer  extends BaseEntity<String> {
    private String pid;
    private Map<String,String> fashionMap;  //类型：唯一id


    public FashionPlayer(){
        fashionMap=new HashMap<String, String>();
    }

    public FashionPlayer(String pId){
        this.pid=pId;
        fashionMap=new HashMap<String, String>();
    }
    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getFashion(String key){
        return fashionMap.get(key);
    }
    protected Map<String, String> getFashionMap() {
        return fashionMap;
    }

    protected void setFashionMap(Map<String, String> fashionMap) {
        this.fashionMap = fashionMap;
    }

    public Set<String> getFashionKeys(){
        return fashionMap.keySet();
    }
    public void removeFashion(String key){
        fashionMap.remove(key);
    }

    public void removeByValue(String value){
        String key=null;
        for(Map.Entry<String ,String> entry:this.fashionMap.entrySet()){
            if(entry.getValue().equals(value)){
                key=entry.getKey();
                break;
            }
        }
        if(key!=null){
            this.fashionMap.remove(key);
        }
    }
    public boolean hasFashion(String key){
        return fashionMap.containsKey(key);
    }
    public void setFashion(String key,String value){
        fashionMap.put(key,value);
    }
    @Override
    public String getId() {
        return pid;
    }

    @Override
    public void setId(String id) {
    }

    @Override
    public String getKey() {
        return pid;
    }
}
