package com.metasoft.flying.test;

import com.metasoft.flying.model.constant.ChessConstant;
import com.metasoft.flying.util.RandomUtils;

public class TestRamdon {

	public static void main(String[] args) {
		for(int journey = 0; journey<ChessConstant.JOURNEY_OUT;journey++){
			for(int i=0;i<10000;i++){
		int random = RandomUtils.nextInt(ChessConstant.JOURNEY_JUMP);		
		if(ChessConstant.TORNADO_FALL < 0){
			random = (random + journey + 1)%ChessConstant.JOURNEY_OUT;//max 49
		}
		if(random==journey)
		System.out.printf("journey:%d->%d\n",journey,random);
			}
		
		}
	}

}
