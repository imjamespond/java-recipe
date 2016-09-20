package com.test.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

/**
 * @author james
 *联系方式
 */
@Table("web_contact")
public class Contact {
	@Id("web_contact_id_seq")
	@Column("uid")
	private long uid;
	@Column("name")
	private String name;
	@Column("mobile")
	private String mobile;
	@Column("email")
	private String email;
	@Column("address")
	private String address;
	@Column("postcode")
	private String postcode;
	

	public Contact() {
		super();
	}

	public Contact(long uid) {
		super();
		this.uid = uid;
	}

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}


}