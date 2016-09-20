package com.pengpeng.stargame.player.rule;

import com.pengpeng.stargame.model.BaseEntity;

import javax.persistence.*;

/**
 * 玩家升级规则
 * @author 林佛权
 *
 */
@Entity()
@Table(name="sg_rule_player_upgrade")
public class PlayerRule extends BaseEntity<String> {
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
	public String getKey() {
		return id;
	}

    @Override
    public void setKey(String key) {
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
