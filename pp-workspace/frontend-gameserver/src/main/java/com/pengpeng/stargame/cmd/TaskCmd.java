package com.pengpeng.stargame.cmd;

import com.pengpeng.stargame.annotation.CmdAnnotation;
import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.cmd.response.Response;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.TaskRpcRemote;
import com.pengpeng.stargame.vo.task.ChaptersInfoVO;
import com.pengpeng.stargame.vo.task.TaskInfoVO;
import com.pengpeng.stargame.vo.task.TaskReq;
import com.pengpeng.stargame.vo.task.TaskVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 任务
 * @auther foquan.lin@pengpeng.com
 * @since: 13-7-12下午2:25
 */
@Component
public class TaskCmd extends AbstractHandler {

    @Autowired
    private TaskRpcRemote taskRpcRemote;
    @CmdAnnotation(cmd = "task.list", req = TaskReq.class, name = "取得任务列表",vo=TaskInfoVO.class)
    public Response getTaskList(Session session, TaskReq req) throws GameException {

        return Response.newObject(taskRpcRemote.getTaskList(session,req));
    }

    @CmdAnnotation(cmd = "task.finish", req = TaskReq.class, name = "立即完成任务",vo=TaskVO.class)
    public Response finished(Session session,TaskReq req) throws GameException {
        TaskVO vo = taskRpcRemote.finished(session, req);
        return Response.newObject(vo);
    }

    @CmdAnnotation(cmd = "task.getreward", req = TaskReq.class, name = "领取任务奖励",vo=TaskVO.class)
    public Response getreward(Session session,TaskReq req) throws GameException {
        TaskVO vo = taskRpcRemote.getreward(session,req);
        return Response.newObject(vo);
    }

    @CmdAnnotation(cmd = "task.chapters", req = TaskReq.class, name = "获取章节信息",vo=ChaptersInfoVO.class)
    public Response chapters(Session session,TaskReq req) throws GameException {

        return Response.newObject(taskRpcRemote.chapters(session,req));
    }

    @CmdAnnotation(cmd = "task.finishcourse", req = TaskReq.class, name = "完成新手教程任务",vo=TaskVO.class)
    public Response finishcourse(Session session,TaskReq req) throws GameException {
        TaskVO taskVO=taskRpcRemote.finishcourse(session,req);
        return Response.newObject(taskVO);
    }

    @CmdAnnotation(cmd = "task.finishspecial", req = TaskReq.class, name = "完成特殊任务",vo=void.class)
    public Response finishspecial(Session session,TaskReq req) throws GameException {
        taskRpcRemote.finishspecial(session,req);
        return Response.newOK();
    }

}
