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
import com.pengpeng.stargame.model.player.Bubble;
import com.pengpeng.stargame.model.player.BubbleConstant;
import com.pengpeng.stargame.model.player.MailAttachment;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.player.container.IBubbleRuleContainer;
import com.pengpeng.stargame.player.dao.IBubbleDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.room.dao.IRoomPackegeDao;
import com.pengpeng.stargame.vo.BaseRewardVO;
import com.pengpeng.stargame.vo.map.BubbleVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-5-28下午5:49
 */
@Component
public class BubbleRuleContainerImpl implements IBubbleRuleContainer {

    private static List<Attach> attachList = new ArrayList<Attach>();
    private static int PROBABILITY = 0;
    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private IFarmPackageDao farmPackageDao;
    @Autowired
    private IRoomPackegeDao roomPackegeDao;
    @Autowired
    private IFashionCupboardDao fashionCupboardDao;
    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IBubbleDao bubbleDao;
    @Autowired
    private IFarmDecoratePkgDao farmDecoratePkgDao;
    @Autowired
    private IIntegralRuleContainer iIntegralRuleContainer;
    @Autowired
    private IFarmLevelRuleContainer farmLevelRuleContainer;
    @Override
    public void addAttachList(String item,String name, int num, int type, int probability) {
        Attach attach = new Attach();

        BaseRewardVO vo = new BaseRewardVO(item, type, num, name);
        attach.vo = vo;
        attach.probability = probability;
        attachList.add(attach);
        PROBABILITY=probability;
    }

    @Override
    public BubbleVO getBubble(String pid, int type) {
        BubbleVO vo = new BubbleVO(pid);
        vo.setAttach(attachList.get(type).vo);
        return vo;
    }

    @Override
    public int accept(Player player, Bubble bubble) throws AlertException {

        Attach attach = attachList.get(bubble.getType());

        int notify = 0;

        FarmPackage fp = null;
        RoomPackege rp = null;
        FashionCupboard fc = null;
        FarmPlayer farmPlayer= null;
        FamilyMemberInfo familyMemberInfo = null;
        FarmDecoratePkg farmDecoratePkg=null;
        if (null != attach) {
                if (BaseRewardConstant.TYPE_GAMECOIN == attach.vo.getType()) {
                    if(attach.vo.getNum()>0){
                        player.incGameCoin(attach.vo.getNum());
                        notify |= BaseRewardConstant.NOTIFY_GAMECOIN;
                    }
                }
                else if (BaseRewardConstant.TYPE_FARMEXP == attach.vo.getType()) {
                    if(attach.vo.getNum()>0){
                        if(null == farmPlayer)
                            farmPlayer=farmPlayerDao.getFarmPlayer(player.getId(), System.currentTimeMillis());
                        farmLevelRuleContainer.addFarmExp(farmPlayer, attach.vo.getNum());
                        notify |= BaseRewardConstant.NOTIFY_FARMEXP;
                    }
                }
                else if (BaseRewardConstant.TYPE_FAMILY_CONTRIBUTION == attach.vo.getType()) {
                    if(attach.vo.getNum()>0){
                        if(null == farmPlayer)
                            familyMemberInfo = familyMemberInfoDao.getFamilyMember(player.getId());
                        familyMemberInfo.incDevote(attach.vo.getNum(), new Date());
                        notify |= BaseRewardConstant.NOTIFY_FAMILY_CONTRIBUTION;
                    }
                }
//                else if (BaseRewardConstant.TYPE_CREDIT == attach.vo.getType()) {
//                    if(attach.vo.getNum()>0){
//                        try {
////                            siteDao.addCustomPointsByGame(player.getUserId(), attachment.getNum());
//                            iIntegralRuleContainer.addIntegralAction(player.getId(),mailInfo.getIntegralType(),attach.vo.getNum());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            exceptionFactory.throwAlertException("active.gamecoin");
//                        }
//                        notify |= BaseRewardConstant.NOTIFY_CREDIT;
//                    }
//                }
                else if (BaseRewardConstant.TYPE_ITEM == attach.vo.getType()) {
                    if (attach.vo.getId() == null) {
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
                    BaseItemRule baseItemRule = baseItemRulecontainer.getElement(attach.vo.getId());
                    if (baseItemRule == null) {
                        exceptionFactory.throwAlertException("item.add.failed");
                    }
                    //add item
                    if (!baseItemRulecontainer.addItemAndCheckNoSaving(baseItemRule, attach.vo.getNum(), fp, rp, fc,farmDecoratePkg)) {
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
    public BubbleVO getRandomAttach(Bubble bubble) {
        Random random = new Random();
        int r = random.nextInt(PROBABILITY);  //System.out.println(r);
        int probability = 0;
        for (int i = 0;i<attachList.size();++i){
            Attach attach = attachList.get(i);
            if(probability<=r && r< attach.probability){
                bubble.setType(i);
                BubbleVO vo = new BubbleVO(bubble.getId());
                vo.setAttach(attach.vo);
                return vo;
            }
            probability = attach.probability;
        }
        return null;
    }


    private class Attach {
        public int probability;
        public BaseRewardVO vo;
    }
}
