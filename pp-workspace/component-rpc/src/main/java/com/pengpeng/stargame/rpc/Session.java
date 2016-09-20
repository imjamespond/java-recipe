package com.pengpeng.stargame.rpc;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-4-3下午5:09
 */
public class Session implements Serializable {

    public HashMap<String,String> params = new HashMap<String, String>() ;

    protected Session(){

    }

    public Session(String pid, String frontend) {
        params.put("pid",pid);
        params.put("frontend.id",frontend);
    }

    public String getScene() {
        if (params.containsKey("scene.id")){
            return params.get("scene.id");
        }
        return null;
    }

    public void setScene(String sceneId){
        params.put("scene.id",sceneId);
    }

    public String getPid() {
        if (params.containsKey("pid")){
            return params.get("pid");
        }
        return null;
    }

    public String getFrontend() {
        if (params.containsKey("frontend.id")){
            return params.get("frontend.id");
        }
        return null;
    }

    public void addParam(String name,String object){
        params.put(name,object);
    }
    public String getParam(String name){
        return params.get(name);
    }

    public Map<String,String> getValues(){
        return params;
    }
}
