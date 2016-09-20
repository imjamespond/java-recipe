package com.metasoft.flying.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.metasoft.flying.model.ChessPlayer;
import com.metasoft.flying.model.Flight;
import com.metasoft.flying.model.GameRoom;
import com.metasoft.flying.model.User;
import com.metasoft.flying.model.UserFashion;
import com.metasoft.flying.model.UserItem;
import com.metasoft.flying.model.constant.ChessConstant;
import com.metasoft.flying.model.constant.GeneralConstant;
import com.metasoft.flying.model.constant.ItemConstant;
import com.metasoft.flying.model.data.ItemData;
import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.net.BaseGroup;
import com.metasoft.flying.net.annotation.HandlerAnno;
import com.metasoft.flying.service.GameRoomService;
import com.metasoft.flying.service.StaticDataService;
import com.metasoft.flying.service.UserPersistService;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.service.common.LocalizationService;
import com.metasoft.flying.util.EpochUtil;
import com.metasoft.flying.vo.DressRequest;
import com.metasoft.flying.vo.FashionListVO;
import com.metasoft.flying.vo.FashionVO;
import com.metasoft.flying.vo.ItemRequest;
import com.metasoft.flying.vo.ShowItemVO;
import com.metasoft.flying.vo.general.GeneralResponse;

@Controller
public class DressController implements GeneralController {

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
	

	@HandlerAnno(name = "着装信息", cmd = "dress.info", req = DressRequest.class)
	public List<FashionVO> dressInfo(DressRequest req) throws GeneralException {
		// long now = System.currentTimeMillis();
		User self = userService.getAnyUserById(req.getUserId());
		List<FashionVO> listVO = new ArrayList<FashionVO>(self.getFashionMap().size());
		Map<Integer, UserItem> itemMap = self.getItemMap();
		Map<Integer, UserFashion> fashionMap = self.getFashionMap();
		Map<Integer, ItemData> itemDataMap = staticDataService.getItemDataMap();
		Iterator<Map.Entry<Integer, UserFashion>> it = fashionMap.entrySet().iterator();
		while (it.hasNext()) {
			Entry<Integer, UserFashion> entry = it.next();
			FashionVO vo = new FashionVO();
			UserFashion fashion = entry.getValue();
			UserItem item = itemMap.get(fashion.getItemId());

			ItemData itemDate = itemDataMap.get(fashion.getItemId());
			if (itemDate == null) {
				continue;
			}

			// 背包检测
			vo.setDeadline(1);
			if (itemDate.getCost() > 0) {
				if (null == item) {
					// it.remove();
					// continue;
					vo.setDeadline(-1);
				} else {
					// 过期的
					if (itemDate.getDeadline() > 0) {
						if (EpochUtil.getEpochDay(item.getItemTime()) < EpochUtil.getEpochDay(System
								.currentTimeMillis() - GeneralConstant.EXPIRE_TIME_OFFSET)) {
							vo.setDeadline(-1);
							
							//删除过期的
							it.remove();
							self.updateData = true;
						}
					}
				}
			}

			vo.setItemId(fashion.getItemId());

			listVO.add(vo);
		}

		return listVO;
	}

	@HandlerAnno(name = "广播着装", cmd = "dress.broadcast", req = DressRequest.class)
	public void broadcast(DressRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		if (null == self.getGroup()) {
			throw new GeneralException(0, "must.be.in.a.room");
		}
		BaseGroup group = chatService.getGroup(self.getGroup());

		FashionListVO vo = new FashionListVO();
		vo.setUserId(self.getId());
		vo.setList(dressInfo(req));
		group.broadcast((GeneralResponse.newObject(vo)));
	}

	@HandlerAnno(name = "着装", cmd = "dress.up", req = DressRequest.class)
	public void dressUp(DressRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		puton(self, req.getItemId(), req.getPos());
	}
	
	public void puton(User user,int itemid, int pos) throws GeneralException{
		// 数据配置
		Map<Integer, ItemData> itemDataMap = staticDataService.getItemDataMap();
		ItemData itemDate = itemDataMap.get(itemid);
		if (itemDate == null) {
			throw new GeneralException(0, "invalid.item.id");
		}

		// 背包检测
		Map<Integer, UserItem> itemMap = user.getItemMap();
		UserItem item = itemMap.get(itemid);
		if (itemDate.getCost() > 0) {
			if (null == item) {
				throw new GeneralException(0, "item.not.exist");
			}
		}
		// 时效检测
		if (itemDate.getDeadline() > 0) {
			// 如果购买日在当日之前
			if (EpochUtil.getEpochDay(item.getItemTime()) < EpochUtil.getEpochDay(System.currentTimeMillis()
					- GeneralConstant.EXPIRE_TIME_OFFSET)) {
				throw new GeneralException(0, "item.expired");
			}
		}

		UserFashion fashion = new UserFashion(itemid, 0l);
		user.getFashionMap().put(pos, fashion);
		user.updateData = true;
	}

	@HandlerAnno(name = "取消着装", cmd = "undress", req = DressRequest.class)
	public void unDress(DressRequest req) throws GeneralException {
		User self = userService.getRequestUser();
		UserFashion fashion = self.getFashionMap().get(req.getPos());
		if (null != fashion) {
			fashion.setItemId(0);
			fashion.setDeadline(0l);
		}
		self.updateData = true;
	}

	@HandlerAnno(name = "展示物品", cmd = "show.item", req = DressRequest.class)
	public Integer animation(ItemRequest req) throws GeneralException {
		User self = userService.getRequestUser();

		// 数据配置
		Map<Integer, ItemData> itemDataMap = staticDataService.getItemDataMap();
		ItemData itemDate = itemDataMap.get(req.getItemId());
		if (itemDate == null) {
			throw new GeneralException(0, "invalid.item.id");
		}

		// 背包检测
		Map<Integer, UserItem> itemMap = self.getItemMap();
		UserItem item = itemMap.get(req.getItemId());
		if (itemDate.getCost() > 0) {
			if (null == item) {
				throw new GeneralException(0, "item.not.exist");
			}
		}
		// 时效检测
		if (itemDate.getDeadline() > 0) {
			// 如果购买日在当日之前
			if (EpochUtil.getEpochDay(item.getItemTime()) < EpochUtil.getEpochDay(System.currentTimeMillis()
					- GeneralConstant.EXPIRE_TIME_OFFSET)) {
				throw new GeneralException(0, "item.expired");
			}
		}

		if (null != self.getGroup()) {
			if (null == self.getGroup()) {
				throw new GeneralException(0, "must.be.in.a.room");
			}
			GameRoom room = chatService.getGroup(self.getGroup());
			Flight flight = room.getFlight();
			// 展示飞机次数检测
			ChessPlayer cp = flight.getChessPlayer(self.getId());
			if (null == cp) {
				throw new GeneralException(0, "invalid.player");
			}
			if (cp.getShow() > ChessConstant.SHOW_ITEM_NUM) {
				throw new GeneralException(0, "show.item.limit");
			}
			cp.setShow(cp.getShow() + 1);

			ShowItemVO vo = new ShowItemVO();
			vo.setName(self.getUserPersist().getNickname());
			vo.setUserId(self.getId());
			vo.setType(req.getItemId());
			vo.setShowNum(ChessConstant.SHOW_ITEM_NUM - cp.getShow());

			// 广播
			room.broadcast(GeneralResponse.newObject(vo));

			return cp.getShow();
		}
		return 0;
	}

	/**
	 * @param user
	 * @return price
	 */
	public int checkPlaneIsPutOn(User user) {
		Map<Integer, ItemData> dataMap = staticDataService.getItemDataMap();
		Map<Integer, UserFashion> fashionMap = user.getFashionMap();
		Map<Integer, UserItem> itemMap = user.getItemMap();
		UserFashion fashion = fashionMap.get(ItemConstant.ITEMPOS_PLANE);
		if (fashion != null) {
			ItemData itemData = dataMap.get(fashion.getItemId());
			if (itemData == null) {
				return 0;
			}
			UserItem item = itemMap.get(fashion.getItemId());
			if (null == item) {
				return 0;
			}
			if (EpochUtil.getEpochDay(item.getItemTime()) < EpochUtil.getEpochDay(System.currentTimeMillis()
					- GeneralConstant.EXPIRE_TIME_OFFSET)) {
				return 0;
			}
			return itemData.getNum();
		}
		return 0;
	}
	
	/**
	 * @param user
	 * @return id
	 */
	public int checkPlaneIsPutOn2(User user) {
		Map<Integer, ItemData> dataMap = staticDataService.getItemDataMap();
		Map<Integer, UserFashion> fashionMap = user.getFashionMap();
		Map<Integer, UserItem> itemMap = user.getItemMap();
		UserFashion fashion = fashionMap.get(ItemConstant.ITEMPOS_PLANE);
		if (fashion != null) {
			ItemData itemData = dataMap.get(fashion.getItemId());
			if (itemData == null) {
				return 0;
			}
			UserItem item = itemMap.get(fashion.getItemId());
			if (null == item) {
				return 0;
			}
			if (EpochUtil.getEpochDay(item.getItemTime()) < EpochUtil.getEpochDay(System.currentTimeMillis()
					- GeneralConstant.EXPIRE_TIME_OFFSET)) {
				return 0;
			}
			return itemData.getId();
		}
		return 0;
	}
}
