package com.metasoft.flying.service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.springframework.stereotype.Service;
import com.metasoft.flying.model.ChessGuess;

@Service
public class ChessGuessService {

	private ConcurrentMap<String, ChessGuess> guessMap = new ConcurrentHashMap<String, ChessGuess>();

	public ChessGuess getGroupEntity(String name) {
		ChessGuess app = guessMap.get(name);
		if (app != null)
			return app;
		app = new ChessGuess(name);
		registerGroup(app);
		return app;
	}

	public ChessGuess getGroup(String name) {
		return guessMap.get(name);
	}

	private void registerGroup(ChessGuess app) {
		guessMap.put(app.getName(), app);
	}

	public void resetGroup() {
		guessMap = new ConcurrentHashMap<String, ChessGuess>();
	}
}
