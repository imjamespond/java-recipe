package com.pengpeng.stargame.farm.dao.impl;

import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.model.farm.FarmItem;
import com.pengpeng.stargame.model.player.Player;
import com.pengpeng.stargame.vo.farm.FarmPkgVO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 仓库物品，包裹
 * @auther foquan.lin@com.pengpeng.com
 * @since: 13-3-8下午3:21
 */
@Deprecated
public class FarmItemDaoImpl  {

    public Class<FarmItem> getClassType() {
        return FarmItem.class;
    }

	public List<FarmPkgVO> getFarmItemByPid(String pid) throws GameException {
		HashMap<String,FarmItem> map = null;
		if(map == null || map.isEmpty()){
			return null;
		}

		List<FarmPkgVO> list = new ArrayList<FarmPkgVO>();
		Iterator iterator = map.entrySet().iterator();
		while (iterator.hasNext()){
			Map.Entry<String,FarmItem> entry = (Map.Entry<String, FarmItem>) iterator.next();
			FarmItem farmItem = entry.getValue();
			if(farmItem == null){
				continue;
			}

			FarmPkgVO vo = new FarmPkgVO();
			BeanUtils.copyProperties(farmItem,vo);
			list.add(vo);
		}
		return list;
	}


	public FarmPkgVO getFarmItem(Player player, String itemId) {
		String pid = player.getKey();
		FarmItem farmItem = null;
		if(farmItem == null){
			return null;
		}

		FarmPkgVO vo = new FarmPkgVO();
		BeanUtils.copyProperties(farmItem,vo);
		return vo;
	}
}
