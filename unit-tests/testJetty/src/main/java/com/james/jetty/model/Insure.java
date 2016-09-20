package com.james.jetty.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import com.james.hibernate.PersistObject;

/**
 * @author cwd
 * 
 */
@Entity
@Table(name = "insure")
public class Insure extends PersistObject {

	public static Insure getById(String id) {
		return PersistObject.get(Insure.class, id);
	}

	public static List<Insure> getByUid(Long uid, int start,int size) {
		String hql = "from Insure a where a.uid=?";// a.date between :dateBegin
													// and :dateEnd";
		Object[] objs = { uid };
		List<Insure> list = PersistObject.query(hql, objs, start, size);
		return list;
	}

	@Id
	@Column(name = "id", length = 32, nullable = false)
	private long id;
	@Column
	private long uid;
	// @Column(columnDefinition="TEXT")
	@Column
	private String express;
	@Column
	private String expressid;
	@Column
	private String item;
	@Column
	private double ivalue;
	@Column
	private double cost;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getExpress() {
		return express;
	}

	public void setExpress(String express) {
		this.express = express;
	}

	public String getExpressid() {
		return expressid;
	}

	public void setExpressid(String expressid) {
		this.expressid = expressid;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}



	public double getIvalue() {
		return ivalue;
	}

	public void setIvalue(double ivalue) {
		this.ivalue = ivalue;
	}

	public double getCost() {
		return cost;
	}

	public void setCost(double cost) {
		this.cost = cost;
	}

	@Override
	public String id() {
		return String.valueOf(id);
	}

}