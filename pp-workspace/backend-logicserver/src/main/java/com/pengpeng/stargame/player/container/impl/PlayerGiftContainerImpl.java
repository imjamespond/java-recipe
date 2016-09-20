package com.pengpeng.stargame.player.container.impl;

import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.dao.IFarmDecoratePkgDao;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.fashion.dao.IFashionCupboardDao;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.decorate.FarmDecoratePkg;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.model.room.FashionCupboard;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.player.container.IPlayerGiftContainer;
import com.pengpeng.stargame.room.dao.IRoomPackegeDao;
import com.pengpeng.stargame.vo.RewardVO;
import com.pengpeng.stargame.vo.farm.GoodsVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-9-18上午11:25
 */
@Component
public class PlayerGiftContainerImpl implements IPlayerGiftContainer {
    private int gameCoin =50000;
    private String itemId = "items_11015";//音乐旋律幼苗
    private String itemId23 = "items_11023";//音乐先锋幼苗
    private int itemNum = 10;

    @Autowired
    private IFarmPackageDao farmPackageDao;
    @Autowired
    private IRoomPackegeDao roomPackegeDao;
    @Autowired
    private IFashionCupboardDao fashionCupboardDao;

    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private IFarmDecoratePkgDao farmDecoratePkgDao;

    public RewardVO give(Player player){
        FarmPackage farmPackage = farmPackageDao.getBean(player.getId());
        RoomPackege roomPackege = roomPackegeDao.getRoomPackege(player.getId());
        FashionCupboard fashionCupboard = fashionCupboardDao.getBean(player.getId());
        FarmDecoratePkg farmDecoratePkg=farmDecoratePkgDao.getFarmDecoratePkg(player.getId());
        baseItemRulecontainer.addGoodsNoSave(itemId,itemNum,farmPackage,roomPackege,fashionCupboard,farmDecoratePkg);
        baseItemRulecontainer.addGoodsNoSave(itemId23,5,farmPackage,roomPackege,fashionCupboard,farmDecoratePkg);
        farmPackageDao.saveBean(farmPackage);
        roomPackegeDao.saveBean(roomPackege);
        fashionCupboardDao.saveBean(fashionCupboard);
        player.incGameCoin(gameCoin);
        RewardVO vo = new RewardVO();
        vo.setGold(gameCoin);
        BaseItemRule rule =  baseItemRulecontainer.getElement(itemId);
        BaseItemRule rule23 =  baseItemRulecontainer.getElement(itemId23);
        vo.setGoodsVOs(new GoodsVO[]{new GoodsVO(rule.getItemsId(),rule.getName(),itemNum,0,rule.getIcon()),new GoodsVO(rule23.getItemsId(),rule23.getName(),5,0,rule23.getIcon())});
        return vo;
    }
}
