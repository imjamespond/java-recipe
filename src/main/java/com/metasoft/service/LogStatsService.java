package com.metasoft.service;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsRequest;
import org.elasticsearch.action.admin.indices.exists.indices.IndicesExistsResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms.Bucket;
import org.elasticsearch.search.aggregations.metrics.cardinality.CardinalityAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.cardinality.InternalCardinality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class LogStatsService {
	
	private Logger logger = Logger.getLogger(LogStatsService.class);
	
	private static final String TIMESTAMP_FIELD = "@timestamp";
	private static final String NOWTIME_FIELD = "nowTime";	
	private static final String DAY_TIME_FORMAT = "yyyy-MM-dd";
	private static final String MONTH_TIME_FORMAT = "yyyy-MM";
	
	private static final String SQLENGINE_SEARCH_INDEX = "sqlengine-sql-*";
	private static final String PC_DATAQUERY_SEARCH_INDEX = "pc_query_state";
	private static final String BELONG_TENANTID_FIELD = "belong_tenantId.keyword";
	private static final String VISIT_TENANTID_FIELD = "visit_tenantId.keyword";
	
	private static String TABLE_TERMS_NAME = "group_tableModel";
	private static String USER_TERMS_NAME = "group_user";
	private static String DISTINCT_USER_NAME = "distinct_user";
	
	private static String TABLE_TERMS_FIELD = "tableModel.keyword";
	private static String LOGIN_USER_FIELD = "loginUser.keyword";
	
	
	private static final String PC_QUERY_STATE_INDEX_NAME = "pc_query_state";
	private static final String MOBILE_QUERY_STATE_INDEX_NAME = "mobile_query_state";
	private static final String TYPE_NAME = "doc";
	
	@Autowired
	private TransportClient client;
	
	@PostConstruct
	public void init() {
		try {
			IndicesExistsResponse reponse = client.admin().indices().exists(new IndicesExistsRequest(PC_QUERY_STATE_INDEX_NAME)).get();
			if (!reponse.isExists()) {
				client.admin().indices().prepareCreate(PC_QUERY_STATE_INDEX_NAME).get();
				client.admin().indices().preparePutMapping(PC_QUERY_STATE_INDEX_NAME).setType(TYPE_NAME)
						.setSource(jsonBuilder()
									.startObject().startObject("properties")
										.startObject("belong_tenantId")
											.field("type", "keyword")
										.endObject()
										.startObject("tableModel")
											.field("type", "keyword")
										.endObject()
										.startObject("@timestamp")
										.field("type", "date")
										.endObject()
									.endObject().endObject())
						.get();
			}
			
			reponse = client.admin().indices().exists(new IndicesExistsRequest(MOBILE_QUERY_STATE_INDEX_NAME)).get();
			if (!reponse.isExists()) {
				client.admin().indices().prepareCreate(MOBILE_QUERY_STATE_INDEX_NAME).get();
				client.admin().indices().preparePutMapping(MOBILE_QUERY_STATE_INDEX_NAME).setType(TYPE_NAME)
						.setSource(jsonBuilder()
									.startObject().startObject("properties")
										.startObject("belong_tenantId")
											.field("type", "keyword")
										.endObject()
										.startObject("@timestamp")
											.field("type", "date")
										.endObject()
									.endObject().endObject())
						.get();
			}
		} catch (Exception e) {
			logger.error("init fail", e);
		}
	}
	
	
	public enum Cycle
	{ 
		DAY("day"), MONTH("month");
		public String cycle;
		Cycle(String cycle){
			this.cycle = cycle;
		}
		public String getCycle() {
			return cycle;
		}
	}
	
	public Map<String, Long> getAdminResourceVisitedState(Integer top, long beginTime, long endTime) {
		return getTenantResourceVisitedState("", top, beginTime, endTime);
	}
	
	public Map<String, Long> getTenantResourceVisitedState(String tenantId, Integer top, long beginTime, long endTime) {
		  SearchResponse searchResponse = null;
		  SearchRequestBuilder searchRequest =  client.prepareSearch(SQLENGINE_SEARCH_INDEX)
				  	.setQuery(QueryBuilders.rangeQuery(TIMESTAMP_FIELD).lt(endTime).gt(beginTime));
		  if (!StringUtils.isEmpty(tenantId))
		  searchRequest =  searchRequest
		  			.setQuery(QueryBuilders.termQuery(BELONG_TENANTID_FIELD, tenantId));
		  searchResponse = searchRequest.addAggregation(AggregationBuilders.terms(TABLE_TERMS_NAME).field(TABLE_TERMS_FIELD).size(top))
	                .get();
	    Terms terms = searchResponse.getAggregations().get(TABLE_TERMS_NAME);
		List<Bucket> buckets = (List<Bucket>)terms.getBuckets();
		Map<String, Long> tenantResVisitState = new LinkedHashMap<>();
		for (Bucket bucket : buckets) {
			tenantResVisitState.put(bucket.getKeyAsString(), bucket.getDocCount());
		}
		return tenantResVisitState;
	}
	
	public Map<String, Long> getTenantVisitResourceState(String tenantId, Integer top, long beginTime, long endTime) {
		SearchResponse searchResponse = client.prepareSearch(SQLENGINE_SEARCH_INDEX)
	                .setQuery(QueryBuilders.matchQuery(VISIT_TENANTID_FIELD, tenantId))
	                .setQuery(QueryBuilders.rangeQuery(TIMESTAMP_FIELD).lt(endTime).gt(beginTime))
	                .setSearchType(SearchType.QUERY_THEN_FETCH)
	                .addAggregation(AggregationBuilders.terms(TABLE_TERMS_NAME).field(TABLE_TERMS_FIELD).size(top))
	                .get();
	    Terms terms = searchResponse.getAggregations().get(TABLE_TERMS_NAME);
		List<Bucket> buckets = (List<Bucket>)terms.getBuckets();
		Map<String, Long> tenantResVisitState = new LinkedHashMap<>();
		for (Bucket bucket : buckets) {
			tenantResVisitState.put(bucket.getKeyAsString(), bucket.getDocCount());
		}
		return tenantResVisitState;
	}
	
	public List<Map<String, Object>> getDayActiveUserNumRecently(Integer recentlyDay) {
		return getDayOrMonthAggretionRecently(LOGIN_USER_FIELD, recentlyDay, Cycle.DAY);
	}
	
	public List<Map<String, Object>> getMonthActiveUserNumRecently(Integer recentlyMonth) {
		return getDayOrMonthAggretionRecently(LOGIN_USER_FIELD, recentlyMonth, Cycle.MONTH);
	}
	
	public List<Map<String, Object>> getMonthActiveResourceNumRecently(Integer recentlyMonth) {
		return getDayOrMonthAggretionRecently(TABLE_TERMS_FIELD, recentlyMonth, Cycle.MONTH);
	}
	
	private List<Map<String, Object>> getDayOrMonthAggretionRecently(String cardinalityField, Integer recentlyTime, LogStatsService.Cycle cycle) {
		Calendar calendar = Calendar.getInstance();
		if (cycle.equals(Cycle.DAY)) {
			calendar.add(Calendar.DAY_OF_MONTH, - recentlyTime);
		} else {
			calendar.add(Calendar.MONTH, - recentlyTime);
		}
		long gtTime = calendar.getTime().getTime();
		CardinalityAggregationBuilder distinctAgg = AggregationBuilders.cardinality(DISTINCT_USER_NAME)
				.field(cardinalityField);
        DateHistogramAggregationBuilder dateAgg = AggregationBuilders.dateHistogram(USER_TERMS_NAME);
        dateAgg.field(NOWTIME_FIELD);
        if (cycle.equals(Cycle.DAY)) {
        	dateAgg.dateHistogramInterval(DateHistogramInterval.DAY);
        	dateAgg.format(DAY_TIME_FORMAT);
        } else {
        	dateAgg.dateHistogramInterval(DateHistogramInterval.MONTH);
        	dateAgg.format(MONTH_TIME_FORMAT);
        }
		SearchResponse searchResponse = client.prepareSearch(SQLENGINE_SEARCH_INDEX)
					.setQuery(QueryBuilders.rangeQuery(NOWTIME_FIELD).gt(gtTime))
	                .setSearchType(SearchType.QUERY_THEN_FETCH)
	                .addAggregation(dateAgg.subAggregation((distinctAgg)))
	                .get();
		Histogram h=searchResponse.getAggregations().get(USER_TERMS_NAME);
		List<? extends org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram.Bucket> buckets = (List<? extends org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram.Bucket>) h.getBuckets();  
		Map<String, Long> userVisitState = new TreeMap<>();
		for (org.elasticsearch.search.aggregations.bucket.histogram.InternalDateHistogram.Bucket bucket : buckets) {
            Aggregations sub = bucket.getAggregations();  
            InternalCardinality count = sub.get(DISTINCT_USER_NAME);
			userVisitState.put(bucket.getKeyAsString(), count.getValue() );
		}
		
		List<Map<String, Object>> xyAxis = new ArrayList<>();
		for (String timeStr : getTimeStrRecently(recentlyTime, cycle)) {
			Long yValue = userVisitState.get(timeStr) == null ? 0L : userVisitState.get(timeStr);
			userVisitState.put(timeStr, yValue);
			Map<String, Object> xy = new HashMap<>();
			xy.put("day", timeStr);
			xy.put("count", yValue);
			xyAxis.add(xy);
		}
		return xyAxis;
		//return changeToXyAxis(userVisitState);
	}
	
	private Map<String, Long> changeToXyAxis(Map<String, Long> userVisitState) {
		Map<String, Long> xyAxis = new TreeMap<>(new MapKeyComparator());
		int xIndex = userVisitState.size();
		for (String timeStr : userVisitState.keySet()) {
			xyAxis.put(xIndex + "", userVisitState.get(timeStr));
			xIndex --;
		}
		return xyAxis;
	}
	
	class MapKeyComparator implements Comparator<String> {
	    @Override
	    public int compare(String str1, String str2) {
	        int p1 = Integer.valueOf(str1);
	        int p2 = Integer.valueOf(str2);
	        return p1 - p2;
	    }
	}

	private List<String> getTimeStrRecently(Integer recentlyDay, Cycle cycle) {
		 Calendar calendar = Calendar.getInstance();
		 Date date = calendar.getTime();
		 List<String> timeStr = new ArrayList<String>();
		 for (int a = recentlyDay - 1 ; a >= 0; a--) {
			 calendar.setTime(date);
			 calendar.add(cycle.equals(Cycle.DAY) ? Calendar.DAY_OF_MONTH : Calendar.MONTH, -a);
			 Date today = calendar.getTime();  
			 SimpleDateFormat format = new SimpleDateFormat(cycle.equals(Cycle.DAY) ? DAY_TIME_FORMAT : MONTH_TIME_FORMAT);  
			 String result = format.format(today);
			 timeStr.add(result);
		 }
		 return timeStr;
	}

	public Integer getPcDataVisitState(String tenantId, long beginTime, long endTime) {
		  SearchResponse searchResponse = null;
		  SearchRequestBuilder searchRequest =  client.prepareSearch(PC_DATAQUERY_SEARCH_INDEX)
				  	.setQuery(QueryBuilders.rangeQuery(TIMESTAMP_FIELD).lt(endTime).gt(beginTime));
		  if (!StringUtils.isEmpty(tenantId))
			  searchRequest =  searchRequest
			  			.setQuery(QueryBuilders.termQuery("belong_tenantId", tenantId));
		 searchResponse = searchRequest.get();
		 SearchHits hits = searchResponse.getHits();
		 return new Long(hits.getTotalHits()).intValue();
	}
	
	public Integer getMobileDataVisitState(String tenantId, long beginTime, long endTime) {
		  SearchResponse searchResponse = null;
		  SearchRequestBuilder searchRequest =  client.prepareSearch(MOBILE_QUERY_STATE_INDEX_NAME)
				  	.setQuery(QueryBuilders.rangeQuery(TIMESTAMP_FIELD).lt(endTime).gt(beginTime));
		  if (!StringUtils.isEmpty(tenantId))
			  searchRequest =  searchRequest
			  			.setQuery(QueryBuilders.termQuery("belong_tenantId", tenantId));
		 searchResponse = searchRequest.get();
		 SearchHits hits = searchResponse.getHits();
		 return new Long(hits.getTotalHits()).intValue();
	}
	
	public Integer getAdminMobileDataVisitState(long beginTime, long endTime) {
		return getMobileDataVisitState("", beginTime, endTime);
	}
	
	public Integer getAdminPcDataVisitState(long beginTime, long endTime) {
		return getPcDataVisitState("", beginTime, endTime);
	}
	
	public void indexPcQueryState(String tenantId, String tableName) {
		try {
			client.prepareIndex(PC_QUERY_STATE_INDEX_NAME, TYPE_NAME)
					.setSource(jsonBuilder()
						.startObject()
							.field("belong_tenantId", tenantId)
							.field("tableModel", tableName)
							.field("@timestamp", System.currentTimeMillis())
						.endObject())
					.get();
		} catch (Exception e) {
			logger.error("failed to index {" + tableName + "}", e);
		}
	}
	
	public void indexMobileQueryState(String tenantId) {
		try {
			client.prepareIndex(MOBILE_QUERY_STATE_INDEX_NAME, TYPE_NAME)
					.setSource(jsonBuilder()
						.startObject()
							.field("belong_tenantId", tenantId)
							.field("@timestamp", System.currentTimeMillis())
						.endObject())
					.get();
		} catch (Exception e) {
			logger.error("failed to indexMobileQueryState.", e);
		}
	}
	
}
