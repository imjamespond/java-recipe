package com.chitu.poker.battle;

import java.util.List;

import com.chitu.poker.data.StaticInstance;

public class Area implements Comparable<Area> {

	public int id;

	public String name;

	public List<StaticInstance> instances;

	@Override
	public int compareTo(Area area) {
		return id - area.id;
	}

}
