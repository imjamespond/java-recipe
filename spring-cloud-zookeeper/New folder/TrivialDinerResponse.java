package com.metasoft;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A Trivial Diner Response.
 * 
 * @author sumiyapathak
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrivialDinerResponse {

	/**open or closed**/
	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}