package com.metasoft.empire.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

/**
 * @author james
 *涉及直接更新资料库的本栏位
 */
@Table("user_wealth")
public class UserWealthPersist {
	public UserWealthPersist() {

	}

	public UserWealthPersist(long id) {
		super();
		this.id = id;

	}
	@Id("wealth_id_seq")
	@Column("id")
	private Long id;
	@Column("rose")
	private int rose;
	@Column("gems")
	private int gems;
	@Column("apple")
	private int apple;
	@Column("credit")
	private int credit;//积分

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getRose() {
		return rose;
	}

	public void setRose(int rose) {
		this.rose = rose;
	}

	public int getGems() {
		return gems;
	}

	public void setGems(int gems) {
		this.gems = gems;
	}

	public int getApple() {
		return apple;
	}

	public void setApple(int apple) {
		this.apple = apple;
	}

	public int getCredit() {
		return credit;
	}

	public void setCredit(int credit) {
		this.credit = credit;
	}


}
