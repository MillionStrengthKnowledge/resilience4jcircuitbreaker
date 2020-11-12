package com.millionstrengthknowledge.circuitbreaker.config;

import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig.SlidingWindowType;
import io.github.resilience4j.circuitbreaker.internal.CircuitBreakerStateMachine;


@Configuration
public class MicroserviceConfig {
	@Bean("DataServiceCb")
	public CircuitBreaker circuitBreaker() {
	    CircuitBreakerConfig circuitBreakerConfig = 
	    	CircuitBreakerConfig.custom()
	    		.minimumNumberOfCalls(2)
	    		.slidingWindowType(SlidingWindowType.COUNT_BASED)
	            .slidingWindowSize(4)
	            .failureRateThreshold(50)
	            .waitDurationInOpenState(Duration.ofSeconds(60))
	            .permittedNumberOfCallsInHalfOpenState(3)
	            .build();
	    
	    CircuitBreaker circuitBreaker = 
	    	(CircuitBreakerStateMachine) 
	    		CircuitBreaker.of("DataServiceCb", circuitBreakerConfig);
	    
		return circuitBreaker;
	}
}
