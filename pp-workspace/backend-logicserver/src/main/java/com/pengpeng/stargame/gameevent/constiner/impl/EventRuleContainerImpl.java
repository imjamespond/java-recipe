package com.pengpeng.stargame.gameevent.constiner.impl;

import com.pengpeng.stargame.common.Constant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.exception.RuleException;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.container.IFarmLevelRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.farm.rule.DropItem;
import com.pengpeng.stargame.fashion.dao.IFashionCupboardDao;
import com.pengpeng.stargame.gameevent.constiner.IDropGfitRuleContainer;
import com.pengpeng.stargame.gameevent.constiner.IEventRuleContainer;
import com.pengpeng.stargame.gameevent.dao.IEventDropDao;
import com.pengpeng.stargame.gameevent.dao.IPlayerEventDao;
import com.pengpeng.stargame.gameevent.rule.DropGiftRule;
import com.pengpeng.stargame.gameevent.rule.EventRule;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.farm.decorate.FarmDecoratePkg;
import com.pengpeng.stargame.model.gameEvent.EventDrop;
import com.pengpeng.stargame.model.gameEvent.OneDrop;
import com.pengpeng.stargame.model.gameEvent.PlayerEvent;
import com.pengpeng.stargame.model.piazza.MoneyTree;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.piazza.container.FamilyConstant;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.dao.IMoneyTreeDao;
import com.pengpeng.stargame.piazza.rule.FamilyRule;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.player.container.ISceneRuleContainer;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.player.dao.impl.SiteDao;
import com.pengpeng.stargame.room.dao.IRoomPackegeDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspFarmFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.util.RandomUtil;
import com.pengpeng.stargame.util.Uid;
import com.pengpeng.stargame.vo.RewardVO;
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
 * Date: 13-12-6
 * Time: 下午4:44
 */
@Component
public class EventRuleContainerImpl extends HashMapContainer<String, EventRule> implements IEventRuleContainer {
    @Autowired
    private IEventDropDao eventDropDao;
    @Autowired
    private IDropGfitRuleContainer dropGfitRuleContainer;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IFarmLevelRuleContainer farmLevelRuleContainer;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private RspFarmFactory rsp;
    @Autowired
    private IFamilyRuleContainer familyRuleContainer;
    @Autowired
    private ISceneRuleContainer sceneRuleContainer;
    @Autowired
    private IMoneyTreeDao moneyTreeDao;

    @Autowired
    private SiteDao siteDao;
    @Autowired
    private IFarmPackageDao farmPackageDao;
    @Autowired
    private IFashionCupboardDao fashionCupboardDao;
    @Autowired
    private IRoomPackegeDao roomPackegeDao;
    @Autowired
    private IPlayerEventDao playerEventDao;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private RspRoleFactory rspRoleFactory;
    @Autowired
    private StatusRemote statusRemote;
    @Autowired
    private FrontendServiceProxy frontendService;
    @Autowired
    private MessageSource message;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;
    @Autowired
    private IPlayerDao playerDao;
    @Override
    public EventRule getEventRuleByGiftId(String giftId) {
        for(EventRule eventRule:items.values()){
            if(eventRule.getDropGifts()!=null){
                boolean  has=false;
                for(String id:eventRule.getDropGifts()){
                    if(giftId.equals(id)){
                        has=true;
                        break;
                    }
                }
                if(has){
                    return eventRule;
                }
            }
        }
        return null;
    }

    @Override
    public EventRule getSpringEventRule() {
        EventRule eventRule=getElement("6");
        return eventRule;
    }

    @Override
    public EventRule getFamilyBankEventRule() {
        EventRule eventRule=getElement("5");
        return eventRule;
    }

    @Override
    public EventRule getStarSendGiftDropEventRule() {
        EventRule eventRule=getElement("3");
        return eventRule;
    }

    @Override
    public boolean openStarSendGiftDrop() {
        Date now=new Date();
        int hour=now.getHours();
        EventRule eventRule1=getElement("4");
        EventRule eventRule2=getElement("3");
        if(hour>=Integer.parseInt(eventRule1.getStartTime())&&hour<Integer.parseInt(eventRule1.getEndTime())){
            return true;
        }
        if(hour>=Integer.parseInt(eventRule2.getStartTime())&&hour<Integer.parseInt(eventRule2.getEndTime())){
            return true;
        }
        return false;
    }

    @Override
    public boolean openStarPlazaEvent() {
        Date now=new Date();
        int hour=now.getHours();
        EventRule eventRule1=getElement("2");
        if(hour>Integer.parseInt(eventRule1.getStartTime())&&hour<=Integer.parseInt(eventRule1.getEndTime())){
            return true;
        }
        return false;
    }

    @Override
    public int getStarSendGiftAddP() {
        if(openStarSendGiftDrop()){
            EventRule eventRule1=getStarSendGiftDropEventRule();
            return eventRule1.getAddP();
        }
       return 0;
    }

    /**
     *  给明星送礼 掉落活动 调用
     * @param channelId
     * @param probability
     */
    @Override
    public boolean starSendGiftDrop(String channelId, int probability) {
        EventDrop eventDrop=eventDropDao.getEventDrop(channelId);
        EventRule eventRule=getStarSendGiftDropEventRule();
        int num= RandomUtil.range(0,10000);
        boolean  isDrop=false;
        /**
         * r如果掉落   概率控制
         */
        if(probability>num){
            isDrop=true;
            /**
             * 随机个数
             */
            int dropnum=RandomUtil.range(5,10);
            List<String>pList=new ArrayList<String>();
            if(eventRule.getPositions()!=null&&eventRule.getPositions().length>0){
                for(String p:eventRule.getPositions()){
                    pList.add(p);
                }
            }

            for(int i=0;i<dropnum;i++){
                /**
                 * 随机 礼物
                 */
                int index=RandomUtil.range(0,(eventRule.getDropGifts().length));
                String giftId=eventRule.getDropGifts()[index];
                DropGiftRule dropGiftRule=dropGfitRuleContainer.getElement(giftId);

                /**
                 * 添加礼物
                 */
                OneDrop oneDrop=new OneDrop();
                oneDrop.setUid(Uid.uuid());
                oneDrop.setGiftId(giftId);
                oneDrop.setName(dropGiftRule.getName());

                int pindex=RandomUtil.range(0,pList.size()-1);
                String positon=pList.get(pindex);
                oneDrop.setPosition(positon);
                pList.remove(positon);

                eventDrop.addOneDrop(oneDrop);
            }
            eventDropDao.saveBean(eventDrop);
        }
        return isDrop;
    }

    /**
     * 明星广场 掉落
     */
    @Override
    public void starPlazaDrop() {
        if(openStarPlazaEvent()){ //如果活动开启
            EventRule eventRule=getElement("2");

            List<String>pList=new ArrayList<String>();
            if(eventRule.getPositions()!=null&&eventRule.getPositions().length>0){
                for(String p:eventRule.getPositions()){
                    pList.add(p);
                }
            }

            for(FamilyRule familyRule:familyRuleContainer.getAll()){
                String channelId = sceneRuleContainer.getChannelId("map_10",null,familyRule.getId());
                EventDrop eventDrop=eventDropDao.getEventDrop(channelId);
                Date now =new Date();
                /**
                 * r如果掉落  明星广场的 掉落用时间 控制,摇钱树成熟的时候 掉落
                 */
                MoneyTree moneyTree=moneyTreeDao.getMoneyTree(familyRule.getId(),now);
                /**
                 * 多少分钟后 再次 出现
                 */
                if(eventDrop.getNextTime()==null){
                    eventDrop.setNextTime(now);
                }
                int nextAdd=Integer.parseInt(eventRule.getFrequency().split(",")[0]);
                if(moneyTree.getStatus()==2){ //如果是摇钱树成熟了，那么立即掉落，下次掉了时间是 5分钟
                    eventDrop.setNextTime(now);
                     nextAdd=Integer.parseInt(eventRule.getFrequency().split(",")[1]);
                }
                if(new Date().after(eventDrop.getNextTime())){

                    /**
                     * 计算个数
                     */
                    Session[] sessions = statusRemote.getMember(null, channelId);
                    int dropnum=RandomUtil.range(5,sessions.length/5+5);
                    if(dropnum>50){
                        dropnum=50;
                    }
                    for(int i=0;i<dropnum;i++){

                        int index=RandomUtil.range(0,eventRule.getDropGifts().length);
                        String giftId=eventRule.getDropGifts()[index];
                        DropGiftRule dropGiftRule=dropGfitRuleContainer.getElement(giftId);

                        /**
                         * 添加礼物
                         */
                        OneDrop oneDrop=new OneDrop();
                        oneDrop.setUid(Uid.uuid());
                        oneDrop.setGiftId(giftId);
                        oneDrop.setName(dropGiftRule.getName());
                        oneDrop.setExpiration(DateUtil.addMinute(new Date(),5));

                        if(pList.size()>0){
                            int pindex=RandomUtil.range(0,pList.size());
                            String positon=pList.get(pindex);
                            oneDrop.setPosition(positon);
                            pList.remove(positon);
                        }else{
                            if(eventRule.getPositions()!=null&&eventRule.getPositions().length>0){
                                int pindex1=RandomUtil.range(0,eventRule.getPositions().length);
                                oneDrop.setPosition(eventRule.getPositions()[pindex1]);
                            }
                        }

                        eventDrop.addOneDrop(oneDrop);

                    }
                    eventDrop.setNextTime(DateUtil.addMinute(now,nextAdd));
                    eventDropDao.saveBean(eventDrop);
                    /**
                     * 广播给家族成员
                     */;
                    ChatVO vo = rspRoleFactory.newFamilyChat("", "", message.getMessage("family.piazza.drop",null, Locale.CHINA));
                    Session[] fsessions = statusRemote.getMember(null,familyRule.getId());
                    frontendService.broadcast(fsessions, vo);
                }
            }
        }

    }

    /**
     * 春节活动的 掉落
     * @param channelId
     */
    @Override
    public void springEventDrop(String channelId) {
        EventDrop eventDrop=eventDropDao.getEventDrop(channelId);
        EventRule eventRule=getSpringEventRule();

        List<String>pList=new ArrayList<String>();
        if(eventRule.getPositions()!=null&&eventRule.getPositions().length>0){
            for(String p:eventRule.getPositions()){
                pList.add(p);
            }
        }

            for(int i=0;i<15;i++){
                /**
                 * 随机 礼物
                 */
                int index=RandomUtil.range(0,eventRule.getDropGifts().length);
                String giftId=eventRule.getDropGifts()[index];
                DropGiftRule dropGiftRule=dropGfitRuleContainer.getElement(giftId);

                /**
                 * 添加礼物
                 */
                OneDrop oneDrop=new OneDrop();
                oneDrop.setUid(Uid.uuid());
                oneDrop.setGiftId(giftId);
                oneDrop.setName(dropGiftRule.getName());

                if(pList.size()>0){
                    int pindex=RandomUtil.range(0,pList.size());
                    String positon=pList.get(pindex);
                    oneDrop.setPosition(positon);
                    pList.remove(positon);
                }else{
                    if(eventRule.getPositions()!=null&&eventRule.getPositions().length>0){
                        int pindex1=RandomUtil.range(0,eventRule.getPositions().length);
                        oneDrop.setPosition(eventRule.getPositions()[pindex1]);
                    }
                }


                eventDrop.addOneDrop(oneDrop);
            }
            eventDropDao.saveBean(eventDrop);

    }

    @Override
    public RewardVO pickDropGift(PlayerEvent playerEvent,Player player, FarmPlayer farmPlayer,FarmPackage farmPackage,FashionCupboard fashionCupboard,RoomPackege roomPackege, OneDrop oneDrop,EventDrop eventDrop,FarmDecoratePkg farmDecoratePkg) throws AlertException, RuleException {
        if(oneDrop==null){
            exceptionFactory.throwAlertException("已经被其它玩家捡取了！");
        }
        EventRule eventRule=getEventRuleByGiftId(oneDrop.getGiftId());
        if(playerEvent.getPickNum(eventRule.getId())+1>eventRule.getMaxNum()){
            exceptionFactory.throwAlertException(eventRule.getDes()+"每日只能拾取"+eventRule.getMaxNum()+"个小礼物");
        }

        playerEvent.addPickNum(eventRule.getId(),1);
        /**
         * 先把礼物 移除掉
         */
        eventDrop.remove(oneDrop);
        eventDropDao.saveBean(eventDrop);


        DropGiftRule dropGiftRule=dropGfitRuleContainer.getElement(oneDrop.getGiftId());
        DropItem dropItem=dropGiftRule.getReward();
        RewardVO rewardVO=new RewardVO();
        if(dropItem.getItemId().equals(Constant.GAME_MONEY_ID)){
//            player.incGameCoin(dropItem.getNum());
            playerRuleContainer.incGameCoin(player,dropItem.getNum());
            rewardVO.setGold(dropItem.getNum());
        }
        else if(dropItem.getItemId().equals(Constant.FARM_EXP_ID)){
            farmLevelRuleContainer.addFarmExp(farmPlayer,dropItem.getNum());
            rewardVO.setFarmExp(dropItem.getNum());
        }
        else if(dropItem.getItemId().equals(Constant.INTEGRAL_ID)){
            try {
//                siteDao.addCustomPointsByGame(player.getUserId(), dropItem.getNum());
                rewardVO.setIntegral(dropItem.getNum());
            } catch (Exception e) {
                logger.error(e);
                exceptionFactory.throwRuleException("active.gamecoin");
            }
        } else {
            baseItemRulecontainer.addGoodsNoSave(dropItem.getItemId(),dropItem.getNum(),farmPackage,roomPackege,fashionCupboard,farmDecoratePkg);
            rewardVO.addGoodsVO(rsp.getGoodsVo(dropItem.getItemId(),dropItem.getNum()));
        }
        rewardVO.setPosition(oneDrop.getPosition());


        playerEventDao.saveBean(playerEvent);
        fashionCupboardDao.saveBean(fashionCupboard);
        roomPackegeDao.saveBean(roomPackege);
        farmPackageDao.saveBean(farmPackage);
        farmPlayerDao.saveBean(farmPlayer);
        playerDao.saveBean(player);
        return rewardVO;
    }
}
