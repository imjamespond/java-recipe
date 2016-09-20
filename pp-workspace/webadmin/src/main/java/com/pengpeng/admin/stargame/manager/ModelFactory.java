package com.pengpeng.admin.stargame.manager;

import com.pengpeng.admin.stargame.model.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-11-12下午12:06
 */
@Component
public class ModelFactory {
    private Map<Integer,Object> map = new HashMap<Integer,Object>();

    public PlayerFamilyActionModel newPlayerFamilyActionModel(PlayerActionModel pa,String[] strs){
        PlayerFamilyActionModel model = new PlayerFamilyActionModel(pa.getUid(),pa.getPid(),pa.getDate());
        model.setPreFamily(strs[4]);
        model.setCurFamily(strs[5]);
        return model;
    }

    public PlayerFamilyAwardActionModel newPlayerFamilyAwardActionModel(PlayerActionModel pa,String[] strs){
        PlayerFamilyAwardActionModel model =  new PlayerFamilyAwardActionModel(pa.getUid(),pa.getPid(),pa.getDate());
        model.setGameCoin(Integer.valueOf(strs[4]));
        return model;
    }

    public PlayerDonationActionModel newPlayerDonationActionModel(PlayerActionModel pa,String[] strs){
        PlayerDonationActionModel model =  new PlayerDonationActionModel(pa.getUid(),pa.getPid(),pa.getDate());
        model.setGoldCoin(Integer.valueOf(strs[4]));
        model.setGameCoin(Integer.valueOf(strs[5]));
        return model;
    }

    public PlayerGivingActionModel newPlayerGivingActionModel(PlayerActionModel pa,String[] strs){
        PlayerGivingActionModel model =  new PlayerGivingActionModel(pa.getUid(),pa.getPid(),pa.getDate());
        model.setItemId(strs[4]);
        return model;
    }

    public PlayerMoneyTreeActionModel newPlayerMoneyTreeActionModel(PlayerActionModel pa,String[] strs){
        PlayerMoneyTreeActionModel model =  new PlayerMoneyTreeActionModel(pa.getUid(),pa.getPid(),pa.getDate());
        return model;
    }

    public PlayerDecorationActionModel newPlayerDecorationActionModel(PlayerActionModel pa,String[] strs){
        PlayerDecorationActionModel model =  new PlayerDecorationActionModel(pa.getUid(),pa.getPid(),pa.getDate());
        return model;
    }

    public PlayerSaleActionModel newPlayerSaleActionModel(PlayerActionModel pa,String[] strs){
        PlayerSaleActionModel model = new PlayerSaleActionModel(pa.getUid(),pa.getPid(),pa.getDate());
        model.setItemId(strs[4]);
        model.setNum(Integer.valueOf(strs[5]));
        model.setType(Integer.valueOf(strs[6]));
        model.setItemType(Integer.valueOf(strs[7]));
        return model;
    }

    public PlayerBuyActionModel newPlayerBuyActionModel(PlayerActionModel pa,String[] strs){
        PlayerBuyActionModel model = new PlayerBuyActionModel(pa.getUid(),pa.getPid(),pa.getDate());
        model.setItemId(strs[4]);
        model.setNum(Integer.valueOf(strs[5]));
        model.setType(Integer.parseInt(strs[6]));
        model.setItemType(Integer.parseInt(strs[7]));
        return model;
    }

    public PlayerOrderResetActionModel newPlayerOrderResetActionModel(PlayerActionModel pa,String[] strs){
        PlayerOrderResetActionModel model = new PlayerOrderResetActionModel(pa.getUid(),pa.getPid(),pa.getDate());
        model.setCost(Integer.valueOf(strs[4]));
        return model;
    }

    public PlayerFarmComplimentActionModel newPlayerFarmComplimentActionModel(PlayerActionModel pa,String[] strs){
        PlayerFarmComplimentActionModel model = new PlayerFarmComplimentActionModel(pa.getUid(),pa.getPid(),pa.getDate());
        model.setFriendId(strs[4]);
        return model;
    }

    public PlayerHarvestActionModel newPlayerHarvestActionModel11(PlayerActionModel pa,String[] strs){
        PlayerHarvestActionModel model = new PlayerHarvestActionModel(pa.getUid(),pa.getPid(),pa.getDate());
        model.setType(pa.getType());
        model.setFieldId(strs[4]);
        model.setRipenNum(Integer.valueOf(strs[5]));
        model.setPlantId(strs[6]);
        if(pa.getType() == 11)
            model.setFriendId(strs[7]);
        return model;
    }
    public PlayerHarvestActionModel newPlayerHarvestActionModel26(PlayerActionModel pa,String[] strs){
        PlayerHarvestActionModel model = new PlayerHarvestActionModel(pa.getUid(),pa.getPid(),pa.getDate());
        model.setType(pa.getType());
        model.setFieldId(strs[4]);
        return model;
    }

    public PlayerHarvestActionModel newPlayerHarvestActionModel(PlayerActionModel pa,String[] strs){
        PlayerHarvestActionModel model = new PlayerHarvestActionModel(pa.getUid(),pa.getPid(),pa.getDate());
        model.setType(pa.getType());
        model.setFieldId(strs[5]);
        model.setRipenNum(0);
        model.setPlantId(strs[4]);
        model.setFriendId("");
        return model;
    }

    public PlayerActivityActionModel newPlayerActivityActionModel(PlayerActionModel pa,String[] strs){
        PlayerActivityActionModel pActivity = new PlayerActivityActionModel(pa.getUid(),pa.getPid(),pa.getDate());
        pActivity.setPoint(Integer.valueOf(strs[4]));
        return pActivity;
    }

    public PlayerPioneerActionModel newPlayerPioneerActionModel(PlayerActionModel pa,String[] strs){
        PlayerPioneerActionModel model = new PlayerPioneerActionModel(pa.getUid(),pa.getPid(),pa.getDate());
        return model;
    }

    public PlayerLotteryActionModel newPlayerLotteryActionModel(PlayerActionModel pa,String[] strs){
        PlayerLotteryActionModel model = new PlayerLotteryActionModel(pa.getUid(),pa.getPid(),pa.getDate());
        if (!"0".equalsIgnoreCase(strs[4])){
            model.setItemId(strs[4]);
        }
        model.setNum(Integer.parseInt(strs[5]));
        model.setGameCoin(Integer.parseInt(strs[6]));
        return model;
    }

    public PlayerTaskActionModel newPlayerTaskActionModel(PlayerActionModel pa,String[] strs){
        PlayerTaskActionModel model =  new PlayerTaskActionModel(pa.getUid(),pa.getPid(),pa.getDate());
        model.setTaskid(strs[4]);
        model.setType(Integer.parseInt(strs[5]));
        model.setSubType(Integer.parseInt(strs[6]));
        return model;
    }
    public PlayerMapActionModel newPlayerMapActionModel(PlayerActionModel pa,String[] strs){
        PlayerMapActionModel model =  new PlayerMapActionModel(pa.getUid(),pa.getPid(),pa.getDate());
        model.setMapId(strs[4]);
        return model;
    }

    public PlayerRechargeActionModel newPlayerRechargeActionModel(PlayerActionModel pa,String[] strs){
        PlayerRechargeActionModel model = new PlayerRechargeActionModel(pa.getUid(),pa.getPid(),pa.getDate());
        model.setMoney(Integer.valueOf(strs[4]));
        return model;
    }
    public PlayerLoginActionModel newPlayerLoginActionModel(PlayerActionModel pa,String[] strs){
        PlayerLoginActionModel model = new PlayerLoginActionModel(pa.getUid(),pa.getPid(),pa.getDate());
        model.setType(1);
        return model;
    }
    public PlayerLoginActionModel newPlayerLogoutActionModel(PlayerActionModel pa,String[] strs){
        PlayerLoginActionModel model = new PlayerLoginActionModel(pa.getUid(),pa.getPid(),pa.getDate());
        model.setType(2);
        return model;
    }


    public PlayerRegisterModel newPlayerRegisterModel(PlayerActionModel pa,String[] strs){
        PlayerRegisterModel model = new PlayerRegisterModel(pa.getUid(),pa.getPid(),pa.getDate());
        //model.setType(Integer.parseInt(strs[4]));//似乎没用
        return model;
    }

    public PlayerItemModel newPlayerItemModel(PlayerActionModel pa, String[] strs) {
        PlayerItemModel model = new PlayerItemModel(pa.getUid(),pa.getPid(),pa.getDate());
        model.setAction(pa.getType());
        model.setItemId(strs[4]);
//        if (strs.length>6){
            model.setNum(Integer.parseInt(strs[5]));
            model.setType(Integer.parseInt(strs[6]));
            model.setItemType(Integer.parseInt(strs[7]));
//        }else{
//            BaseItemRule rule = baseItemRulecontainer.getElement(strs[4]);
//            model.setNum(Integer.parseInt(strs[5]));
//            model.setType(rule.getType());
//            model.setItemType(rule.getItemtype());
//        }
        return model;
    }

    public UseGoldModel newUseGoldModel(PlayerActionModel pa, String[] strs) {
        UseGoldModel model = new UseGoldModel(pa.getUid(),pa.getPid(),pa.getDate());
        model.setType(pa.getType());
        model.setNum(Integer.parseInt(strs[4]));
        return model;
    }
}
