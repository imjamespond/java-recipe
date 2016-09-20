package com.james.hibernate;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * @author cwd
 *
 */
@Entity
@Table(name="goods")
public class Goods extends PersistObject {
	
	public static Goods getById(String id) {
		return PersistObject.get(Goods.class, id);
	}
	
	public static List<Goods> getByName(String name) {
		String hql =  "from Goods a where a.name=?";//a.date between :dateBegin and :dateEnd";
		Object[] objs = {name};
		List<Goods> list = PersistObject.query(hql, objs,0 ,1);
		return list;
	}
	
	
	//商品编号id，商品名称name，商品类型type，商品价格price
	@Id
	@Column(name="id",length=32,nullable=false,unique=true)
	@GenericGenerator(name="generator",strategy="uuid.hex")//strategy="increment")
	@GeneratedValue(generator="generator")
	private String id;
	@Column
	private String name;
	@Column
	private String type;
	@Column
	private double price;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	
	@Override
	public String id() {
		return id;
	}

}