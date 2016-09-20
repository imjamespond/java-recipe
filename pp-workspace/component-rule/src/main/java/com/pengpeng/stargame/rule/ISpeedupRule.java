package com.pengpeng.stargame.rule;

public interface ISpeedupRule<CheckType,ConsumeType,EffectType> {
	public int getSpeedTime();
	
	public boolean canSpeed(CheckType bean);
	
	public int consumeSpeed(ConsumeType bean);
	
	public void effectSpeed(EffectType bean);
}
