package com.pengpeng.admin.stargame.action;

import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.dao.RedisKeyValueDB;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.farm.rule.BaseItemRule;
import com.pengpeng.stargame.model.player.RechargeLog;
import com.pengpeng.stargame.rpc.*;
import com.pengpeng.stargame.vo.farm.FarmItemReq;
import com.pengpeng.stargame.vo.farm.FarmItemVO;
import com.pengpeng.stargame.vo.farm.FarmPkgVO;
import com.pengpeng.stargame.vo.gm.VideoFamilyInfoVO;
import com.pengpeng.stargame.vo.gm.VideoReq;
import com.pengpeng.stargame.vo.piazza.FamilyInfoVO;
import com.pengpeng.stargame.vo.piazza.FamilyReq;
import com.pengpeng.stargame.vo.role.PlayerVO;
import com.tongyi.action.Page;
import com.tongyi.action.ResResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: mql
 * Date: 14-2-24
 * Time: 下午5:11
 */
@Controller
@RequestMapping("/video")
public class VideoAction {
    @Autowired
    private GmRpcRemote gmRpcRemote;
    @Autowired
    private PlayerRpcRemote playerRpcRemote;
    @Autowired
    private FamilyInfoRpcRemote familyInfoRpcRemote;
    @Autowired
    private FarmPkgRpcRemote farmPkgRpcRemote;
    @Autowired
    private RedisKeyValueDB redisKeyValueDB;
    @RequestMapping(method={RequestMethod.GET})
    public ModelAndView indexFarmLevelRule(HttpServletRequest request,HttpServletResponse response) throws Exception {
        return new ModelAndView("video/view");
    }
    @RequestMapping(value="/viewFamilyVideo",method={RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody
    Page listGold(@RequestParam(value="page",defaultValue="1")int page,@RequestParam(value="rows",defaultValue="10")int rows,HttpServletRequest request,HttpServletResponse response) throws GameException, ModelAndViewDefiningException {
        String fid=request.getParameter("fid");


        VideoReq videoReq=new VideoReq();
        videoReq.setfId(fid);
        videoReq.setItemId("");
        videoReq.setPageNo(page);
        videoReq.setSize(rows);
        videoReq.setItemId(FarmConstant.TIANXIN_ID);

        VideoFamilyInfoVO videoFamilyInfoVO=gmRpcRemote.videoFamilyNum(null,videoReq);

        if(page==1){
            if(videoFamilyInfoVO.getValues()!=null&&videoFamilyInfoVO.getValues().size()>0){
                String key="familyItemNum_"+fid+"_"+FarmConstant.TIANXIN_ID;
                String allNum=redisKeyValueDB.get(key);
                if(allNum==null){
                    allNum="0";
                }
                List< Map<String, String> > list=new ArrayList<Map<String, String>>();
                for(int i=0;i<videoFamilyInfoVO.getValues().size();i++){
                    Map<String, String> oneMap=videoFamilyInfoVO.getValues().get(0);
                    if(i==0){
                        oneMap.put("allNum",allNum);
                    }
                    list.add(oneMap);
                }

            }
        }
        Page pageObj = new Page();
        pageObj.setPage(page);
        pageObj.setRows(videoFamilyInfoVO.getValues());
        pageObj.setTotal(videoFamilyInfoVO.getMaxPage());
        return pageObj;
    }

    @RequestMapping(value="/findUserVideoNum/u/{uid}",method={RequestMethod.GET,RequestMethod.POST})
    public @ResponseBody
    ModelAndView findUserVideoNum(@PathVariable int uid,HttpServletRequest request,HttpServletResponse response) throws GameException, ModelAndViewDefiningException {
        String pid = playerRpcRemote.getPid(null, uid);
        Session session = new Session(pid,"");

        ModelAndView modelAndView=  new ModelAndView("video/view");

        if(pid == null){
            return modelAndView;
        }
        PlayerVO pv = playerRpcRemote.getPlayerInfo(session,pid);

        modelAndView.addObject("uid",pv.getUserId());
        modelAndView.addObject("pName",pv.getNickName());
        if(pv.getFamilyId()!=null){
            FamilyReq famReq = new FamilyReq();
            famReq.setPid(pid);
            famReq.setFamilyId(pv.getFamilyId());
            FamilyInfoVO fivo = familyInfoRpcRemote.getFamilyById(session, famReq);
        }

        FarmItemReq req = new FarmItemReq();
        req.setPid(pid);
        FarmPkgVO fpv = farmPkgRpcRemote.getItemAll(session, req);
        FarmItemVO[] fiv = fpv.getFarmItemVO();
        int num=0;
        if(fiv != null){
            for(FarmItemVO fi:fiv){
               if(fi.getItemId().equals(FarmConstant.TIANXIN_ID)){
                   num+=fi.getNum();
               }
            }
        }
        return modelAndView;

    }

    }
