package com.metasoft.flying.service;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.copycat.framework.annotation.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.metasoft.flying.controller.StaticDataController;
import com.metasoft.flying.model.constant.ItemConstant;
import com.metasoft.flying.model.data.ArenaData;
import com.metasoft.flying.model.data.ItemData;
import com.metasoft.flying.model.data.MatchData;
import com.metasoft.flying.model.data.NpcData;
import com.metasoft.flying.model.data.PrizeData;
import com.metasoft.flying.model.data.PveData;
import com.metasoft.flying.model.data.PveNpcData;
import com.metasoft.flying.model.data.RosePresentData;
import com.metasoft.flying.util.ExcelUtils;
import com.metasoft.flying.util.RandomUtils;
import com.metasoft.flying.vo.ItemDataVO;

@Service
public class StaticDataService {
	@Autowired
	private StaticDataController sc;

	private static final Logger logger = LoggerFactory.getLogger(StaticDataService.class);

	private Map<Integer, ItemData> itemDataMap = null;
	private Map<Integer, RosePresentData> roseDataMap = null;
	private List<ItemData> planeList = null;
	private List<ItemData> chessList = null;
	private Map<Integer, PrizeData> prizeDataMap = null;
	public Map<Integer, NpcData> npcDataMap = null;
	public Map<Integer, PveNpcData> pveNpcDataMap;
	public Map<String, Long> pveNpcNameDataMap = null;
	public Map<Integer, PveData> pveDataMap = null;
	public Map<Integer, MatchData> matchMap = null;
	public Map<Integer, ArenaData> arenaMap = null;
	//@PostConstruct
	public void init() {
		itemDataMap = update(ItemData.class);
		roseDataMap = update(RosePresentData.class);
		prizeDataMap = update(PrizeData.class);		
		npcDataMap = update(NpcData.class);	
		pveNpcDataMap = update(PveNpcData.class);		
		pveDataMap = update(PveData.class);
		matchMap = update(MatchData.class);
		arenaMap = update(ArenaData.class);
		
		//飞机列表
		planeList = new ArrayList<ItemData>();
		//事件列表
		chessList = new ArrayList<ItemData>();
		
		for (Entry<Integer, ItemData> entry : itemDataMap.entrySet()) {
			if ((ItemConstant.ITEM_PLANE & entry.getValue().getEffect()) > 0) {
				if ( entry.getValue().getCost() > 0) {
					planeList.add(entry.getValue());
				}
			}

			if((ItemConstant.ITEM_CHESS_FORTUNE & entry.getValue().getEffect()) > 0){
				chessList.add(entry.getValue());				
			}
		}
		
		if (logger.isDebugEnabled()) {
			print();
		}
		
		//道具配置数据
		sc.itemList = new ArrayList<ItemDataVO>(itemDataMap.size());
		for (Entry<Integer, ItemData> entry : itemDataMap.entrySet()) {
			ItemDataVO vo = new ItemDataVO();
			BeanUtils.copyProperties(entry.getValue(), vo);
			sc.itemList.add(vo);
		}
	}

	private <T> Map<Integer, T> update(Class<T> clazz) {
		Table dataAnno = clazz.getAnnotation(Table.class);
		try {
			File excel = new File(getClass().getResource(dataAnno.value()).toURI());
			if (excel.exists()) {
				Map<Integer, T> map = ExcelUtils.load(clazz, excel);
				return map;
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Map<Integer, ItemData> getItemDataMap() {
		return itemDataMap;
	}

	public ItemData getRandomPrize() {	
		int random = RandomUtils.nextInt(100);
		int price = 0;
		if(random<10){
			price = 50;
		}else if(random<40){
			price = 30;
		}else{
			price = 10;
		}
		for(int i = 0; i<planeList.size(); i++){
			ItemData plane = planeList.get((random+i)%planeList.size());
			if(plane.getCost() == price){
				return plane;
			}
		}
		
		return null;
	}

	public RosePresentData getRosePresentData(int num) {
		return roseDataMap.get(num);
	}
	public PrizeData getPrizeData(int id) {
		return prizeDataMap.get(id);
	}
	public Map<Integer, PrizeData> getPrizeDataMap() {
		return prizeDataMap;
	}

	private void print() {

	}

	public List<ItemData> getChessList() {
		return chessList;
	}
	
	
}
