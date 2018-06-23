package com.ftd.io.batch;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class ApplicationConfiguration {
	

	@Value("${ftd.injestion.source.resource-pattern:input/zip*.out}")
	private String resourcePattern;
	
	@Value("${ftd.injestion.source.resource-path:input/zipfiles0/zip*.out}")
	private String resourcePath;
	
	@Value("${ftd.injestion.source.file.delimiter:,}")
	private String delimiter;
	
	@Value("${ftd.injestion.source.file.field-names}")
	private String[] fields;
	
	@Value("${ftd.injestion.destination.mongo.collection-name}")
	private String collectionName;
	
	@Value("${ftd.injestion.chunk.size:100}")
	private int chunkSize;
	
	@Value("${ftd.injestion.coreThreads:10}")
	private int coreThreads;
	
	@Value("${ftd.injestion.maxThreads:2}")
	private int maxThreads;
	
	@Value("${ftd.injestion.queCapacity:2}")
	private int queCapacity;

}
