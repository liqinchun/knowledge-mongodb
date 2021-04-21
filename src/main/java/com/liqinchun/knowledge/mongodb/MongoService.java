package com.liqinchun.knowledge.mongodb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class MongoService {

    @Autowired MongoTemplate mongoTemplate;

    public void gridFs() {}
}
