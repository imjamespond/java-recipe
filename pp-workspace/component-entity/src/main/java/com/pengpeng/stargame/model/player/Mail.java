package com.pengpeng.stargame.model.player;

import com.pengpeng.stargame.model.BaseEntity;
import org.apache.commons.lang.StringUtils;
import java.util.concurrent.atomic.AtomicLong;
import java.util.*;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-28下午4:57
 */
public class Mail extends BaseEntity<String> {
    private String pid;

    //邮件id计数
    private long counter;
    //邮件闪烁提示
    private int notify;
    //事件<counter,timestamp>
    private Map<Long,MailLog> log;
    private Map<Long,MailInfo> mailList;

    @Override
    public String getId() {
        return pid;
    }

    @Override
    public void setId(String id) {
        this.pid = pid;
    }

    @Override
    public String getKey() {
        return pid;
    }

    public MailInfo getMailInfo(long counter){
        if(mailList.containsKey(counter))
            return mailList.get(counter);
        return null;
    }

    public void addMailInfo(MailInfo info){
        if(mailList.size()>=MailConstant.MAIL_MAX)
            return;
        info.setCounter(++counter);
        mailList.put(info.getCounter(),info);
    }

    public void removeMail(long counter){
        mailList.remove(counter);
    }

    public void removeBulletin(Mail bulletin,long counter){
        Iterator<Map.Entry<Long, MailLog>> it = log.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry<Long, MailLog> entry = it.next();
            MailLog mailLog = entry.getValue();
            if(!bulletin.getMailList().containsKey(entry.getKey()))
                it.remove();
        }
        if(log.containsKey(counter)){
            MailLog mailLog = log.get(counter);
            mailLog.setDeleted(true);
            return;
        }
        log.put(counter,new MailLog(true,true));
    }

    public void setAccepted(long counter){
        if(log.containsKey(counter)){
            MailLog mailLog = log.get(counter);
            mailLog.setAccepted(true);
            return;
        }
        log.put(counter,new MailLog(false,true));
    }

    public boolean accepted(long counter){
        if(log.containsKey(counter)){
            MailLog mailLog = log.get(counter);
            return mailLog.isAccepted();
        }
        log.put(counter,new MailLog(false,false));
        return false;
    }

    public int size(){
        return mailList.size();
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public long getCounter() {
        return counter;
    }

    public void setCounter(long counter) {
        this.counter = counter;
    }

    public Map<Long, MailLog> getLog() {
        return log;
    }

    public void setLog(Map<Long, MailLog> log) {
        this.log = log;
    }

    public Map<Long, MailInfo> getMailList() {
        return mailList;
    }

    public void setMailList(Map<Long, MailInfo> mailList) {
        this.mailList = mailList;
    }

    public int getNotify() {
        return notify;
    }

    public void setNotify(int notify) {
        this.notify = notify;
    }
}
