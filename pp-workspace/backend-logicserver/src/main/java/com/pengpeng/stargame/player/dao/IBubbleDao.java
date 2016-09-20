package com.pengpeng.stargame.player.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.player.Bubble;
import com.pengpeng.stargame.model.player.Mail;

/**
 * @auther james
 * @since: 13-5-28下午5:44
 */
public interface IBubbleDao extends BaseDao<String,Bubble> {
    public Bubble getBubble(String pid);
}
