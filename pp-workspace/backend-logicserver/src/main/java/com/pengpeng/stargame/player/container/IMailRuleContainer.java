package com.pengpeng.stargame.player.container;

import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.player.Mail;
import com.pengpeng.stargame.model.player.MailInfo;
import com.pengpeng.stargame.model.player.MailPlus;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.vo.role.MailPlusVO;
import com.pengpeng.stargame.vo.role.MailReq;

import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-28下午5:48
 */
public interface IMailRuleContainer {

    /**
     * 检测列表
     */
    public List<MailPlusVO> getMailList(Mail sysMail, Mail mail);

    /**
     * 解析附件
     */
    public void parseAttachmentsString(MailInfo mailInfo, String Attachments) throws AlertException;


    /**
     * 领取
     */
    public int accept(MailInfo mailInfo, Player player) throws AlertException;

    void getMailPlus(Mail mail, List<MailPlusVO> list);

    void getBulletinMailPlus(Mail mail,Mail bulletin, List<MailPlusVO> list);

    void getAttachment(MailInfo mailInfo, MailPlusVO mailPlus);

    /**
     * 发送邮件
     * @param id
     * @param mailReq
     */
    void sendMail(String id,MailReq mailReq) throws AlertException;
}
