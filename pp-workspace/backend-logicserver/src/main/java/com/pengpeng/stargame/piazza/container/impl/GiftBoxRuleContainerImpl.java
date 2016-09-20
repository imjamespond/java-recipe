package com.pengpeng.stargame.piazza.container.impl;

import com.pengpeng.stargame.common.Constant;
import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.farm.rule.DropItem;
import com.pengpeng.stargame.fashion.dao.IFashionCupboardDao;
import com.pengpeng.stargame.gameevent.constiner.IEventRuleContainer;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.piazza.StarGift;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.container.IGiftBoxRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.piazza.dao.IStarGiftDao;
import com.pengpeng.stargame.piazza.rule.FamilyRule;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.room.dao.IRoomPackegeDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.SessionUtil;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.util.RandomUtil;
import com.pengpeng.stargame.vo.chat.ChatVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * User: mql
 * Date: 13-7-4
 * Time: 下午4:15
 */
@Component
public class GiftBoxRuleContainerImpl implements IGiftBoxRuleContainer {

    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IFarmPackageDao farmPackageDao;
    @Autowired
    private IRoomPackegeDao roomPackegeDao;
    @Autowired
    private IFashionCupboardDao fashionCupboardDao;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private IStarGiftDao starGiftDao;
    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;
    @Autowired
    private IFamilyDao familyDao;
    @Autowired
    private IEventRuleContainer eventRuleContainer;
    @Autowired
    private MessageSource message;
    @Autowired
    private RspRoleFactory rspRoleFactory;
    @Autowired
    private StatusRemote statusRemote;
    @Autowired
    private FrontendServiceProxy frontendService;

    @Autowired
    private IFamilyRuleContainer familyRuleContainer;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;
    @Override
    public List<BaseItemRule> getExquisitetGift() {
        List<BaseItemRule> baseItemRuleList = baseItemRulecontainer.getAll();
        List<BaseItemRule> rList = new ArrayList<BaseItemRule>();
        for (BaseItemRule baseItemRule : baseItemRuleList) {
            if (baseItemRule.getType() == 6&&baseItemRule.getStarGift() == 1) {
                rList.add(baseItemRule);
            }
        }
        return rList;
    }

    @Override
    public List<BaseItemRule> getPtGift() {
        List<BaseItemRule> baseItemRuleList = baseItemRulecontainer.getAll();
        List<BaseItemRule> rList = new ArrayList<BaseItemRule>();
        for (BaseItemRule baseItemRule : baseItemRuleList) {
            if (baseItemRule.getStarGift() == 1) {
                rList.add(baseItemRule);
            }
        }
        return rList;
    }

    @Override
    public void checkSend(Player player, int type, String id, int num, FarmPlayer farmPlayer, FamilyMemberInfo familyMemberInfo) throws AlertException {
        /**
         * 每日次数判断
         */
        if (familyMemberInfo.getSendGiftNum() + num > farmPlayer.getLevel()) {
            exceptionFactory.throwAlertException("每日赠送礼物数量上限为" + farmPlayer.getLevel() + "，提升农场等级可以提高上限");
        }

        if (type == 1) {
            BaseItemRule baseItemRule = baseItemRulecontainer.getElement(id);
            baseItemRule.checkMoney(player, num);
        } else if (type == 2) {
            int mynum = baseItemRulecontainer.getGoodsNum(player.getId(), id);
            if (mynum < num) {
                exceptionFactory.throwAlertException("数量不够");
            }
        } else {
            exceptionFactory.throwAlertException("参数错误");
        }
    }

    @Override
    public DropItem send(Session session, Player player, int type, String id, int num, String words, FamilyMemberInfo familyMemberInfo) throws AlertException {
        DropItem rdropItem = null;
        BaseItemRule baseItemRule = baseItemRulecontainer.getElement(id);
        if (type == 1) {  //精美礼物

//            baseItemRule.deductMoney(player, num);
            playerRuleContainer.decGoldCoin(player, baseItemRule.getGoldPrice() * num, PlayerConstant.GOLD_ACTION_17);

        } else if (type == 2) { //普通礼物

            //农场物品
            if (baseItemRule.getType() == 1) {
                FarmPackage farmPackage = farmPackageDao.getBean(player.getId());
                farmPackage.deduct(baseItemRule.getId(), num);
                farmPackageDao.saveBean(farmPackage);
            }

            //个人房间
            if (baseItemRule.getType() == 2) {
                RoomPackege roomPackege = roomPackegeDao.getRoomPackege(player.getId());
                for (int i = 0; i < num; i++) {
                    roomPackege.useItem(baseItemRule.getId());
                }
                roomPackegeDao.saveBean(roomPackege);

            }
            //时装道具
            if (baseItemRule.getType() == 3) {
                FashionCupboard fashionCupboard = fashionCupboardDao.getBean(player.getId());
                fashionCupboard.getFashionPkg(String.valueOf(baseItemRule.getItemtype())).deduct(baseItemRule.getId(), num);
                fashionCupboardDao.saveBean(fashionCupboard);
            }

        }


        /**
         * 提交礼物
         */
//        for (int i = 0; i < num; i++) {
        StarGift starGift = new StarGift();
        starGift.setId(baseItemRule.getId());
        starGift.setPid(player.getId());
        starGift.setWords(words);
        starGift.setNum(num);
        if (eventRuleContainer.openStarSendGiftDrop()) {
            starGift.setEventStart(1);
        }
        String familyIdKey = familyMemberInfo.getFamilyId();
        if (baseItemRule.getGoldPrice() > 0) {
            familyIdKey += starGiftDao.goldCoin();
        }
        if (starGiftDao.getGiftSize(familyIdKey) > 100) {
            starGiftDao.rPop(familyIdKey);
        }
        starGiftDao.lPush(familyIdKey, starGift);

//        }
        /**
         * 添加粉丝值
         */
        int addFuns = baseItemRule.getFansValues() * num;
        if (eventRuleContainer.openStarSendGiftDrop()) {
            addFuns = (int) (addFuns * 1.5);
        }
        Family family = familyDao.getBean(familyMemberInfo.getFamilyId());
        family.incFansValue(addFuns);

        /**
         * 掉宝功能（送礼物 个人 有概率获得明星反馈的礼物）
         */
        if (baseItemRule.getDropItemList() != null && baseItemRule.getDropItemList().size() > 0) {
            for (DropItem dropItem : baseItemRule.getDropItemList()) {
                int randomNum = RandomUtil.range(0, 1000);
                if (dropItem.getProbability() < randomNum) {
                    continue;
                }
                baseItemRulecontainer.addGoods(player, dropItem.getItemId(), dropItem.getNum());
                rdropItem = dropItem;
                break;
            }
        }

        familyDao.saveBean(family);
        playerDao.saveBean(player);
        familyMemberInfo.incGiftNum(num, new Date());
        familyMemberInfoDao.saveBean(familyMemberInfo);

        /**
         * 给明星送礼的 场景内 的掉落  活动
         */
        if (eventRuleContainer.openStarSendGiftDrop()) {
            String channelId = SessionUtil.getChannelScene(session);
//          boolean isdrop=eventRuleContainer.starSendGiftDrop(channelId, 10000);
           boolean isdrop=eventRuleContainer.starSendGiftDrop(channelId,baseItemRule.getFansValues()*num);
           if(isdrop){
               FamilyRule rule = familyRuleContainer.getElement(family.getId());
               ChatVO vo = rspRoleFactory.newFamilyChat("", player.getNickName(), message.getMessage("family.giftbox.drop", new String[]{player.getNickName(),rule.getStarName()}, Locale.CHINA));
               Session[] sessions = statusRemote.getMember(session, familyMemberInfo.getFamilyId());
               frontendService.broadcast(sessions, vo);
            }
        }

        return rdropItem;
    }


}
