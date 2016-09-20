package com.chitu.poker.data;

import cn.gecko.broadcast.BroadcastMessage;

public class StaticNewbieStep implements BroadcastMessage {

	private int id;
	private boolean isClickSpriteTrigger;
	private String triggerPathName;
	private int padding;
	private boolean isAutoShade;
	private String shade;
	private int rotation;
	private int arrowsX;
	private int arrowsY;
	private int spriteX;
	private int spriteY;
	private String content;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isClickSpriteTrigger() {
		return isClickSpriteTrigger;
	}

	public void setClickSpriteTrigger(boolean isClickSpriteTrigger) {
		this.isClickSpriteTrigger = isClickSpriteTrigger;
	}

	public String getTriggerPathName() {
		return triggerPathName;
	}

	public void setTriggerPathName(String triggerPathName) {
		this.triggerPathName = triggerPathName;
	}

	public int getPadding() {
		return padding;
	}

	public void setPadding(int padding) {
		this.padding = padding;
	}

	public boolean isAutoShade() {
		return isAutoShade;
	}

	public void setAutoShade(boolean isAutoShade) {
		this.isAutoShade = isAutoShade;
	}

	public String getShade() {
		return shade;
	}

	public void setShade(String shade) {
		this.shade = shade;
	}

	public int getRotation() {
		return rotation;
	}

	public void setRotation(int rotation) {
		this.rotation = rotation;
	}

	public int getArrowsX() {
		return arrowsX;
	}

	public void setArrowsX(int arrowsX) {
		this.arrowsX = arrowsX;
	}

	public int getArrowsY() {
		return arrowsY;
	}

	public void setArrowsY(int arrowsY) {
		this.arrowsY = arrowsY;
	}

	public int getSpriteX() {
		return spriteX;
	}

	public void setSpriteX(int spriteX) {
		this.spriteX = spriteX;
	}

	public int getSpriteY() {
		return spriteY;
	}

	public void setSpriteY(int spriteY) {
		this.spriteY = spriteY;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
