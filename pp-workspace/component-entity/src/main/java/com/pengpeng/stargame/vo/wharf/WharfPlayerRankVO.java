package com.pengpeng.stargame.vo.wharf;

import com.pengpeng.stargame.annotation.Desc;

/**
 * @author james
 * @since 13-5-29 上午10:46
 */
@Desc("码头玩家排行")
public class WharfPlayerRankVO {
	@Desc("玩家昵称")
	private String name;
    @Desc("玩家头像")
    private String avatar;
    @Desc("玩家等级")
    private int level;
    @Desc("周贡献")
    private int wContribution;
    @Desc("总贡献")
    private int contribution;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getwContribution() {
        return wContribution;
    }

    public void setwContribution(int wContribution) {
        this.wContribution = wContribution;
    }

    public int getContribution() {
        return contribution;
    }

    public void setContribution(int contribution) {
        this.contribution = contribution;
    }
}
