package com.chitu.chess.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.gecko.broadcast.MultiGeneralController;
import cn.gecko.broadcast.RequestUtils;
import cn.gecko.broadcast.annotation.IncludeEnum;
import cn.gecko.broadcast.annotation.IncludeEnums;
import cn.gecko.commons.model.GeneralException;
import cn.gecko.commons.utils.IdUtils;
import com.chitu.chess.model.ChessErrorCodes;
import com.chitu.chess.model.ChessPlayer;
import com.chitu.chess.model.ChessRoomPlayer;
import com.chitu.chess.model.ChessUtils;
import com.chitu.chess.model.PersistChessPlayer;
import com.chitu.chess.model.ChessPlayer.Gender;
import com.chitu.chess.msg.ChessPlayerDto;
import com.chitu.chess.msg.ChessPlayerRegisterDto;
import com.chitu.chess.service.ChessPlayerManager;

@Service
@IncludeEnums({ @IncludeEnum(Gender.class) })
public class LoginController extends MultiGeneralController {

	@Autowired
	private ChessPlayerManager chessPlayerManager;

	/**
	 * 角色登录
	 * 
	 * @param id
	 * @param accountId
	 * @param ip
	 * @return
	 */
	public ChessPlayerDto login(String id, String accountId, String password, String ip) {
		
		PersistChessPlayer pp = PersistChessPlayer.getByAccountId(accountId);

		if (pp == null) {
			throw new GeneralException(ChessErrorCodes.ROLE_NOT_EXIST);
		} else {
			if(pp.getPassword().equals(ChessUtils.md5(password+pp.getSalt()))){
				pp.lastLoginTime = pp.getLoginTime();
				pp.setLoginTime(System.currentTimeMillis());
				pp.firstLogin = false;				
			}else{
				throw new GeneralException(ChessErrorCodes.WRONG_PASSWORD);
			}				
		}
		
		pp.setLoginCount(1);
		pp.setLoginIp(ip);

		ChessPlayer player = chessPlayerManager.registerPlayer(RequestUtils.getCurrentSid(), pp.getId(), pp.getAccountId());
		player.init(pp);
		player.ip = ip;

		player.login();
		ChessPlayerDto playerDto = new ChessPlayerDto(player, true);
		return playerDto;
	}
	
	
	/**
	 * 角色注册
	 * 
	 * @param id
	 * @param accountId
	 * @param password 密码
	 * @param gender 男0女1
	 * @param nickname 昵称
	 * @param ip 注册ip
	 * @return
	 */
	public ChessPlayerRegisterDto register(String id, String accountId, String password,String nickname,int gender,String ip) {

		// PersistChessPlayer persistTdPlayer =
		// PersistChessPlayer.get(Long.valueOf(id));
		PersistChessPlayer pp = PersistChessPlayer.getByAccountId(accountId);

		if (pp == null) {
			Long now = System.currentTimeMillis();
			pp = new PersistChessPlayer();
			UUID u = UUID.randomUUID();
			pp.setId(IdUtils.generateLongId());
			pp.setPassword(ChessUtils.md5(password+u.toString()));
			pp.setSalt(u.toString());
			pp.setAccountId(accountId);
			pp.setNickname(nickname);
			pp.setRegistryIp(ip);
			pp.setMoney(1000);
			pp.setExp(100);
			pp.setGender(gender);
			pp.setCreateTime(now);
			pp.setLoginTime(now);

			pp.setMission(new byte[5]);
			pp.setAchievement(new byte[5]);// 初始成就
			pp.setReplay(new byte[1]);//不须要初始化
			pp.setBuffLoginMoneyGiving(new byte[4]);

			pp.lastLoginTime = now;

			pp.save();
			
			chessPlayerManager.setRegistry(now);
			
			ChessPlayerRegisterDto rd = new ChessPlayerRegisterDto();
			rd.setGender(gender);
			rd.setMoney(1000);
			rd.setNickname(nickname);
			rd.setAccountId(accountId);
			rd.setPassword(password);
			return rd;
			// PersistSession.commit();
			// throw new GeneralException(ChessErrorCodes.ROLE_NOT_EXIST);
		} else {
			throw new GeneralException(ChessErrorCodes.ROLE_EXIST);
		}

	}

	/**
	 * 角色头像
	 * @param id
	 * @param type头像类型
	 */
	public void avatar(int type) {
		ChessPlayer player = chessPlayerManager.getRequestPlayer();
		player.avatar = type;
	}

	/**
	 * 角色密码
	 * @param id
	 * @param String deprecatePwd 旧密码
	 * @param String pwd 新密码
	 * @param String confirmPwd 新密码确认
	 */
	public void password(String deprecatePwd, String pwd,String confirmPwd) {
		ChessPlayer player = chessPlayerManager.getRequestPlayer();

		if(!ChessUtils.md5(deprecatePwd+player.getSalt()).equals(player.getPassword())){
			throw new GeneralException(ChessErrorCodes.WRONG_PASSWORD);
		}
		
		if(pwd.equals(confirmPwd)){
			player.setPassword(ChessUtils.md5(pwd+player.getSalt()));
		}else{
			throw new GeneralException(ChessErrorCodes.WRONG_PASSWORD);
		}
	}
	
	/**
	 * 角色昵称,性别
	 * @param id
	 * @param String nickname
	 */
	public void nickname(String nickname,int gender) {
		ChessPlayer player = chessPlayerManager.getRequestPlayer();
		player.nickname = nickname;
		player.gender = Gender.values()[gender];
	}
}
