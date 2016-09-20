package com.pengpeng.admin.stargame.action;

import com.pengpeng.stargame.rpc.GmRpcRemote;
import com.pengpeng.stargame.rpc.PlayerRpcRemote;
import com.pengpeng.stargame.vo.gm.AddGoldReq;
import com.pengpeng.stargame.vo.gm.AddGoldVO;
import com.pengpeng.stargame.vo.gm.MsgReq;
import com.pengpeng.stargame.vo.role.PlayerVO;
import com.tongyi.action.ResResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-15下午6:23
 */
@Controller
@RequestMapping("/gold")
public class GoldAction {

    @Autowired
    private GmRpcRemote gmRpcRemote;
    @Autowired
    private PlayerRpcRemote playerRpcRemote;

    /** list */
    @RequestMapping(method={RequestMethod.GET})
    public ModelAndView index(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("player/goldView");
    }

    /** list */
    @RequestMapping(method={RequestMethod.POST})
    public @ResponseBody AddGoldVO list(HttpServletRequest request,HttpServletResponse response) throws Exception {
        AddGoldReq req = new AddGoldReq();
        AddGoldVO vo = gmRpcRemote.getAddGoldInfo(null,req );
        return vo;
    }

    @RequestMapping(value="/save",method={RequestMethod.GET})
    public @ResponseBody ResResult save(@RequestParam(value="gold",defaultValue="") int gold,@RequestParam(value="uids",defaultValue="")String uids,HttpServletRequest request,HttpServletResponse response) throws Exception {
        String[] arr = uids.split(",");
        AddGoldReq req = new AddGoldReq();
        req.setGold(gold);
        req.setUids(arr);
        gmRpcRemote.saveAddGold(null,req);
        return ResResult.newOk();
    }


}


