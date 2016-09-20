package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class MoneyTreeRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*获取摇钱树信息
*/
public com.pengpeng.stargame.vo.piazza.MoneyTreeVO getinfo (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.MoneyTreeReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.MoneyTreeVO  rValue=  proxy.execute("moneytree.getinfo",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.MoneyTreeVO.class);
      return rValue;
}
/**
*捡取地面上的钱
*/
public com.pengpeng.stargame.vo.piazza.MoneyPickInfoVO pickMoney (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.MoneyTreeReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.MoneyPickInfoVO  rValue=  proxy.execute("moneytree.pickMoney",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.MoneyPickInfoVO.class);
      return rValue;
}
/**
*祝福
*/
public void blessing (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.MoneyTreeReq req ) throws GameException{
     proxy.execute("moneytree.blessing",session, proxy.gson.toJson(req),void.class);
    
}
/**
*摇钱
*/
public void rock (com.pengpeng.stargame.rpc.Session sessions1, com.pengpeng.stargame.vo.piazza.MoneyTreeReq session ) throws GameException{
     proxy.execute("moneytree.rock",sessions1, proxy.gson.toJson(session),void.class);
    
}
/**
*获取所有家族Id列表
*/
public String[] getFamilyIds (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.MoneyTreeReq req ) throws GameException{
      String[]  rValue=  proxy.execute("moneytree.getFamilyIds",session, proxy.gson.toJson(req),String[].class);
      return rValue;
}
/**
*获取摇钱树的提示信息
*/
public com.pengpeng.stargame.vo.piazza.MoneyTreeHintVO getMoneyTreeHint (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.piazza.MoneyTreeReq req ) throws GameException{
      com.pengpeng.stargame.vo.piazza.MoneyTreeHintVO  rValue=  proxy.execute("moneytree.getMoneyTreeHint",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.piazza.MoneyTreeHintVO.class);
      return rValue;
}

}