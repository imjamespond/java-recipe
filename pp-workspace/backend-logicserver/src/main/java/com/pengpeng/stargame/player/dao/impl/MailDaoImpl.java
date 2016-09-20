package com.pengpeng.stargame.player.dao.impl;

import com.pengpeng.stargame.annotation.DaoAnnotation;
import com.pengpeng.stargame.dao.RedisDao;
import com.pengpeng.stargame.model.player.Mail;
import com.pengpeng.stargame.model.player.MailInfo;
import com.pengpeng.stargame.model.player.MailLog;
import com.pengpeng.stargame.player.dao.IMailDao;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.TreeMap;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-28下午5:44
 */
@Component()
@DaoAnnotation(prefix = "player.mail.")
public class MailDaoImpl extends RedisDao<Mail> implements IMailDao {
    @Override
    public Class<Mail> getClassType() {
        return Mail.class;
    }

    public Mail getMail(String pid) {
        Mail mail = super.getBean(pid);
        if (mail==null){
            mail = new Mail();
            mail.setPid(pid);
        }
        if(null==mail.getMailList()){
            mail.setMailList(new TreeMap<Long,MailInfo>());
        }
        if(null==mail.getLog()){
            mail.setLog(new HashMap<Long, MailLog>());
        }

        return mail;
    }



}
