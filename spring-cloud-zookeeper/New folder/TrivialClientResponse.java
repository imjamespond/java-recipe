package com.metasoft;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * A trivial client response to map the diner response properties.
 * 
 * @author sumiyapathak
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TrivialClientResponse {

	private String status;

}