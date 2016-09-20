package com.pengpeng.stargame.piazza;

import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyBuildInfo;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.piazza.MoneyTree;
import com.pengpeng.stargame.piazza.container.FamilyConstant;
import com.pengpeng.stargame.piazza.container.IMoneyTreeRuleContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * User: mql
 * Date: 13-6-26
 * Time: 下午6:00
 */
@Component()
public class PiazzaBuilder {
    @Autowired
    private IMoneyTreeRuleContainer moneyTreeRuleContainer;

    public  Family newFamily(String fmilyId){
        Family family=new Family();
        family.setId(fmilyId);
        return family;
    }

    public  FamilyBuildInfo newFamilyBuildInfo(String familyId){
        FamilyBuildInfo familyBuildInfo=new FamilyBuildInfo(familyId);
        familyBuildInfo.setBuilds(FamilyConstant.BUILD_BANK,1);
        familyBuildInfo.setBuilds(FamilyConstant.BUILD_CASTLE,1);
        familyBuildInfo.setBuilds(FamilyConstant.BUILD_STORE,1);
        familyBuildInfo.setBuilds(FamilyConstant.BUILD_TREE,1);
        familyBuildInfo.setBuilds(FamilyConstant.BUILD_WAREHOUSE,1);
        return familyBuildInfo;
    }

    public FamilyMemberInfo newFamilyMemberInfo(String fid,String pid){
        FamilyMemberInfo familyMemberInfo =new FamilyMemberInfo();
        familyMemberInfo.setFamilyId(fid);
        familyMemberInfo.setPid(pid);
        return familyMemberInfo;
    }

    public FamilyMemberInfo newFamilyMemberInfo(String fid,String pid,int identity){
        FamilyMemberInfo familyMemberInfo =new FamilyMemberInfo();
        familyMemberInfo.setFamilyId(fid);
        familyMemberInfo.setPid(pid);
        familyMemberInfo.setIdentity(identity);
        return familyMemberInfo;
    }

    public  MoneyTree newMoneyTree(String familyId){
        MoneyTree  moneyTree=new MoneyTree(familyId);
        Date ripeTime=moneyTreeRuleContainer.getRipeTime(familyId,null);
        moneyTree.setRipeTime(ripeTime);
        moneyTree.setRipeEndTime(moneyTreeRuleContainer.getRipeEndTime(ripeTime));
        return moneyTree;
    }
}
