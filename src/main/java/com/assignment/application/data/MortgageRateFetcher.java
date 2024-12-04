package com.assignment.application.data;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.assignment.application.data.entity.MortgageRate;

import jakarta.annotation.PostConstruct;

@Repository
public class MortgageRateFetcher {

	private final Map<Integer, BigDecimal> interestRates = new TreeMap<>();
    private final List<MortgageRate> mortgageRates = new ArrayList<>();

    @Value("${mortgage.rates}")
    private String mortgageRateMap;

    @PostConstruct
    public void initializeRates() {
    	
		interestRates.putAll(Arrays.stream(mortgageRateMap.split(","))
				     .map(mortgageRate -> mortgageRate.split(":"))
				     .collect(Collectors
				    		 .toMap(entry -> Integer.parseInt(entry[0]), 
				    				value -> new BigDecimal(value[1]))
				    		 ));

        interestRates.forEach((maturityPeriod, rate) -> 
            mortgageRates.add(new MortgageRate(maturityPeriod, rate, Instant.now()))
        );
    }

    public List<MortgageRate> getAllRates() {
        return mortgageRates;
    }
    
    public BigDecimal getInterestRate(int maturityPeriod) {
        return interestRates.getOrDefault(maturityPeriod, BigDecimal.TEN);
    }
}

