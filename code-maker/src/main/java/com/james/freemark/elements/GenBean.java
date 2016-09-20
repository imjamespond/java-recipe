package com.james.freemark.elements;


import java.util.ArrayList;
import java.util.List;

public class GenBean {
    private String pkg;
    private String cls;
    private String clsDesc ="";
    private String fName;
    private String idSeq;
    private String event="";
    private String table ="";
    private List<Item> items;

    public GenBean(){
        items = new ArrayList<Item>();
    }
    public GenBean(String pkg,String name,String desc){
        this.pkg = pkg;
        this.cls = name;
        this.clsDesc = desc;
        items = new ArrayList<Item>();
    }
    public void addMethod(String type,String name){
        items.add(new Item(type,name));
    }
    public void addMethod(String type,String name,String desc){
        items.add(new Item(type,name,desc));
    }
    public String getPkg() {
        return pkg;
    }

    public void setPkg(String pkg) {
        this.pkg = pkg;
    }

    public String getCls() {
        return cls;
    }

    public void setCls(String cls) {
        this.cls = cls;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getClsDesc() {
        return clsDesc;
    }

    public void setClsDesc(String clsDesc) {
        this.clsDesc = clsDesc;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }
	public String getTable() {
		return table;
	}
	public void setTable(String table) {
		this.table = table;
	}
	public String getIdSeq() {
		return idSeq;
	}
	public void setIdSeq(String idSeq) {
		this.idSeq = idSeq;
	}
	public String getEvent() {
		return event;
	}
	public void setEvent(String event) {
		this.event = event;
	}
}