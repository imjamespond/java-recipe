package com.metasoft.service;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.metasoft.model.AnalyticalModelDoc;
import com.metasoft.model.KpiDoc;

import static org.elasticsearch.common.xcontent.XContentFactory.*;

@Service
public class KpiFullTextSearchService {

	private static final String INDEX_NAME = "kpi";
	private static final String TYPE_NAME = "doc";

	@Value("${es.host}")
	private String esHost;
	@Value("${es.cluster}")
	private String esCluster;

	private TransportClient client;

	private Logger logger = LoggerFactory.getLogger(KpiFullTextSearchService.class);
	
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
									.startObject("angleName")
										.field("type", "text")
									.endObject()
									.startObject("indName")
										.field("type", "text")
									.endObject()
									.startObject("remarks")
										.field("type", "text")
									.endObject()
									.startObject("typ")
										.field("type", "integer")
									.endObject()
									.startObject("catalogId")
										.field("type", "integer")
									.endObject()
									.startObject("resPath")
										.field("type", "keyword")
									.endObject()
									.startObject("category")
										.field("type", "keyword")
									.endObject()
									.startObject("ind_disname")
										.field("type", "text")
									.endObject()
									.startObject("ind_businessBelong")
										.field("type", "text")
									.endObject()
									.startObject("ind_typ")
										.field("type", "integer")
									.endObject()
									.startObject("ind_businessDefine")
										.field("type", "text")
									.endObject()
									.startObject("ind_remarks")
										.field("type", "text")
									.endObject()
									.startObject("ind_descr")
										.field("type", "text")
									.endObject()
									.startObject("ind_unit")
										.field("type", "text")
									.endObject()
									.startObject("ind_fmt")
										.field("type", "text")
									.endObject()
									.startObject("ind_createUser")
										.field("type", "text")
									.endObject()
									.startObject("ind_createTime")
										.field("type", "text")
										.endObject()
								.endObject().endObject()).get();
			}
		} catch (Exception e) {
			logger.error("failed to init KpiFullTextSearchService", e);
		}
	}
	
	public List<String> getKpiCategories() {
		List<String> categories = new ArrayList<String>();		
		SearchResponse scrollResp = client.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME).setQuery(QueryBuilders.termQuery("typ", 0))
		        .addAggregation(AggregationBuilders.terms("distinct").field("category"))
		        .setScroll(new TimeValue(60000))
		        .setSize(1000).get(); 
		do {
		    for (SearchHit hit : scrollResp.getHits().getHits()) {
				categories.add((String) hit.getSourceAsMap().get("category"));
		    }

		    scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
		} while(scrollResp.getHits().getHits().length != 0);
		
		return categories;
	}

	public List<String> getAnalyticalModelCategories() {
		List<String> categories = new ArrayList<String>();		
		SearchResponse scrollResp = client.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME)
				.setQuery(QueryBuilders.termQuery("typ", 1))
		        .addAggregation(AggregationBuilders.terms("distinct").field("category"))
		        .setScroll(new TimeValue(60000))
		        .setSize(1000).get(); 
		do {
		    for (SearchHit hit : scrollResp.getHits().getHits()) {
				categories.add((String) hit.getSourceAsMap().get("category"));
		    }

		    scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
		} while(scrollResp.getHits().getHits().length != 0);
		
		return categories;
	}
	
	public List<KpiDoc> searchKpiDefsByCategory(String category) {
		List<KpiDoc> kpiDocs = new ArrayList<>();
		SearchResponse scrollResp = client.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME)
				.setQuery(QueryBuilders.termQuery("typ", 0))
				.setQuery(QueryBuilders.prefixQuery("category", category))
				.setScroll(new TimeValue(60000))
				.setSize(1000).get();
		do {
			for (SearchHit hit : scrollResp.getHits().getHits()) {
				String ind_disname = ((String) hit.getSourceAsMap().get("ind_disname"));
				String ind_bussinessBelong = ((String) hit.getSourceAsMap().get("ind_businessBelong"));
				String ind_bussinessDefine = ((String) hit.getSourceAsMap().get("ind_businessDefine"));
				
				String ind_descr = ((String) hit.getSourceAsMap().get("ind_descr"));
				String ind_unit = ((String) hit.getSourceAsMap().get("ind_unit"));
				String resPath = ((String) hit.getSourceAsMap().get("resPath"));
				
				kpiDocs.add(new KpiDoc(ind_disname, ind_bussinessBelong, ind_bussinessDefine, ind_descr, ind_unit, resPath));
		    }
			
		    scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
		} while(scrollResp.getHits().getHits().length != 0);
		
		return kpiDocs;
	}
	
	public List<AnalyticalModelDoc> searchAnalyticalModelDefsByCategory(String category) {
		List<AnalyticalModelDoc> analyticalModelDocs = new ArrayList<>();
		SearchResponse scrollResp = client.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME)
				.setQuery(QueryBuilders.termQuery("typ", 1))
				.setQuery(QueryBuilders.matchPhrasePrefixQuery("category", category))
				.setScroll(new TimeValue(60000))
				.setSize(1000).get();;
		do {		
			for (SearchHit hit : scrollResp.getHits().getHits()) {
				String name = ((String) hit.getSourceAsMap().get("name"));
				String angleName = ((String) hit.getSourceAsMap().get("angleName"));
				String remarks = ((String) hit.getSourceAsMap().get("remarks"));
				String resPath = ((String) hit.getSourceAsMap().get("resPath"));
				
				analyticalModelDocs.add(new AnalyticalModelDoc(name, angleName, remarks, resPath));
		    }
			
		    scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
		} while(scrollResp.getHits().getHits().length != 0);
				
		return analyticalModelDocs;
	}

	public String getKpiDefByNameAndCatalog(String name, String catalog) {
		List<KpiDoc> kpiDocs = new ArrayList<>();
		SearchResponse scrollResp = client.prepareSearch(INDEX_NAME).setTypes(TYPE_NAME)
				.setQuery(QueryBuilders.termQuery("typ", 0))
				.setQuery(QueryBuilders.termQuery("resPath", catalog))
				.setQuery(QueryBuilders.matchQuery("name", name))
				.setScroll(new TimeValue(60000))
				.setSize(1000).get();
		do {
			for (SearchHit hit : scrollResp.getHits().getHits()) {
				String ind_disname = ((String) hit.getSourceAsMap().get("ind_disname"));
				String ind_bussinessBelong = ((String) hit.getSourceAsMap().get("ind_businessBelong"));
				String ind_bussinessDefine = ((String) hit.getSourceAsMap().get("ind_businessDefine"));
				
				String ind_descr = ((String) hit.getSourceAsMap().get("ind_descr"));
				String ind_unit = ((String) hit.getSourceAsMap().get("ind_unit"));
				String resPath = ((String) hit.getSourceAsMap().get("resPath"));
				
				kpiDocs.add(new KpiDoc(ind_disname, ind_bussinessBelong, ind_bussinessDefine, ind_descr, ind_unit, resPath));
		    }
			
		    scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
		} while(scrollResp.getHits().getHits().length != 0);
		
		if (kpiDocs.size() == 0)
			return "";
		else
			return kpiDocs.get(0).toString();
	}
	
}
