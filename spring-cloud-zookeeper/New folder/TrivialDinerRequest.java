package com.metasoft;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Trivial Diner Request.
 * 
 * @author sumiyapathak
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrivialDinerRequest {

	/**The Day of Week, 1 = Sunday, 2, 3, ...**/
	private String day;

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}
	
}