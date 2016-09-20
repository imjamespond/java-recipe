package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class FamilyBankRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*家族银行面板信息
*/
public com.pengpeng.stargame.vo.piazza.bank.FamilyBankVO rankInfo (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.bank.FamilyBankReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.bank.FamilyBankVO  rValue=  proxy.execute("family.bank.info",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.bank.FamilyBankVO.class);
      return rValue;
}
/**
*取款
*/
public com.pengpeng.stargame.vo.piazza.bank.FamilyBankVO rankGet (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.bank.FamilyBankReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.bank.FamilyBankVO  rValue=  proxy.execute("family.bank.get",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.bank.FamilyBankVO.class);
      return rValue;
}
/**
*存款
*/
public com.pengpeng.stargame.vo.piazza.bank.FamilyBankVO rankSave (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.bank.FamilyBankReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.bank.FamilyBankVO  rValue=  proxy.execute("family.bank.save",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.bank.FamilyBankVO.class);
      return rValue;
}
/**
*获取玩家存款数量
*/
public int getPlayerMoney (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.bank.FamilyBankReq req ) throws GameException{
      int  rValue=  proxy.execute("family.bank.playerMoney",session, proxy.gson.toJson(req),int.class);
      return rValue;
}

}