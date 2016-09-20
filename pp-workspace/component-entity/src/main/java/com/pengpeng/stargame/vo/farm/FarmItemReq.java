package com.pengpeng.stargame.vo.farm;

import com.pengpeng.stargame.annotation.Desc;
import com.pengpeng.stargame.req.BaseReq;


/**
 * @author jinli.yuan@pengpeng.com
 * @since 13-4-27 上午10:37
 */
@Desc("仓库")
public class FarmItemReq extends BaseReq{

	@Desc("玩家id,好友id")
	private String pid;
	@Desc("物品id")
	private String itemId;
	@Desc("道具类型")
	private Integer type;
	@Desc("物品类型")
	private Integer subType;
	@Desc("数量")
	private Integer num;
	@Desc("物品key,一个key代表一个格子")
	private String id;

    public String [] ids;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getSubType() {
		return subType;
	}

	public void setSubType(Integer subType) {
		this.subType = subType;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }
}
