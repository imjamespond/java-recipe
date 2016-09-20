package com.pengpeng.stargame.room;

import com.pengpeng.stargame.model.room.DecoratePosition;
import com.pengpeng.stargame.model.room.RoomEvaluate;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.model.room.RoomPlayer;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.util.Uid;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * User: mql
 * Date: 13-5-22
 * Time: 下午3:06
 */
@Component()
public class RoomBuilder {
    public static Map<String, String> INIT_ROOM = new HashMap<String, String>();

    static {
        INIT_ROOM.put("items_20000", "");//基本墙纸
        INIT_ROOM.put("items_21000", "");//基本地板
//        INIT_ROOM.put("items_22500", "1111,226,1");//绿色窗户
//
//        INIT_ROOM.put("items_24000", "1026,405,0");//简易单人床
//
//        INIT_ROOM.put("items_25000", "792,360,0");//红色轻便椅
//
////        INIT_ROOM.put("items_25000", "");//红色轻便椅
//        INIT_ROOM.put("items_25005", "828,396,0");//标准  茶几

        INIT_ROOM.put("items_23000", "1152,273");//简易衣柜
        INIT_ROOM.put("items_22000", "948,211");//绿色 铁门

    }

    public RoomPlayer newRoomPlayer(String pId) {
        RoomPlayer roomPlayer = new RoomPlayer(pId);

        for (String key : INIT_ROOM.keySet()) {
            String id = Uid.uuid();
            roomPlayer.add(new DecoratePosition(id, key, INIT_ROOM.get(key)));
        }

        return roomPlayer;
    }

    public RoomPackege newRoompackege(String pId) {
        RoomPackege roomPackege = new RoomPackege(pId);
        return roomPackege;
    }

    public RoomEvaluate newFarmEvaluate(String pId){
        RoomEvaluate roomEvaluate=new RoomEvaluate(pId);
        roomEvaluate.setNextTime(DateUtil.getNextCountTime());
        return  roomEvaluate;
    }

}
