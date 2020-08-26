package com.movistar.demo.hilos.dp;

import java.time.Duration;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {
	
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	
	@Value("${corePoolSize}")
	int corePoolSize;
	
	@Value("${maxPoolSize}")
	int maxPoolSize;
	
	@Value("${queueCapacity}")
	int queueCapacity;
	
	@Value("${connectionTimeOut}")
	int connectionTimeOut; 
	
	@Value("${readTimeOut}")
	int readTimeOut; 

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {

		log.info("Configurando RestTemplate ConnectionTimeOut={} ms ReadTimeOut={} ms",connectionTimeOut, readTimeOut );

		return builder.setConnectTimeout(Duration.ofMillis(connectionTimeOut))
				.setReadTimeout(Duration.ofMillis(readTimeOut))
				.build();
	}

	
	@Bean(name = "pool")
    public Executor poolGenesys() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setThreadNamePrefix("RefreshLookup-");
        executor.initialize();
        return executor;
    }
}
