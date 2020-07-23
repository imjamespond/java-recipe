package com.test;

import org.apache.http.HttpHost;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Cancellable;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;

import java.io.IOException;

//_analyzer API
public class Test2_Analyzer {

    public static void main(String[] args) throws IOException, InterruptedException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.0.254", 9200, "http")
                ));

        Test2_Analyzer.GetRequest(client);

        client.close();
    }

    /*
    curl -XGET http://192.168.0.254:9200/_analyze -H 'Content-Type:application/json' -d'
    {
        "analyzer": "standard",
        "text" : "Mastering Elasticsearch , elasticsearch in Action"
    }'
    {"tokens":
        [{"token":"mastering","start_offset":0,"end_offset":9,"type":"<ALPHANUM>","position":0},...

    curl -XPOST http://192.168.0.254:9200/some-index/_analyze -H 'Content-Type:application/json' -d'
    {
        "field": "title",
         "text": "Mastering Elasticesearch"
    }'
    */

/*
    curl -XGET http://192.168.0.254:9200/_analyze -H 'Content-Type:application/json' -d'
    {
        "analyzer": "icu_analyzer",
            "text": "各国有企业相继倒闭"
    }'
    https://www.elastic.co/guide/en/elasticsearch/plugins/current/analysis-icu.html
    bin/elasticsearch-plugin install file:///tmp/analysis-icu-7.8.0.zip
    then restart es
*/
/*
    https://github.com/medcl/elasticsearch-analysis-ik
    curl -XGET http://192.168.0.254:9200/_analyze -H 'Content-Type:application/json' -d'
    {
        "analyzer": "ik_smart",
            "text": "中国驻洛杉矶领事馆遭亚裔男子枪击 嫌犯已自首"
    }'

    create a index:
    curl -XPUT http://192.168.0.254:9200/test-ik-index
    create a mapping:
    curl -XPOST http://192.168.0.254:9200/test-ik-index/_mapping -H 'Content-Type:application/json' -d'
    {
            "properties": {
                "content": {
                    "type": "text",
                    "analyzer": "ik_max_word",
                    "search_analyzer": "ik_smart"
                }
            }

    }'
    index some docs:
    curl -XPOST http://192.168.0.254:9200/test-ik-index/_create/1 -H 'Content-Type:application/json' -d'
    {"content":"美国留给伊拉克的是个烂摊子吗"}
    '
    curl -XPOST http://192.168.0.254:9200/test-ik-index/_create/2 -H 'Content-Type:application/json' -d'
    {"content":"公安部：各地校车将享最高路权"}
    '
    query with highlighting:
    curl -XPOST http://192.168.0.254:9200/test-ik-index/_search  -H 'Content-Type:application/json' -d'
    {
        "query" : { "match" : { "content" : "公安" }},
        "highlight" : {
            "pre_tags" : ["<tag1>", "<tag2>"],
            "post_tags" : ["</tag1>", "</tag2>"],
            "fields" : {
                "content" : {}
            }
        }
    }
    '
*/


    private static void GetRequest(RestHighLevelClient client) throws IOException, InterruptedException {

    }

}
