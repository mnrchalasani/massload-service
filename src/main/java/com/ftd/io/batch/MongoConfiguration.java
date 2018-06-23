package com.ftd.io.batch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;

@Configuration
public class MongoConfiguration
{
	@Value("${spring.data.mongodb.database:test}")
	private String database;

	@Value("${spring.data.mongodb.host:localhost}:${spring.data.mongodb.port:27017}")
	private String host;

	@Bean
	public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory, MappingMongoConverter mongoConverter)
			throws Exception {
		mongoConverter.setTypeMapper(new DefaultMongoTypeMapper(null));
		MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory, mongoConverter);
		return mongoTemplate;
	}
}