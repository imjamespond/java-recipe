package com.pengpeng.stargame.vo.fashion;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.vo.BaseShopItemVO;

import java.util.Arrays;

/**
 * @author jinli.yuan@pengpeng.com
 * @since 13-5-29 上午10:43
 */
@Desc("时装商店信息，商店内的单个物品")
public class FashionShopItemVO extends BaseShopItemVO {
	@Desc("性别要求，0表示无性别要求、1表示女性，2表示男性")
	private int sex;
	@Desc("")
	private String image;
	@Desc("用于衡量玩家穿戴的时装时尚指数的数值")
	private int fashionIndex;
    @Desc("最低等级限制")
    private int level;

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public int getFashionIndex() {
		return fashionIndex;
	}

	public void setFashionIndex(int fashionIndex) {
		this.fashionIndex = fashionIndex;
	}

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
	public String toString() {
		return "FashionShopItemVO{" +
				"sex=" + sex +
				", image='" + image + '\'' +
				", fashionIndex=" + fashionIndex +
				'}';
	}
}
