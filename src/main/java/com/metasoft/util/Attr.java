package com.metasoft.util;

public class Attr implements Comparable<Attr> {
	
	public String attr;
	public String desc;
	public int prior;
	public String field;
	
	public Attr(String attr, String desc, int prior) {
		this.attr = attr;
		this.desc = desc;
		this.prior = prior;
	}
	
	public Attr(String attr, String desc, String field) {
		super();
		this.attr = attr;
		this.desc = desc;
		this.field = field;
	}

	public Attr(String attr, String desc) {
		this(attr, desc, 0);
	}
	
	@Override
	public int compareTo(Attr other) {
		if (this.prior > other.prior) 
			return 1;
		else if (this.prior < other.prior)
			return -1;
		else
			return this.attr.compareTo(other.attr);
	}
	
}