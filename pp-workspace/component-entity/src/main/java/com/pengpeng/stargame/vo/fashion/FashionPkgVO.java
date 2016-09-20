package com.pengpeng.stargame.vo.fashion;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.annotation.EventAnnotation;
import com.pengpeng.stargame.vo.BaseItemVO;
import com.pengpeng.stargame.vo.BasePkgVO;

import java.util.Arrays;

/**
 * @author jinli.yuan@pengpeng.com
 * @since 13-5-29 上午10:46
 */
@Desc("时装仓库信息")
@EventAnnotation(name="event.fashion.pkg.update",desc="时装仓库数据更新")
public class FashionPkgVO extends BasePkgVO {
	@Desc("时装类型")
	private String itemType;
    @Desc("仓库物品列表")
    private FashionItemVO[] baseItemVO;

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

    public FashionItemVO[] getBaseItemVO() {
        return baseItemVO;
    }

    public void setBaseItemVO(FashionItemVO[] baseItemVO) {
        this.baseItemVO = baseItemVO;
    }
}
