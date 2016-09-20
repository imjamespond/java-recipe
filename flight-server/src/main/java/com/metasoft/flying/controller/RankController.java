package com.metasoft.flying.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.metasoft.flying.model.FlightRp;
import com.metasoft.flying.model.IPage;
import com.metasoft.flying.model.User;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.net.annotation.HandlerAnno;
import com.metasoft.flying.service.MatchService;
import com.metasoft.flying.service.RankService;
import com.metasoft.flying.service.UserPersistService;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.vo.MatchPlayerListVO;
import com.metasoft.flying.vo.MatchPlayerVO;
import com.metasoft.flying.vo.PlayerGoldListVO;
import com.metasoft.flying.vo.PlayerPveListVO;
import com.metasoft.flying.vo.PlayerScoreListVO;
import com.metasoft.flying.vo.RankRequest;
import com.metasoft.flying.vo.RoomListVO;
import com.metasoft.flying.vo.RpRankVO;
import com.metasoft.flying.vo.general.GeneralRequest;

@Controller
public class RankController implements GeneralController {

	@Autowired
	private UserService userService;
	@Autowired
	private RankService rankService;
	@Autowired
	private MatchService matchService;
	@Autowired
	private UserPersistService userPsService;

	/**
	 * @param rankList
	 * @param rankMap
	 * @param req
	 * @param self
	 * @param type
	 *            1必须在线,2必须在房间
	 * @return
	private RankUserListVO getRankUserListVO(List<RankUserVO> rankList, Map<Long, Integer> rankMap, RankRequest req,
			User self, int type) {
		int count = 0;
		int offset = req.getOffset() * req.getSize();
		List<RankUserVO> listVO = new ArrayList<RankUserVO>(req.getSize());
		while (count < req.getSize()) {
			int index = offset + count++;
			if (index < rankList.size()) {
				
				RankUserVO vo = new RankUserVO();
				RankUserVO vo_ = rankList.get(index);
				// 取在线玩家
				User user = userService.getOnlineUserById(vo_.getUserId());
		
				if ((type & 1) == 1) {
					if (null == user) {
						continue;
					}
				}
				if ((type & 3) == 3) {
					if (null == user.getGroup()) {
						continue;
					} else if (Long.valueOf(user.getGroup()) != user.getId()) {
						continue;
					}
				}
				if (null != user) {
					vo.setOnline(1);
					if (null != user.getGroup()) {
						if (Integer.valueOf(user.getGroup()) == user.getId()) {
							vo.setOnline(2);
						}
					}
				}

				vo.setUserId(vo_.getUserId());
				vo.setUserName(vo_.getUserName());
				vo.setRose(vo_.getRose());
				vo.setContribute(vo_.getContribute());
				// 是否关注
				UserFollow uf = self.getFollowingMap().get(vo_.getUserId());
				if (uf != null) {
					if (uf.getState() == 0) {
						vo.setFollow(1);
					}
				}
				if(null!=vo_)
				listVO.add(vo);
			}
			
		}

		int size = rankList.size();
		int amount = size / req.getSize() + (size % req.getSize() > 0 ? 1 : 0);
		RankUserListVO vo = new RankUserListVO(amount, 0, listVO);
		Integer rank = rankMap.get(self.getId());
		if (null == rank) {
			vo.setMyRank(0);
		} else {
			vo.setMyRank(rank);
		}
		vo.setPage(req.getOffset());
		return vo;
	}
	
	@HandlerAnno(name = "女玩家上周榜", cmd = "rank.last.week", req = RankRequest.class)
	public RankUserListVO lastWeek(RankRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		return getRankUserListVO(rankService.girlWeekList, rankService.girlWeekMap, req, self, 0);
	}

	@HandlerAnno(name = "更多女玩家周榜", cmd = "rank.more.female", req = RankRequest.class)
	public RankUserListVO rankMoreFemale(RankRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		return getRankUserListVO(rankService.girlWeekList, rankService.girlWeekMap, req, self, 0);
	}

	@HandlerAnno(name = "女玩家总榜", cmd = "rank.all.female", req = RankRequest.class)
	public RankUserListVO rankAllFemale(RankRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		return getRankUserListVO(rankService.girlList, rankService.girlMap, req, self, 0);
	}

	@HandlerAnno(name = "玩家贡献值总榜", cmd = "rank.contribute", req = RankRequest.class)
	public RankUserListVO rankContribute(RankRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		return getRankUserListVO(rankService.contributeList, rankService.contributeMap, req, self, 0);
	}

	@HandlerAnno(name = "更多玩家贡献值总榜", cmd = "rank.more.contribute", req = RankRequest.class)
	public RankUserListVO rankMoreContribute(RankRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		return getRankUserListVO(rankService.contributeList, rankService.contributeMap, req, self, 0);
	}
	
	@HandlerAnno(name = "苹果活动榜", cmd = "rank.prize.apple", req = RankRequest.class)
	public RankUserListVO applePrize(RankRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		return getRankUserListVO(rankService.rankPrizeList, rankService.rankPrizeMap, req, self, 0);
	}
	 */
	@HandlerAnno(name = "比赛日榜", cmd = "rank.daily.match", req = RankRequest.class)
	public MatchPlayerListVO matchDaily(RankRequest req) throws GeneralException {	
		List<MatchPlayerVO> list = matchService.matchRankMap.get(req.getId());
		Map<Long,Integer> map = matchService.matchMyRankMap.get(req.getId());
		if(null==list||null==map){
			return null;
		}
		
		User self = userService.getRequestUser();
		MatchPlayerListVO vo = new MatchPlayerListVO();
		getListVO(vo, 0, list, req);
		Integer rank = map.get(self.getId());
		if (null != rank ) {
			vo.setMyRank(rank);
		}
		return vo;
	}

	@HandlerAnno(name = "比赛周榜", cmd = "rank.match.week", req = RankRequest.class)
	public MatchPlayerListVO weekMatch(RankRequest req) throws GeneralException {	
		User self = userService.getRequestUser();
		MatchPlayerListVO vo = new MatchPlayerListVO();
		getListVO(vo, 0, rankService.matchWeekRank, req);
		Integer rank = rankService.matchWeekRankMap.get(self.getId());
		if (null != rank) {
			vo.setMyRank(rank);
		}
		return vo;
	} 
	
	@HandlerAnno(name = "等级分数榜", cmd = "rank.score", req = RankRequest.class)
	public PlayerScoreListVO score(RankRequest req) throws GeneralException {	
		User self = userService.getRequestUser();
		PlayerScoreListVO vo = new PlayerScoreListVO();
		getListVO(vo, 0, rankService.expRank, req);
		Integer rank = rankService.expRankMap.get(self.getId());
		if (null != rank) {
			vo.setMyRank(rank);
		}
		return vo;
	}
	
	@HandlerAnno(name = "pve榜", cmd = "rank.pve", req = RankRequest.class)
	public PlayerPveListVO pve(RankRequest req) throws GeneralException {	
		User self = userService.getRequestUser();
		PlayerPveListVO vo = new PlayerPveListVO();
		getListVO(vo, 0, rankService.pveRank, req);
		Integer rank = rankService.pveRankMap.get(self.getId());
		if (null != rank) {
			vo.setMyRank(rank);
		}
		return vo;
	}
	@HandlerAnno(name = "财富榜 ", cmd = "rank.gold", req = RankRequest.class)
	public PlayerGoldListVO gold(RankRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		PlayerGoldListVO vo = new PlayerGoldListVO();
		getListVO(vo, 0, rankService.goldRank, req);
		Integer rank = rankService.goldRankMap.get(self.getId());
		if (null != rank) {
			vo.setMyRank(rank);
		}
		return vo;
	}
	
	@HandlerAnno(name = "房间列表 ", cmd = "chat.roomlist", req = RankRequest.class)
	public RoomListVO roomList(RankRequest req) {
		RoomListVO vo = new RoomListVO();
		getListVO2(vo, 0, rankService.roomList, req, rankService.roomAvailable);
		return vo;
	}
	
	@HandlerAnno(name = "RP王排行,返回30条 ", cmd = "rank.rp.today", req = RankRequest.class)
	public List<RpRankVO> rpToday(GeneralRequest req) throws GeneralException {
		return FlightRp.getRpRank(FlightRp.kRpRankToday);
	}
	@HandlerAnno(name = "RP王昨天排行,返回30条 ", cmd = "rank.rp.lastday", req = RankRequest.class)
	public List<RpRankVO> rpLastday(GeneralRequest req) throws GeneralException {
		return FlightRp.getRpRank(FlightRp.kRpRankLastday);
	}
	

	private <TPageItem> void getListVO(IPage<TPageItem> page, int rank, List<TPageItem> list, RankRequest req) {
		int count = 0;
		int offset = req.getOffset() * req.getSize();
		int size = list.size();
		while (count < req.getSize()) {
			int index = offset + count++;
			if (index < size) {
				page.addItem(list.get(index));
			}
		}
		int amount = size / req.getSize() + (size % req.getSize() > 0 ? 1 : 0);
		page.setPage(amount, req.getOffset());
	}
	private <TPageItem> void getListVO2(IPage<TPageItem> page, int rank, TPageItem[] list, RankRequest req, int num) {
		int count = 0;
		int offset = req.getOffset() * req.getSize();
		int size = num;
		while (count < req.getSize()) {
			int index = offset + count++;
			if (index < size&&list[index]!=null) {
				page.addItem(list[index]);
			}
		}
		int amount = size / req.getSize() + (size % req.getSize() > 0 ? 1 : 0);
		page.setPage(amount, req.getOffset());
	}
}
