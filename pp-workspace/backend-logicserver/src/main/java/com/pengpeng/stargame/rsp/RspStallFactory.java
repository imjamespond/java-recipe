package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.player.Friend;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.stall.PlayerShelf;
import com.pengpeng.stargame.model.stall.PlayerStall;
import com.pengpeng.stargame.model.stall.StallConstant;
import com.pengpeng.stargame.player.dao.IFriendDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.stall.container.IStallFriShelfContainer;
import com.pengpeng.stargame.stall.container.IStallGoldShelfContainer;
import com.pengpeng.stargame.stall.container.IStallPriceContainer;
import com.pengpeng.stargame.stall.container.IStallVipShelfContainer;
import com.pengpeng.stargame.stall.dao.IPlayerStallDao;
import com.pengpeng.stargame.vip.container.IPayMemberRuleContainer;
import com.pengpeng.stargame.vo.farm.FarmItemVO;
import com.pengpeng.stargame.vo.stall.StallAdvertisementVO;
import com.pengpeng.stargame.vo.stall.StallInfoVO;
import com.pengpeng.stargame.vo.stall.StallPlayerAdvVO;
import com.pengpeng.stargame.vo.stall.StallPlayerShelfVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @auther james
 * @since: 13-6-4上午11:27
 */


@Component
public class RspStallFactory extends RspFactory {
    @Autowired
    private IPlayerStallDao playerStallDao;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IFriendDao friendDao;
    @Autowired
    private IStallGoldShelfContainer stallGoldShelfContainer;
    @Autowired
    private IStallFriShelfContainer stallFriShelfContainer;
    @Autowired
    private IStallVipShelfContainer stallVipShelfContainer;
    @Autowired
    private IPayMemberRuleContainer payMemberRuleContainer;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;


    private static List<FarmItemVO> assistantItems = new ArrayList<FarmItemVO>();

    public void addAssistantItem(FarmItemVO vo) {
        assistantItems.add(vo);
    }

    public FarmItemVO[] getAssistantItem() {
        return assistantItems.toArray(new FarmItemVO[0]);
    }

    public StallInfoVO qinmaStallInfoVO(PlayerStall playerStall) {

        StallInfoVO stallInfoVO = new StallInfoVO();
        stallInfoVO.setEnable(playerStall.isEnable());

        boolean isVip = payMemberRuleContainer.isPayMember(playerStall.getPid());
        stallInfoVO.setBuyingTimes2(playerStall.getBuyingTimes(), isVip);

        List<StallPlayerShelfVO> list = new ArrayList<StallPlayerShelfVO>();
        int order = 0;
        if(null!= playerStall.getPlayerMomShelf()){
            List<PlayerShelf> shelfs = playerStall.getPlayerMomShelf();
            //order = 0;
            for(PlayerShelf shelf:shelfs){
                StallPlayerShelfVO shelfVO = shelfVO(shelf,StallConstant.SHELF_TYPE_MOM,order++);
                list.add(shelfVO);
            }
        }
        StallPlayerShelfVO[] shelfVOs = new StallPlayerShelfVO[list.size()];
        list.toArray(shelfVOs);
        stallInfoVO.setShelfs(shelfVOs);
        return stallInfoVO;
    }

    public StallInfoVO stallInfoVO(PlayerStall playerStall,PlayerStall myStall) {

        Calendar today = Calendar.getInstance();
        boolean needUpdate = false;
        boolean needUpdate2 = false;

        StallInfoVO stallInfoVO = new StallInfoVO();
        stallInfoVO.setEnable(playerStall.isEnable());

        //reset购买次数
        Calendar buyingDate = Calendar.getInstance();
        buyingDate.setTime(myStall.getBuyingDate());
        if(today.get(Calendar.DAY_OF_YEAR)!=buyingDate.get(Calendar.DAY_OF_YEAR)){
            myStall.setBuyingTimes(0);
            needUpdate = true;
        }

        boolean isVip = payMemberRuleContainer.isPayMember(myStall.getPid());
        if(isVip){
            stallVipShelfContainer.enable(myStall);
            needUpdate = true;
            stallInfoVO.setHitShelfNum(StallConstant.HIT_SHELF_VIP_NUM - playerStall.getHitShelfNum());
            stallInfoVO.setBuyingTimes(StallConstant.BUYING_VIP_NUM - myStall.getBuyingTimes());
        }else{
            stallInfoVO.setHitShelfNum(StallConstant.HIT_SHELF_NUM - playerStall.getHitShelfNum());
            stallInfoVO.setBuyingTimes(StallConstant.BUYING_NUM - myStall.getBuyingTimes());
        }
        stallInfoVO.setHitShelfTime(playerStall.getHitShelfTime());

        //是否提示需要达 人币
        if(!playerStall.isEnable()){
            FarmPlayer farmPlayer = farmPlayerDao.getFarmPlayer(playerStall.getId(), System.currentTimeMillis());
            if(farmPlayer.getLevel()<StallConstant.ENABLE_LEVEL){
                stallInfoVO.setNeedGold(true);
            }else{
                stallInfoVO.setNeedGold(false);
            }
        }
        //reset上架次数
        Calendar ref = Calendar.getInstance();
        ref.setTimeInMillis(playerStall.getHitShelfTime());
        if(today.get(Calendar.DAY_OF_YEAR)!=ref.get(Calendar.DAY_OF_YEAR)){
            playerStall.setHitShelfNum(0);
        }

        List<StallPlayerShelfVO> list = new ArrayList<StallPlayerShelfVO>();
        //货架
        int order = 0;
        if(null!= playerStall.getPlayerFriShelf()){
            PlayerShelf[] shelfs = playerStall.getPlayerShelfs();
            for(PlayerShelf shelf:shelfs){
                StallPlayerShelfVO shelfVO = shelfVO(shelf,StallConstant.SHELF_TYPE_ORDINARY,order++);
                list.add(shelfVO);
            }
        }

        //好友架
        if(null!= playerStall.getPlayerFriShelf()){
            List<PlayerShelf> shelfs = playerStall.getPlayerFriShelf();
            order = 0;
            for(PlayerShelf shelf:shelfs){
                StallPlayerShelfVO shelfVO = shelfVO(shelf,StallConstant.SHELF_TYPE_FRIEND,order++);
                list.add(shelfVO);
            }
            stallInfoVO.setFriendShelf(stallFriShelfContainer.getFriend(playerStall.getPlayerFriShelf().size()));
            Friend friend = friendDao.getBean(playerStall.getId());
            try{
                stallFriShelfContainer.enable(playerStall,friend);
                needUpdate2 = true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        //达人币架
        if(null!= playerStall.getPlayerGoldShelf()){
            List<PlayerShelf> shelfs = playerStall.getPlayerGoldShelf();
            order = 0;
            for(PlayerShelf shelf:shelfs){
                StallPlayerShelfVO shelfVO = shelfVO(shelf,StallConstant.SHELF_TYPE_GOLD,order++);
                list.add(shelfVO);
            }
        }

        //vip货架
        if(null!= playerStall.getPlayerVipShelf()){
            List<PlayerShelf> shelfs = playerStall.getPlayerVipShelf();
            order = 0;
            for(PlayerShelf shelf:shelfs){
                StallPlayerShelfVO shelfVO = shelfVO(shelf,StallConstant.SHELF_TYPE_VIP,order++);
                list.add(shelfVO);
            }
            stallInfoVO.setGoldShelf(stallGoldShelfContainer.getGold(playerStall.getPlayerGoldShelf().size()));
        }

        if(needUpdate){
            playerStallDao.saveBean(myStall);
        }
        if(needUpdate2){
            playerStallDao.saveBean(playerStall);
        }

        StallPlayerShelfVO[] shelfVOs = new StallPlayerShelfVO[list.size()];
        list.toArray(shelfVOs);
        stallInfoVO.setShelfs(shelfVOs);
        return stallInfoVO;
    }

    public StallInfoVO stallInfoVO2(PlayerStall playerStall) {
        StallInfoVO stallInfoVO = new StallInfoVO();
        stallInfoVO.setAssistant(playerStall.getAssistantTime().getTime()-System.currentTimeMillis());
        if(playerStall.getAssistantNextTime().compareTo(new Date())>0){
            stallInfoVO.setAssCoolDown(playerStall.getAssistantNextTime().getTime()-System.currentTimeMillis());
        }
        return stallInfoVO;
    }

    public StallPlayerShelfVO shelfVO(PlayerShelf shelf,int type,int order) {
        StallPlayerShelfVO shelfVO = new StallPlayerShelfVO();
        shelfVO.setOrder(order);
        shelfVO.setType(type);

        if(null == shelf){
            shelfVO.setState(StallConstant.SHELF_FREE);
            return shelfVO;
        }


        //购买者信息
        if(shelf.getState() == StallConstant.SHELF_SOLD){
            String buyerId = shelf.getBuyerId();
            if(null!=buyerId){
                Player buyer = playerDao.getBean(buyerId);
                if(null==buyer){
                    shelfVO.setBuyer("碰碰亲妈");
                    shelfVO.setPortrait("qinma_10000");
                }else{
                    shelfVO.setBuyer(buyer.getNickName());
                    shelfVO.setPortrait(getUserPortrait(buyer.getUserId()));
                }
            }
        }

        if(null==shelf.getItemId()){
            return shelfVO;
        }
        shelfVO.setItemId(shelf.getItemId());
        shelfVO.setItemNum(shelf.getItemNum());
        boolean overTime = System.currentTimeMillis()-shelf.getHitShelfTime()>StallConstant.OFF_SHELF_TIME?true:false;
        shelfVO.setOverTime( overTime );
        shelfVO.setAdvTime(shelf.getAdvTime()-System.currentTimeMillis());
        shelfVO.setPrice(shelf.getItemPrice());
        shelfVO.setState(shelf.getState());

        BaseItemRule item = baseItemRulecontainer.getElement(shelf.getItemId());
        if(null != item){
            shelfVO.setItemName(item.getName());
        }
        return shelfVO;
    }


    public StallAdvertisementVO stallAdvVO(StallPlayerAdvVO[] stallPlayerAdvVOs) {
        StallAdvertisementVO stallAdvertisementVO = new StallAdvertisementVO();
        stallAdvertisementVO.setStallPlayerAdvVO(stallPlayerAdvVOs);
        return stallAdvertisementVO;
    }
}
