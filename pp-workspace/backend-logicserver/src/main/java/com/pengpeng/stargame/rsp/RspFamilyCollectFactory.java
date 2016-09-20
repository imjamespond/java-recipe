package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.model.piazza.collectcrop.CollectControl;
import com.pengpeng.stargame.model.piazza.collectcrop.FamilyCollect;
import com.pengpeng.stargame.piazza.container.IFamilyCollectRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyCollectControlDao;
import com.pengpeng.stargame.piazza.rule.FamilyCollectRule;
import com.pengpeng.stargame.vo.piazza.collectcrop.FamilyCollectInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * User: mql
 * Date: 14-3-26
 * Time: 上午11:17
 */
@Component
public class RspFamilyCollectFactory extends  RspFactory{
    @Autowired
   private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
   private IFamilyCollectControlDao familyCollectControlDao;
    @Autowired
   private IFamilyCollectRuleContainer familyCollectRuleContainer;
   public FamilyCollectInfoVO getFamilyCollectInfoVO(FamilyCollect familyCollect){
        FamilyCollectInfoVO familyCollectInfoVO=new FamilyCollectInfoVO();
        FamilyCollectRule familyCollectRule=familyCollectRuleContainer.getElement(familyCollect.getRuleId());
        if(familyCollectRule!=null){
            BaseItemRule baseItemRule=baseItemRulecontainer.getElement(familyCollectRule.getItemId());
            familyCollectInfoVO.setNum(familyCollect.getNum());
            familyCollectInfoVO.setDes(familyCollectRule.getActivetyDesc());
            familyCollectInfoVO.setItemId(familyCollectRule.getItemId());
            familyCollectInfoVO.setNeedNum(familyCollectRule.getItemNum());
            familyCollectInfoVO.setRewardDes(familyCollectRule.getRewardDesc());
        }
        CollectControl collectControl=familyCollectControlDao.getCollectControl();
        Date now=new Date();
        familyCollectInfoVO.setResidueTime(collectControl.getNextTime().getTime()-now.getTime());

        return familyCollectInfoVO;
    }
}
