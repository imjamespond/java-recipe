package com.metasoft.empire.model;

import com.metasoft.empire.vo.PlayerVO;

public class GamePlayer {
	public PlayerVO vo;
	public int turn;
	public int swap;
	public long swaped;
	
	public int maxhp =0;
	public int mengpo=0;
	public int linlu=0;
	public int petrify=0;//turn
	public int yushe=0;
	
	public GamePlayer() {
		//vo = new PlayerVO();
	}
	
	public void reset(){
		turn = -1;
	}
	
	public void setSwap(int swap, int turn){
		this.turn = turn;
		this.swap = swap;
		
		swaped |= 1<<swap;
	}

	public int[] swap() {
		
		if (swap == 0){
			return null;
		}

		if (swap == 8) {
			int[] roles = vo.getRoles();
			int[] ids = new int[8];
			int curid = roles[3];
			// counter clockwise
			for (int i = 0; i < 4; i++) {
				int tmp = roles[i];
				roles[i] = curid;
				curid = tmp;
				ids[i * 2] = roles[i];
				ids[i * 2 + 1] = curid;
			}
			return ids;
		} else if (swap == 7) {
			int[] roles = vo.getRoles();
			int[] ids = new int[8];
			int curid = roles[0];
			// clockwise
			for (int i = 3; i >= 0; i--) {
				int tmp = roles[i];
				roles[i] = curid;
				curid = tmp;
				ids[i * 2] = roles[i];
				ids[i * 2 + 1] = curid;
			}
			return ids;
		}

		int swap0 = 0, swap1 = 1;
		switch (swap) {
		case 1:
			swap1 = 1;
			break;
		case 2:
			swap1 = 2;
			break;
		case 3:
			swap1 = 3;
			break;
		case 4:
			swap0 = 1;
			swap1 = 2;
			break;
		case 5:
			swap0 = 1;
			swap1 = 3;
			break;
		case 6:
			swap0 = 2;
			swap1 = 3;
			break;
		}
		int[] roles = vo.getRoles();
		int tmp = roles[swap0];
		roles[swap0] = roles[swap1];
		roles[swap1] = tmp;

		int[] ids = { roles[swap0], roles[swap1] };
		return ids;
	}

	public void autoSwap() {
		swap = 0;
		for(int i=1; i<vo.getSwap().length; i++){
			int s = vo.getSwap()[i];
			if((swaped&1<<s)==0){
				swap = s;
				swaped |= 1<<s;
				break;
			}
		}
	}
}
