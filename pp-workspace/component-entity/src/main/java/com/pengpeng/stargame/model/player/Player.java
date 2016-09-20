package com.pengpeng.stargame.model.player;

import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.*;
import java.util.Date;

/**
 * 玩家对象
 * <p>
 * 
 * @author 林佛权
 * 
 */
@Entity
@Table(name="pp_stargame_player")
public class Player extends BaseEntity<String> {
	@Id
    @Column(length = 32)
	public String id;

    @Column(nullable=false)
    private int userId;//主站userId

    @Column(length = 32)
	private String nickName;// 角色名

    @Column(nullable=false)
    private int type;//角色类型 ,//性别,0女,1男

    @Column(nullable=false)
    private int gameCoin;//(游戏普通货币)

    @Column(nullable=false)
    private int goldCoin;//碰币 (充值币)

    private int score;//积分
    @Column
    private int sex;//性别,0女,1男

////////////////////////////附加数据//////////////////////
    @Temporal(TemporalType.TIMESTAMP)
	private Date createTime;// 创建时间
////////////////////////////附加数据//////////////////////
    private int starId;
    //玩家类型  0 普通玩家   1 NPc 帐号
    private int playerType;
    //主站那边的 是否是 超级粉丝  1是  0不是

    private int payMember;
    //充值时间 统计用到
    private Date rechargeTime;

	public Player() {
	}

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getStarId() {
        return starId;
    }

    public void setStarId(int starId) {
        this.starId = starId;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getNickName() {
		return nickName;
	}

	public void setNickName(String userName) {
		this.nickName = userName;
	}


	public void incGameCoin(int value){
		gameCoin +=Math.abs(value);
	}
	public void decGameCoin(int value){
        gameCoin -=Math.abs(value);
        if(gameCoin<0){
            gameCoin=0;
        }
	}

    public void incGoldCoin(int value){
        goldCoin +=Math.abs(value);
    }
    public void decGoldCoin(int value){
        goldCoin -=Math.abs(value);
    }


    @Override
	public String getKey() {
		return id;
	}


	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

    public int getGoldCoin() {
        return goldCoin;
    }

    public int getGameCoin() {
        return gameCoin;
    }

    public void setGameCoin(int coin) {
        this.gameCoin = coin;
    }
    @Override
    public int hashCode(){
        if (id==null){
            return 0;
        }else{
            return id.hashCode();
        }
    }
    @Override
    public boolean equals(Object obj){
        if (!(obj instanceof Player)){
            return false;
        }
        Player p = (Player)obj;
        return p.id == this.id;
    }

    public void setGoldCoin(int gold) {
        this.goldCoin = gold;
    }

    public int getPlayerType() {
        return playerType;
    }

    public void setPlayerType(int playerType) {
        this.playerType = playerType;
    }

    public boolean isNpc() {
        return playerType==1;
    }

    public int getPayMember() {
        return payMember;
    }

    public void setPayMember(int payMember) {
        this.payMember = payMember;
    }

    public Date getRechargeTime() {
        return rechargeTime;
    }

    public void setRechargeTime(Date rechargeTime) {
        this.rechargeTime = rechargeTime;
    }
}
