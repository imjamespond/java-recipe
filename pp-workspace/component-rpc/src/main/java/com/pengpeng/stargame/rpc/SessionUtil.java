package com.pengpeng.stargame.rpc;

import java.util.HashMap;
import java.util.Map;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-11下午3:47
 */
public class SessionUtil {
    public static final String KEY_UID= "uid";

    public static final String KEY_CHAT_WORLD= "chat.world";
    public static final String KEY_CHAT_ORG= "chat.org";
    public static final String KEY_CHAT_PERSON= "chat.person";
    public static final String KEY_CHANNEL_SCENE = "channel.scene";
    public static final String KEY_CHANNEL_FAMILY = "family.family";

    public String getPid(Session session){
        return session.getPid();
    }
    public static String getChannelScene(Session session){
        return session.getParam(KEY_CHANNEL_SCENE);
    }
    public static void setChannelScene(Session session, String cid){
        session.addParam(KEY_CHANNEL_SCENE,cid);
    }
    public static int getInt(Session session ,String name){
        String v = session.getParam(name);
        if (null==v){
            return 0;
        }
        try{
            return Integer.parseInt(v);
        }catch(Exception e){
            return 0;
        }
    }

    public static void setParam(Session session,String name,int value){
        session.addParam(name,String.valueOf(value));
    }

    public static void setParam(Session session,String name,String value){
        session.addParam(name,value);
    }

    public static void setParamX(Session session, int x) {
        setParam(session,"x",x);
    }

    public static void setParamY(Session session, int y) {
        setParam(session,"y",y);
    }

    public static Map<String,String> newPoint(String pid,int x,int y){
        Map<String,String> map = new HashMap<String,String>();
        map.put("pid",pid);
        map.put("x",String.valueOf(x));
        map.put("y",String.valueOf(y));
        return map;
    }

    public static Map<String,String> newUpdateParam(Session session,String name,String value){
        Map<String,String> map = new HashMap<String,String>();
        map.put("pid",session.getPid());
        map.put(name,value);
        return map;
    }
}
