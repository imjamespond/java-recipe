package com.metasoft.empire.model.data;

import org.copycat.framework.annotation.Table;

import com.metasoft.empire.common.annotation.DescAnno;

@Table("/data/role.xls")
public class RoleData{
	@DescAnno("id")
	protected int id;
	@DescAnno("类型")
	protected int type;
	@DescAnno("文明")
	protected int civil;
	@DescAnno("")
	protected int basehp;
	@DescAnno("")
	protected int baseatk;
	@DescAnno("")
	protected int factorhp;
	@DescAnno("")
	protected int factoratk;
	@DescAnno("")
	protected int defence;
	@DescAnno("")
	protected int factorheal;
	@DescAnno("名称")
	protected String name;
	@DescAnno("描述")
	protected String descript;
	@DescAnno("类型名称")
	protected String civilname;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getBasehp() {
		return basehp;
	}
	public void setBasehp(int basehp) {
		this.basehp = basehp;
	}
	public int getBaseatk() {
		return baseatk;
	}
	public void setBaseatk(int baseatk) {
		this.baseatk = baseatk;
	}
	public int getFactorhp() {
		return factorhp;
	}
	public void setFactorhp(int factorhp) {
		this.factorhp = factorhp;
	}
	public int getFactoratk() {
		return factoratk;
	}
	public void setFactoratk(int factoratk) {
		this.factoratk = factoratk;
	}
	public int getDefence() {
		return defence;
	}
	public void setDefence(int defence) {
		this.defence = defence;
	}
	public int getFactorheal() {
		return factorheal;
	}
	public void setFactorheal(int factorheal) {
		this.factorheal = factorheal;
	}
	public String getDescript() {
		return descript;
	}
	public void setDescript(String descript) {
		this.descript = descript;
	}
	public int getCivil() {
		return civil;
	}
	public void setCivil(int civil) {
		this.civil = civil;
	}
	public String getCivilname() {
		return civilname;
	}
	public void setCivilname(String civilname) {
		this.civilname = civilname;
	}

}
