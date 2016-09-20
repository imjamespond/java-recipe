package com.metasoft.empire.controller;

import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.metasoft.empire.common.GeneralController;
import com.metasoft.empire.common.GeneralException;
import com.metasoft.empire.common.GeneralResponse;
import com.metasoft.empire.common.annotation.HandlerAnno;
import com.metasoft.empire.model.User;
import com.metasoft.empire.model.UserPersist;
import com.metasoft.empire.model.UserUpgrade;
import com.metasoft.empire.model.data.RoleData;
import com.metasoft.empire.net.NettyConnection;
import com.metasoft.empire.service.ConnectionService;
import com.metasoft.empire.service.RankService;
import com.metasoft.empire.service.StaticDataService;
import com.metasoft.empire.service.UserPersistService;
import com.metasoft.empire.service.UserService;
import com.metasoft.empire.service.common.LocalizationService;
import com.metasoft.empire.utils.HashUtils;
import com.metasoft.empire.utils.RandomUtils;
import com.metasoft.empire.utils.RequestUtils;
import com.metasoft.empire.utils.RsaUtils;
import com.metasoft.empire.utils.ValidateUtils;
import com.metasoft.empire.vo.ChatListVO;
import com.metasoft.empire.vo.ChatReqFangPiao;
import com.metasoft.empire.vo.ChatRequest;
import com.metasoft.empire.vo.ExchangeReq;
import com.metasoft.empire.vo.LoginRequest;
import com.metasoft.empire.vo.LoginVO;
import com.metasoft.empire.vo.RankRequest;
import com.metasoft.empire.vo.RankVO;
import com.metasoft.empire.vo.RecieveVO;
import com.metasoft.empire.vo.RedeemRecvReq;
import com.metasoft.empire.vo.UpgradeRequest;
import com.metasoft.empire.vo.UpgradeVO;
import com.metasoft.empire.vo.VoidRequest;

@Controller
public class UserController implements GeneralController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private ConnectionService connectionService;
	@Autowired
	private LocalizationService localizationService;
	@Autowired
	private UserPersistService userPersistService;
	@Autowired
	private RankService rankService;
	@Autowired
	private UserService userService;
	@Autowired
	private StaticDataService sdService;

	@HandlerAnno(name = "Ping", cmd = "UserPing", req = VoidRequest.class)
	public void ping(VoidRequest req) {
		NettyConnection conn = (NettyConnection) RequestUtils.getCurrentConn();
		conn.markNow();
	}
	
	@HandlerAnno(name = "登录", cmd = "UserLogin", req = LoginRequest.class)
	public LoginVO login(LoginRequest req) throws GeneralException {
		// db query
		UserPersist up = userPersistService.getByName(req.getUsername());
		if (null == up) {
			throw new GeneralException(0, "invalid.user");
		}
		String shaPasswd = HashUtils.sha256(up.getPasswd());
		if(!shaPasswd.equals(req.getPassword())){
			throw new GeneralException(0, "invalid.passwd");
		}
		User user = userService.getAnyUserById(up.getId());
		userService.onSignIn(user);
		
		return LoginVO.getLoginVO(user);
	}

	@HandlerAnno(name = "新建", cmd = "UserCreate", req = LoginRequest.class)
	public LoginVO create(LoginRequest req) throws GeneralException, NoSuchAlgorithmException {
		ValidateUtils.checkTalkKeyword(req.getUsername());
		
		User user = null;
		UserPersist up = userPersistService.getByName(req.getUsername());
		if (null == up) {
			
			byte[] rsaBytes = DatatypeConverter.parseBase64Binary(req.getPassword());
			String password = RsaUtils.decryptWithPkcs8(rsaBytes, RsaUtils.privKey);
			log.debug("password:{}", password);
			
			// db persistence
			up = new UserPersist(0, req.getUsername(), "");
			up.setPasswd(password);
			up.setImei(req.getDeviceId());
			long uid = userPersistService.save(up);
			
			user = userService.getAnyUserById(uid);
			userService.onSignIn(user);
		} else {
			throw new GeneralException(0, "name.already.existed");
		}
		
		//default 4 role
		Map<Integer, UserUpgrade> map = user.getUpgradeMap();
		int roleid = 0;
		roleid = 1+RandomUtils.nextInt(4)*4;
		map.put(roleid, new UserUpgrade(roleid, 0, 0, 0));
		roleid = 2+RandomUtils.nextInt(4)*4;
		map.put(roleid, new UserUpgrade(roleid, 0, 0, 0));
		roleid = 3+RandomUtils.nextInt(4)*4;
		map.put(roleid, new UserUpgrade(roleid, 0, 0, 0));
		roleid = 4+RandomUtils.nextInt(4)*4;
		map.put(roleid, new UserUpgrade(roleid, 0, 0, 0));
		user.updateData = true;

		return LoginVO.getLoginVO(user);
	}

	@HandlerAnno(name = "改变信息", cmd = "UserChange", req = LoginRequest.class)
	public void change(LoginRequest req) throws GeneralException {
		User user = userService.getRequestUser();

		UserPersist up = user.getUserPersist();
		if(req.getUsername()!=null){
			ValidateUtils.checkTalkKeyword(req.getUsername());
			up.setUsername(req.getUsername());
		}
		if(req.getPassword()!=null){
			byte[] rsaBytes = DatatypeConverter.parseBase64Binary(req.getPassword());
			String password = RsaUtils.decryptWithPkcs8(rsaBytes, RsaUtils.privKey);
			log.debug("password:{}", password);
			
			up.setPasswd(password);
		}
		
		//up.setGender(req.getGender());
		userPersistService.update(up);

		//return VoFactory.getUserVO(user);
	}
	
	@HandlerAnno(name = "排行", cmd = "UserRank", req = RankRequest.class)
	public List<RankVO> rank(RankRequest req) throws GeneralException {
		return rankService.rankList;
	}
	@HandlerAnno(name = "赎罪排行", cmd = "UserRankRedeem", req = RankRequest.class)
	public List<RankVO> rankRedeem(RankRequest req) throws GeneralException {
		return rankService.rankRedeemList;
	}
	@HandlerAnno(name = "升级", cmd = "UserUpgrade", req = UpgradeRequest.class)
	public UpgradeVO upgrade(UpgradeRequest req) throws GeneralException {
		User user = userService.getRequestUser();
		UserUpgrade uu = user.getUpgrade(req.getId());
		if(!uu.upgrade()){
			throw new GeneralException(0, "upgrade.fails");
		}
		user.updateData=true;
		
		return new UpgradeVO(req.getId(), uu.getNumber(), uu.getUpgrade(), uu.getLevel(),
				uu.getAttack(), uu.getHp());
	}
	
	@HandlerAnno(name = "重置升级", cmd = "UserReset", req = VoidRequest.class)
	public void reupgrade(VoidRequest req) throws GeneralException {
		
	}
	
	@HandlerAnno(name = "定时领取", cmd = "UserRecieve", req = VoidRequest.class)
	public List<RecieveVO> recieve(VoidRequest req) throws GeneralException {
		User user = userService.getRequestUser(); 
		List<RecieveVO> list = new ArrayList<>(2);
		RecieveVO vo = null;
		int random = 0;
		for(int i=0; i<2; i++){
			if(random<50){
				int randomid = RandomUtils.nextInt(1, 16);
				int randomnum = RandomUtils.nextInt(1, 2);
				if(null!=vo&&vo.getId()==randomid)
					continue;
				vo = new RecieveVO();
				vo.setId(randomid);
				vo.setNum(randomnum);
				UserUpgrade uu = user.getUpgrade(randomid);
				uu.setNumber(uu.getNumber()+randomnum);
				uu.updateLevel();
				vo.setAmount(uu.getNumber());
				vo.setLevel(uu.getLevel());
				list.add(vo);
			}
			random = RandomUtils.nextInt(100);
		}
		user.updateData=true;
		return list;
	}
	//FIXME clear map in some time 
	private HashMap<String, ChatReqFangPiao> redeemMap = new HashMap<String, ChatReqFangPiao>();
	private SecureRandom random = new SecureRandom();
	@HandlerAnno(name = "放漂", cmd = "GameFangPiao", req = ChatReqFangPiao.class)
	public UpgradeVO fangPiao(ChatReqFangPiao req) throws GeneralException {
		User user = userService.getRequestUser();
		UserUpgrade uu = user.getUpgradeMap().get(req.getRoleid());
		if(null==uu){
			throw(new GeneralException(0, "invalid.data"));
		}else if(uu.getNumber()<req.getNum()){
			throw(new GeneralException(0, "insufficient.card"));
		}
		uu.setNumber(uu.getNumber() - req.getNum());
		user.addRedeem(req.getNum());
		RoleData rd = sdService.roleMap.get(req.getRoleid());
		String msg = localizationService.getLocalString("game.fangpiao", 
				new String[]{user.getUserPersist().getUsername(), rd.getName()});
		req.setMsg(msg);
		req.setType(4);
		String key = new BigInteger(130, random).toString(32);;
		req.setKey(key);
		redeemMap.put(key, req);
		addChat(req);
		
		return new UpgradeVO(req.getRoleid(), uu.getNumber(), uu.getUpgrade(), uu.getLevel(),
				uu.getAttack(), uu.getHp());
	}
	@HandlerAnno(name = "领取放漂", cmd = "GameRecvFangPiao", req = RedeemRecvReq.class)
	public UpgradeVO recvFangPiao(RedeemRecvReq req) throws GeneralException {
		User user = userService.getRequestUser();
		ChatReqFangPiao fangpiao = redeemMap.remove(req.getKey());
		if(null != fangpiao && fangpiao instanceof ChatReqFangPiao){
			fangpiao.setNum(0);
			UserUpgrade uu = user.getUpgrade(fangpiao.getRoleid());
			uu.setNumber(uu.getNumber()+fangpiao.getNum());
			return new UpgradeVO(fangpiao.getRoleid(), uu.getNumber(), uu.getUpgrade(), uu.getLevel(),
					uu.getAttack(), uu.getHp());
		}
		throw new GeneralException(0, "redeem.recieved");
	}
	@HandlerAnno(name = "兑换", cmd = "UserExchange", req = ExchangeReq.class)
	public List<RecieveVO> exchange(ExchangeReq req) throws GeneralException {
		User user = userService.getRequestUser(); 
		if(!user.decrScore(req.getScore())){
			throw new GeneralException(0, "insuffient.score");
		}
		int type = 1;
		int all = 3;
		switch(req.getScore()){
		case 300:
			type = 1;
			all=15;
			break;
		case 500:
			type = 1;
			break;
		case 800:
			type = 5;
			break;
		case 1000:
			type = 9;
			break;
		case 1500:
			type = 13;
			break;
		}
		
		List<RecieveVO> list = new ArrayList<>(2);
		RecieveVO vo = null;
		int random = 0;
		for(int i=0; i<2; i++){
			if(random<50){
				int randomid = RandomUtils.nextInt(type, type+all);
				int randomnum = RandomUtils.nextInt(1, 2);
				if(null!=vo&&vo.getId()==randomid)
					continue;
				vo = new RecieveVO();
				vo.setId(randomid);
				vo.setNum(randomnum);
				UserUpgrade uu = user.getUpgrade(randomid);
				uu.setNumber(uu.getNumber()+randomnum);
				uu.updateLevel();
				vo.setAmount(uu.getNumber());
				vo.setLevel(uu.getLevel());
				list.add(vo);
			}
			random = RandomUtils.nextInt(100);
		}
		user.updateData=true;
		return list;
	}

	private int chatSize = 4;
	private int chatId = 0;
	private ChatRequest[] chatDeq = new ChatRequest[chatSize];
	
	@HandlerAnno(name = "聊天", cmd = "GameChat", req = ChatRequest.class)
	public void chat(ChatRequest req) throws GeneralException {
		addChat(req);
	}
	@HandlerAnno(name = "聊天列表", cmd = "GameChatList", req = VoidRequest.class)
	public ChatListVO chatList(VoidRequest req) throws GeneralException {
		ChatListVO list = new ChatListVO();
		list.setList(chatDeq);
		return list;
	}
	public void addChat(ChatRequest req) throws GeneralException{
		User user = userService.getRequestUser();
		req.setUid(user.getId());
		req.setName(user.getUserPersist().getUsername());
		req.setMsg(ValidateUtils.replaceTalkKeyword(req.getMsg()));
		connectionService.broadcast(GeneralResponse.newJsonObj(req));
		
		synchronized(chatDeq){
			chatDeq[chatId] = req;
			req.setId(chatId);
			chatId=++chatId&(chatSize-1);
		}
	}
}
