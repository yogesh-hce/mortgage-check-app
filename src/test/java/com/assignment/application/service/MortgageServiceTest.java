package com.assignment.application.service;

import com.assignment.application.data.MortgageRateFetcher;
import com.assignment.application.data.entity.MortgageRate;
import com.assignment.application.exception.MortgageServiceException;
import com.assignment.application.model.MortgageCheckRequest;
import com.assignment.application.model.MortgageCheckResponse;
import com.assignment.application.model.MortgageRateResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MortgageServiceTest {

    @Mock
    private MortgageRateFetcher mortgageRateFetcher;

    @InjectMocks
    private MortgageService mortgageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetInterestRates() {

        when(mortgageRateFetcher.getAllRates()).thenReturn(List.of(
            new MortgageRate(10, new BigDecimal("3.5"), Instant.parse("2024-12-01T10:00:00Z")),
            new MortgageRate(20, new BigDecimal("4.0"), Instant.parse("2024-12-02T10:00:00Z"))
        ));

        MortgageRateResponse response = mortgageService.getInterestRates();

        assertNotNull(response);
        assertEquals(2, response.getRates().size());
        assertEquals(10, response.getRates().get(0).getMaturityPeriodInYears());
        assertEquals("3.5%", response.getRates().get(0).getInterestRate());
        assertEquals("2024-12-01T10:00:00Z", response.getRates().get(0).getLastUpdate());
    }

    @Test
    void testCheckMortgageFeasible() {
        MortgageCheckRequest request = new MortgageCheckRequest();
        request.setIncome(new BigDecimal("100000"));
        request.setLoanValue(new BigDecimal("200000"));
        request.setHomeValue(new BigDecimal("250000"));
        request.setMaturityPeriod(20);

        when(mortgageRateFetcher.getInterestRate(20)).thenReturn(new BigDecimal("4.0"));

        MortgageCheckResponse response = mortgageService.checkMortgage(request);

        assertNotNull(response);
        assertTrue(response.isFeasible());
        assertEquals(1211.96, response.getMonthlyCost(), 0.01);
    }

    @Test
    void testCheckMortgageLoanExceedsIncome() {
        MortgageCheckRequest request = new MortgageCheckRequest();
        request.setIncome(new BigDecimal("50000"));
        request.setLoanValue(new BigDecimal("250001"));
        request.setHomeValue(new BigDecimal("300000"));
        request.setMaturityPeriod(20);

        MortgageServiceException exception = assertThrows(
            MortgageServiceException.class,
            () -> mortgageService.checkMortgage(request)
        );

        assertEquals("Loan value exceeds 4 times the income.", exception.getMessage());
    }

    @Test
    void testCheckMortgageLoanExceedsHomeValue() {
    	
        MortgageCheckRequest request = new MortgageCheckRequest();
        request.setIncome(new BigDecimal("100000"));
        request.setLoanValue(new BigDecimal("300000"));
        request.setHomeValue(new BigDecimal("250000"));
        request.setMaturityPeriod(20);

        MortgageServiceException exception = assertThrows(
            MortgageServiceException.class,
            () -> mortgageService.checkMortgage(request)
        );

        assertEquals("Loan value exceeds the home value.", exception.getMessage());
    }

    @Test
    void testCalculateMonthlyCost() {
        BigDecimal loanValue = new BigDecimal("200000");
        BigDecimal interestRate = new BigDecimal("4.0");
        int maturityPeriod = 20;

        double monthlyCost = mortgageService.calculateMonthlyCost(loanValue, interestRate, maturityPeriod);

        assertEquals(1211.96, monthlyCost, 0.01);
    }
}

