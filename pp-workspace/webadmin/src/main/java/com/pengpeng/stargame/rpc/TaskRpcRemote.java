package com.pengpeng.stargame.rpc;

import org.springframework.stereotype.Component;
import com.pengpeng.stargame.exception.GameException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Component
public class TaskRpcRemote {
    @Autowired
    @Qualifier("backendServiceProxy")
    private BackendServiceProxy proxy ;

/**
*取得任务列表
*/
public com.pengpeng.stargame.vo.task.TaskInfoVO getTaskList (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.task.TaskReq req ) throws GameException{
      com.pengpeng.stargame.vo.task.TaskInfoVO  rValue=  proxy.execute("task.list",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.task.TaskInfoVO.class);
      return rValue;
}
/**
*领取任务奖励
*/
public com.pengpeng.stargame.vo.task.TaskVO getreward (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.task.TaskReq req ) throws GameException{
      com.pengpeng.stargame.vo.task.TaskVO  rValue=  proxy.execute("task.getreward",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.task.TaskVO.class);
      return rValue;
}
/**
*获取章节信息
*/
public com.pengpeng.stargame.vo.task.ChaptersInfoVO chapters (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.task.TaskReq req ) throws GameException{
      com.pengpeng.stargame.vo.task.ChaptersInfoVO  rValue=  proxy.execute("task.chapters",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.task.ChaptersInfoVO.class);
      return rValue;
}
/**
*完成新手教程任务
*/
public com.pengpeng.stargame.vo.task.TaskVO finishcourse (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.task.TaskReq req ) throws GameException{
      com.pengpeng.stargame.vo.task.TaskVO  rValue=  proxy.execute("task.finishcourse",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.task.TaskVO.class);
      return rValue;
}
/**
*立即完成任务
*/
public com.pengpeng.stargame.vo.task.TaskVO finished (com.pengpeng.stargame.rpc.Session session, com.pengpeng.stargame.vo.task.TaskReq req ) throws GameException{
      com.pengpeng.stargame.vo.task.TaskVO  rValue=  proxy.execute("task.finish",session, proxy.gson.toJson(req),com.pengpeng.stargame.vo.task.TaskVO.class);
      return rValue;
}

}