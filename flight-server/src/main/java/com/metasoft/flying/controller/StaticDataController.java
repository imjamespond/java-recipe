package com.metasoft.flying.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.metasoft.flying.model.exception.GeneralException;
import com.metasoft.flying.net.annotation.HandlerAnno;
import com.metasoft.flying.service.StaticDataService;
import com.metasoft.flying.service.UserService;
import com.metasoft.flying.vo.ItemDataVO;
import com.metasoft.flying.vo.general.GeneralRequest;

@Controller
public class StaticDataController implements GeneralController {
	//private static final Logger logger = LoggerFactory.getLogger(StaticDataController.class);
	
	@Autowired
	private UserService userService;	
	@Autowired
	private StaticDataService staticDataService;

	public List<ItemDataVO> itemList = null;

	@HandlerAnno(name = "道具数据", cmd = "data.item", req = GeneralRequest.class)
	public List<ItemDataVO> item(GeneralRequest req) throws GeneralException {
		return itemList;
	}

}
