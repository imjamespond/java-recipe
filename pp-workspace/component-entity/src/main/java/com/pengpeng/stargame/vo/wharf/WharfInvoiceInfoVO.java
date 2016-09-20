package com.pengpeng.stargame.vo.wharf;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("码头货单信息")
public class WharfInvoiceInfoVO {
	@Desc("货单id")
	private String invoiceId;
    @Desc("货单是否完成")
    private Boolean completed;
    @Desc("好友帮助")
    private Boolean help;
    @Desc("道具id")
    private String itemId;
    @Desc("道具名称")
    private String itemName;
    @Desc("道具需要数量")
    private int itemNum;
    @Desc("道具存货数量")
    private int itemInventory;
    @Desc("农场经验")
    private int farmExp;
    @Desc("游戏币")
    private int gameCoin;
    @Desc("名字")
    private String name;
    @Desc("头像")
    private String portrait;

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Boolean getHelp() {
        return help;
    }

    public void setHelp(Boolean help) {
        this.help = help;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public int getItemNum() {
        return itemNum;
    }

    public void setItemNum(int itemNum) {
        this.itemNum = itemNum;
    }

    public int getItemInventory() {
        return itemInventory;
    }

    public void setItemInventory(int itemInventory) {
        this.itemInventory = itemInventory;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getFarmExp() {
        return farmExp;
    }

    public void setFarmExp(int farmExp) {
        this.farmExp = farmExp;
    }

    public int getGameCoin() {
        return gameCoin;
    }

    public void setGameCoin(int gameCoin) {
        this.gameCoin = gameCoin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
}
