package com.pengpeng.admin.vo.ums;

/**
 * 用户角色信息
 * @author kenny
 *
 */
public class AdminRoleQryVO {
	
    private Integer id;
    
    private Integer parent;
	
	private String name;
	
	private String description;
	
	private Integer status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParent() {
		return parent;
	}

	public void setParent(Integer parent) {
		this.parent = parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
