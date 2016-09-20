package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.common.ItemData;
import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.farm.FarmBuilder;
import com.pengpeng.stargame.farm.container.*;
import com.pengpeng.stargame.farm.dao.IFarmActionDao;
import com.pengpeng.stargame.farm.dao.IFarmDecorateDao;
import com.pengpeng.stargame.farm.dao.IFarmMessageDao;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.rule.*;
import com.pengpeng.stargame.model.farm.*;
import com.pengpeng.stargame.model.farm.box.PlayerFarmBox;
import com.pengpeng.stargame.model.farm.decorate.FarmDecorate;
import com.pengpeng.stargame.model.farm.decorate.OneDecorate;
import com.pengpeng.stargame.model.farm.process.FarmProcessQueue;
import com.pengpeng.stargame.model.farm.process.OneQueue;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.vo.RewardVO;
import com.pengpeng.stargame.vo.farm.*;
import com.pengpeng.stargame.vo.farm.box.FarmBoxVO;
import com.pengpeng.stargame.vo.farm.decorate.FarmDecorateShopItemVO;
import com.pengpeng.stargame.vo.farm.decorate.FarmDecorateVO;
import com.pengpeng.stargame.vo.farm.process.FarmOneQueueVO;
import com.pengpeng.stargame.vo.farm.process.FarmProcessItemVO;
import com.pengpeng.stargame.vo.farm.process.FarmQueueInfoVO;
import com.pengpeng.stargame.vo.room.DecorateVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 农场
 *
 * @author jinli.yuan@pengpeng.com
 * @since 13-4-25 上午10:20
 */
@Component()
public class RspFarmFactory extends RspFactory {
    @Autowired
    private IFarmLevelRuleContainer farmLevelRuleContainer;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private IFarmActionDao farmActionDao;
    @Autowired
    private IFarmMessageDao farmMessageDao;
    @Autowired
    private IFarmProcessRuleContainer farmProcessRuleContainer;
    @Autowired
    private IFarmPackageDao farmPackageDao;
    @Autowired
    private IFarmOrderRuleContainer farmOrderRuleContainer;
    @Autowired
    private IFarmDecorateRuleContainer farmDerocateRuleContainer;
    @Autowired
    private IFarmDecorateRuleContainer farmDecorateRuleContainer;

    public FarmVO transitionVO(Player player, FarmPlayer fp, FarmEvaluate farmEvaluate, FarmFriendHarvest farmFriendHarvest, FarmOrder farmOrder) {

        FarmActionInfo farmActionInfo=farmActionDao.getFarmActionInfo(player.getId());
        FarmMessageInfo farmMessageInfo=farmMessageDao.getFarmMessageInfo(player.getId());
        FarmVO fVo = new FarmVO(fp.getId(), player.getNickName());
        fVo.setLevel(fp.getLevel());
        fVo.setExp(fp.getExp());
        fVo.setCanHarvestNum(fp.getCanHarvestNum());
        fVo.setAllseedNum(fp.getAllCropNum());
        fVo.setGoodReputation(farmEvaluate.getNum());
        fVo.setFriendHelpNum(farmFriendHarvest.getFriendNum());
        fVo.setAllOrderNum(farmOrderRuleContainer.getMaxOrderNum(farmOrder.getpId()));
        fVo.setSurplusOrderNum(farmOrderRuleContainer.getMaxOrderNum(farmOrder.getpId())- farmOrder.getFinishNum());
        fVo.setNextExp(farmLevelRuleContainer.getNeedExpByLevel(fp.getLevel() + 1));

        fVo.setFarmActionNum(farmActionInfo.getNewNum()+farmMessageInfo.getNewNum());

        List<FieldVO> fvos = new ArrayList<FieldVO>();
        fvos.addAll(this.getFieldVOList(fp));


        /**
         * 设置未开放田地
         */
//        for (int i = fp.getFields().size(); i < 24; i++) {
//            FieldVO fieldVO = new FieldVO();
//            fieldVO.setId(i + 1);
//            fieldVO.setPosition(FarmBuilder.FIELD_P.get(String.valueOf(i+1)));
//            fieldVO.setStatus(FarmConstant.FIELD_CLOSE);
//            fvos.add(fieldVO);
//
//        }
        //农场装饰的代码
        if(fp.getLevel()<20){
            for (int i = fp.getFields().size(); i < FarmConstant.FIELD_SEND_NUM; i++) {
                FieldVO fieldVO = new FieldVO();
                fieldVO.setId(i + 1);
                fieldVO.setPosition(farmDecorateRuleContainer.getFieldPosition(i+1));
                fieldVO.setStatus(FarmConstant.FIELD_CLOSE);
                fvos.add(fieldVO);

            }
        }

        fVo.setFieldVOs(fvos.toArray(new FieldVO[0]));
        return fVo;
    }

    public List<FieldVO> getFieldVOList(FarmPlayer farmPlayer) {
        List<FieldVO> list = new ArrayList<FieldVO>();
        for (int i = 0; i < farmPlayer.getFields().size(); i++) {
            FarmField fd = farmPlayer.getFields().get(i);
            FieldVO fieldVO = new FieldVO();

            fieldVO.setId(Integer.parseInt(fd.getId()));
            /**
             * 设置田地的位置
             */
            if(fd.getPosition()==null||fd.getPosition().equals("")){
                fieldVO.setPosition(farmDecorateRuleContainer.getFieldPosition(fieldVO.getId()));
            } else {
                fieldVO.setPosition(fd.getPosition());
            }
//           if(farmDecorate.getItems().get(fd.getId())!=null){
//               fieldVO.setPosition(farmDecorate.getItems().get(fd.getId()).getPosition());
//           } else {
//               if(FarmBuilder.FIELD_P.get(fd.getId())!=null){
//                   fieldVO.setPosition(FarmBuilder.FIELD_P.get(fd.getId()));
//               }
//           }

            /**
             * 作物 状态的 更改，如果作物 时间 到了 1熟或者2熟 ，如果玩家还未 收获，那么客户端 显示可以收获状态
             * 如果玩家 已经收获了 显示种植状态
             */
            fieldVO.setStatus(fd.getStatus());
            if (fd.getRipeId() > 0) {
                if (!fd.isHarvestRipe(fd.getRipeId())) {
                    fieldVO.setStatus(FarmConstant.FIELD_STATUS_RIP);
                }
            }
            /**
             *如果有作物
             */
            if (fieldVO.getStatus() != FarmConstant.FIELD_STATUS_FREE) {
                FarmSeedRule farmSeedRule = (FarmSeedRule) baseItemRulecontainer.getElement(fd.getSeedId());
                fieldVO.setName(farmSeedRule.getName());
                if (fieldVO.getStatus() == FarmConstant.FIELD_STATUS_PLANT) {

                    fieldVO.setImage(farmSeedRule.getGrowthImage());
                    //剩余时间
                    fieldVO.setRipetime(fd.getHarvestTime().getTime() - System.currentTimeMillis());
                }
                if (fieldVO.getStatus() == FarmConstant.FIELD_STATUS_RIP) {
                    fieldVO.setImage(farmSeedRule.getMatureImage());
                }


                /**
                 * 设置下次成熟时间
                 */
                if (fieldVO.getStatus() == FarmConstant.FIELD_STATUS_PLANT) {
                    Product p = farmSeedRule.getOneProduct(fd.getRipeId() + 1);
                    fieldVO.setNextRipeTime(fd.getHarvestTime().getTime() - (farmSeedRule.getGrowthTime() - p.getTime()) * 1000 - System.currentTimeMillis());
                }
            }
            list.add(fieldVO);

        }
        return list;
    }

    public FieldVO getFieldVO(FarmField fd, FarmPlayer farmPlayer, int exp) {
        List<FieldVO> fieldVOs = this.getFieldVOList(farmPlayer);

        long minNestRipeTime = 0;
        for (FieldVO fieldVO : fieldVOs) {
            if (fieldVO.getNextRipeTime() != 0) {
                if (minNestRipeTime == 0) {
                    minNestRipeTime = fieldVO.getNextRipeTime();
                }
                if (fieldVO.getNextRipeTime() < minNestRipeTime) {
                    minNestRipeTime = fieldVO.getNextRipeTime();
                }
            }
        }
        for (FieldVO fieldVO : fieldVOs) {
            if (fieldVO.getId() == Integer.parseInt(fd.getId())) {
                fieldVO.setAddExp(exp);
                fieldVO.setMinNextRipeTime(minNestRipeTime);
                return fieldVO;
            }
        }
        return null;
    }

    public FieldAddVO getFieldAddVO(int id) {
        FieldAddVO fieldAddVO = new FieldAddVO();
        fieldAddVO.setId(id);
        return fieldAddVO;
    }

    public GoodsVO getGoodsVo(String itemId, int num) {
        BaseItemRule baseItemRule=baseItemRulecontainer.getElement(itemId);
        GoodsVO goodsVO = new GoodsVO();
        goodsVO.setMyNum(num);
        goodsVO.setItemId(itemId);
        goodsVO.setName(baseItemRule.getName());
        goodsVO.setIcon(baseItemRule.getIcon());
        return goodsVO;
    }

    public RewardVO getRewardVO(int type) {
        RewardVO rewardVO = new RewardVO(type);

        return rewardVO;
    }

    public FarmProcessItemVO [] getFramProcessItemVOS(String pid,int type){
        List<FarmProcessItemVO> farmProcessItemVOList=new ArrayList<FarmProcessItemVO>();
        FarmPackage farmPackage=farmPackageDao.getBean(pid);
        for(FarmProcessRule farmProcessRule:farmProcessRuleContainer.getByType(type)){
            farmProcessItemVOList.add(getOneFramProcessItemVO(farmProcessRule,farmPackage));
        }
        return farmProcessItemVOList.toArray(new FarmProcessItemVO[0]);
    }

    /**
     * 获取单个  FramProcessItemVO
     * @param farmProcessRule
     * @return
     */
    public FarmProcessItemVO getOneFramProcessItemVO(FarmProcessRule farmProcessRule,FarmPackage farmPackage){
        FarmProcessItemVO farmProcessItemVO=new FarmProcessItemVO();
        farmProcessItemVO.setProceId(farmProcessRule.getProceId());
        farmProcessItemVO.setType(farmProcessRule.getType());
        farmProcessItemVO.setTime(farmProcessRule.getTime());

        BaseItemRule goodsItems=baseItemRulecontainer.getElement(farmProcessRule.getItems());
        farmProcessItemVO.setItems(farmProcessRule.getItems());
        farmProcessItemVO.setItemsName(goodsItems.getName());

        List<GoodsVO> goodsVOList=new ArrayList<GoodsVO>();
        for(ItemData itemData:farmProcessRule.getItemDataList()){
           GoodsVO goodsVO=getGoodsVo(itemData.getItemId(),farmPackage.getSumByNum(itemData.getItemId()));
           goodsVO.setNeedNum(itemData.getNum());
           goodsVOList.add(goodsVO);
        }
        farmProcessItemVO.setMaterialGoodsVO(goodsVOList.toArray(new GoodsVO[0]));

        return  farmProcessItemVO;
    }

    public FarmQueueInfoVO getFarmQueueInfoVO(String pid,FarmProcessQueue farmProcessQueue){
        FarmQueueInfoVO farmQueueInfoVO=new FarmQueueInfoVO();
        farmQueueInfoVO.setQueueNum(farmProcessQueue.getNum()+FarmConstant.GRID_NUM);
        farmQueueInfoVO.setGold(50);
        if(farmProcessQueue.hasProceed()){
            farmQueueInfoVO.setAllTime(farmProcessQueue.getAllsurplusesTime());
        }else{
            farmQueueInfoVO.setAllTime(0);
        }

        List<FarmOneQueueVO> farmOneQueueVOList=new ArrayList<FarmOneQueueVO>();
        Date now=new Date();
        for(OneQueue oneQueue:farmProcessQueue.getOneQueueList()){
            FarmOneQueueVO farmOneQueueVO=new FarmOneQueueVO();
            farmOneQueueVO.setId(oneQueue.getId());
            farmOneQueueVO.setTime(oneQueue.getTime()+oneQueue.getAlreadyTime());
            farmOneQueueVO.setStatus(oneQueue.getStatus());
            FarmProcessRule farmProcessRule=farmProcessRuleContainer.getElement(oneQueue.getProcessid());
            farmOneQueueVO.setItmeId(farmProcessRule.getItems());
            farmOneQueueVO.setIcon(baseItemRulecontainer.getElement(farmProcessRule.getItems()).getIcon());
            if(oneQueue.getStatus()==2){
              int processTime= (int) ((now.getTime()-farmProcessQueue.getStartTime().getTime())/1000);
              farmOneQueueVO.setProceedTime(processTime+oneQueue.getAlreadyTime()+farmProcessQueue.alreadySpeedTime());
            }
            farmOneQueueVOList.add(farmOneQueueVO);
        }

        farmQueueInfoVO.setFarmOneQueueVOs(farmOneQueueVOList.toArray(new FarmOneQueueVO[0]));
        if(farmProcessQueue.getSpeedTime()!=null){
            if(farmProcessQueue.getSpeedTime().after(now)){
                farmQueueInfoVO.setSpeedTime(farmProcessQueue.getSpeedTime().getTime()-now.getTime());            }
        }
        return farmQueueInfoVO;
    }

    /**
     * 获取装饰品 对象VO
     * @param farmDecorate
     * @return
     */
    public FarmDecorateVO getFarDecorateVo(FarmDecorate farmDecorate,String[] hints){
        FarmDecorateVO farmDecorateVO=new FarmDecorateVO();
        farmDecorateVO.setPid(farmDecorate.getPid());
        farmDecorateVO.setFieldGold(FarmConstant.FIELD_GOLD_B);
        farmDecorateVO.setBuyFieldNum(farmDecorate.getBuyFieldNum());
        farmDecorateVO.setMaxBuyFieldNum(FarmConstant.FIELD_MAX_BUY);
        List<DecorateVO> decorateVOList=new ArrayList<DecorateVO>();
        for(OneDecorate decoratePosition:farmDecorate.getDecoratePositionList()){
            FarmDecorateRule farmDecorateRule=farmDerocateRuleContainer.getElement(decoratePosition.getiId());

            DecorateVO decorateVO=new DecorateVO();
            decorateVO.setItemId(decoratePosition.getiId());
            decorateVO.setType(farmDecorateRule.getDecoratetype());
            decorateVO.setImage(farmDecorateRule.getImage());
            decorateVO.setPosition(decoratePosition.getP());
            decorateVO.setId(decoratePosition.getId());
            decorateVOList.add(decorateVO);
        }
        farmDecorateVO.setDecorateVOs(decorateVOList.toArray(new DecorateVO[0]));
        farmDecorateVO.setHints(hints);
        return farmDecorateVO;
    }

    /**
     * 获取农场装饰品列表
     * @param listItem
     * @return
     */
    public FarmDecorateShopItemVO[] geFarmDecorateShopItemVOArray(List<FarmDecorateRule> listItem){
        List<FarmDecorateShopItemVO> list = new ArrayList<FarmDecorateShopItemVO>();
        if(listItem == null || listItem.isEmpty()){
            return list.toArray(new FarmDecorateShopItemVO[0]);
        }
        for(FarmDecorateRule baseItemRule : listItem){
            if(baseItemRule.getShopSell()==0){
                continue;
            }
            list.add(this.getFarmShopItemVO(baseItemRule));
        }
        return list.toArray(new FarmDecorateShopItemVO[0]);
    }

    /**
     * 获取单个 农场装饰品 信息VO
     * @param baseItemRule
     * @return
     */
    public FarmDecorateShopItemVO getFarmShopItemVO(FarmDecorateRule baseItemRule){
        FarmDecorateShopItemVO vo = new FarmDecorateShopItemVO();
        BeanUtils.copyProperties(baseItemRule, vo);
        /**
         * 传给客户端的类型 用     FarmDecorateRule里面的
         */
        vo.setItemtype(baseItemRule.getDecoratetype());
        return vo;
    }

    public FarmBoxVO getFarmBoxVo(PlayerFarmBox playerFarmBox){
        FarmBoxVO farmBoxVO=new FarmBoxVO();
        farmBoxVO.setGold(FarmConstant.OPEN_BOX);
        farmBoxVO.setStatus(playerFarmBox.getBoxstatu());
        return farmBoxVO;
    }
}
