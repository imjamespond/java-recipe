package com.pengpeng.stargame.player.dao.impl;


import com.pengpeng.common.service.ICommonService;
import com.pengpeng.stargame.vo.role.UserInfo;
import com.pengpeng.user.*;
import com.pengpeng.user.exception.AuthFailException;
import com.pengpeng.user.service.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-3-18下午12:30
 */
@Component
public class SiteDao {
    private static Logger logger = Logger.getLogger(SiteDao.class);
    @Autowired
    @Qualifier("remotingUserSecurityService")
    private IUserSecurityService service;

    @Autowired
    @Qualifier("remotingUserSocialService")
    private ISocialService socialService;

    @Autowired
    @Qualifier("remotingUserDataService")
    private IUserDataService dataService;

    @Autowired
    @Qualifier("remotingCommonService")
    private ICommonService commonService;


    @Autowired
    @Qualifier("remotingMusicBoxService")
    private IMusicBoxService musicBoxService;

    @Autowired
    @Qualifier("remotingLiveVideoService")
    private ILiveVideoService remotingLiveVideoService;

    public SiteDao() {
    }

    public boolean isLogin(String token){
        UserDetail data = null;
        try {
            data = service.authenticate(token);
            if (data!=null){
                return true;
            }
        } catch (AuthFailException e) {
            logger.trace(e);
        }
        return false;
    }
    public UserInfo getUserInfo(String token) throws AuthFailException {
        UserDetail data = null;
        data = service.authenticate(token);
        if (data != null) {
            UserProfile profile = dataService.findProfile(data.getId());
            UserInfo user = new UserInfo(profile.getId(), profile.getNickName(), profile.getSex());
            user.setStarId(profile.getFavoriteSpaceId());
            return user;
        }
        return null;
    }

    public List<UserProfile> getFriendUser(Integer userId){
        long t = System.currentTimeMillis();
        List<UserProfile> list = null;
        try{
            list = socialService.getFollowees(userId);
        }catch(Exception e){
            logger.trace(e);
            list = new ArrayList<UserProfile>();
        }
        t = System.currentTimeMillis() - t;
        logger.debug(String.format("ISocialService接口调用时间:%d/ms",t));
        return list;
    }

	public UserInfo getUserInfoById(Integer userId){
		UserProfile data = null;
        try{
            data = dataService.findProfile(userId);
        }catch(Exception e){
            logger.trace(e);
            data = null;
        }
		if(data !=null){
            UserInfo user = new UserInfo(data.getId(),data.getNickName(),data.getSex());
            user.setStarId(data.getFavoriteSpaceId());
			return user;
		}
		return null;
	}

    /**
     * 增加积分 接口  废弃
     * @param uid
     * @param coin
     * @throws Exception
     */

//    public void addCustomPointsByGame(int uid,int coin) throws Exception {
//        dataService.addCustomPointsByGame(uid,coin);
//    }

    /**
     * 添加积分 接口
     * @param uid
     * @param num
     * @param type
     * @param title
     */
    public void addFansDoyenPoint(int uid,int num,String type,String title) throws Exception {
        dataService.addFansDoyenPoint(uid,num,type,title);
    }

    /**
     * 聊天验证接口
     * @param msg
     * @return
     */

    public String dropIllegalWord(String msg){
        try{
            return commonService.dropIllegalWord(msg);
        }catch(Exception e){
            logger.error("聊天验证接口失败.....................");
            e.printStackTrace();
            logger.trace(e);
            return "";
        }
    }

    /**
     * 获取榜单
     * @return
     */
    public List<SongListItemInfo> getSongList(){
        List<SongListItemInfo> list = musicBoxService.getMusicBoxSongList(null,15);
        return list;
    }

    /**
     * 查询音乐指数 排行榜，前几名
     * @param num  需要查询的 前几
     * @return
     */

    public List<SongListItemInfo> getTopSongList(int num){
        List<SongListItemInfo> list = musicBoxService.getMusicBoxSongList(null,num);
        return list;
    }

    public int zan(Integer uid,Integer listId,Integer listItemId,int votes){
        return musicBoxService.updateSongPraise(uid,listId,listItemId,votes);
    }

    /**
     * 查询即将直播的视频列表
     * <p>只列出当前开放时间到直播时间之间的视频</p>
     *  @param userId 用户id 不接受空值
     *
     * @return 视频列表结合
     */
    public List<LiveVideoInfo> willLivingVideos(Integer userId){
        return remotingLiveVideoService.willLivingVideos(userId);
    }

    /**
     * 分页查询历史直播视频，按直播时间先后的倒序显示
     *
     * @param userId 用户id 不接受空值
     * @param startRow 记录起始行 不接受空值
     * @param maxRow 最大记录数 不接受空值
     * @return 视频列表
     */
    public List<LiveVideoInfo> historyLives( Integer userId, Integer startRow, Integer maxRow){
        return remotingLiveVideoService.historyLives(userId,startRow,maxRow);
    }

    /**
     * 历史直播视频总数
     *
     * @return 历史视频总数
     */
    public int countHistoryLives(){
        return remotingLiveVideoService.countHistoryLives();
    }

    /**
     * 粉丝达人用户兑换视频直播验证码
     * <p>此接口只负责发放验证码和系统通知，不负责扣除用户帐户积分或金币，请慎用</p>
     *
     * @param userId 用户ID  不接受空值
     * @param liveId 视频ID 不接受空值
     *
     * @return 远程请求通用消息对象
     */
    public RemotMsg exchangeCode(Integer userId,Integer liveId){
        return remotingLiveVideoService.exchangeCode(userId,liveId);
    }

}
