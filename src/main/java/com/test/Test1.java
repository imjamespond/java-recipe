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

public class Test1 {

    public static void main(String[] args) throws IOException, InterruptedException {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("192.168.0.254", 9200, "http")
                ));

        Test1.IndexRequest(client);
        Test1.GetRequest(client);

        client.close();
    }

    /*
    * 读取索引
    * */
    private static void GetRequest(RestHighLevelClient client) throws IOException, InterruptedException {

        GetRequest getRequest = new GetRequest( "posts", "1");
        String[] includes = new String[]{"message", "*Date", "user"};
        String[] excludes = Strings.EMPTY_ARRAY;
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includes, excludes);
        getRequest.fetchSourceContext(fetchSourceContext);
        getRequest.version(2);
        //getRequest.routing("routing");

        //GetResponse resp = client.get(getRequest, RequestOptions.DEFAULT);
        //System.out.println(resp.toString());
        Cancellable cc = client.getAsync(getRequest, RequestOptions.DEFAULT, new ActionListener() {
            public void onResponse(Object o) {
                GetResponse resp = (GetResponse) o;
                String index = resp.getIndex();
                String id = resp.getId();
                if (resp.isExists()) {
                    long version = resp.getVersion();
                    String sourceAsString = resp.getSourceAsString();
                    //Map<String, Object> sourceAsMap = resp.getSourceAsMap();
                    //byte[] sourceAsBytes = resp.getSourceAsBytes();
                    //resp.getField("message").getValue();
                    System.out.printf("version: %d, sourceAsString: %s\n", version, sourceAsString);
                }
            }

            public void onFailure(Exception e) {
                if (e instanceof ElasticsearchException) {
                    ElasticsearchException esex = (ElasticsearchException) e;
                    if (esex.status() == RestStatus.CONFLICT) {
                        System.out.println("RestStatus.CONFLICT"+RestStatus.CONFLICT);
                    }
                }
            }
        });
        Thread.sleep(1000l);
    }

    /*
    * 增加索引
    * */
    public static void IndexRequest(RestHighLevelClient client) throws IOException {
        IndexRequest request = new IndexRequest("posts");
        request.id("1");
        String jsonString = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2014-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        request.source(jsonString, XContentType.JSON);

        IndexResponse resp = client.index(request, RequestOptions.DEFAULT);
        System.out.println(resp.toString());
    }
}
