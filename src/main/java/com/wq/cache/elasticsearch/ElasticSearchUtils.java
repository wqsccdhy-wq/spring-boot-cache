package com.wq.cache.elasticsearch;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.replication.ReplicationResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchAllQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: ElasticSearch 工具类
 * @Author: karma
 * @Date: 2019/11/25 10:34 上午
 */
@Component
public class ElasticSearchUtils {

    private static Logger log = LoggerFactory.getLogger(ElasticSearchUtils.class);


    @Resource
    private RestHighLevelClient restHighLevelClient ;

    /**
     * 判断索引是否存在
     *
     * @param indexName
     * @return
     */
    public boolean isIndexExists(String indexName) {
        boolean exists = false;
        try {
            GetIndexRequest getIndexRequest = new GetIndexRequest(indexName);
            getIndexRequest.humanReadable(true);
            exists = restHighLevelClient.indices().exists(getIndexRequest,RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exists;
    }

    /**
     * 创建索引
     * @param indexName
     * @return
     */
    public boolean createIndex(String indexName){
        try {
            XContentBuilder builder = XContentFactory.jsonBuilder()
                    .startObject()
                    .field("properties")
                    .startObject()
                    .field("name").startObject().field("index", "true").field("type", "keyword").endObject()
                    .field("age").startObject().field("index", "true").field("type", "integer").endObject()
                    .field("money").startObject().field("index", "true").field("type", "double").endObject()
                    .field("address").startObject().field("index", "true").field("type", "text").field("analyzer", "ik_max_word").endObject()
                    .field("birthday").startObject().field("index", "true").field("type", "date").field("format", "strict_date_optional_time||epoch_millis").endObject()
                    .endObject()
                    .endObject();
            CreateIndexRequest createIndexRequest = new CreateIndexRequest(indexName);
            createIndexRequest.mapping(builder);
            CreateIndexResponse createIndexResponse = restHighLevelClient.indices().create(createIndexRequest, RequestOptions.DEFAULT);
            boolean acknowledged = createIndexResponse.isAcknowledged();
            if (acknowledged) {
                //return new ResponseBean(200, "创建成功", null);
            } else {
                //return new ResponseBean(1002, "创建失败", null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    public void saveInfo(ESUserInfo esUserInfo) {
        IndexRequest indexRequest = new IndexRequest();
        indexRequest.index("people");
        //indexRequest.type("user");
        indexRequest.id("2");
        indexRequest.source(JSON.toJSONString(esUserInfo), XContentType.JSON);
        try {
            IndexResponse indexResponse = restHighLevelClient.index(indexRequest, RequestOptions.DEFAULT);
            if(indexResponse != null){
                String id = indexResponse.getId();
                String index = indexResponse.getIndex();
                long version = indexResponse.getVersion();
                log.info("index:{},id:{}", index, id);
                if (indexResponse.getResult() == DocWriteResponse.Result.CREATED) {
                    System.out.println("新增文档成功!" + index + "-" + id + "-" + version);
                    //return new ResponseBean(200, "插入成功", id);
                } else if (indexResponse.getResult() == DocWriteResponse.Result.UPDATED) {
                    System.out.println("修改文档成功!");
                    //return new ResponseBean(10001, "插入失败", null);
                }
                // 分片处理信息
                ReplicationResponse.ShardInfo shardInfo = indexResponse.getShardInfo();
                if (shardInfo.getTotal() != shardInfo.getSuccessful()) {
                    System.out.println("分片处理信息.....");
                }
                // 如果有分片副本失败，可以获得失败原因信息
                if (shardInfo.getFailed() > 0) {
                    for (ReplicationResponse.ShardInfo.Failure failure : shardInfo.getFailures()) {
                        String reason = failure.reason();
                        System.out.println("副本失败原因：" + reason);
                    }
                }

            }
            System.out.println();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        restHighLevelClient.indexAsync(indexRequest, RequestOptions.DEFAULT,new ActionListener<IndexResponse>(){
//            @Override
//            public void onResponse(IndexResponse indexResponse) {
//                System.out.println("ok");
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                //System.out.println("fail");
//                //e.printStackTrace();
//                log.error("fail",e);
//            }
//        });

    }

    /**
     * 批量插入
     */
    public void bulkInsert(List<ESUserInfo> esUserInfoList,String indexName){

        try {
            BulkRequest bulkAddRequest = new BulkRequest();
            for (ESUserInfo esUserInfo : esUserInfoList) {
                IndexRequest indexRequest = new IndexRequest(indexName);
                indexRequest.id(String.valueOf(esUserInfo.getId()));
                indexRequest.source(JSON.toJSONString(esUserInfo), XContentType.JSON);
                bulkAddRequest.add(indexRequest);
            }
            BulkResponse bulkAddResponse = restHighLevelClient.bulk(bulkAddRequest, RequestOptions.DEFAULT);
            if (bulkAddResponse != null){
                boolean b = bulkAddResponse.hasFailures();
                System.out.println("b:" + b);
               // bulkAddResponse.getItems()[0].getResponse().getResult()

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * description: 多字段匹配查询
     * @author karma
     * @param index 索引
     * @param fieldMap 字段map集合
     * @return java.lang.String
     * @date 2019/11/27 3:47 下午
     **/
    public String getByMultiFieldNames(String index, Map<String,Object> fieldMap) throws IOException{
        if(StringUtils.isBlank(index) || MapUtils.isEmpty(fieldMap)){
            return null;
        }
        SearchRequest searchRequest = new SearchRequest(index);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //循环传入搜索参数
        fieldMap.forEach((key, value) ->{
            boolQueryBuilder.must(QueryBuilders.termQuery(key,value));
        });

        sourceBuilder.query(boolQueryBuilder);
        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
        return handleSearchResponse2Json(searchResponse);
    }


    /**
     * description: 根据id 查询
     * @author karma
     * @param index 索引
     * @param id (es数据id  _id)
     * @return java.lang.String
     * @date 2019/11/25 1:53 下午
     **/
    public String getByIndexAndId(String index, String id)throws IOException{
        if(StringUtils.isBlank(index) || StringUtils.isBlank(id)){
            return null;
        }

        GetRequest getRequest = new GetRequest(index,id);
        GetResponse getResponse = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        return getResponse.getSourceAsString();
    }

    /**
     * description: 根据索引查询
     * @author karma
     * @param index 索引
     * @param pageNum 第几页
     * @param pageSize 每页条数
     * @return java.lang.String
     * @date 2019/11/25 1:54 下午
     **/
    public String getByIndex(String index, int pageNum, int pageSize)throws IOException{
        if(StringUtils.isBlank(index)){
            return null;
        }

        // 搜索请求
        SearchRequest searchRequest = new SearchRequest(index);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        // 分页
        if(pageNum >= 0 && pageSize >= 0){
            searchSourceBuilder.from(pageSize*(pageNum - 1));
            searchSourceBuilder.size(pageSize);
        }else {
            // 如果不传分页参数 默认给20条数据
            searchSourceBuilder.from(0);
            searchSourceBuilder.size(19);
        }

        // 查询条件
        MatchAllQueryBuilder queryBuilder = QueryBuilders.matchAllQuery();

        // 传入搜索条件
        searchSourceBuilder.query(queryBuilder);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
        return handleSearchResponse2Json(searchResponse);
    }


    /**
     * description: 根据字段查询
     * @author karma
     * @param index 索引
     * @param fileName 字段名
     * @param value 字段值
     * @return java.lang.String
     * @date 2019/11/27 4:00 下午
     **/
    public String getByFieldName(String index, String fileName,String value) throws IOException{

        if(StringUtils.isBlank(index) || StringUtils.isBlank(fileName) || StringUtils.isBlank(value)){
            return null;
        }

        SearchRequest searchRequest = new SearchRequest(index);

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.matchQuery(fileName,value);
        sourceBuilder.query(queryBuilder);

        searchRequest.source(sourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest,RequestOptions.DEFAULT);
        return handleSearchResponse2Json(searchResponse);
    }



    /**
     * description: 将SearchResponse 取出数据 转换成json
     * @author karma
     * @param searchResponse
     * @return java.lang.String
     * @date 2019/11/27 2:33 下午
     **/
    private String handleSearchResponse2Json(SearchResponse searchResponse){

        SearchHit[] hits = searchResponse.getHits().getHits();

        if(hits.length == 0){
            return null;
        }

        List<Map<String,Object>> dataList = new ArrayList<>(hits.length);
        for(int i=0; i< hits.length; i++){
            dataList.add(hits[i].getSourceAsMap());
        }
        return JSONObject.toJSONString(dataList);
    }
}