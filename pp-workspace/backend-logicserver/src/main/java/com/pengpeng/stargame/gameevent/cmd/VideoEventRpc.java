package com.pengpeng.stargame.gameevent.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.constant.FarmConstant;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IBaseItemRulecontainer;
import com.pengpeng.stargame.farm.dao.IFarmPackageDao;
import com.pengpeng.stargame.model.farm.FarmPackage;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.player.dao.impl.SiteDao;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rsp.RspGameEventFactory;
import com.pengpeng.stargame.vo.event.EventReq;
import com.pengpeng.stargame.vo.event.VideoPanelVO;
import com.pengpeng.stargame.vo.event.VideoVO;
import com.pengpeng.user.LiveVideoInfo;
import com.pengpeng.user.RemotMsg;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * User: mql
 * Date: 14-3-3
 * Time: 下午5:01
 */
@Component()
public class VideoEventRpc extends RpcHandler {
    @Autowired
    private SiteDao siteDao;
    @Autowired
    private IFarmPackageDao farmPackageDao;
    @Autowired
    private IPlayerDao playerDao;
    @Autowired
    private RspGameEventFactory gameEventFactory;
    @Autowired
    private IBaseItemRulecontainer baseItemRulecontainer;
    @Autowired
    private IExceptionFactory exceptionFactory;

    @RpcAnnotation(cmd = "gameevent.video.list", lock = false, req = EventReq.class, name = "取得视频列表",vo = VideoPanelVO.class)
    public VideoPanelVO getVideoList(Session session, EventReq req) throws GameException {
        Player player=playerDao.getBean(session.getPid());
        VideoPanelVO videoPanelVO=new VideoPanelVO();
        videoPanelVO.setNum(baseItemRulecontainer.getGoodsNum(session.getPid(), FarmConstant.TIANXIN_ID));
        if(req.getType()==1){
           List<LiveVideoInfo> liveVideoInfoList=siteDao.willLivingVideos(player.getUserId());
            List<VideoVO> videoVOList=new ArrayList<VideoVO>();
           for(LiveVideoInfo liveVideoInfo:liveVideoInfoList){
               VideoVO videoVO=new VideoVO();
               BeanUtils.copyProperties(liveVideoInfo,videoVO);
               videoVO.setBuy(liveVideoInfo.isBuy());
               if(liveVideoInfo.getOpeningTime()!=null&&liveVideoInfo.getLiveTime()!=null){
                   videoVO.setOpeningTimeL(liveVideoInfo.getOpeningTime().getTime());
                   videoVO.setLiveTimeL(liveVideoInfo.getLiveTime().getTime());
               }
               videoVOList.add(videoVO);
           }
            videoPanelVO.setVideoVOs(videoVOList.toArray(new VideoVO[0]));
        }
        if(req.getType()==2){
            int pageNo=req.getPageNo();
            if(pageNo==0){
                pageNo=1;
            }
            int max=siteDao.countHistoryLives();
            int pageSize=2;
            int maxPage=max%pageSize==0?max/pageSize:max/pageSize+1;
            List<LiveVideoInfo> liveVideoInfoList=siteDao.historyLives(player.getUserId(),(pageNo-1)*pageSize,pageNo*pageSize);
            List<VideoVO> videoVOList=new ArrayList<VideoVO>();
            for(LiveVideoInfo liveVideoInfo:liveVideoInfoList){
                VideoVO videoVO=new VideoVO();
                BeanUtils.copyProperties(liveVideoInfo,videoVO);
                videoVO.setBuy(liveVideoInfo.isBuy());
                if(liveVideoInfo.getOpeningTime()!=null&&liveVideoInfo.getLiveTime()!=null){
                    videoVO.setOpeningTimeL(liveVideoInfo.getOpeningTime().getTime());
                    videoVO.setLiveTimeL(liveVideoInfo.getLiveTime().getTime());
                }

                videoVOList.add(videoVO);
            }
            videoPanelVO.setVideoVOs(videoVOList.toArray(new VideoVO[0]));
            videoPanelVO.setMaxPage(maxPage);
            videoPanelVO.setPageNo(pageNo);
        }
        return videoPanelVO;
    }

    @RpcAnnotation(cmd = "gameevent.video.exchange", lock = false, req = EventReq.class, name = "兑换",vo = VideoPanelVO.class)
    public VideoPanelVO exchange(Session session, EventReq req) throws GameException {
        VideoPanelVO videoPanelVO=new VideoPanelVO();
        Player player=playerDao.getBean(session.getPid());
        int liveId=req.getVedeoId();
        int tianxianNum=baseItemRulecontainer.getGoodsNum(session.getPid(),FarmConstant.TIANXIN_ID);
        List<LiveVideoInfo> liveVideoInfoList=siteDao.willLivingVideos(player.getUserId());
        LiveVideoInfo liveVideoInfo=null;
        if(liveVideoInfoList==null||liveVideoInfoList.size()<=0){
            exceptionFactory.throwAlertException("video.no.live");
        }
        for(LiveVideoInfo temp:liveVideoInfoList){
            if(temp.getId()==liveId){
                liveVideoInfo=temp;
            }
        }
        if(liveVideoInfo==null){
            exceptionFactory.throwAlertException("直播视频不存在！");
        }
        if(tianxianNum<liveVideoInfo.getNeedCaptcha()){
            exceptionFactory.throwAlertException("video.num.no");
        }


        /**
         * 调用网站 给玩家发送验证码
         */
        RemotMsg remotMsg=siteDao.exchangeCode(player.getUserId(),liveVideoInfo.getId());

        if(!remotMsg.getStatus().equals("200")){
            exceptionFactory.throwAlertException(remotMsg.getMesssag());
        }
        /**
         * 扣除天线
         */
        baseItemRulecontainer.useGoods(session.getPid(),FarmConstant.TIANXIN_ID,liveVideoInfo.getIstarUserPrice());


        List<LiveVideoInfo> liveVideoInfoList1=siteDao.willLivingVideos(player.getUserId());
        List<VideoVO> videoVOList=new ArrayList<VideoVO>();
        for(LiveVideoInfo temp:liveVideoInfoList1){
            VideoVO videoVO=new VideoVO();
            BeanUtils.copyProperties(temp,videoVO);
            videoVO.setBuy(temp.isBuy());
            if(liveVideoInfo.getOpeningTime()!=null&&liveVideoInfo.getLiveTime()!=null){
                videoVO.setOpeningTimeL(liveVideoInfo.getOpeningTime().getTime());
                videoVO.setLiveTimeL(liveVideoInfo.getLiveTime().getTime());
            }
            videoVOList.add(videoVO);
        }
        videoPanelVO.setVideoVOs(videoVOList.toArray(new VideoVO[0]));
        videoPanelVO.setNum(baseItemRulecontainer.getGoodsNum(session.getPid(), FarmConstant.TIANXIN_ID));


         return videoPanelVO;
    }
}
