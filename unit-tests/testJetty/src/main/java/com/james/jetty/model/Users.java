package com.james.jetty.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.james.hibernate.PersistObject;

/**
 * @author cwd
 *
 */
@Entity
@Table(name="users")
public class Users extends PersistObject {
	
	public static Users getById(String id) {
		return PersistObject.get(Users.class, id);
	}
	
	public static List<Users> getByName(String name) {
		String hql =  "from Users a where a.username=?";//a.date between :dateBegin and :dateEnd";
		Object[] objs = {name};
		List<Users> list = PersistObject.query(hql, objs, 0, 1);
		return list;
	}
	
	
	@Id
	@Column(name="id",length=32,nullable=false,unique=true)
	@GenericGenerator(name="generator",strategy="native")//strategy="uuid.hex")
	@GeneratedValue(generator="generator")
	private long id;
	@Column
	private String username;
	@Column
	private String password;
	@Column
	private String realname;
	@Column
	private String contact;
	@Column
	private String email;
	@Column
	private short enabled = 1;
	

	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public short getEnabled() {
		return enabled;
	}

	public void setEnabled(short enabled) {
		this.enabled = enabled;
	}

	
	public String getRealname() {
		return realname;
	}

	public void setRealname(String realname) {
		this.realname = realname;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String id() {
		return String.valueOf(id);
	}

}