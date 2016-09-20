package com.metasoft.flying.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.metasoft.flying.model.GameRoom;
import com.metasoft.flying.model.User;
import com.metasoft.flying.model.UserItem;
import com.metasoft.flying.model.VoFactory;
import com.metasoft.flying.model.constant.GeneralConstant;
import com.metasoft.flying.model.constant.ItemConstant;
import com.metasoft.flying.model.data.ItemData;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.model.job.AppleJob;
import com.metasoft.flying.net.annotation.HandlerAnno;
import com.metasoft.flying.service.GameRoomService;
import com.metasoft.flying.service.StaticDataService;
import com.metasoft.flying.service.UserPersistService;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.JobService;
import com.metasoft.flying.service.common.LocalizationService;
import com.metasoft.flying.util.EpochUtil;
import com.metasoft.flying.vo.ChatVO;
import com.metasoft.flying.vo.ItemRequest;
import com.metasoft.flying.vo.ItemVO;
import com.metasoft.flying.vo.UserGiftVO;
import com.metasoft.flying.vo.general.GeneralRequest;
import com.metasoft.flying.vo.general.GeneralResponse;

@Controller
public class ItemController implements GeneralController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserPersistService userPersistService;
	@Autowired
	private GameRoomService chessRoomService;
	@Autowired
	private GameRoomService chatService;
	@Autowired
	private StaticDataService staticDataService;
	@Autowired
	private LocalizationService localService;
	@Autowired
	private JobService jobService;	

	@HandlerAnno(name = "物品数量", cmd = "item.num", req = ItemRequest.class)
	public ItemVO itemNum(ItemRequest req) throws GeneralException {
		User user = userService.getRequestUser();
		Map<Integer, UserItem> itemMap = user.getItemMap();
		UserItem item = itemMap.get(req.getItemId());
		ItemVO vo = new ItemVO();
		vo.setItemId(req.getItemId());
		if (item != null) {
			vo.setNum(item.getItemNum());
		}
		return vo;
	}

	@HandlerAnno(name = "物品信息", cmd = "item.info", req = GeneralRequest.class)
	public List<ItemVO> itemInfo(GeneralRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		List<ItemVO> listVO = new ArrayList<ItemVO>(self.getItemMap().size());
		Map<Integer, ItemData> itemDataMap = staticDataService.getItemDataMap();
		Map<Integer, UserItem> itemMap = self.getItemMap();
		Iterator<Map.Entry<Integer, UserItem>> it = itemMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, UserItem> entry = it.next();
			ItemVO vo = new ItemVO();
			UserItem item = entry.getValue();
			if (item.getItemNum() == 0) {
				it.remove();
				self.updateData = true;
				continue;
			}
			// 过期的应删除
			ItemData itemDate = itemDataMap.get(item.getItemId());
			if (itemDate == null) {
				it.remove();
				self.updateData = true;
				continue;
			}
			if (itemDate.getDeadline() > 0) {
				// 如果购买日在当日之前
				if (EpochUtil.getEpochDay(item.getItemTime()) < EpochUtil.getEpochDay(System.currentTimeMillis()
						- GeneralConstant.EXPIRE_TIME_OFFSET)) {
					it.remove();
					self.updateData = true;
					continue;
				}
				vo.setDeadline(item.getItemTime() + itemDate.getDeadline());
			}
			vo.setItemId(item.getItemId());
			vo.setNum(item.getItemNum());

			listVO.add(vo);
		}
		
		return listVO;
	}

	@HandlerAnno(name = "物品使用", cmd = "item.use", req = ItemRequest.class)
	public void itemUse(ItemRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		ItemData itemData = staticDataService.getItemDataMap().get(req.getItemId());
		if (null == itemData) {
			throw new GeneralException(0, "invalid.item.id");
		}
		self.addItem(req.getItemId(), -req.getNum(), "使用");
		if (ItemConstant.ITEM_ROSE == itemData.getEffect()) {
			self.addRose(itemData.getNum(), "by item");
		}// else if(ItemConstant.ITEM_GEMS == itemData.getEffect()){
			// self.addGems(itemData.getNum(), "by item");
		// }
	}

	@HandlerAnno(name = "赠送列表", cmd = "item.gift", req = GeneralRequest.class)
	public List<UserGiftVO> gift(GeneralRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		List<UserGiftVO> list = self.getGiftList();
		for (UserGiftVO vo : list) {
			if (null == vo.getUserName()) {
				User user = userService.getAnyUserById(vo.getUserId());
				vo.setUserName(user.getUserPersist().getNickname());
			}
		}
		return list;
	}

	public void giveTo(ItemData itemData, int num, User user, User self) throws GeneralException {
		if (null == itemData) {
			throw new GeneralException(0, "invalid.item.id");
		}

		//赠送玫瑰
		if (ItemConstant.ITEM_ROSE == itemData.getEffect()) {
			int rose = itemData.getNum() * num;
			user.addRose(rose, "赠");
			user.addCharm(rose);
			self.addContribution(rose);
		} 
		//赠送苹果
		else if (ItemConstant.ITEMID_APPLE1 == itemData.getId()) {
			int apple = itemData.getNum() * num;
			//user.addApple(apple, "赠");
			jobService.produce(new AppleJob(self.getId(), user.getId(), apple, self.getUserPersist().getNickname()));
		} 
		//赠送其它
		else {
			// 附赠玫瑰
			int rose = itemData.getRose() * num;
			if ((8 & itemData.getEffect()) > 0) {
				// self.addItem(ItemConstant.ITEMID_ROSE1, itemData.getRose(),
				// "附赠玫瑰");
				user.addRose(rose, "附赠");
				user.addCharm(rose);
			}
			self.addContribution(rose);
			UserItem item = user.addItem(itemData.getId(), num, String.format("by %d:", self.getId()));
			// 物品通知
			if (null != user.getConn()) {
				user.getConn().deliver(GeneralResponse.newObject(VoFactory.getItemVO(item)));
			}
		}
		
		// 财富通知
		if (null != user.getConn()) {
			user.getConn().deliver(GeneralResponse.newObject(VoFactory.getWealthVO(user)));
		}
		if (null != self.getConn()) {
			self.getConn().deliver(GeneralResponse.newObject(VoFactory.getWealthVO(self)));
		}

		// 增加赠送列表
		UserGiftVO giftVO = new UserGiftVO(itemData.getId(), num, self.getId(), System.currentTimeMillis());
		giftVO.setUserName(self.getUserPersist().getNickname());
		List<UserGiftVO> list = user.getGiftList();
		synchronized (list) {
			list.add(giftVO);
		}

		// 广播
		GameRoom room = chessRoomService.getGroup(self.getGroup());
		room.broadcast(GeneralResponse.newObject(giftVO));

		// 广播赠送
		String giftMsg = localService.getLocalString("item.give", new String[] { self.getUserPersist().getNickname(),
				itemData.getName(), String.valueOf(num) });
		if ((ItemConstant.ITEM_ROSE_PRESENT & itemData.getEffect()) > 0) {
			int rose = itemData.getNum() * num;
			giftMsg = String.format("%s ,玫瑰 * %d", giftMsg, rose);
		}
		room.broadcast(GeneralResponse.newObject(new ChatVO(giftMsg)));
	}

	@HandlerAnno(name = "赠送物品", cmd = "item.give", req = ItemRequest.class)
	public void give(ItemRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}

		User user = userService.getAnyUserById(req.getUserId());
		ItemData itemData = staticDataService.getItemDataMap().get(req.getItemId());

		UserItem item = self.addItem(req.getItemId(), -req.getNum(), "赠送");

		giveTo(itemData, req.getNum(), user, self);
		
		// 物品通知
		if (null != self.getConn()) {
			self.getConn().deliver(GeneralResponse.newObject(VoFactory.getItemVO(item)));
		}
	}

	@HandlerAnno(name = "购买赠送", cmd = "item.buy.give", req = ItemRequest.class)
	public void buyNGive(ItemRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}

		User user = userService.getAnyUserById(req.getUserId());
		ItemData itemData = staticDataService.getItemDataMap().get(req.getItemId());

		int needGold = itemData.getCost() * req.getNum();
		self.reduceGems(needGold, String.format("购赠:%d", req.getItemId()));

		giveTo(itemData, req.getNum(), user, self);
		// 财富通知
		self.getConn().deliver(GeneralResponse.newObject(VoFactory.getWealthVO(self)));
	}

	@HandlerAnno(name = "赠送答谢", cmd = "item.thank", req = ItemRequest.class)
	public void thank(ItemRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		List<UserGiftVO> list = self.getGiftList();
		int index = req.getNum();
		UserGiftVO vo = list.get(index);
		vo.setThank(1);
		self.updateData = true;

		User user = userService.getAnyUserById(vo.getUserId());

		GameRoom room = chessRoomService.getGroupEntity(self.getGroup());
		if (null != room) {
			String giftMsg = localService.getLocalString("item.thank", new String[] {
					self.getUserPersist().getNickname(), user.getUserPersist().getNickname() });
			room.broadcast(GeneralResponse.newObject(new ChatVO(giftMsg)));
		}
	}

	@HandlerAnno(name = "购买物品", cmd = "item.buy", req = ItemRequest.class)
	public void buy(ItemRequest req) throws GeneralException {
		User self = userService.getRequestUser();

		ItemData itemData = staticDataService.getItemDataMap().get(req.getItemId());
		if (null == itemData) {
			throw new GeneralException(0, "invalid.item.id");
		}
		int needGold = itemData.getCost() * req.getNum();

		// 附赠玫瑰
		if ((8 & itemData.getEffect()) > 0) {
			UserItem rose = self.addItem(ItemConstant.ITEMID_ROSE1, itemData.getRose() * req.getNum(), "附赠玫瑰");
			// 物品通知	
			self.getConn().deliver(GeneralResponse.newObject(VoFactory.getItemVO(rose)));
		}

		self.reduceGems(needGold, String.format("购:%d", req.getItemId()));
		UserItem item = self.addItem(req.getItemId(), req.getNum(), "buy");		
		// 物品通知		
		self.getConn().deliver(GeneralResponse.newObject(VoFactory.getItemVO(item)));
		// 财富通知
		self.getConn().deliver(GeneralResponse.newObject(VoFactory.getWealthVO(self)));
	}
}
