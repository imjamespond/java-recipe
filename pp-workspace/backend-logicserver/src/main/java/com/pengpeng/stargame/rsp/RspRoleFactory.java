package com.pengpeng.stargame.rsp;

import com.pengpeng.stargame.farm.container.IFarmLevelRuleContainer;
import com.pengpeng.stargame.farm.dao.IFarmPlayerDao;
import com.pengpeng.stargame.model.farm.FarmPlayer;
import com.pengpeng.stargame.model.player.*;
import com.pengpeng.stargame.model.piazza.Family;
import com.pengpeng.stargame.model.piazza.FamilyMemberInfo;
import com.pengpeng.stargame.model.room.FashionPlayer;
import com.pengpeng.stargame.model.room.RoomPlayer;
import com.pengpeng.stargame.piazza.container.IFamilyRuleContainer;
import com.pengpeng.stargame.piazza.dao.IFamilyDao;
import com.pengpeng.stargame.piazza.dao.IFamilyMemberInfoDao;
import com.pengpeng.stargame.piazza.rule.FamilyRule;
import com.pengpeng.stargame.player.dao.IOtherPlayerDao;
import com.pengpeng.stargame.player.dao.IScenePlayerDao;
import com.pengpeng.stargame.task.TaskConstants;
import com.pengpeng.stargame.task.container.ITaskRuleContainer;
import com.pengpeng.stargame.util.DateUtil;
import com.pengpeng.stargame.vip.container.IPayMemberRuleContainer;
import com.pengpeng.stargame.vip.dao.IVipDao;
import com.pengpeng.stargame.vo.chat.ChatVO;
import com.pengpeng.stargame.vo.role.FriendVO;
import com.pengpeng.stargame.vo.role.PlayerInfoVO;
import com.pengpeng.stargame.vo.role.PlayerVO;
import com.pengpeng.stargame.vo.role.album.AlbumItemPageVO;
import com.pengpeng.stargame.vo.role.album.AlbumItemVO;
import com.pengpeng.stargame.vo.role.album.AlbumPageVO;
import com.pengpeng.stargame.vo.role.album.AlbumVO;
import com.pengpeng.user.AlbumProfile;
import com.pengpeng.user.UserProfile;
import com.pengpeng.user.service.AlbumItemProfile;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 玩家
 * @author jinli.yuan@pengpeng.com
 * @since 13-4-25 上午10:16
 */
@Component()
public class RspRoleFactory extends RspFactory {

    @Autowired
    private IFamilyRuleContainer familyRuleContainer;
    @Autowired
    private IFarmPlayerDao farmPlayerDao;
    @Autowired
    private IFarmLevelRuleContainer farmLevelRuleContainer;

    @Autowired
    private IFamilyMemberInfoDao familyMemberInfoDao;

    @Autowired
    private IScenePlayerDao scenePlayerDao;

    @Autowired
    private MessageSource message;

    @Autowired
    private IOtherPlayerDao otherPlayerDao;

    @Autowired
    private ITaskRuleContainer taskRuleContainer;
    @Autowired
    private IVipDao vipDao;

    @Autowired
    private IFamilyDao familyDao;
    public PlayerVO newPlayerVO(Player player) {
        PlayerVO vo = new PlayerVO();
        //BeanUtils.copyProperties(player, vo);//不建议采用
        //vo.setCharm();
        //vo.setGift();
        vo.setId(player.getId());
        //vo.setIntegral();
        vo.setNickName(player.getNickName());
        vo.setSex(player.getSex());
        vo.setType(player.getType());
        vo.setUserId(player.getUserId());
        //vo.setVip();
        vo.setRmb(player.getGoldCoin());
        vo.setGold(player.getGameCoin());
        vo.setPortrait(getUserPortrait(player.getUserId()));
        String sceneId = scenePlayerDao.getBean(player.getId()).getSceneId();
        vo.setSceneId(sceneId);
        Family family = familyDao.getBeanByStarId(player.getStarId());
        FamilyMemberInfo memberInfo = familyMemberInfoDao.getFamilyMember(player.getId());
        boolean isMember = familyRuleContainer.isMember(family,memberInfo);

        FamilyRule rule = familyRuleContainer.getRuleByStarId(player.getStarId());
        if(rule!=null){
            vo.setStarIcon(rule.getStarIcon());
        }else {
            vo.setStarIcon("1");
        }
        if (isMember){
            vo.setFamilyId(family.getId());
            vo.setfName(family.getName());
        }
        vo.setFarmLevel(farmPlayerDao.getFarmLevel(player.getId()));
        vo.setMaxGold(farmLevelRuleContainer.getElement(farmPlayerDao.getFarmLevel(player.getId())).getMaxGold());
        OtherPlayer op = otherPlayerDao.getBean(player.getId());
        vo.setClaim(op.isClaim());

        vo.setVip(vipDao.getPlayerVip(player.getId()).getViP());
        return  vo;
    }


    public PlayerInfoVO newPlayerInfoVO(Player player,PlayerInfo playerInfo,Family family,FamilyMemberInfo familyMemberInfo,FarmPlayer farmPlayer,FashionPlayer fashionPlayer,RoomPlayer roomPlayer) {
        PlayerInfoVO vo = new PlayerInfoVO();

        vo.setId(player.getId());
        vo.setUserId(player.getUserId());

        //属性信息
        vo.setPortrait(getUserPortrait(player.getUserId()));
        vo.setNickName(player.getNickName());
        vo.setSex(player.getSex());
        vo.setFamily(family.getName());
        vo.setFamilyIdentity(familyMemberInfo.getIdentity());
        vo.setTitle(playerInfo.getTitle());
        vo.setFarmLevel(farmPlayer.getLevel());
        vo.setFashionIndex(taskRuleContainer.getNumById(player.getId(),TaskConstants.CONDI_TYPE_FASHION_5,null));
        vo.setRoomIndex(taskRuleContainer.getNumById(player.getId(),TaskConstants.CONDI_TYPE_ROOM_4,null));
        vo.setGameRank(null);

        //个人信息
        vo.setPayMember(player.getPayMember());
        if(playerInfo.getBirth()!=null)
        vo.setBirth(playerInfo.getBirth().getTime());
        vo.setProvince(playerInfo.getProvince());
        vo.setCity(playerInfo.getCity());
        vo.setSignature(playerInfo.getSignature());

        //相册
        vo.setAlbum(null);

        return  vo;
    }

    public AlbumPageVO getAlbumPageVO(List<AlbumProfile> albumProfileList,int maxSize,int pageNo){
        AlbumPageVO albumPageVO=new AlbumPageVO();
        int pageSize=6;
        if(pageNo==0){
            pageNo=1;
        }
        albumPageVO.setPageNo(pageNo);
        int maxPage= maxSize%pageSize>0?maxSize/pageSize+1:maxSize/pageSize;
        albumPageVO.setMaxPage(maxPage);
        List<AlbumVO> albumVOList=new ArrayList<AlbumVO>();
        for(AlbumProfile albumProfile:albumProfileList) {
            AlbumVO albumVO=new AlbumVO();
            BeanUtils.copyProperties(albumProfile,albumPageVO);
            albumVOList.add(albumVO);
        }
        albumPageVO.setAlbumVOs(albumProfileList.toArray(new AlbumVO[0]));
        return albumPageVO;
    }

    public AlbumItemPageVO getAlbumPageVO(List<AlbumItemProfile> AlbumItemPageVO,int maxSize,int pageNo){
        AlbumItemPageVO albumPageVO=new AlbumItemPageVO();
        int pageSize=4;
        if(pageNo==0){
            pageNo=1;
        }
        albumPageVO.setPageNo(pageNo);
        int maxPage= maxSize%pageSize>0?maxSize/pageSize+1:maxSize/pageSize;
        albumPageVO.setMaxPage(maxPage);
        List<AlbumItemVO> albumVOList=new ArrayList<AlbumItemVO>();
        for(AlbumItemProfile albumProfile:AlbumItemPageVO) {
            AlbumItemVO albumItemVO=new AlbumItemVO();
            BeanUtils.copyProperties(albumProfile,albumItemVO);
            albumVOList.add(albumItemVO);
        }
        albumPageVO.setAlbumVOs(albumVOList.toArray(new AlbumItemVO[0]));
        return albumPageVO;
    }


    public FriendVO newFriendVO(Player p,FriendItem friendItem){
        FriendVO vo = new FriendVO();
        vo.setNickName(p.getNickName());
        vo.setSex(p.getSex());
        vo.setId(p.getId());
        vo.setUserId(p.getUserId());
        vo.setPortrait(getUserPortrait(p.getUserId()));
        vo.setTop(friendItem.getSort3());
        vo.setInactive(friendItem.getSort2());
        return vo;
    }
    public FriendVO newFriendVO(Player p){
        FriendVO vo = new FriendVO();
        vo.setSex(p.getSex());
        vo.setId(p.getId());
        vo.setNickName(p.getNickName());
        vo.setUserId(p.getUserId());
        vo.setPortrait(getUserPortrait(p.getUserId()));
        return vo;
    }
    public List<UserProfile> filterUserprofile(List<UserProfile> listUser,Set<Integer> uids){
        List<UserProfile> list = new ArrayList<UserProfile>();
        for(UserProfile userProfile : listUser){
            if (uids!=null&&uids.contains(userProfile.getId())){
                continue;
            }
            list.add(userProfile);
        }
        return list;
    }
	public FriendVO[] getPPListFriendToArr(List<UserProfile> listUser){
		List<FriendVO> list = new ArrayList<FriendVO>();
		if(listUser ==null || listUser.isEmpty()){
			return null;
		}
		for(UserProfile userProfile : listUser){
			FriendVO vo = new FriendVO();
			vo.setUserId(userProfile.getId());
			vo.setNickName(userProfile.getNickName());
			vo.setSex(userProfile.getSex());
			vo.setPortrait(getUserPortrait(userProfile.getId()));
			list.add(vo);
		}
		return list.toArray(new FriendVO[0]);
	}

    public ChatVO newFamilyChat(String pid,String name,String msg){
        //家族方面的聊天
        return new ChatVO("fans",pid,name,msg);
    }

    public ChatVO newWorldChat(String pid,String name,String msg){
        //综合方面的聊天
        return new ChatVO("world",pid,name,msg);
    }

    public ChatVO newShoutChat(String pid,String name,String msg){
        //千里传音
        return new ChatVO("shout",pid,name,msg);
    }

    public ChatVO newTalkChat(String pid,String name,String msg){
        //普通说话
        return new ChatVO(null,pid,name,msg);
    }

    public FriendVO newQinMaFriendVO(FriendItem friend) {
        FriendVO vo = new FriendVO(friend.getFid(),message.getMessage("pp.qinma",null, Locale.CHINA));
        return vo;
    }

    public List<String> newFriendIds(Friend friend) {
        List<String> ids = new ArrayList<String>();
        Map<String,FriendItem> map = friend.getFriends();
        for(FriendItem item:map.values()){
            ids.add(item.getFid());
        }
        return ids;
    }

    /**
     * 取得当天加过好友的名单
     * @param friend
     * @return
     */
    public List<String> newUnknownsFriendIds(Friend friend){
        List<String> ids = new ArrayList<String>();
        Map<String,FriendItem> map = friend.getUnknownsFriends();
        Date now = new Date();
        for(FriendItem item:map.values()){
            if (item.getCreateTime()!=null&& DateUtil.getDayOfYear(item.getCreateTime())==DateUtil.getDayOfYear(now)){
                ids.add(item.getFid());
            }
        }
        return ids;
    }

    public List<String> getInviteFriendsIds(Friend friend){
        List<String> ids = new ArrayList<String>();
        Map<String,FriendItem> map = friend.getInviteFriends();
        Date now = new Date();
        for(FriendItem item:map.values()){
            if (item.getCreateTime()!=null&& DateUtil.getDayOfYear(item.getCreateTime())==DateUtil.getDayOfYear(now)){
                ids.add(item.getFid());
            }
        }
        return ids;
    }

}
