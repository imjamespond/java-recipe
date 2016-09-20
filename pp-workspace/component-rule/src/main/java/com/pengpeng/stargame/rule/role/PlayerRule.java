package com.pengpeng.stargame.rule.role;

import com.pengpeng.stargame.exception.GameException;
import com.pengpeng.stargame.model.Player;
import com.pengpeng.stargame.rule.IUpgradeRule;

import javax.persistence.*;

/**
 * 玩家升级规则
 * @author 林佛权
 *
 */
@Entity()
@Table(name="sg_rule_player_upgrade")
public class PlayerRule implements IUpgradeRule<Player, Player, Player, String> {
    @Id
    private String id;
    @Column
	private int level;
	@Column
    private int exp;

    @Column
    private int type;//角色类型

    public PlayerRule(){
		
	}
	public PlayerRule(int level){
		this.level = level;
	}

    @Override
    public void checkUpgrade(Player bean) throws GameException {
        //TODO:方法需要实现
    }

    @Override
	public boolean canUpgrade(Player bean) {
		return true;
	}

	@Override
	public void consumeUpgrade(Player bean) {

	}

	@Override
	public void effectUpgrade(Player bean) {
        //bean.getBattle().setAct(battle.getAct());
        //TODO:补充全部属性
	}

	@Override
	public String getKey() {
		return id;
	}

    @Override
    public void setKey(String key) {
        //TODO:方法需要实现
    }

    public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getExp() {
		return exp;
	}

	public void setExp(int exp) {
		this.exp = exp;
	}

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
