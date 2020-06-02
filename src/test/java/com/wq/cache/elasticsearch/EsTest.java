package com.wq.cache.elasticsearch;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EsTest {

    @Autowired
    private ElasticSearchUtils elasticSearchUtils;


    @Test
    public void saveEs() throws InterruptedException {
        ESUserInfo esUserInfo = new ESUserInfo(1,"小强",32,new Date());
        elasticSearchUtils.saveInfo(esUserInfo);

    }

    @Test
    public void saveBulkInsert() throws InterruptedException {
        String indexName = "people";
        int count = 10;
        List<ESUserInfo> list = new ArrayList<>(count);
        for (int i = 1; i <= count; i++) {
            ESUserInfo esUserInfo = new ESUserInfo((i + 6),"李强" + ((i + 6)),22,new Date());
            list.add(esUserInfo);
        }
        elasticSearchUtils.bulkInsert(list,indexName);
    }


    @Test
    public void testGetByIndexAndId() throws Exception {
        String indexName = "people";
        String byIndexAndId = elasticSearchUtils.getByIndexAndId(indexName, "1");
        ESUserInfo esUserInfo = JSON.parseObject(byIndexAndId, ESUserInfo.class);
        System.out.println(esUserInfo.getBirthday());
    }


    @Test
    public void testIsIndexExists() throws InterruptedException {
        boolean people = elasticSearchUtils.isIndexExists("people");
        System.out.println(people);

    }




}
