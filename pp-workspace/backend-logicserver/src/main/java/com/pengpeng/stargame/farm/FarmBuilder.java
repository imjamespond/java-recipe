package com.pengpeng.stargame.farm;


import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.farm.container.IFarmDecorateRuleContainer;
import com.pengpeng.stargame.farm.container.IFarmWareHouseContainer;
import com.pengpeng.stargame.farm.rule.FarmDecorateRule;
import com.pengpeng.stargame.farm.rule.FarmWareHouseRule;
import com.pengpeng.stargame.model.farm.*;
import com.pengpeng.stargame.model.farm.decorate.FarmDecorate;
import com.pengpeng.stargame.model.farm.decorate.OneDecorate;
import com.pengpeng.stargame.model.farm.process.FarmProcessQueue;
import com.pengpeng.stargame.model.room.DecoratePosition;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.util.Uid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;


/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-4-27上午11:00
 */
@Component()
public class FarmBuilder {

	@Autowired
	private IFarmWareHouseContainer farmWareHouseContainer;
    public static Map<String,String> FIELD_P=new HashMap<String, String>();
    @Autowired
    private IFarmDecorateRuleContainer farmDecorateRuleContainer;

    static {
        FIELD_P.put("1","59,60");
        FIELD_P.put("2","61,60");
        FIELD_P.put("3","63,60");
        FIELD_P.put("4","65,60");
        FIELD_P.put("5","59,62");
        FIELD_P.put("6","61,62");
        FIELD_P.put("7","63,62");
        FIELD_P.put("8","65,62");
        FIELD_P.put("9","59,64");
        FIELD_P.put("10","61,64");
        FIELD_P.put("11","63,64");
        FIELD_P.put("12","65,64");
        FIELD_P.put("13","59,66");
        FIELD_P.put("14","61,66");
        FIELD_P.put("15","63,66");
        FIELD_P.put("16","65,66");
        FIELD_P.put("17","59,68");
        FIELD_P.put("18","61,68");
        FIELD_P.put("19","63,68");
        FIELD_P.put("20","65,68");
        FIELD_P.put("21","59,70");
        FIELD_P.put("22","61,70");
        FIELD_P.put("23","63,70");
        FIELD_P.put("24","65,70");

    }

    /**
     * 初始化  农场装饰
     */
    public FarmDecorate newFarmDecorate(String pid){

        FarmDecorate farmDecorate=new FarmDecorate(pid);
        farmDecorate.setIdCounter(10000);
        /**
         * 初始化 普通装饰品
         */
        for(FarmDecorateRule farmDecorateRule:farmDecorateRuleContainer.getInitList()){
            if(farmDecorateRule.getPosition()!=null){
                String [] ps=farmDecorateRule.getPosition().split(";");
                for(String p:ps){
                    String id = Uid.uuid();
                    farmDecorate.add(new OneDecorate(id, farmDecorateRule.getItemsId(),p,null));
                }
            }
        }
        /**
         * 初始化土地  位置
         */
//        for (String key : FIELD_P.keySet()) {
//            farmDecorate.add(new OneDecorate(key, FarmConstant.FIELD_ID, FIELD_P.get(key),null));
//        }
        return farmDecorate;
    }

    /**
     * 初始化  农场信息
     * @param pId
     * @return
     */
    public FarmPlayer newFarm(String pId){
        FarmPlayer fp=new FarmPlayer(pId,1,0);
        List<FarmField> farmFields=new ArrayList<FarmField>();
        for (int i=0;i<6;i++){
            fp.addField(newFarmField(String.valueOf(i+1)));
        }
        return fp;

    }

    /**
     * 通过已有的田地id 返回一个新的田地id ,保证田地Id的唯一性
     * @param farmFields
     * @return
     */
    public String getFileId(List<FarmField> farmFields){
        //获取玩家 土地Id中 最大的一个 Id
        int maxId=0;
        for(FarmField farmField:farmFields){
            int id=Integer.parseInt(farmField.getId());
           if(id>maxId){
               maxId =id;
           }
        }
        List<String> allIds=new ArrayList<String>();
        List<String> hasIds=new ArrayList<String>();
        for(int i=0;i<maxId+1;i++){
            allIds.add(String.valueOf(i+1));
        }
        for(FarmField farmField:farmFields){
            hasIds.add(farmField.getId());
        }
        allIds.removeAll(hasIds);
        return allIds.get(0);
    }

    /**
     * 初始化 一个田地 信息
     * @param fieldId
     * @return
     */
    public FarmField newFarmField(String fieldId){
        FarmField ff=new FarmField();
        ff.setId(fieldId) ;
        //ff.setHarvestIds(new ArrayList());//在构造方法中产生ArrayList
        ff.setStatus(FarmConstant.FIELD_STATUS_FREE);

        return ff;
    }
    /**
     * 初始化农场 帮好友 收获统计信息
     */
    public FarmFriendHarvest newFarmFriendHarvest(String pId){
        FarmFriendHarvest farmFriendHarvest=new FarmFriendHarvest();
        farmFriendHarvest.setpId(pId);
        farmFriendHarvest.setNum(0);
        farmFriendHarvest.setFriendNum(0);
        farmFriendHarvest.setNextTime(new Date());
        return farmFriendHarvest;
    }

    /**
     * 初始化 一个 农场评价 信息
     * @param pId
     * @return
     */
    public FarmEvaluate newFarmEvaluate(String pId){
        FarmEvaluate farmEvaluate=new FarmEvaluate(pId);
        farmEvaluate.setNextTime(DateUtil.getNextCountTime());
        return  farmEvaluate;
    }

    /**
     * 初始化  一个 玩家 订单 信息 实体
     * @param pId
     * @return
     */
    public FarmOrder newFarmOrder(String pId){
        return  new FarmOrder(pId,new Date(),null) ;
    }

	/**
	 * 初始化 玩家仓库 1级
	 * @param pid
	 * @return FarmPackage
	 */
	public FarmPackage newFarmPackage(final String pid){
		FarmWareHouseRule farmWareHouseRule = farmWareHouseContainer.getElement(1);
		FarmPackage vo = new FarmPackage();
		vo.setLevel(farmWareHouseRule.getId());
		vo.setSize(farmWareHouseRule.getCapacity());
		vo.setPid(pid);
		return vo;
	}

    /**
     * 初始化一个 玩家 动态信息 对象
     * @param pid
     * @return
     */
    public FarmActionInfo newFarmActionInfo(String pid){
        FarmActionInfo farmActionInfo=new FarmActionInfo(pid);
        farmActionInfo.setNextTime(DateUtil.getNextCountTime());
        return farmActionInfo;
    }

    public FarmMessageInfo newFarmMessageInfo(String pid){
        FarmMessageInfo farmMessageInfo=new FarmMessageInfo(pid);
        return farmMessageInfo;
    }


    public FarmProcessQueue newFarProcessQueue(String pid){
        FarmProcessQueue farmProcessQueue=new FarmProcessQueue(pid,0);
        return farmProcessQueue;
    }


}
