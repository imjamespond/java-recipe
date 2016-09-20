package com.metasoft.flying.model.constant;

public class ArenaConstant {
	public static final int IDLE = 0;//空闲
	public static final int READY = 1;//准备
	public static final int COMMENCE = 2;//开始
	public static final int INTERRUPT = 3;//中止
	
	public static final long AREAN_LENGTH = 600000l;
	public static final long AREAN_WAIT = 60000l;
	
	public static final int AREAN_POSX[] = {30,350,670,30,670,30,350,670};
	public static final int AREAN_POSY[] = {30,30,30,350,350,670,670,670};
	
	public static final int AREAN_EMPEROR[][] = {
		{},
		{3},
		{3,4},
		{3,4,2},
		{3,4,2,3},
		{3,4,2,3,4},
		{3,4,2,3,2,3},
		{3,4,2,3,2,4,3}};//三国杀角色1主公，2忠臣，3反贼，4内奸
	public static final int TYPE_PK = 1;
	public static final int TYPE_EMPEROR = 2;
}
