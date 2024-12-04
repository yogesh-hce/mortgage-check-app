package com.assignment.application.service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.assignment.application.data.MortgageRateFetcher;
import com.assignment.application.exception.MortgageServiceException;
import com.assignment.application.model.MortgageCheckRequest;
import com.assignment.application.model.MortgageCheckResponse;
import com.assignment.application.model.MortgageRateResponse;
import com.assignment.application.util.ValidationUtil;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MortgageService {

    private final MortgageRateFetcher mortgageRateFetcher;

    public MortgageService(MortgageRateFetcher mortgageRateFetcher) {
        this.mortgageRateFetcher = mortgageRateFetcher;
    }

    /**
     * Fetches the list of all available interest rates and maps them to response objects.
     *
     * @return List of MortgageRateResponse objects
     */
    public MortgageRateResponse getInterestRates() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_INSTANT;

        // Map MortgageRate entities to MortgageRateResponse.MortgageRateItem
        List<MortgageRateResponse.MortgageRateItem> rates = mortgageRateFetcher.getAllRates().stream()
            .map(rate -> new MortgageRateResponse.MortgageRateItem(
                rate.maturityPeriod(),
                rate.interestRate() + "%",
                formatter.format(rate.lastUpdate())
            ))
            .collect(Collectors.toList());

        return new MortgageRateResponse(rates);
    }

    /**
     * Checks if a mortgage is feasible and calculates the monthly cost.
     *
     * @param request The request containing income, maturity period, loan value, and home value
     * @return A MortgageCheckResponse indicating feasibility and monthly cost
     */
    public MortgageCheckResponse checkMortgage(MortgageCheckRequest request) {
    	
    	validateRequest(request);
        validateBusinessRules(request);
    	
        BigDecimal interestRate = mortgageRateFetcher.getInterestRate(request.getMaturityPeriod());
        
        double monthlyCost = calculateMonthlyCost(request.getLoanValue(), interestRate, request.getMaturityPeriod());

        return new MortgageCheckResponse(true, monthlyCost);
    }
    
    private void validateRequest(MortgageCheckRequest request) {
        ValidationUtil.validateGreaterThanZero(request.getIncome(), "Income");
        ValidationUtil.validateGreaterThanZero(request.getLoanValue(), "Loan Value");
        ValidationUtil.validateGreaterThanZero(request.getHomeValue(), "Home Value");
        ValidationUtil.validateGreaterThanZero(request.getMaturityPeriod(), "Maturity Period");
    }
    
    private void validateBusinessRules(MortgageCheckRequest request) {
        BigDecimal maxLoanBasedOnIncome = request.getIncome().multiply(BigDecimal.valueOf(4));

        if (request.getLoanValue().compareTo(maxLoanBasedOnIncome) > 0) {
            throw new MortgageServiceException("Loan value exceeds 4 times the income.");
        }

        if (request.getLoanValue().compareTo(request.getHomeValue()) > 0) {
            throw new MortgageServiceException("Loan value exceeds the home value.");
        }
    }

    /** This method calculates the EMI on a generic formula for monthly payments
     * 
     * @param loanValue
     * @param annualInterestRate
     * @param maturityPeriod
     * @return
     */
    double calculateMonthlyCost(BigDecimal loanValue, BigDecimal annualInterestRate, int maturityPeriod) {
    	
        BigDecimal monthlyRate = annualInterestRate
                .divide(BigDecimal.valueOf(100), MathContext.DECIMAL64) 
                .divide(BigDecimal.valueOf(12), MathContext.DECIMAL64);

        int numberOfPayments = maturityPeriod * 12; 

        BigDecimal factor = BigDecimal.valueOf(Math.pow(1 + monthlyRate.doubleValue(), numberOfPayments));

        BigDecimal numerator = loanValue.multiply(monthlyRate).multiply(factor);

        BigDecimal denominator = factor.subtract(BigDecimal.ONE);

        return numerator.divide(denominator, 2, RoundingMode.HALF_EVEN).doubleValue();
    }
}
