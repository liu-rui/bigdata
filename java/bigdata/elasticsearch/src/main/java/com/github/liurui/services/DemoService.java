package com.github.liurui.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.stereotype.Service;

@Service
public class DemoService {
    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    public void test(){
        System.out.println(elasticsearchTemplate.indexExists("index"));
    }
}
