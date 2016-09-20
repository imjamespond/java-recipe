package com.pengpeng.admin.stargame.action;

import com.pengpeng.admin.stargame.manager.ILineHandleManager;
import com.pengpeng.admin.stargame.manager.IUserActionManager;
import com.pengpeng.admin.stargame.model.UserActionModel;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.rpc.*;
import com.pengpeng.stargame.tool.LogParser;
import com.pengpeng.stargame.vo.farm.FarmItemReq;
import com.pengpeng.stargame.vo.farm.FarmItemVO;
import com.pengpeng.stargame.vo.farm.FarmPkgVO;
import com.pengpeng.stargame.vo.fashion.FashionIdReq;
import com.pengpeng.stargame.vo.fashion.FashionItemVO;
import com.pengpeng.stargame.vo.fashion.FashionPkgVO;
import com.pengpeng.stargame.vo.gm.CoinReq;
import com.pengpeng.stargame.vo.gm.ItemReq;
import com.pengpeng.stargame.vo.gm.MsgReq;
import com.pengpeng.stargame.vo.role.PlayerVO;
import com.pengpeng.stargame.vo.role.TimeReq;
import com.pengpeng.stargame.vo.room.RoomIdReq;
import com.pengpeng.stargame.vo.room.RoomItemVO;
import com.pengpeng.stargame.vo.room.RoomPkgVO;
import com.pengpeng.stargame.vo.room.RoomVO;
import com.tongyi.action.Page;
import com.tongyi.action.ResResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-15下午6:23
 */
@Controller
@RequestMapping("/message")
public class MessageAction {

    @Autowired
    private GmRpcRemote gmRpcRemote;
    @Autowired
    private PlayerRpcRemote playerRpcRemote;

    /** list */
    @RequestMapping(method={RequestMethod.GET})
    public ModelAndView indexFarmLevelRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("player/msgView");
    }

    /** list */
    @RequestMapping(method={RequestMethod.POST})
    public @ResponseBody PlayerVO search(@RequestParam int uid,HttpServletRequest request,HttpServletResponse response) throws Exception {
        String pid = playerRpcRemote.getPid(null,uid);
        PlayerVO vo = playerRpcRemote.getPlayerInfo(null,pid);
        return vo;
    }

    @RequestMapping(value="/sendAll",method={RequestMethod.GET})
    public @ResponseBody ResResult sendAll(@RequestParam(value="msg",defaultValue="")String msg,HttpServletRequest request,HttpServletResponse response) throws Exception {
        MsgReq req = new MsgReq();
        req.setMsg(msg);
        req.setType(MsgReq.ALL);
        gmRpcRemote.message(null,req);
        return ResResult.newOk();
    }

    @RequestMapping(value="/sendFamily",method={RequestMethod.GET})
    public @ResponseBody ResResult sendFamily(@RequestParam(value="id",defaultValue="")String id,@RequestParam(value="msg",defaultValue="")String msg,HttpServletRequest request,HttpServletResponse response) throws Exception {
        MsgReq req = new MsgReq();
        req.setMsg(msg);
        req.setType(MsgReq.FAMILY);
        req.setId(id);
        gmRpcRemote.message(null,req);
        return ResResult.newOk();
    }

    @RequestMapping(value="/sendSingle",method={RequestMethod.GET})
    public @ResponseBody ResResult sendSingle(@RequestParam(value="id",defaultValue="")int id,@RequestParam(value="msg",defaultValue="")String msg,HttpServletRequest request,HttpServletResponse response) throws Exception {
        String pid = playerRpcRemote.getPid(null,id);
        MsgReq req = new MsgReq();
        req.setMsg(msg);
        req.setType(MsgReq.SINGLE);
        req.setId(pid);
        gmRpcRemote.message(null,req);
        return ResResult.newOk();
    }
}


