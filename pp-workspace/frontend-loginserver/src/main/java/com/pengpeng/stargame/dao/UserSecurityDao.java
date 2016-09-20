package com.pengpeng.stargame.dao;

import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.vo.role.UserInfo;
import com.pengpeng.user.UserDetail;
import com.pengpeng.user.UserProfile;
import com.pengpeng.user.exception.AuthFailException;
import com.pengpeng.user.service.ISocialService;
import com.pengpeng.user.service.IUserDataService;
import com.pengpeng.user.service.IUserSecurityService;
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
public class UserSecurityDao {
    private static Logger logger = Logger.getLogger(UserSecurityDao.class);
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
    private IExceptionFactory exceptionFactory;

    public UserSecurityDao() {
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
        logger.info(String.format("开始调用 IUserSecurityService service.authenticate token:%s",token));
        long time = System.currentTimeMillis();
        data = service.authenticate(token);
        long t = System.currentTimeMillis() -time;
        t = t/1000;
        logger.info(String.format("调用IUserSecurityService service.authenticate完毕 token:%s，耗时:%d 秒" ,token,t));
        if (data != null) {
            logger.info(String.format("开始调用 IUserDataService findProfile"));
            long time1 = System.currentTimeMillis();
            UserProfile profile = dataService.findProfile(data.getId());
            long t1 = System.currentTimeMillis() -time1;
            t1 = t1/1000;
            logger.info(String.format("调用IUserDataService findProfile完毕 耗时:%d 秒" ,t1));
            UserInfo user = new UserInfo(profile.getId(), profile.getNickName(), profile.getSex());
            user.setStarId(profile.getFavoriteSpaceId());
            user.setPayMember(profile.getPayMember());
            user.setBirthCity(profile.getBirthCity());
            user.setCity(profile.getCity());
            user.setProvince(profile.getProvince());
            user.setSignature(profile.getSignature());
            user.setBirthCity(profile.getBirthCity());
            user.setBirthProvince(profile.getBirthProvince());
            logger.info("玩家 UID："+profile.getId()+" 玩家名字："+profile.getNickName()+" 超级粉丝数据："+profile.getPayMember());

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
}
