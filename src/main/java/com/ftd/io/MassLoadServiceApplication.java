package com.ftd.io;

import java.time.LocalTime;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@EnableBatchProcessing
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MassLoadServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MassLoadServiceApplication.class, args);
		System.out.println("------------------------------------------------------------------------");
		System.out.println(LocalTime.now());
		System.out.println("------------------------------------------------------------------------");
	}
}
