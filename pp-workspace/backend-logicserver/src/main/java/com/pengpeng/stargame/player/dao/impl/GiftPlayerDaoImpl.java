package com.pengpeng.stargame.player.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.GiftPlayer;
import com.pengpeng.stargame.player.dao.IGiftPlayerDao;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-6-3下午8:24
 */
@Component()
@DaoAnnotation(prefix = "gift.")
public class GiftPlayerDaoImpl extends RedisDao<GiftPlayer> implements IGiftPlayerDao {

    @Override
    public Class<GiftPlayer> getClassType() {
        return GiftPlayer.class;
    }

    @Override
    public GiftPlayer getBean(String index) {
        GiftPlayer gp = super.getBean(index);
        if (gp==null){
            gp = new GiftPlayer(index);
            this.saveBean(gp);
        }
        if (gp.isNewDay(new Date())){//如果是新的一天则清理赠送次数
            gp.clean();
        }
        gp.calc(new Date());
        this.saveBean(gp);
        return gp;
    }
}
