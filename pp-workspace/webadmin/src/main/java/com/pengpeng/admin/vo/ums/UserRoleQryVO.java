package com.pengpeng.admin.vo.ums;

/**
 * 用户权限信息
 * @author kenny
 *
 */

public class UserRoleQryVO {
	
    private Integer id;
    
    private Integer parent;
	
	private String name;	
		
	private boolean selected;
	
	private Integer userId;
	
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

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}	

}
