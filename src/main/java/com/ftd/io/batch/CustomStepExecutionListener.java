package com.ftd.io.batch;

import org.apache.log4j.Logger;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class CustomStepExecutionListener implements StepExecutionListener {
	private final Logger logger = Logger.getLogger(this.getClass());

	@Override
	public void beforeStep(StepExecution stepExecution) {
		logger.info(" Beginning Step : stepId :" + stepExecution.getId() + ", step name : " + stepExecution.getStepName());

	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		
		logger.info("step Execution Id :"+stepExecution.getId());
		logger.info("Step Name : "+stepExecution.getStepName());
		logger.info("Step Execution Summary: "+stepExecution.getSummary());
		logger.info("Step Execution Job Parameters : "+stepExecution.getJobParameters());
		logger.info("Step Execution Exit Status :"+stepExecution.getExitStatus());
		logger.info("Step Execution Read Count: "+stepExecution.getReadCount());
		logger.info("Step Execution Write Count: "+stepExecution.getWriteCount());
		logger.info("Step Execution  Commit Count :"+stepExecution.getCommitCount());
		return stepExecution.getExitStatus();
	}

}
