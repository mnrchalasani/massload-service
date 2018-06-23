package com.ftd.io.batch;

import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;

import org.apache.log4j.Logger;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;

import lombok.AllArgsConstructor;

@Configuration
@AllArgsConstructor
public class MassLoadJobConfiguration {
	
	private final Logger logger = Logger.getLogger(this.getClass());
	private ApplicationConfiguration applicationConfig;
	private MongoTemplate mongoTemplate;
	private ResourcePatternResolver resoursePatternResolver;
	private JobBuilderFactory jobs;

	private StepBuilderFactory steps;
	private CustomStepExecutionListener stepExecutionListener;

	@Bean
	@StepScope
	public FlatFileItemReader<CSVRecord> itemReader(@Value("#{stepExecutionContext[fileName]}") String fileName) {
		FlatFileItemReader<CSVRecord> reader = new FlatFileItemReader<>();
		FileSystemResource fileSystemResource = new FileSystemResource(applicationConfig.getResourcePath()+fileName);
		Assert.notNull(fileSystemResource, "Input file to Reader " + fileName +" , not found");
		logger.info("Reading file:" + fileSystemResource.getFilename());
		logger.info("Reading file from path :" + fileSystemResource.getPath());
		reader.setResource(fileSystemResource);
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		String[] tokens = applicationConfig.getFields();
		tokenizer.setNames(tokens);
		DefaultLineMapper<CSVRecord> lineMapper = new DefaultLineMapper<>();
		lineMapper.setLineTokenizer(tokenizer);
		lineMapper.setFieldSetMapper(new BeanWrapperFieldSetMapper<CSVRecord>() {
			{
				setTargetType(CSVRecord.class);
			}
		});
		reader.setLineMapper(lineMapper);
		return reader;
	}
	
	@Bean
	@StepScope
	public MongoItemWriter<Availability> mongoWriter() {
		MongoItemWriter<Availability> writer = new MongoItemWriter<Availability>();
		writer.setTemplate(mongoTemplate);
		writer.setCollection(applicationConfig.getCollectionName());
		return writer;
	}

	@Bean
	public ItemProcessor<CSVRecord, Availability> availabilityProcessor() {
		return new AvailabilityProcessor();
	}

	@Bean(name = "partitionerJob")
	public Job partitionerJob() throws UnexpectedInputException, MalformedURLException, ParseException {
		return jobs.get("partitionerJob")
				   .start(partitionStep())
				   .build();
	}

	@Bean
	public Step partitionStep() {
		return steps.get("partitionStep")
					.partitioner("slaveStep", partitioner())
					.gridSize(applicationConfig.getMaxThreads())
					.step(slaveStep())
					.taskExecutor(taskExecutor())
					.build();
	}

	@Bean
	public Step slaveStep(){
		return steps.get("slaveStep")
				.<CSVRecord, Availability>chunk(applicationConfig.getChunkSize())
				.reader(itemReader(null))
				.processor(availabilityProcessor())
				.writer(mongoWriter())
				.listener(stepExecutionListener)
				.build();
	}

	@Bean
	public CustomMultiResourcePartitioner partitioner() {
		CustomMultiResourcePartitioner partitioner = new CustomMultiResourcePartitioner();
		
		Resource[] resources;
		 
		try 
		{
			resources = resoursePatternResolver.getResources(applicationConfig.getResourcePattern());
			Assert.notEmpty(resources, "No Resources Found in the location:" + applicationConfig.getResourcePath());
		} 
		catch (IOException e) 
		{
			throw new RuntimeException("I/O problems when resolving the input file pattern.", e);
		}
		
		partitioner.setResources(resources);
		return partitioner;
	}

	@Bean
	public TaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
		taskExecutor.setMaxPoolSize(applicationConfig.getMaxThreads());
		taskExecutor.setCorePoolSize(applicationConfig.getCoreThreads());
		taskExecutor.setQueueCapacity(applicationConfig.getQueCapacity());
		taskExecutor.setThreadGroupName("MassLoadThreads");
		taskExecutor.afterPropertiesSet();
		taskExecutor.setWaitForTasksToCompleteOnShutdown(true);
		return taskExecutor;
	}
	
	
/*	@Bean
	public TaskExecutor taskExecutor(){
		SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor(this.getClass().getName());
		taskExecutor.setConcurrencyLimit(applicationConfig.getMaxThreads());
	    return taskExecutor;
	}*/
	

}
