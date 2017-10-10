package com.metasoft.dependencies;

import org.springframework.web.client.RestTemplate;

/**
 * @author Marcin Grzejszczak
 */
public class TestRibbonClient extends TestServiceRestClient {

	private final String thisAppName;

	public TestRibbonClient(RestTemplate restTemplate) {
		super(restTemplate);
		this.thisAppName = "someName";
	}

	public TestRibbonClient(RestTemplate restTemplate, String thisAppName) {
		super(restTemplate);
		this.thisAppName = thisAppName;
	}

	public String thisHealthCheck() {
		return this.restTemplate
				.getForObject("http://" + this.thisAppName + "/health", String.class);
	}

	public Integer thisPort() {
		return this.restTemplate
				.getForObject("http://" + this.thisAppName + "/port", Integer.class);
	}
}
