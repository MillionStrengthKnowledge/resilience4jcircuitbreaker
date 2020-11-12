package com.millionstrengthknowledge.circuitbreaker.restful;

import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreaker.Metrics;

@RestController
public class RestfulService {
	@Autowired private CircuitBreaker circuitBreaker;
	
	@GetMapping("/test")
	public void test(@RequestParam(required=false) String simulatedError) {
		String error = simulatedError == null ? "":simulatedError;

	    try {
		    Supplier<String> sup=()->{
				if(error.equals("1")) 
					throw new IllegalStateException();
				
				return "data";
			};
			
		    String test = CircuitBreaker
		    	.decorateSupplier(
		    		circuitBreaker, sup
		    	)
		    	.get();
		    
		    
		    System.out.println("testresult: "+test);
	    }
	    catch(IllegalStateException excp) {
	    	System.out.println("IllegalStateException");
		}
	    catch(CallNotPermittedException excp) {	    
	    	System.out.println("CallNotPermittedException");
	    }
	    
	    showStatistic();
	}
	
	private void showStatistic() {
		Metrics met = circuitBreaker.getMetrics();
		
	    System.out.println("Circuit Breaker Statistic:");
	    System.out.println("--------------------------");
	    System.out.println("State: "+
	    		circuitBreaker.getState());
	    System.out.println("Failure rate: "+
	    	met.getFailureRate());
	    System.out.println("Failure invoke count: "+
	    	met.getNumberOfFailedCalls());
	    System.out.println("Success invoke count: "+
	    	met.getNumberOfSuccessfulCalls());
	    System.out.println("CallNotPermitted count: "+
		    	met.getNumberOfNotPermittedCalls());	    
	    System.out.println("--------------------------");
	}
}
