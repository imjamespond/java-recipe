package com.qianxun.model;

import org.copycat.framework.annotation.Column;
import org.copycat.framework.annotation.Id;
import org.copycat.framework.annotation.Table;

@Table("applerecord")
public class ApplePersist{
	public ApplePersist() {

	}

	@Id("applerecord_id_seq")
	@Column("id")
	private Long id;
	@Column("fromuid")
	private Long fromuid;
	@Column("touid")
	private Long touid;
	@Column("number")
	private Integer number;
	@Column("time")
	private Long time;
	@Column("nickname")
	private String nickname;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFromuid() {
		return fromuid;
	}

	public void setFromuid(Long fromuid) {
		this.fromuid = fromuid;
	}

	public Long getTouid() {
		return touid;
	}

	public void setTouid(Long touid) {
		this.touid = touid;
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

}
