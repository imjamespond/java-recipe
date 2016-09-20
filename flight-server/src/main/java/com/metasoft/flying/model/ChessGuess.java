package com.metasoft.flying.model;

import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.metasoft.flying.model.constant.ItemConstant;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.LocalizationService;
import com.metasoft.flying.service.common.SpringService;
import com.metasoft.flying.vo.ChatVO;
import com.metasoft.flying.vo.general.GeneralResponse;

public class ChessGuess {

	private String name;

	private ConcurrentMap<Long, Integer> guessMap = new ConcurrentHashMap<Long, Integer>();

	public ChessGuess(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void guess(long user, int pos) {
		guessMap.put(user, pos);
	}

	public int getSize() {
		return guessMap.size();
	}

	public Integer getGuessPos(long userId) {
		return guessMap.get(userId);
	}

	public void settle(int winnerPos) {
		for (Entry<Long, Integer> entry : guessMap.entrySet()) {		
			LocalizationService ls = SpringService.getBean(LocalizationService.class);

			long userId = entry.getKey();
			int pos = entry.getValue();
			UserService us = SpringService.getBean(UserService.class);
			try {

				User user = us.getAnyUserById(userId);
				ChatVO vo = new ChatVO();
				if (winnerPos == pos) {
					user.addItem(ItemConstant.ITEMID_MAGIC_DICE, 3, "猜中");
					String msg = ls.getLocalString("guess.correct");
					vo.setMsg(msg);
				} else {
					user.addItem(ItemConstant.ITEMID_MAGIC_DICE, 1, "未猜中");
					String msg = ls.getLocalString("guess.wrong");
					vo.setMsg(msg);
				}

				if (user.getConn() != null) {
					user.getConn().deliver(GeneralResponse.newObject(vo));
				}
			} catch (GeneralException e) {
				return;
			}
		}
	}

	public void clear() {
		guessMap.clear();
	}
}
