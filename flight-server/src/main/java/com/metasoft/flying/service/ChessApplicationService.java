package com.metasoft.flying.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;

import com.metasoft.flying.model.ChessApplication;

@Service
public class ChessApplicationService {

	private ConcurrentMap<String, ChessApplication> applictionMap = new ConcurrentHashMap<String, ChessApplication>();

	public ChessApplication getGroupEntity(String name) {
		ChessApplication app = applictionMap.get(name);
		if (app != null)
			return app;
		app = new ChessApplication(name);
		registerGroup(app);
		return app;
	}

	public ChessApplication getGroup(String name) {
		return applictionMap.get(name);
	}

	private void registerGroup(ChessApplication app) {
		applictionMap.put(app.getName(), app);
	}

	public void resetGroup() {
		applictionMap = new ConcurrentHashMap<String, ChessApplication>();
	}
}
