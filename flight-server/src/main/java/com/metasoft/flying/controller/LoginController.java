package com.metasoft.flying.controller;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.metasoft.flying.model.User;
import com.metasoft.flying.model.UserDataPersist;
import com.metasoft.flying.model.UserPersist;
import com.metasoft.flying.model.VoFactory;
import com.metasoft.flying.model.constant.ErrorCodes;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.net.BaseConnection;
import com.metasoft.flying.net.annotation.HandlerAnno;
import com.metasoft.flying.service.MatchService;
import com.metasoft.flying.service.StaticDataService;
import com.metasoft.flying.service.UserPersistService;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.LocalizationService;
import com.metasoft.flying.service.net.ConnectionService;
import com.metasoft.flying.util.RandomUtils;
import com.metasoft.flying.util.RequestUtils;
import com.metasoft.flying.util.ValidateUtils;
import com.metasoft.flying.vo.LoginRequest;
import com.metasoft.flying.vo.LoginVO;
import com.metasoft.flying.vo.UserAvatarRequest;
import com.metasoft.flying.vo.UserVO;
import com.qianxun.model.Session;
import com.qianxun.service.SessionService;

@Controller
public class LoginController implements GeneralController {

	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private ConnectionService connectionService;
	@Autowired
	private LocalizationService localizationService;
	@Autowired
	private UserPersistService userPersistenceService;
	@Autowired
	private StaticDataService staticDataService;
	@Autowired
	private MatchService matchService;	
	@Autowired
	private StaticDataController staticDataController;
	@Autowired
	private ItemController itemController;
	@Autowired
	private RelationController relationController;
	@Autowired
	private SessionService sessionService;

	@HandlerAnno(name = "从网站登录", cmd = "login.web", req = LoginRequest.class)
	public LoginVO web(LoginRequest req) throws GeneralException {
		Session session = sessionService.get(req.getName());// get code
		if (null == session) {
			throw new GeneralException(ErrorCodes.LOGIN_PLEASE, "login.please");
		}
		long userId = Long.valueOf(session.getUid());
		String name = session.getUsername();
		User user = null;
		try {
			user = userService.getAnyUserById(userId);
		} catch (Exception e) {
			log.debug("new user id:{}, name:{}", userId, name);
			UserPersist up = new UserPersist(userId, name, "");
			up.setCreatedate(System.currentTimeMillis());
			userPersistenceService.save(userId, up);
			user = userService.getAnyUserById(userId);
		}
		userService.onSignIn(user);

		LoginVO vo = new LoginVO();
		vo.setUser(VoFactory.getUserVO(user));
		vo.setItemDatas(staticDataController.item(null));
		vo.setItems(itemController.itemInfo(null));
		vo.setRelation(relationController.relationInfo(null));
		vo.setArenaList(VoFactory.getArenaVOList(staticDataService.arenaMap));
		return vo;
	}

	@HandlerAnno(name = "登录", cmd = "login.login", req = LoginRequest.class)
	public LoginVO login(LoginRequest req) throws GeneralException {
		
		if(System.getProperty("debug") == null){
			throw new GeneralException(0, "debug.mode.off");
		}

		// db query
		UserPersist up = userPersistenceService.getByName(req.getName());
		if (null == up) {
			// throw new GeneralException(0,"name.not.found");

			up = new UserPersist(0, req.getName(), "");
			// FIXME
			//up.setRose(1000);
			up.setCreatedate(System.currentTimeMillis());
			up.setId(userPersistenceService.save(up));
		}
		User user = userService.getAnyUserById(up.getId());
		userService.onSignIn(user);

		LoginVO vo = new LoginVO();
		vo.setUser(VoFactory.getUserVO(user));
		vo.setItemDatas(staticDataController.item(null));
		vo.setItems(itemController.itemInfo(null));
		vo.setRelation(relationController.relationInfo(null));
		vo.setArenaList(VoFactory.getArenaVOList(staticDataService.arenaMap));
		return vo;
	}

	@HandlerAnno(name = "登出", cmd = "login.logout", req = LoginRequest.class)
	public void logout(LoginRequest req) {
		BaseConnection conn = RequestUtils.getCurrentConn();
		userService.onSignout(conn.getSessionId());
	}

	@HandlerAnno(name = "注册", cmd = "login.regist", req = LoginRequest.class)
	public void regist(LoginRequest req) throws GeneralException {
		UserPersist up = userPersistenceService.getByName(req.getName());
		if (null == up) {
			// db persistence
			UserPersist user = new UserPersist(0, req.getName(), "");
			userPersistenceService.save(user);
		} else {
			throw new GeneralException(0, "name.already.existed");
		}
	}

	@HandlerAnno(name = "改变信息", cmd = "login.change", req = LoginRequest.class)
	public UserVO change(LoginRequest req) throws GeneralException {
		User user = userService.getRequestUser();
		if (user == null) {
			throw new GeneralException(0, "invalid.userId");
		}
		List<UserPersist> list = userPersistenceService.getByNickName(req.getName());
		if (list.size() > 0) {
			throw new GeneralException(0, "invalid.nickname");
		}
		
		ValidateUtils.checkTalkKeyword(req.getName());
		
		UserPersist up = user.getUserPersist();
		up.setNickname(req.getName());
		up.setGender(req.getGender());

		userPersistenceService.update(up);

		return VoFactory.getUserVO(user);
	}

	@HandlerAnno(name = "玩家信息", cmd = "login.info", req = LoginRequest.class)
	public UserVO info(LoginRequest req) throws GeneralException {
		User user = userService.getAnyUserById(req.getId());
		return VoFactory.getUserVO(user);
	}

	@HandlerAnno(name = "新手引导设置,return (up.getGuide() | req.getId())", cmd = "login.guide", req = LoginRequest.class)
	public Long guide(LoginRequest req) throws GeneralException {
		User user = userService.getRequestUser();
		UserDataPersist ud = user.getUserDataPersist();
		//up.setGuide(up.getGuide() ^ req.getId());
		ud.setGuide(req.getId());
		user.updateData = true;
		return ud.getGuide();
	}

	@HandlerAnno(name = "修改头像", cmd = "avatar.change", req = UserAvatarRequest.class)
	public void upload(UserAvatarRequest req) throws GeneralException, IOException {
		User self = userService.getRequestUser();
		self.getUserPersist().setAvatar(String.valueOf(RandomUtils.nextInt(1000)));
		self.updateData = true;
		//logger.debug("data size:{}",req.getData().length);
		//FileUtils.writeByteArrayToFile(new File(avatar), req.getData());
	}
//	@HandlerAnno(name = "取得头像", cmd = "avatar.aquire", req = GeneralRequest.class)
//	public byte[] aquire(GeneralRequest req) throws GeneralException, IOException {
//		User self = userService.getRequestUser();
//		return FileUtils.readFileToByteArray(new File(self.getUserPersist().getAvatar()));
//	}
	// //Localization
	// //String content = message.getMessage("foobar.start", new
	// String[]{":1号伺服器"}, Locale.CHINA);
	// //System.out.println(content);
}
