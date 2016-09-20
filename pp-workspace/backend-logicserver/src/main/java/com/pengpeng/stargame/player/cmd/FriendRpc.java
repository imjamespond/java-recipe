package com.pengpeng.stargame.player.cmd;

import com.pengpeng.stargame.annotation.RpcAnnotation;
import com.pengpeng.stargame.common.Constant;
import com.pengpeng.stargame.constant.EventConstant;
import com.pengpeng.stargame.dispatcher.RpcHandler;
import com.pengpeng.stargame.exception.AlertException;
import com.pengpeng.stargame.exception.IExceptionFactory;
import com.pengpeng.stargame.farm.container.IFarmRuleContainer;
import com.pengpeng.stargame.model.player.FriendItem;
import com.pengpeng.stargame.player.EventLogBuilder;
import com.pengpeng.stargame.player.dao.IEventLogDao;
import com.pengpeng.stargame.player.dao.IFriendDao;
import com.pengpeng.stargame.player.dao.IPlayerDao;
import com.pengpeng.stargame.player.dao.impl.SiteDao;
import com.pengpeng.stargame.model.player.Friend;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.rpc.FrontendServiceProxy;
import com.pengpeng.stargame.rpc.Session;
import com.pengpeng.stargame.rpc.StatusRemote;
import com.pengpeng.stargame.rsp.RspFactory;
import com.pengpeng.stargame.rsp.RspRoleFactory;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.util.Page;
import com.pengpeng.stargame.vo.IdReq;
import com.pengpeng.stargame.vo.MsgVO;
import com.pengpeng.stargame.vo.role.*;
import com.pengpeng.user.UserProfile;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @author jinli.yuan@pengpeng.com
 * @since 13-4-24 上午9:50
 */
@Component()
public class FriendRpc extends RpcHandler {
	private static final Logger logger = Logger.getLogger(FriendRpc.class);
    // 通过 , 成功
    private static final int SUCCESS = 1;
    // 通过 ,不通过
    private static final int REJECT = 2;

    // 待审核
    private static final int AUDITING = 0;

    @Autowired
    private IFarmRuleContainer farmRuleContainer;

	@Autowired
	private IPlayerDao playerDao;

	@Autowired
	private StatusRemote statusService;

	@Autowired
	private FrontendServiceProxy frontendService;

	@Autowired
	private IFriendDao friendDao;

	@Autowired
	private SiteDao siteDao;

    @Autowired
    private RspRoleFactory roleFactory;

    @Autowired
    private IExceptionFactory exceptionFactory;

    @Autowired
    private MessageSource message;

    @Autowired
    private IEventLogDao eventLogDao;

    @Autowired
    private EventLogBuilder eventLogBuilder;


	@RpcAnnotation(cmd="friend.get.info",req=String.class,name="取得游戏内好友信息")
	public PlayerVO getFriendInfo(Session session,String friendId){
		Player player = playerDao.getBean(friendId);
		if(player == null){
			return null;
		}
		PlayerVO pv = roleFactory.newPlayerVO(player);
		return pv;
	}


	@RpcAnnotation(cmd="friend.get.list",req=PageReq.class,name="取得游戏内好友列表")
	public FriendPage getListFriend(Session session,PageReq req){
        Friend my = friendDao.getBean(session.getPid());
        Page<FriendItem> page =  friendDao.findPage(my,req.getPageNo(), 5);

        if(page==null){
            return null;
        }

        List<FriendItem> list = page.getElements();
        if(list == null || list.isEmpty()){
            return null;
        }

        List<FriendVO> listFriend = new ArrayList<FriendVO>();
        for(FriendItem friend : list){
            if (Constant.QINMA_ID.equalsIgnoreCase(friend.getFid())){
                FriendVO vo = roleFactory.newQinMaFriendVO(friend);
                listFriend.add(vo);
            }else{
                Player p = playerDao.getBean(friend.getFid());
                if(p == null){
                    continue;
                }
                //查询好友农场的状态
                FriendVO vo = roleFactory.newFriendVO(p,friend);
                listFriend.add(vo);
            }
        }

        FriendPage friendRes = new FriendPage();
        friendRes.setPageNo(page.getBegin());
        friendRes.setMaxPage(page.getMaxPage());
        friendRes.setFriends(listFriend.toArray(new FriendVO[0]));
        return friendRes;
	}

	@RpcAnnotation(cmd="friend.get.site",req=String.class,name="取得网站好友")
	public FriendVO[] getPPListFriend(Session session,String pid){
		Player player = playerDao.getBean(pid);
		if(player == null){
			return null;
		}
        Friend friend = friendDao.getBean(pid);
        List<String> list = new ArrayList<String>();
        List<UserProfile> friends = siteDao.getFriendUser(player.getUserId());
        if (null!=friends&&friends.size()>0){//有网站好友
            list.addAll(roleFactory.newFriendIds(friend));//已经是好友的排除掉
            list.addAll(roleFactory.getInviteFriendsIds(friend));//排除今天邀请过的名单
            HashSet<Integer> sets = new HashSet<Integer>(list.size());
            sets.add(player.getUserId());
            for(String id:list){
                Player fp = playerDao.getBean(id);
                sets.add(fp.getUserId());
            }
            for(UserProfile item:friends){//没有创建角色的要排除掉
                String id = playerDao.getPid(item.getId());
                if (id==null){
                    sets.add(item.getId());
                }
            }
            friends = roleFactory.filterUserprofile(friends,sets);//过滤好友结果
            if (friends.size()>0){//如果还有则返回网站好友
                return roleFactory.getPPListFriendToArr(friends);
            }//没有就取得在线好友
        }
        list = roleFactory.newFriendIds(friend);
        list.addAll(roleFactory.getInviteFriendsIds(friend));//排除今天邀请过的名单
        list.add(pid);//排除自己
        String[] ids = list.toArray(new String[0]);
                //如果网站没有好友,则随机找6个在线玩家
        Session[] s = statusService.randomSession(session,ids);
        if (s==null||s.length<=0){
            return null;
        }
        FriendVO[] vos = new FriendVO[s.length];
        for(int i=0;i<s.length;i++){
            Player p = playerDao.getBean(s[i].getPid());
            vos[i] = roleFactory.newFriendVO(p);
        }
        return vos;
	}

	@RpcAnnotation(cmd="friend.import",vo=void.class,req=IdReq.class,name="导入好友")
	public void addBatchFriend(Session session,IdReq idReq){
		final String pId = session.getPid();
		Integer [] userIds = idReq.getUserIds();
		if(userIds == null || userIds.length==0){
			return;
		}

        Player myPlayer = playerDao.getBean(pId);
        Friend my = friendDao.getBean(pId);
        for(Integer userId : userIds){
			String fId = playerDao.getPid(userId);
			if(StringUtils.isBlank(fId)){
				continue;
			}
            // 检查是否已经存在好友
            if(my.isFriend(fId)){
                continue;
            }
            Friend friend = friendDao.getBean(fId);
            my.invite(fId);
            friend.addUnknownsFriend(new FriendItem(pId, new Date()));
            friendDao.saveBean(my);
            friendDao.saveBean(friend);
            Session s = statusService.getSession(session, fId);
            if (s != null) {
                //如果好友已登录存在session才需要广播
                frontendService.broadcast(new Session[]{s}, new FriendAuditVO(myPlayer.getId(), myPlayer.getNickName(),RspFactory.getUserPortrait(myPlayer.getUserId())));
                //通知对方有新消息
                frontendService.broadcast(new Session[]{s}, new MsgVO(EventConstant.EVENT_EMAIL,1));
            }
        }
	}


	@RpcAnnotation(cmd="friend.audit",req=AuditReq.class,name="审核好友")
	public void auditFriend(Session session, AuditReq auditReq){
		String fId = auditReq.getId();
		Integer status = auditReq.getStatus();

		if(StringUtils.isBlank(fId) || status == null){
			return;
		}

        Player myPlayer = playerDao.getBean(session.getPid());
        Player friendPlayer = playerDao.getBean(fId);
        Friend my = friendDao.getBean(myPlayer.getId());
        Friend friend = friendDao.getBean(friendPlayer.getId());

		if(my ==null){
			return;
		}

        if(status==REJECT){
            my.reject(friendPlayer.getId());
            friend.reject(myPlayer.getId());
            String msg = message.getMessage("log.event2", new Object[]{DateUtil.getDateFormat(new Date(),null),friendPlayer.getNickName()}, Locale.CHINA);
            eventLogDao.saveBean(myPlayer.getId(),eventLogBuilder.newFriendEvent(myPlayer.getId(),msg));

            String friendMsg = message.getMessage("log.event4", new Object[]{DateUtil.getDateFormat(new Date(),null),myPlayer.getNickName()}, Locale.CHINA);
            eventLogDao.saveBean(friendPlayer.getId(),eventLogBuilder.newFriendEvent(friendPlayer.getId(),friendMsg));

        }else if (status == SUCCESS){
            my.approve(friendPlayer.getId());
            friend.approveInvite(myPlayer.getId());
            String msg = message.getMessage("log.event1", new Object[]{DateUtil.getDateFormat(new Date(),null),friendPlayer.getNickName()}, Locale.CHINA);
            eventLogDao.saveBean(myPlayer.getId(),eventLogBuilder.newFriendEvent(myPlayer.getId(),msg));

            String friendMsg = message.getMessage("log.event3", new Object[]{DateUtil.getDateFormat(new Date(),null),myPlayer.getNickName()}, Locale.CHINA);
            eventLogDao.saveBean(friendPlayer.getId(),eventLogBuilder.newFriendEvent(friendPlayer.getId(),friendMsg));

            //审核通过,进行广播
            Session s = statusService.getSession(session, friendPlayer.getId());
            if (s != null) {
                //如果好友已登录存在session才需要广播
                FriendAuditVO vo = new FriendAuditVO(myPlayer.getId(), myPlayer.getNickName(),1,RspFactory.getUserPortrait(myPlayer.getUserId()));
                frontendService.broadcast(new Session[]{s}, vo);
            }
        }

        friendDao.saveBean(my);
        friendDao.saveBean(friend);
	}

	@RpcAnnotation(cmd="friend.audit.batch",req=AuditReq.class,name="批量审核好友")
	public void auditBatchFriend(Session session, AuditReq auditReq){
		String [] ids = auditReq.getIds();
		Integer status = auditReq.getStatus();
		if(ids == null || ids.length ==0 || status==null){
			return;
		}
        if (status!=REJECT&&status!=SUCCESS){
            return ;
        }
        Player myPlayer = playerDao.getBean(session.getPid());
        Friend my = friendDao.getBean(session.getPid());
        if(status==REJECT){
            for(String fId:ids){
                Friend friend = friendDao.getBean(fId);
                my.reject(fId);
                friend.reject(session.getPid());
                friendDao.saveBean(friend);

                Player friendPlayer = playerDao.getBean(fId);
                String msg = message.getMessage("log.event2", new Object[]{DateUtil.getDateFormat(new Date(),null),friendPlayer.getNickName()}, Locale.CHINA);
                eventLogDao.saveBean(myPlayer.getId(),eventLogBuilder.newFriendEvent(myPlayer.getId(),msg));

                String friendMsg = message.getMessage("log.event4", new Object[]{DateUtil.getDateFormat(new Date(),null),myPlayer.getNickName()}, Locale.CHINA);
                eventLogDao.saveBean(friendPlayer.getId(),eventLogBuilder.newFriendEvent(friendPlayer.getId(),friendMsg));
            }
            friendDao.saveBean(my);
            return ;
        }
        if (status == SUCCESS){
            for(String fId:ids){
                Friend friend = friendDao.getBean(fId);
                my.approve(fId);
                friend.approveInvite(session.getPid());
                friendDao.saveBean(friend);

                Player friendPlayer = playerDao.getBean(fId);
                String msg = message.getMessage("log.event1", new Object[]{DateUtil.getDateFormat(new Date(),null),friendPlayer.getNickName()}, Locale.CHINA);
                eventLogDao.saveBean(myPlayer.getId(),eventLogBuilder.newFriendEvent(myPlayer.getId(),msg));

                String friendMsg = message.getMessage("log.event3", new Object[]{DateUtil.getDateFormat(new Date(),null),myPlayer.getNickName()}, Locale.CHINA);
                eventLogDao.saveBean(friendPlayer.getId(),eventLogBuilder.newFriendEvent(friendPlayer.getId(),friendMsg));

                //审核通过,进行广播
                Session s = statusService.getSession(session, fId);
                if (s != null) {
                    //如果好友已登录存在session才需要广播
                    frontendService.broadcast(new Session[]{s}, new FriendAuditVO(myPlayer.getId(), myPlayer.getNickName(), 1,RspFactory.getUserPortrait(myPlayer.getUserId())));
                }
            }
            friendDao.saveBean(my);
        }
	}

	@RpcAnnotation(cmd="friend.del",req=AuditReq.class,name="删除好友")
	public void deleteFriend(Session session,AuditReq auditReq){
		String fId = auditReq.getId();
		if(StringUtils.isBlank(fId)){
			return;
		}
        Friend my = friendDao.getBean(session.getPid());
        Friend friend = friendDao.getBean(fId);
        my.removeFriend(fId);
        friend.removeFriend(my.getId());
        friendDao.saveBean(my);
        friendDao.saveBean(friend);

        Player myPlayer = playerDao.getBean(my.getId());
        Player friendPlayer = playerDao.getBean(fId);
        String msg = message.getMessage("log.event5", new Object[]{DateUtil.getDateFormat(new Date(),null),friendPlayer.getNickName()}, Locale.CHINA);
        eventLogDao.saveBean(myPlayer.getId(),eventLogBuilder.newFriendEvent(myPlayer.getId(),msg));

    }

    @RpcAnnotation(cmd="friend.add",req=IdReq.class,name="添加好友")
    public void addFriend(Session session,IdReq req) throws AlertException {
        String fId = req.getId();
        if (StringUtils.isBlank(fId)) {
            return ;
        }

        Friend my = friendDao.getBean(session.getPid());
        if (my.getId().equalsIgnoreCase(fId)){
            exceptionFactory.throwAlertException("friend.ismy");
        }
        // 检查是否已经存在好友
        if (my.isFriend(fId)) {
            exceptionFactory.throwAlertException("friend.isfriend");
        }

        Player myPlayer = playerDao.getBean(session.getPid());
        Friend friend = friendDao.getBean(fId);
        my.invite(fId);
        friend.addUnknownsFriend(new FriendItem(myPlayer.getId(), new Date()));
        friendDao.saveBean(my);
        friendDao.saveBean(friend);
        Session s = statusService.getSession(session, fId);
        if (s != null) {
            //如果好友已登录存在session才需要广播
            frontendService.broadcast(new Session[]{s}, new FriendAuditVO(myPlayer.getId(), myPlayer.getNickName(),RspFactory.getUserPortrait(myPlayer.getUserId())));
            //通知对方有新消息
            frontendService.broadcast(new Session[]{s}, new MsgVO(EventConstant.EVENT_EMAIL,1));
        }
    }

	@RpcAnnotation(cmd="friend.apply",vo=Boolean.class,req=String.class,name="是否有好友需要审核")
	public boolean applyFriend(Session session,String pid){
        Friend my = friendDao.getBean(pid);
        boolean audit = ! my.getUnknownsFriends().isEmpty();
		return audit;
	}

    @RpcAnnotation(cmd="friend.get.audit",vo=FriendAuditVO[].class,req=String.class,name="取得需要审核的申请列表")
    public FriendAuditVO[] getAuditFriend(Session session,String pid){
        Friend my = friendDao.getBean(pid);
        FriendItem[] items = my.getUnknownsFriends().values().toArray(new FriendItem[0]);
        FriendAuditVO[] list = new FriendAuditVO[items.length];
        for(int i=0;i<items.length;i++){
            Player p = playerDao.getBean(items[i].getFid());
            list[i] = new FriendAuditVO(p.getId(),p.getNickName(),RspFactory.getUserPortrait(p.getUserId()));
        }
        return list;
    }

    @RpcAnnotation(cmd="friend.top",vo=void.class,req=IdReq.class,name="好友顶置")
    public void topFriend(Session session,IdReq req){
        String fId = req.getId();
        Friend my = friendDao.getBean(session.getPid());
        if(my.getFriends().containsKey(fId)){
            FriendItem fi = my.getFriends().get(fId);
            fi.setSort3(1);
            friendDao.saveBean(my);
        }
    }

    @RpcAnnotation(cmd="friend.cancel.top",vo=void.class,req=IdReq.class,name="取消好友顶置")
    public void cancelTopFriend(Session session,IdReq req){
        String fId = req.getId();
        Friend my = friendDao.getBean(session.getPid());
        if(my.getFriends().containsKey(fId)){
            FriendItem fi = my.getFriends().get(fId);
            fi.setSort3(0);
            friendDao.saveBean(my);
        }
    }

}
