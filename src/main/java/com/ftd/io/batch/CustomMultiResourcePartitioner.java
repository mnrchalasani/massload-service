package com.ftd.io.batch;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

import lombok.Setter;

@Setter
public class CustomMultiResourcePartitioner implements Partitioner {
	
	private final Logger logger = Logger.getLogger(this.getClass());

    private static final String DEFAULT_KEY_NAME = "fileName";

    private static final String PARTITION_KEY = "partition";

    private Resource[] resources = new Resource[0];

    private String keyName = DEFAULT_KEY_NAME;


    /**
     * Assign the filename of each of the injected resources to an
     * {@link ExecutionContext}.
     *
     * @see Partitioner#partition(int)
     */
    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
    	logger.info("Grid Size : "+gridSize);
        Map<String, ExecutionContext> map = new HashMap<String, ExecutionContext>(gridSize);
        int i = 0;
        for (Resource resource : resources) {
            ExecutionContext context = new ExecutionContext();
            Assert.state(resource.exists(), "Resource does not exist: " + resource);
            context.putString(keyName, resource.getFilename());
            logger.info("PARTITION_KEY :"+ PARTITION_KEY+i +" , Context Key : " + keyName + ", context key value(fileName) :" + resource.getFilename());
            map.put(PARTITION_KEY + i, context);
            i++;
        }
        
        logger.info("Step Execution Context Map Size : "+map.size());
        return map;
    }

}
