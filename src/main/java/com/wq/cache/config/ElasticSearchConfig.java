package com.wq.cache.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: ElasticSearch 配置类
 * @Author: karma
 * @Date: 2019/11/25 3:11 下午
 */
@Configuration
public class ElasticSearchConfig {

    /**
     * 集群地址，多个用,隔开
     **/
    @Value("${elasticSearch.hosts}")
    private String hosts;

    /**
     * 端口号
     **/
    @Value("${elasticSearch.port}")
    private int port;

    /**
     * 使用的协议
     **/
    @Value("${elasticSearch.schema}")
    private String schema;

    /**
     * 连接超时时间
     **/
    @Value("${elasticSearch.client.connectTimeOut}")
    private int connectTimeOut;

    /**
     * 连接超时时间
     **/
    @Value("${elasticSearch.client.socketTimeOut}")
    private int socketTimeOut;

    /**
     * 获取连接的超时时间
     **/
    @Value("${elasticSearch.client.connectionRequestTimeOut}")
    private static int connectionRequestTimeOut;

    /**
     * 最大连接数
     **/
    @Value("${elasticSearch.client.maxConnectNum}")
    private static int maxConnectNum;

    /**
     *  最大路由连接数
     **/
    @Value("${elasticSearch.client.maxConnectPerRoute}")
    private static int maxConnectPerRoute;


    private List<HttpHost> hostList = new ArrayList<>();

    @PostConstruct
    private  void init(){
        hostList = new ArrayList<>();
        String[] hostArray = hosts.split(",");
        for (String host : hostArray) {
            hostList.add(new HttpHost(host, port, schema));
        }
    }

    @Bean
    public RestHighLevelClient getRestHighLevelClient() {

        RestClientBuilder builder = RestClient.builder(hostList.toArray(new HttpHost[0]));
        // 异步httpclient连接延时配置
        builder.setRequestConfigCallback(requestConfigBuilder -> {
            requestConfigBuilder.setConnectTimeout(connectTimeOut);
            requestConfigBuilder.setSocketTimeout(socketTimeOut);
            requestConfigBuilder.setConnectionRequestTimeout(connectionRequestTimeOut);
            return requestConfigBuilder;
        });
        // 异步httpclient连接数配置
        builder.setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.setMaxConnTotal(maxConnectNum);
            httpClientBuilder.setMaxConnPerRoute(maxConnectPerRoute);
            return httpClientBuilder;
        });
        RestHighLevelClient client = new RestHighLevelClient(builder);
        return client;
    }

}
