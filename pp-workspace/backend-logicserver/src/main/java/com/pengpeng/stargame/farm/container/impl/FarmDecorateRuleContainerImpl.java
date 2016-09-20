package com.pengpeng.stargame.farm.container.impl;

import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.container.HashMapContainer;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.FarmBuilder;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.container.IFarmDecorateRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmDecorateDao;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.farm.rule.FarmDecorateRule;
import com.pengpeng.stargame.model.farm.FarmField;
import com.pengpeng.stargame.model.farm.FarmItem;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.farm.decorate.FarmDecorate;
import com.pengpeng.stargame.model.farm.decorate.FarmDecoratePkg;
import com.pengpeng.stargame.model.farm.decorate.OneDecorate;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.container.IPlayerRuleContainer;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.util.RandomUtil;
import com.pengpeng.stargame.util.Uid;
import com.pengpeng.stargame.vo.room.DecorateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * User: mql
 * Date: 14-3-14
 * Time: 下午3:29
 */
@Component
public class FarmDecorateRuleContainerImpl extends HashMapContainer<String, FarmDecorateRule> implements IFarmDecorateRuleContainer {

    @Autowired
    private IExceptionFactory exceptionFactory;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private FarmBuilder farmBuilder;
    @Autowired
    private IFarmPackageDao farmPackageDao;
    @Autowired
    private IFarmDecorateDao farmDecorateDao;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private IPlayerRuleContainer playerRuleContainer;
    @Override
    public void checkAndBuy(FarmPlayer farmPlayer, List<String> buyList, List<String> removeList, Player player, FarmDecorate farmDecorate) throws AlertException {


        /**
         * 购买判断
         */
        int gold = 0;
        int game = 0;
        for (String itemId : buyList) {
            BaseItemRule rule = baseItemRulecontainer.getElement(itemId);
            rule.checkBuy();
            if (itemId.equals(FarmConstant.FIELD_ID)) {

//                int num= (int) Math.pow(FarmConstant.FIELD_GOLD_B,farmDecorate.getBuyFieldNum());
                farmDecorate.setBuyFieldNum(farmDecorate.getBuyFieldNum() + 1);
                gold += rule.getGoldPrice() * farmDecorate.getBuyFieldNum();
            } else {
                gold += rule.getGoldPrice();
                game += rule.getGamePrice();
            }

        }
        if (farmDecorate.getBuyFieldNum() > FarmConstant.FIELD_MAX_BUY) {
            exceptionFactory.throwAlertException("最多可购买20块土地!");
        }
        /**
         * 清除
         */
        for (String itemId : removeList) {
            FarmDecorateRule rule = getElement(itemId);
            gold += rule.getGoldCost();
            game += rule.getGameCost();
        }
        //达人币或游戏币不足
        if(player.getGameCoin()<game){
            exceptionFactory.throwAlertException("gamecoin.notenough");
        }
        if (player.getGoldCoin() < gold) {
            exceptionFactory.throwAlertException("goldcoin.notenough");
        }

        /**
         * 扣钱
         */
//        player.decGameCoin(game);
//        player.decGoldCoin(gold);
        playerRuleContainer.decGameCoin(player,game);
        playerRuleContainer.decGoldCoin(player,gold, PlayerConstant.GOLD_ACTION_3);

    }

    /**
     * 回收 物品 到 背包
     *
     * @param farmDecorate
     * @param farmDecoratePkg
     * @param decorateVO
     */
    @Override
    public void recycleDecorate(FarmDecorate farmDecorate, FarmDecoratePkg farmDecoratePkg, DecorateVO decorateVO, FarmPlayer farmPlayer) {

        /**
         * 如果回收的是土地   删除一个田地
         */
        BaseItemRule baseItemRule = baseItemRulecontainer.getElement(decorateVO.getItemId());
        if(decorateVO.getItemId().equals(FarmConstant.FIELD_ID)){
           farmPlayer.deleteField(decorateVO.getId());
           farmDecoratePkg.addItem(decorateVO.getItemId(), 1, baseItemRule.getOverlay(), baseItemRule.getValidDete());
       }else{
            OneDecorate oneDecorate = farmDecorate.getOneDecorate(decorateVO.getId());
            farmDecorate.remove(decorateVO.getId());
            //添加物品时候 过期时间 使用 装饰品内的过期时间（物品过期 从购买那一刻开始算起）
            farmDecoratePkg.addItem(oneDecorate.getiId(), 1, baseItemRule.getOverlay(), oneDecorate.geteTime());


        }


    }

    @Override
    public void addDecorate(FarmDecorate farmDecorate, DecorateVO decorateVO, FarmPlayer farmPlayer,FarmDecoratePkg farmDecoratePkg) {

        BaseItemRule baseItemRule = baseItemRulecontainer.getElement(decorateVO.getItemId());
        FarmDecorateRule farmDecorateRule=getElement(decorateVO.getItemId());
        if (decorateVO.getItemId().equals(FarmConstant.FIELD_ID)) {//如果是 新加的田地
            FarmField farmField=farmBuilder.newFarmField(farmBuilder.getFileId(farmPlayer.getFields()));
            farmField.setPosition(decorateVO.getPosition());
            farmPlayer.addField(farmField);

            //扣除背包里面的东西
            if(decorateVO.getAddType()==2){
               farmDecoratePkg.deduct(decorateVO.getItemId(),1);
            }
//            OneDecorate decoratePosition = new OneDecorate(id, decorateVO.getItemId(), decorateVO.getPosition(), baseItemRule.getValidDete());
//            farmDecorate.add(decoratePosition);
        } else {
            if(baseItemRule.getValidDete()!=null){//有时效性的物品
                String id = Uid.uuid();

                OneDecorate decoratePosition=null;
                if (farmDecorateRule.getDecoratetype()==5){
                    decoratePosition = new OneDecorate(id, decorateVO.getItemId(), decorateVO.getPosition(), baseItemRule.getValidDete());
                }
                if (farmDecorateRule.getDecoratetype()==6){// 如果是小动物，添加小动物的作用时间
                    decoratePosition = new OneDecorate(id, decorateVO.getItemId(), decorateVO.getPosition(), baseItemRule.getValidDete(),new Date());
                }


                if(decorateVO.getAddType()==2){ //如果是从 背包里面拖进去的，过期时间 要用背包里面的过期时间
                    FarmItem farmItem=farmDecoratePkg.getFarmItem(decorateVO.getItemGid());
                    decoratePosition.seteTime(farmItem.getValidDete());
                    farmDecoratePkg.clearItemByKey(decorateVO.getId());
                }
                farmDecorate.add(decoratePosition);


            }else {
                String id = Uid.uuid();
                OneDecorate decoratePosition=null;
                if (farmDecorateRule.getDecoratetype()==6){// 如果是小动物，添加小动物的作用时间
                    decoratePosition = new OneDecorate(id, decorateVO.getItemId(), decorateVO.getPosition(), baseItemRule.getValidDete(),new Date());
                } else { //普通装饰品5  花 7
                    decoratePosition = new OneDecorate(id, decorateVO.getItemId(), decorateVO.getPosition(), baseItemRule.getValidDete());
                }
                farmDecorate.add(decoratePosition);
                farmDecoratePkg.deduct(decorateVO.getItemId(),1);
            }

        }

    }

    @Override
    public void updateDecorate(FarmDecorate farmDecorate, DecorateVO decorateVO, FarmPlayer farmPlayer) {
        if (farmDecorate.getItems().containsKey(decorateVO.getId())) {
            OneDecorate decoratePosition = farmDecorate.getItems().get(decorateVO.getId());
            decoratePosition.setP(decorateVO.getPosition());
            farmDecorate.updateDecorate(decorateVO.getId(), decoratePosition);
        }
        if (decorateVO.getItemId().equals(FarmConstant.FIELD_ID)) {//如果 修改的是田地
           FarmField farmField=farmPlayer.getOneFarmField(decorateVO.getId());
           farmField.setPosition(decorateVO.getPosition());
        }
    }

    /**
     * 每次请求 刷新小动物
     * @param farmDecorate
     * @return
     */
    @Override
    public boolean refreshAnimal(FarmDecorate farmDecorate) {
        boolean change = false;
        Date now = new Date();
        /**
         *  鼹鼠（地鼠）
         *  每隔1小时有30%几率出现1个，农场中最多出现5个
         */
        String itemId=FarmConstant.Y_S;
        FarmDecorateRule farmDecorateRule = get(itemId);
        if (farmDecorateRule.getMaxNum() != 0) {
            if (farmDecorate.getNumByItemId(itemId) < farmDecorateRule.getMaxNum()) { //如果数量达到最大不刷新
                Date lastTime = farmDecorate.getRefreshTime().get(itemId);
                //如果 长时间没登录 需要多刷几次
                int refreshNum=0;
                if(lastTime==null){
                    refreshNum=1;
                }
                if(lastTime!=null){
                    refreshNum = (int) ((now.getTime() - lastTime.getTime()) / (3600 * 1000));
                }
                //刷出 动物的数量
                int animalNum=0;
                for(int i=0;i<refreshNum;i++){
                    int rNum= RandomUtil.range(0,100);
                    if(rNum<25){
                        animalNum++ ;
                    }
                    //如果 数量超过 上限，不在添加
                    if(animalNum+farmDecorate.getNumByItemId(itemId)>=farmDecorateRule.getMaxNum()){
                        break;
                    }
                }

                //添加 小动物
                for(int i=0;i<animalNum;i++){
                    String id=Uid.uuid();
                    OneDecorate oneDecorate=new OneDecorate(id,itemId,"",null,now);
                   farmDecorate.add(oneDecorate);
                }
                //修改 刷新时间
                if(refreshNum>0){
                    farmDecorate.updateRefreshTime(itemId,now);
                    change=true;
                }
            }
        }
        /**
         * 每个地鼠每隔30分钟有50%几率偷吃仓库的一个农作物/种子（不包括农场材料）
         */
        List<OneDecorate> ysList=farmDecorate.getList(itemId);
        if(ysList.size()>0){
            int reduceNum=0;// 吃多少个农作物
            for(OneDecorate oneDecorate:ysList){
                int num= (int) ((now.getTime()-oneDecorate.getEffectTime().getTime())/(3600 * 1000)); //60分钟
//                int num= (int) ((now.getTime()-oneDecorate.getEffectTime().getTime())/(60 * 1000));//1分钟
                for(int i=0;i<num;i++){
                    int rNum = RandomUtil.range(0, 100);
                    if (rNum < 30) {
                        reduceNum++;
                    }
                }
                if(num>0){
                    oneDecorate.setEffectTime(now);
                    farmDecorate.updateDecorate(oneDecorate.getId(),oneDecorate);
                }
            }
            //长时间未上线 登录 ，最多偷吃50个
            if(reduceNum>50){
                reduceNum=50;
            }
            if(reduceNum>0){
                //扣除作物 或 种子
                FarmPackage farmPackage=farmPackageDao.getBean(farmDecorate.getPid());

                List<String> itemIds=new ArrayList<String>();
               for(FarmItem farmItem:farmPackage.getItemAll()){
                   BaseItemRule baseItemRule=baseItemRulecontainer.getElement(farmItem.getItemId());
                   if(baseItemRule.getItemtype()==1||baseItemRule.getItemtype()==2){
                       for(int i=0;i<farmItem.getNum();i++){
                           itemIds.add(baseItemRule.getItemsId());
                       }
                   }
               }
                /**
                 * 随机 扣除
                 */
                List<String> deleteIds=new ArrayList<String>();//吃掉的 作物或者种子
                if(itemIds.size()<reduceNum){
                    deleteIds.addAll(itemIds);
                }else {
                    while(true){
                        int rIndex=RandomUtil.range(0,itemIds.size());
                        deleteIds.add(itemIds.get(rIndex));
                        if(deleteIds.size()>=reduceNum){
                            break;
                        }
                    }
                }
                for(String dId:deleteIds){
                    baseItemRulecontainer.useGoods(farmDecorate.getPid(),dId,1);
                }

                if(deleteIds.size()>0){
                    farmDecorate.incrAnimalEffectNum(itemId, deleteIds.size());
                }
                change=true;
            }

        }
        /**
         * //3）刷新 瓢虫
         * 如果有小鸟 ，那么 不刷新
         */
        FarmPlayer farmPlayer=farmPlayerDao.getFarmPlayer(farmDecorate.getPid(),System.currentTimeMillis());
        if(farmPlayer.getLevel()<25){  //25级之前 如果有瓢虫，删掉
            int pNum=farmDecorate.getNumByItemId(FarmConstant.P_C);
            if(pNum>0){
                farmDecorate.deleteByItemId(FarmConstant.P_C,pNum);
            }
        }
        if(farmPlayer.getLevel()>25){ //25级别之后 才开始刷新 瓢虫
            itemId=FarmConstant.P_C;
            farmDecorateRule = get(itemId);
            if (farmDecorateRule.getMaxNum() != 0) {
                if(farmDecorate.getNumByItemId(FarmConstant.X_N)>0){
                    //如果有小鸟那么删除所有的瓢虫
                    int pNum=farmDecorate.getNumByItemId(FarmConstant.P_C);
                    if(pNum>0){
                        farmDecorate.deleteByItemId(FarmConstant.P_C,pNum);
                    }
                    farmDecorate.updateRefreshTime(itemId,now);
                }else{
                    if (farmDecorate.getNumByItemId(itemId) < farmDecorateRule.getMaxNum()) {
                        Date lastTime = farmDecorate.getRefreshTime().get(itemId);
                        //如果 长时间没登录 需要多刷几次
                        int refreshNum=0;
                        if(lastTime==null){
                            refreshNum=1;
                        }
                        if(lastTime!=null){
                            refreshNum = (int) ((now.getTime() - lastTime.getTime()) / (3600 * 1000));//一个小时出现一只
                        }
                        //刷出 动物的数量
                        int animalNum=0;
                        for(int i=0;i<refreshNum;i++){
                            int rNum= RandomUtil.range(0,100);
                            if(rNum<50){
                                animalNum++ ;
                            }
                            //如果 数量超过 上限，不在添加
                            if(animalNum+farmDecorate.getNumByItemId(itemId)>=farmDecorateRule.getMaxNum()){
                                break;
                            }
                        }

                        //添加 小动物
                        for(int i=0;i<animalNum;i++){
                            String id=Uid.uuid();
                            OneDecorate oneDecorate=new OneDecorate(id,itemId,"",null,now);
                            farmDecorate.add(oneDecorate);
                        }
                        // 如果 时间段内 有刷新 ，修改 刷新时间 为当前时间
                        if(refreshNum>0){
                            farmDecorate.updateRefreshTime(itemId,now);
                            change=true;
                        }
                    }
                }

            }
        }


        /**
         * 母鸡+小鸡  每半小时减少一个瓢虫
         */
        itemId=FarmConstant.X_J;
        List<OneDecorate> oneDecorateList=farmDecorate.getList(itemId);
        if(oneDecorateList.size()>0){
            int reduceNum=0;//减少了 多少个 瓢虫
            for(OneDecorate oneDecorate:oneDecorateList){
                int num= (int) ((now.getTime()-oneDecorate.getEffectTime().getTime())/(1800 * 1000));
                reduceNum+=num;
                if(num>0){
                    oneDecorate.setEffectTime(now);
                    farmDecorate.updateDecorate(oneDecorate.getId(),oneDecorate);
                }
            }
            if(reduceNum>0){
                int pcNum=farmDecorate.getNumByItemId(FarmConstant.P_C);
                if(pcNum<reduceNum){ //没有那么多瓢虫
                   reduceNum=pcNum;
                }
                if(reduceNum>0){
                   farmDecorate.incrAnimalEffectNum(itemId,reduceNum);
                }
                farmDecorate.deleteByItemId(FarmConstant.P_C,reduceNum);
                change=true;
            }

        }

        return change;
    }

    @Override
    public void animalOperation(FarmDecorate farmDecorate, String id) throws AlertException {
        OneDecorate oneDecorate=farmDecorate.getItems().get(id);
        if(oneDecorate==null){
            exceptionFactory.throwAlertException("不存在此物件！");
        }
        farmDecorate.remove(id);
    }

    @Override
    public List<String> getHints(FarmDecorate farmDecorate) {
        List<String> list=new ArrayList<String>();
        for(String itemId:farmDecorate.getAnimalEffect().keySet()){
            if(itemId.equals(FarmConstant.X_J)){
                list.add("母鸡在您农场吃掉了"+farmDecorate.getAnimalEffect().get(itemId)+"只瓢虫!");
            }
            if(itemId.equals(FarmConstant.Y_S)){
                list.add("有地鼠闯进你的农场，偷吃了仓库中的"+farmDecorate.getAnimalEffect().get(itemId)+"个农作物");
            }
        }
        farmDecorate.setAnimalEffect(new HashMap<String, Integer>());
        return list;
    }

    @Override
    public int getCropReduce(String pid) {
        /**
         * 作用：每次收获作物时有（15*数量%）机率少获得一个该作物
         每次收获时，如何减少产出，则提示信息：很遗憾，这次虫害使您收获的农作物减少1个
         */
        int reduceNum=0;
        FarmDecorate farmDecorate=farmDecorateDao.getFarmDecorate(pid);
        int num=farmDecorate.getNumByItemId(FarmConstant.P_C);
        int rNum=RandomUtil.range(0,100);
        if(15*num>rNum){
           reduceNum=1;
        }
        return reduceNum;
    }

    @Override
    public int getCropAdd(String pid) {

        /*8
        蜜蜂不可操作，不可点击
        作用：增加农场产量，每次最后一次收获时有（25%*数量）的几率额外一个作物
         */

        int addNum=0;
        FarmDecorate farmDecorate=farmDecorateDao.getFarmDecorate(pid);
        int num=farmDecorate.getNumByItemId(FarmConstant.M_F);
        int rNum=RandomUtil.range(0,100);
        if(25*num>rNum){
            addNum=1;
        }
        return addNum;

    }

    @Override
    public List<FarmDecorateRule> getInitList() {
        List<FarmDecorateRule> list=new ArrayList<FarmDecorateRule>();
        for(FarmDecorateRule farmDecorateRule:this.values()){
            if(farmDecorateRule.getDecoratetype()!=6&&!farmDecorateRule.getItemsId().equals(FarmConstant.FIELD_ID)&&!farmDecorateRule.getPosition().equals("0")){
                list.add(farmDecorateRule);
            }
        }
        return list;
    }

    @Override
    public String getFieldPosition(int  fieldId) {
        FarmDecorateRule farmDecorateRule=get(FarmConstant.FIELD_ID);
        String []ss=farmDecorateRule.getPosition().split(";");
//        System.out.println(ss[fieldId-1]);
        if(fieldId>ss.length){
            return "";
        }
        return ss[fieldId-1];
    }


}
