### 所有索引  
https://www.elastic.co/guide/en/elasticsearch/reference/current/cat-indices.html  
``curl -X GET "localhost:9200/_cat/indices?pretty"``  
Shard count, Document count, Deleted document count, Primary store size,  
Total store size of all shards, including shard replicas  

### singl index  
curl -X GET "localhost:9200/posts/_doc/1?pretty"  
