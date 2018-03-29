package com.metasoft.model;



import com.keymobile.dataSharingMgr.interfaces.resource.TableModel;

public class ExtTableModel extends TableModel implements UserAccessible {
	
	private boolean isUserAccessible = false;
	
	private boolean isSubscribed = false; //是否订阅
	private boolean isSelected = false; //是否精选
	private boolean subsrcibable = false; // 是否可订阅
	private boolean selectable = false; //是否可精选
	
	private String misc;
	
	public ExtTableModel(TableModel tableModel, boolean isUserAccessible) {
		super(tableModel.getSQL(), tableModel.getMoldType());
		super.setTableModelId(tableModel.getTableModelId());
		super.setDbAddressId(tableModel.getDBAddressId());
		super.setTenantId(tableModel.getTenantId());
		super.setDirId(tableModel.getDirId());
		super.setName(tableModel.getName());
		super.setNameInSource(tableModel.getNameInSource());
		super.setRemarks(tableModel.getRemarks());
		super.setIsEnabled(tableModel.getIsEnabled());
		super.setDBTableId(tableModel.getDBTableId());
		tableModel.getAttachedColumns().forEach(c -> super.addAttachedColumn(c));
		super.setType(tableModel.getType());
		super.setSingleFlag(tableModel.getSingleFlag());
		super.setCycle(tableModel.getCycle());
		
		this.isUserAccessible = isUserAccessible;
		setSubsrcibable(tableModel.getType().equals("kpi") || tableModel.getType().equals("analyticalModel"));
	}
	
	public boolean isSubscribed() {
		return isSubscribed;
	}

	public void setSubscribed(boolean isSubscribed) {
		this.isSubscribed = isSubscribed;
	}
	
	public boolean isSelected() {
		return isSelected;
	}
	
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	public void setSelectable(boolean selectable) {
		this.selectable = selectable;
	}
	
	public boolean isSelectable() {
		return selectable;
	}

	public boolean isUserAccessible() {
		return isUserAccessible;
	}
	
	public void setUserAccessible(boolean isUserAccessible) {
		this.isUserAccessible = isUserAccessible;
	}

	public boolean isSubsrcibable() {
		return subsrcibable;
	}

	public void setSubsrcibable(boolean subsrcibable) {
		this.subsrcibable = subsrcibable;
	}
	
	public void setMisc(String misc) {
		this.misc = misc;
	}
	
}
