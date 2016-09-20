package com.metasoft.empire.service;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.copycat.framework.annotation.Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.metasoft.empire.model.data.RoleData;
import com.metasoft.empire.utils.ExcelUtils;

@Service
public class StaticDataService {

	private static final Logger logger = LoggerFactory.getLogger(StaticDataService.class);

	public Map<Integer, RoleData> roleMap = null;
	@PostConstruct
	public void init() {
		roleMap = update(RoleData.class);
		
		if (logger.isDebugEnabled()) {
			print();
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

	private void print() {

	}
}
