package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;

@DescAnno("飞行场")
public class ArenaVO {

		@DescAnno("id")
		private int id;
		@DescAnno("报名金币")
		private int cost;
		@DescAnno("奖励金币")
		private int gold;

		public int getGold() {
			return gold;
		}

		public void setGold(int gold) {
			this.gold = gold;
		}

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}

		public int getCost() {
			return cost;
		}

		public void setCost(int cost) {
			this.cost = cost;
		}

}
