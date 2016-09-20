package com.pengpeng.admin.stargame.action;

import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.piazza.rule.FamilyRule;
import com.pengpeng.stargame.rpc.FamilyAssistantRpcRemote;
import com.pengpeng.stargame.rpc.PlayerRpcRemote;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.vo.piazza.ApplicationListVO;
import com.pengpeng.stargame.vo.piazza.ApplicationVO;
import com.pengpeng.stargame.vo.piazza.FamilyAssistantReq;
import com.pengpeng.stargame.vo.role.PlayerVO;
import com.tongyi.action.Page;
import com.tongyi.action.ResResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-8-15下午6:23
 */
@Controller
@RequestMapping("/assistant")
public class AssistantAction {

    @Autowired
    private FamilyAssistantRpcRemote familyAssistantRpcRemote;
    @Autowired
    private PlayerRpcRemote playerRpcRemote;
    @Autowired
    private HibernateTemplate template;

    /**
     * list
     */
    @RequestMapping(method = {RequestMethod.GET})
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("player/assistantViewList");
    }

    /**
     * ajax list
     */
    @RequestMapping(value = "/list", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    Page<ApplicationVO> list(@RequestParam(value = "fid", defaultValue = "") String fid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FamilyAssistantReq req = new FamilyAssistantReq();
        req.setFid(fid);
        ApplicationListVO vo = familyAssistantRpcRemote.list(null, req);
        Page<ApplicationVO> page = new Page<ApplicationVO>();
        for (ApplicationVO avo : vo.getApplicationVOs()) {
            PlayerVO pv = playerRpcRemote.getPlayerInfo(null, avo.getPid());
            avo.setName(pv.getNickName());
            avo.setUid(pv.getUserId());
        }
        page.setRows(Arrays.asList(vo.getApplicationVOs()));
        return page;
    }

    /**
     * ajax list
     */
    @RequestMapping(value = "/family", method = {RequestMethod.GET, RequestMethod.POST})
    public
    @ResponseBody
    List<Kv> family(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<Kv> list = new ArrayList<Kv>();
        List<FamilyRule> frList = template.loadAll(FamilyRule.class);
        for (FamilyRule fr : frList) {
            list.add(new Kv(fr.getName(), fr.getId()));
        }
        return list;
    }

    @RequestMapping(value = "/approve", method = {RequestMethod.GET})
    public
    @ResponseBody
    ResResult save(@RequestParam(value = "pid", defaultValue = "") String pid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Session session = new Session(pid, null);
        familyAssistantRpcRemote.approve(session, null);
        return ResResult.newOk();
    }

    @RequestMapping(value = "/clean", method = {RequestMethod.GET})
    public
    @ResponseBody
    ResResult clean(@RequestParam(value = "fid", defaultValue = "") String fid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        FamilyAssistantReq req = new FamilyAssistantReq();
        req.setFid(fid);
        familyAssistantRpcRemote.clean(null, req);
        return ResResult.newOk();
    }

    @RequestMapping(value = "/designate", method = {RequestMethod.GET})
    public
    @ResponseBody
    ResResult designate(@RequestParam(value = "fid", defaultValue = "") String fid, HttpServletRequest request, HttpServletResponse response) throws Exception {

        FamilyAssistantReq familyAssistantReq = new FamilyAssistantReq();
        familyAssistantReq.setFid(fid);
        try {
            familyAssistantRpcRemote.designate(null, familyAssistantReq);
        } catch (GameException e) {
            e.printStackTrace();
        }
        return ResResult.newOk();
    }

    private class Kv {
        public String key;
        public String val;

        public Kv(String key, String val) {
            this.key = key;
            this.val = val;
        }
    }
}


