package com.pengpeng.stargame.player.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.player.Bubble;
import com.pengpeng.stargame.player.dao.IBubbleDao;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @auther james
 * @since: 13-5-28下午5:44
 */
@Component()
@DaoAnnotation(prefix = "player.bubble.")
public class BubbleDaoImpl extends RedisDao<Bubble> implements IBubbleDao {
    @Override
    public Class<Bubble> getClassType() {
        return Bubble.class;
    }

    public Bubble getBubble(String pid) {
        Bubble bubble = super.getBean(pid);
        if (bubble==null){
            bubble = new Bubble();
            bubble.setPid(pid);
        }

        if(bubble.getAccDate() == null){
            bubble.setAccDate(new Date(0));
        }

        return bubble;
    }



}
