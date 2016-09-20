package com.pengpeng.stargame.model.piazza;

import com.pengpeng.stargame.model.BaseEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * User: mql
 * Date: 13-6-27
 * Time: 上午11:50
 * 家族建筑 信息
 */
public class FamilyBuildInfo extends BaseEntity<String>{
    private String familyId;
    private Map<Integer,Integer> builds;

    public FamilyBuildInfo(){

    }

    public FamilyBuildInfo(String familyId){
        this.familyId=familyId;
        builds=new HashMap<Integer, Integer>();

    }

    public String getFamilyId() {
        return familyId;
    }

    public void setFamilyId(String familyId) {
        this.familyId = familyId;
    }
    public int getLevel(int type){
        if (!builds.containsKey(type)){
            builds.put(type,1);
        }
        return builds.get(type);
    }

    public void setBuilds(int type,int level){
        builds.put(type,level);
    }
    public void setLevel(int type,int lv){
        builds.put(type,lv);
    }

    public Map<Integer, Integer> getBuilds() {
        return builds;
    }

    protected void setBuilds(Map<Integer, Integer> builds) {
        this.builds = builds;
    }

    @Override
    public String getId() {
        return familyId;
    }

    @Override
    public void setId(String id) {

    }

    @Override
    public String getKey() {
        return familyId;
    }
}
