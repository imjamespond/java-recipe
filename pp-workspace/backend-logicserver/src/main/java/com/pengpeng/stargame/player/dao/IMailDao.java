package com.pengpeng.stargame.player.dao;

import com.pengpeng.stargame.dao.BaseDao;
import com.pengpeng.stargame.model.player.Mail;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-28下午5:44
 */
public interface IMailDao  extends BaseDao<String,Mail> {
    public Mail getMail(String pid);
}
