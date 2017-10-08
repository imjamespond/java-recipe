package com.metasoft;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A trivial client request to map the diner request properties.
 * 
 * @author sumiyapathak
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrivialClientRequest {

	public String getDay() {
		return day;
	}

	public void setDay(String day) {
		this.day = day;
	}

	private String day;

}