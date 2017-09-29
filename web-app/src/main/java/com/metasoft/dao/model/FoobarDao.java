package com.metasoft.dao.model;

import com.metasoft.model.annotation.AutoId;
import com.metasoft.model.annotation.Column;
import com.metasoft.model.annotation.Index;
import com.metasoft.model.annotation.Table;
import com.metasoft.util.GenericDaoHelper.GenericDao;


@Table("da_foobar")
public class FoobarDao extends GenericDao{
	@AutoId
	@Column(value="id", type="bigint not null")
	Long id;
	@Index("unique")
	@Column(value="username", desc="用户名")
	String username;
	@Column(value="type_")
	String type = "";
	public FoobarDao(){}
	public FoobarDao( String username, String type) {
		this.username = username;
		this.type = type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getType() {
		return type;
	}
	
}
