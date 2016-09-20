package com.pengpeng.stargame.player.container.impl;

import com.pengpeng.stargame.constant.BaseItemConstant;
import com.pengpeng.stargame.constant.BaseRewardConstant;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.container.IFarmLevelRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmDecoratePkgDao;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.fashion.dao.IFashionCupboardDao;
import com.pengpeng.stargame.integral.container.IIntegralRuleContainer;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.farm.decorate.FarmDecoratePkg;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.player.*;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.player.container.IMailRuleContainer;
import com.pengpeng.stargame.player.dao.IMailBulletinDao;
import com.pengpeng.stargame.player.dao.IMailDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.player.dao.impl.SiteDao;
import com.pengpeng.stargame.room.dao.IRoomPackegeDao;
import com.pengpeng.stargame.vo.BaseRewardVO;
import com.pengpeng.stargame.vo.role.MailPlusVO;
import com.pengpeng.stargame.vo.role.MailReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-28下午5:49
 */
@Component
public class MailRuleContainerImpl implements IMailRuleContainer {
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IMailDao mailDao;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private IFarmPackageDao farmPackageDao;
    @Autowired
    private IRoomPackegeDao roomPackegeDao;
    @Autowired
    private IFashionCupboardDao fashionCupboardDao;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private IFarmLevelRuleContainer farmLevelRuleContainer;
    @Autowired
    private SiteDao siteDao;
    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;
    @Autowired
    private IMailBulletinDao mailBulletinDao;
    @Autowired
    private IFarmDecoratePkgDao farmDecoratePkgDao;
    @Autowired
    private IIntegralRuleContainer iIntegralRuleContainer;
    @Override
    public List<MailPlusVO> getMailList(Mail bulletin, Mail mail) {
        List<MailPlusVO> list = new ArrayList<MailPlusVO>();
        //系统邮件
        getBulletinMailPlus(mail, bulletin, list);

        //自己邮件
        boolean needUpdate = false;
        long now = System.currentTimeMillis();
        TreeMap<Long, MailInfo> newMap = new TreeMap<Long, MailInfo>(new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                return o1 > o2 ? 1 : -1;
            }
        });
        for (Map.Entry<Long, MailInfo> entry : mail.getMailList().entrySet()) {
            MailInfo mailInfo = entry.getValue();
            if (now - mailInfo.getCreateDate().getTime() < MailConstant.AVAILABLETIME) { //过期的邮件清除
                newMap.put(entry.getKey(), mailInfo);
            } else {
                needUpdate = true;
            }
        }
        mail.setMailList(newMap);

        //取消邮件闪烁
        if(mail.getNotify()==1){
            mail.setNotify(0);
            needUpdate = true;
        }
        if (needUpdate) {
            mailDao.saveBean(mail);
        }
        getMailPlus(mail, list);

        return list;
    }

    @Override
    public void parseAttachmentsString(MailInfo mailInfo, String attachments) throws AlertException {

        if (null == mailInfo.getAttachments()) {
            mailInfo.setAttachments(new ArrayList<MailAttachment>());
        }
        List<MailAttachment> list = mailInfo.getAttachments();


            if (null == attachments) {
                return;
            }

            String[] arrItems = attachments.split(",");//id:num;...
            if (arrItems.length <= 0) {
                return;
            }

            for (int i = 0; i < arrItems.length; i++) {
                String[] item = arrItems[i].split(":");
                if (item.length != 3) {
                    continue;
                }
                MailAttachment attachment = new MailAttachment();
                attachment.setType(Integer.valueOf(item[2]));
                attachment.setNum(Integer.valueOf(item[1]));
                if(BaseRewardConstant.TYPE_ITEM==attachment.getType())
                attachment.setItemId(item[0]);
                list.add(attachment);
            }

    }

    @Override
    public int accept(MailInfo mailInfo, Player player) throws AlertException {
        List<MailAttachment> attachments = mailInfo.getAttachments();

        int notify = 0;

        FarmPackage fp = null;
        RoomPackege rp = null;
        FashionCupboard fc = null;
        FarmPlayer farmPlayer= null;
        FamilyMemberInfo familyMemberInfo = null;
        FarmDecoratePkg farmDecoratePkg=null;
        if (null != attachments) {
            //each attachment
            for (MailAttachment attachment : attachments) {
                if (BaseRewardConstant.TYPE_GAMECOIN == attachment.getType()) {
                    if(attachment.getNum()>0){
                        player.incGameCoin(attachment.getNum());
                        notify |= BaseRewardConstant.NOTIFY_GAMECOIN;
                    }
                }
                else if (BaseRewardConstant.TYPE_FARMEXP == attachment.getType()) {
                    if(attachment.getNum()>0){
                        if(null == farmPlayer)
                        farmPlayer=farmPlayerDao.getFarmPlayer(player.getId(), System.currentTimeMillis());
                        farmLevelRuleContainer.addFarmExp(farmPlayer, attachment.getNum());
                        notify |= BaseRewardConstant.NOTIFY_FARMEXP;
                    }
                }
                else if (BaseRewardConstant.TYPE_FAMILY_CONTRIBUTION == attachment.getType()) {
                    if(attachment.getNum()>0){
                        if(null == farmPlayer)
                            familyMemberInfo = familyMemberInfoDao.getFamilyMember(player.getId());
                        familyMemberInfo.incDevote(attachment.getNum(), new Date());
                        notify |= BaseRewardConstant.NOTIFY_FAMILY_CONTRIBUTION;
                    }
                }
                else if (BaseRewardConstant.TYPE_CREDIT == attachment.getType()) {
                    if(attachment.getNum()>0){
                        try {
//                            siteDao.addCustomPointsByGame(player.getUserId(), attachment.getNum());
                            iIntegralRuleContainer.addIntegralAction(player.getId(),mailInfo.getIntegralType(),attachment.getNum());
                        } catch (Exception e) {
                            e.printStackTrace();
                            exceptionFactory.throwAlertException("active.gamecoin");
                        }
                        notify |= BaseRewardConstant.NOTIFY_CREDIT;
                    }
                }
                else if (BaseRewardConstant.TYPE_ITEM == attachment.getType()) {
                    if (attachment.getItemId() == null) {
                        exceptionFactory.throwAlertException("item.id.invalid");
                    }
                    //entity
                    if(null == fp)
                    fp = farmPackageDao.getBean(player.getId());
                    if(null == rp)
                    rp = roomPackegeDao.getBean(player.getId());
                    if(null == fc)
                    fc = fashionCupboardDao.getBean(player.getId());
                    if(null==farmDecoratePkg)
                    farmDecoratePkg=farmDecoratePkgDao.getFarmDecoratePkg(player.getId());
                    //item rule
                    BaseItemRule baseItemRule = baseItemRulecontainer.getElement(attachment.getItemId());
                    if (baseItemRule == null) {
                        exceptionFactory.throwAlertException("item.add.failed");
                    }
                    //add item
                    if (!baseItemRulecontainer.addItemAndCheckNoSaving(baseItemRule, attachment.getNum(), fp, rp, fc,farmDecoratePkg)) {
                        if (baseItemRule.getItemtype() == BaseItemConstant.TYPE_FARM)
                            exceptionFactory.throwAlertException("package.farm.full");
                        else if (baseItemRule.getItemtype() == BaseItemConstant.TYPE_FASHION)
                            exceptionFactory.throwAlertException("package.fashion.full");
                        else
                            exceptionFactory.throwAlertException("item.add.failed");
                    }
                    notify |= BaseRewardConstant.NOTIFY_ITEM;
                }
            }
        }
        //persistence
        if(null != fp)
            farmPackageDao.saveBean(fp);
        if(null != rp)
            roomPackegeDao.saveBean(rp);
        if(null != fc)
            fashionCupboardDao.saveBean(fc);
        if(null != farmPlayer)
            farmPlayerDao.saveBean(farmPlayer);
        if(null != familyMemberInfo)
            familyMemberInfoDao.saveBean(familyMemberInfo);
        if(null!=farmDecoratePkg)
            farmDecoratePkgDao.saveBean(farmDecoratePkg);
        if((notify&BaseRewardConstant.NOTIFY_GAMECOIN)>0)
        playerDao.saveBean(player);

        return notify;
    }

    @Override
    public void getBulletinMailPlus(Mail mail, Mail bulletin, List<MailPlusVO> list) {
        long now = System.currentTimeMillis();
        for (Map.Entry<Long, MailInfo> entry : bulletin.getMailList().entrySet()) {
            MailInfo mailInfo = entry.getValue();

            //系统邮件是否标记为删除
            if (mail.getLog().containsKey(mailInfo.getCounter())) {
                if (mail.getLog().get(mailInfo.getCounter()).isDeleted())
                    continue;
            }

            MailPlusVO mailPlus = new MailPlusVO();
            mailPlus.setId(mailInfo.getCounter());
            mailPlus.setTitle(mailInfo.getTitle());
            mailPlus.setContent(mailInfo.getContent());
            mailPlus.setType(MailConstant.TYPE_BULLETIN);
            mailPlus.setCreateDate(mailInfo.getCreateDate().getTime());
            mailPlus.setAccepted(mail.accepted(mailInfo.getCounter()));
            getAttachment(mailInfo, mailPlus);
            list.add(mailPlus);
        }
    }

    @Override
    public void getMailPlus(Mail mail, List<MailPlusVO> list) {
        long now = System.currentTimeMillis();
        for (Map.Entry<Long, MailInfo> entry : mail.getMailList().entrySet()) {
            MailInfo mailInfo = entry.getValue();
            MailPlus mailPlus = new MailPlus();
            mailPlus.setId(mailInfo.getCounter());
            mailPlus.setTitle(mailInfo.getTitle());
            mailPlus.setContent(mailInfo.getContent());
            mailPlus.setType(MailConstant.TYPE_ORDINARY);
            mailPlus.setCreateDate(mailInfo.getCreateDate().getTime());
            mailPlus.setRestTime(mailInfo.getCreateDate().getTime() + MailConstant.AVAILABLETIME - now);
            mailPlus.setAccepted(mailInfo.accepted());
            getAttachment(mailInfo, mailPlus);
            list.add(mailPlus);
        }
    }

    @Override
    public void getAttachment(MailInfo mailInfo, MailPlusVO mailPlus) {
        List<MailAttachment> list = mailInfo.getAttachments();

        if (null == list) {
            return;
        }

        BaseRewardVO[] listVO = new BaseRewardVO[list.size()];
        int i = 0;
        for (MailAttachment mailAttachment : list) {
            BaseRewardVO vo = new BaseRewardVO();
            vo.setNum(mailAttachment.getNum());
            vo.setType(mailAttachment.getType());
            listVO[i++] = vo;
            if (BaseRewardConstant.TYPE_ITEM != mailAttachment.getType())
                continue;
            BaseItemRule item = baseItemRulecontainer.getElement(mailAttachment.getItemId());
            if (null != item) {
                vo.setId(item.getId());
                vo.setItemName(item.getName());
                vo.setGoodsType(item.getType());
            }
        }
        mailPlus.setAttachmentVOList(listVO);
    }

    @Override
    public void sendMail(String id, MailReq req) throws AlertException {
        Mail mail = null;
        if(req.getType() == MailConstant.TYPE_BULLETIN){
            mail = mailBulletinDao.getMail();
        } else if(req.getType() == MailConstant.TYPE_ORDINARY){
            mail = mailDao.getMail(id);
        }

        MailInfo mailInfo = new MailInfo();
        mailInfo.setTitle(req.getTitle());
        mailInfo.setContent(req.getContent());
        mailInfo.setIntegralType(req.getIntegralType());
        parseAttachmentsString(mailInfo, req.getAttachments());
        mail.setNotify(1);
        mail.addMailInfo(mailInfo);

        if(req.getType() == MailConstant.TYPE_BULLETIN){
            mailBulletinDao.saveBean(mail);
        } else if(req.getType() == MailConstant.TYPE_ORDINARY){
            mailDao.saveBean(mail);
        }
    }
}
