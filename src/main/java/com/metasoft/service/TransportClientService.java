package com.metasoft.service;
import java.net.InetAddress;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransportClientService {
	
	private static Logger logger = LoggerFactory.getLogger(TransportClientService.class);
	
	@SuppressWarnings("resource")
	public static TransportClient GetTransportClient(String esHost, String esCluster) {
		TransportClient client = null;
		try {
			String hostName = esHost.split(":")[0];
			int port = Integer.parseInt(esHost.split(":")[1]);

			Settings settings = Settings.builder().put("cluster.name", esCluster).build();
			client = new PreBuiltTransportClient(settings)
					.addTransportAddress(new TransportAddress(InetAddress.getByName(hostName), port));
			return client;
		} catch (Exception e) {
			logger.error("failed to init TableModelFullTextSearchService", e);
		}
		return null;
	}
	

}
