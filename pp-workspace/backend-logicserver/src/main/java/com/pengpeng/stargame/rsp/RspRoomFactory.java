package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.model.room.DecoratePosition;
import com.pengpeng.stargame.model.room.RoomEvaluate;
import com.pengpeng.stargame.model.room.RoomPackege;
import com.pengpeng.stargame.model.room.RoomPlayer;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.room.container.IRoomExpansionRuleContainer;
import com.pengpeng.stargame.room.container.IRoomItemRuleContainer;
import com.pengpeng.stargame.room.dao.IRoomEvaluateDao;
import com.pengpeng.stargame.room.rule.RoomExpansionRule;
import com.pengpeng.stargame.room.rule.RoomItemRule;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.vo.room.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: mql
 * Date: 13-5-22
 * Time: 下午5:25
 */
@Component()
public class RspRoomFactory extends RspFactory {

    @Autowired
    private IRoomItemRuleContainer roomItemRuleContainer;
    @Autowired
    protected IRoomEvaluateDao roomEvaluateDao;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private IRoomExpansionRuleContainer roomExpansionRuleContainer;

    public RoomVO getRoomVO(RoomPlayer roomPlayer){
        RoomVO roomVO=new RoomVO(roomPlayer.getId());
        int luxuryDegree=0;
        List<DecorateVO> decorateVOList=new ArrayList<DecorateVO>();
        for(DecoratePosition decoratePosition:roomPlayer.getDecoratePositionList()){
            RoomItemRule roomItemRule=roomItemRuleContainer.getElement(decoratePosition.getItemId());
            luxuryDegree+=roomItemRule.getLuxuryDegree();
            DecorateVO decorateVO=new DecorateVO();
            BeanUtils.copyProperties(decoratePosition, decorateVO);
            decorateVOList.add(decorateVO);

            decorateVO.setType(roomItemRule.getItemtype());
            decorateVO.setImage(roomItemRule.getImage());
        }
        roomVO.setDecorateVOs(decorateVOList.toArray(new DecorateVO [0]));

        roomVO.setGlamour(luxuryDegree);
        roomVO.setName(playerDao.getBean(roomPlayer.getId()).getNickName());
        RoomEvaluate roomEvaluate=roomEvaluateDao.getRoomEvaluate(roomPlayer.getPid());
        roomVO.setGoodReputation(roomEvaluate.getNum());

        roomVO.setX(roomPlayer.getX());
        roomVO.setY(roomPlayer.getY());
        if(roomPlayer.getExpansionId()!=0){
          RoomExpansionRule roomExpansionRule=roomExpansionRuleContainer.getElement(roomPlayer.getExpansionId());
          roomVO.setExtension(roomExpansionRule.getLocation());
          Date now=new Date();
          roomVO.setTime(roomPlayer.getExpansionEndTime().getTime()-now.getTime());
        }else{
            roomVO.setExtension(2);
        }

        return   roomVO;
    }

    public RoomShopItemVO[] getRoomShopVOArray(List<RoomItemRule> listItem){
        List<RoomShopItemVO> list = new ArrayList<RoomShopItemVO>();
        if(listItem == null || listItem.isEmpty()){
            return list.toArray(new RoomShopItemVO[0]);
        }
        for(RoomItemRule baseItemRule : listItem){
            list.add(this.getRoomShopItemVO(baseItemRule));
        }
        return list.toArray(new RoomShopItemVO[0]);
    }

    public RoomShopItemVO getRoomShopItemVO(RoomItemRule baseItemRule){
        RoomShopItemVO vo = new RoomShopItemVO();
        BeanUtils.copyProperties(baseItemRule, vo);
        vo.setImage(baseItemRule.getImage());
        return vo;
    }


    public RoomPkgVO buildRoomPkgVO(RoomPackege roomPackege){
        RoomPkgVO roomPkgVO=new RoomPkgVO();
        List<RoomItemVO> farmItemVOList=new ArrayList<RoomItemVO>();

        for(String key:roomPackege.getItems().keySet()){
            RoomItemVO vo=new RoomItemVO();
            vo.setItemId(key);
            vo.setNum(roomPackege.getItems().get(key));

            RoomItemRule roomItemRule=roomItemRuleContainer.getElement(key);
            vo.setRecyclingPrice(roomItemRule.getRecyclingPrice());
            vo.setItemtype(String.valueOf(roomItemRule.getItemtype()));
            vo.setIcon(roomItemRule.getIcon());
            vo.setName(roomItemRule.getName());
            vo.setImage(roomItemRule.getImage());
            farmItemVOList.add(vo);
        }
        roomPkgVO.setBaseItemVO(farmItemVOList.toArray(new RoomItemVO[0]));

        return roomPkgVO;
    }

    public ExtensionVO getExtensionVO(RoomExpansionRule expansionRule){
        ExtensionVO extensionVO=new ExtensionVO();
        extensionVO.setGameCoin(expansionRule.getGameCoin());
        extensionVO.setLevel(expansionRule.getLevel());
        extensionVO.setTime(expansionRule.getTime()*1000);
        extensionVO.setEndTime(DateUtil.addSecond(new Date(),expansionRule.getTime()).getTime());
        return extensionVO;
    }
}


