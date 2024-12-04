package com.assignment.application.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.assignment.application.model.MortgageCheckRequest;
import com.assignment.application.model.MortgageCheckResponse;
import com.assignment.application.model.MortgageRateResponse;
import com.assignment.application.service.MortgageService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api")
@Slf4j
public class MortgageController {

    private final MortgageService mortgageService;

    public MortgageController(MortgageService mortgageService) {
        this.mortgageService = mortgageService;
    }

    /**
     * Get the list of current mortgage interest rates.
     * @return List of MortgageRate objects
     */
    @GetMapping("/interest-rates")
    public ResponseEntity<MortgageRateResponse>  getInterestRates() {
    	log.info("Fetching all interest rates from the database.");
    	var interestRates = mortgageService.getInterestRates();
        log.info("Successfully fetched and mapped {} interest rates.", interestRates.getRates().size());
        return ResponseEntity.ok(interestRates);
    }

    /**
     * Perform a mortgage feasibility check.
     * @return The result of the feasibility and the monthly cost
     */
    @PostMapping("/mortgage-check")
    public ResponseEntity<MortgageCheckResponse> checkMortgage(@RequestBody MortgageCheckRequest request) {
    	log.info("Starting mortgage feasibility check for request: {}", request);
        return ResponseEntity.ok(mortgageService.checkMortgage(request));
    }
}

