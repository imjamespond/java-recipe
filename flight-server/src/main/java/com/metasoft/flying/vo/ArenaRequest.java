package com.metasoft.flying.vo;

import com.metasoft.flying.net.annotation.DescAnno;
import com.metasoft.flying.vo.general.GeneralRequest;

@DescAnno("飞行场")
public class ArenaRequest extends GeneralRequest {

		@DescAnno("id")
		private int id;

		public int getId() {
			return id;
		}

		public void setId(int id) {
			this.id = id;
		}
}
