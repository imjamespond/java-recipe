package com.pengpeng.stargame.player.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.player.OtherPlayer;
import com.pengpeng.stargame.player.dao.IOtherPlayerDao;
import com.pengpeng.stargame.util.DateUtil;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-15下午3:29
 */
@Component
@DaoAnnotation(prefix="p.other.")
public class OtherPlayerDaoImpl extends RedisDao<OtherPlayer> implements IOtherPlayerDao {
    @Override
    public Class<OtherPlayer> getClassType() {
        return OtherPlayer.class;
    }

    @Override
    public OtherPlayer getBean(String index) {
        OtherPlayer  op = super.getBean(index);
        if (null==op){
            op = new OtherPlayer(index);
            op.init(new Date());
            saveBean(op);
            return op;
        }
        if (op.init(new Date())){
            saveBean(op);
        }
        return op;
    }
}
