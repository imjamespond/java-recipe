package com.metasoft.service;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrError;
import com.keymobile.dataSharingMgr.interfaces.DataSharingMgrService;
import com.keymobile.dataSharingMgr.interfaces.resource.TableModel;

import static org.elasticsearch.common.xcontent.XContentFactory.*;

@Service
public class TableModelFullTextSearchService {

	private static final String INDEX_NAME = "table-models";
	private static final String TYPE_NAME = "doc";

	@Value("${es.host}")
	private String esHost;
	@Value("${es.cluster}")
	private String esCluster;

	private TransportClient client;
	@Autowired
	private DataSharingMgrService dataSharingMgrService;

	private Logger logger = LoggerFactory.getLogger(TableModelFullTextSearchService.class);
	
	@PostConstruct
	private void init() {
		try {
			String hostName = esHost.split(":")[0];
			int port = Integer.parseInt(esHost.split(":")[1]);

			Settings settings = Settings.builder().put("cluster.name", esCluster).build();
			client = new PreBuiltTransportClient(settings)
					.addTransportAddress(new TransportAddress(InetAddress.getByName(hostName), port));

			IndicesExistsResponse reponse = client.admin().indices().exists(new IndicesExistsRequest(INDEX_NAME)).get();
			if (!reponse.isExists()) {
				client.admin().indices().prepareCreate(INDEX_NAME).get();

				client.admin().indices().preparePutMapping(INDEX_NAME).setType(TYPE_NAME)
						.setSource(jsonBuilder()
									.startObject().startObject("properties")
										.startObject("id")
											.field("type", "keyword")
										.endObject()
										.startObject("name")
											.field("type", "text")
										.endObject()
										.startObject("category")
											.field("type", "keyword")
										.endObject()
									.endObject().endObject())
						.get();
			}
		} catch (Exception e) {
			logger.error("failed to init TableModelFullTextSearchService", e);
		}
	}

	public void indexTableModel(TableModel tableModel) {
		try {
			client.prepareIndex(INDEX_NAME, TYPE_NAME)
					.setSource(jsonBuilder()
						.startObject()
							.field("id", tableModel.getTableModelId())
							.field("name", tableModel.getName())
							.field("category", tableModel.getNameInSource() == null ? null : tableModel.getNameInSource().split("\\.")[0])
						.endObject())
					.get();
		} catch (IOException e) {
			logger.error("failed to index {" + tableModel.getTableModelId() + "}", e);
		}
	}

	public void unindexTableModel(String tableModelId) {
		DeleteByQueryAction.INSTANCE.newRequestBuilder(client).filter(QueryBuilders.termQuery("id", tableModelId))
				.source(INDEX_NAME).get();
	}

	public List<TableModel> searchTableModelsByPath(String path) {		
		Map<String, TableModel> tableModels = new HashMap<String, TableModel>();
		SearchResponse searchResponse = client.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME)
				.setQuery(QueryBuilders.matchPhraseQuery("category", path)).get();
		
		SearchHits searchHits = searchResponse.getHits();
		Iterator<SearchHit> i = searchHits.iterator();
		while (i.hasNext()) {
			SearchHit hit = i.next();
	
			String tableModelId = ((String) hit.getSourceAsMap().get("id"));
			TableModel tableModel = null;
			try {
				tableModel = dataSharingMgrService.getTableModelById(tableModelId);
			} catch (DataSharingMgrError e) {
				logger.error("searchTableModelsByPath error", e);
			}
			if (tableModel != null && tableModel.isEnabled())
				tableModels.put(tableModel.getTableModelId(), tableModel);
		}

		return Arrays.asList(tableModels.values().toArray(new TableModel[tableModels.keySet().size()]));
	}
	
	public List<TableModel> searchTableModelsByName(String query) {
		Map<String, TableModel> tableModels = new HashMap<String, TableModel>();
		SearchResponse scrollResp = client.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME)
				.setQuery(QueryBuilders.queryStringQuery("*" + query + "*"))
				.setScroll(new TimeValue(60000)).setSize(1000).get();
		do {
			for (SearchHit hit : scrollResp.getHits().getHits()) {
				String tableModelId = ((String) hit.getSourceAsMap().get("id"));
				TableModel tableModel = null;
				try {
					tableModel = dataSharingMgrService.getTableModelById(tableModelId);
				} catch (DataSharingMgrError e) {
					logger.error("searchTableModelsByName error", e);
				}
				if (tableModel != null && tableModel.isEnabled())
					tableModels.put(tableModel.getTableModelId(), tableModel);
			}
			
		    scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
		} while(scrollResp.getHits().getHits().length != 0);
				
		return Arrays.asList(tableModels.values().toArray(new TableModel[tableModels.keySet().size()]));
	}
	
}
