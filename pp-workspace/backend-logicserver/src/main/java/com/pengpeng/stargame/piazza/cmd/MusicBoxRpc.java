package com.pengpeng.stargame.piazza.cmd;

import com.pengpeng.common.service.ICommonService;
import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.constant.PlayerConstant;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.model.piazza.MusicBox;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.piazza.container.IMusicBoxRuleContainer;
import com.pengpeng.stargame.piazza.dao.IMusicBoxDao;
import com.pengpeng.stargame.player.container.IActivePlayerContainer;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.player.dao.impl.SiteDao;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.vip.container.IPayMemberRuleContainer;
import com.pengpeng.stargame.vo.CommonReq;
import com.pengpeng.stargame.vo.IdReq;
import com.pengpeng.stargame.vo.piazza.MusicDetailVO;
import com.pengpeng.stargame.vo.piazza.MusicItemVO;
import com.pengpeng.stargame.vo.piazza.MusicZanVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther foquan.lin@pengpeng.com
 * @since: 13-12-4上午10:40
 */
@Component
public class MusicBoxRpc extends RpcHandler {

    @Autowired
    private IMusicBoxRuleContainer musicBoxRuleContainer;

    @Autowired
    private IMusicBoxDao musicBoxDao;

    @Autowired
    private IPlayerDao playerDao;

    @Autowired
    private StatusRemote statusRemote;

    @Autowired
    private FrontendServiceProxy frontendServiceProxy;
    @Autowired
    private RspRoleFactory roleFactory;
    @Autowired
    IActivePlayerContainer activePlayerContainer;


    @RpcAnnotation(cmd = "music.box.list", req = CommonReq.class, name = "音乐盒歌曲列表",vo=  MusicItemVO[].class)
    public MusicItemVO[] list(Session session,CommonReq req){
        //取得音乐列表
        List<MusicItemVO> list = musicBoxRuleContainer.getList();

        return  list.toArray(new MusicItemVO[0]);
    }

    @RpcAnnotation(cmd = "music.box.zaninfo", req = CommonReq.class, name = "赞-信息",vo= MusicZanVO.class)
    public MusicZanVO zaninfo(Session session,CommonReq req) throws GameException {
        MusicBox box =  musicBoxDao.getBean(session.getPid());
        return musicBoxRuleContainer.getZanInfo(box);
    }


    @RpcAnnotation(cmd = "music.box.zan", req = IdReq.class, name = "音乐盒-赞",vo= MusicZanVO.class)
    public MusicZanVO zan(Session session,IdReq req) throws GameException {
        Integer[] ids = req.getUserIds();
        //赞一首歌
        MusicBox box =  musicBoxDao.getBean(session.getPid());
        Player player = playerDao.getBean(session.getPid());
        musicBoxRuleContainer.checkZan(player,box,ids[0],ids[1]);

        musicBoxRuleContainer.zan(player,box,ids[0],ids[1]);

        frontendServiceProxy.broadcast(session, roleFactory.newPlayerVO(player));

        //活跃度
        activePlayerContainer.finish(session, session.getPid(), PlayerConstant.ACTIVE_TYPE_14, 1);

        return musicBoxRuleContainer.getZanInfo(box);


    }

    @RpcAnnotation(cmd = "music.box.detail", req = IdReq.class, name = "音乐盒-详细",vo= MusicDetailVO.class)
    public MusicDetailVO detail(Session session,IdReq req) throws GameException {

        MusicDetailVO musicDetailVO = new MusicDetailVO();

        List<MusicItemVO> listTop=musicBoxRuleContainer.getTopList(3);
        musicDetailVO.setYear(listTop.get(0).getYear());
        musicDetailVO.setListNum(listTop.get(0).getIssue());
        musicDetailVO.setTop3VO(listTop.toArray(new MusicItemVO[0]));

        MusicBox musicBox=musicBoxDao.getBean(session.getPid());
        List<MusicItemVO> mylist = new ArrayList<MusicItemVO>();
        List<MusicItemVO> list = musicBoxRuleContainer.getList();
        for(MusicItemVO musicItemVO:list){
           if(musicBox.getZanInfo().containsKey(musicItemVO.getId())){
               musicItemVO.setVotes(musicBox.getZanInfo().get(musicItemVO.getId())*10);
               mylist.add(musicItemVO);
           }
        }
        musicDetailVO.setMusicItemVO(mylist.toArray(new MusicItemVO[0]));
        musicDetailVO.setRestNum(musicBoxRuleContainer.getFreeZanNum(session.getPid())-musicBox.getZan());
        return musicDetailVO;
    }
}
