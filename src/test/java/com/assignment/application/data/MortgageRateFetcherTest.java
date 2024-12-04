package com.assignment.application.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.assignment.application.data.entity.MortgageRate;

class MortgageRateFetcherTest {

    private MortgageRateFetcher mortgageRateFetcher;

    @BeforeEach
    void setUp() {
        mortgageRateFetcher = new MortgageRateFetcher();
        ReflectionTestUtils.setField(mortgageRateFetcher, "mortgageRateMap", "10:3.5,20:4.0,30:4.5");
   
        mortgageRateFetcher.initializeRates();
    }

    @Test
    void testInitializeRates() {
        BigDecimal rate10 = mortgageRateFetcher.getInterestRate(10);
        BigDecimal rate20 = mortgageRateFetcher.getInterestRate(20);
        BigDecimal rate30 = mortgageRateFetcher.getInterestRate(30);

        assertEquals(new BigDecimal("3.5"), rate10);
        assertEquals(new BigDecimal("4.0"), rate20);
        assertEquals(new BigDecimal("4.5"), rate30);
    }

    @Test
    void testGetAllRates() {
        List<MortgageRate> rates = mortgageRateFetcher.getAllRates();
        assertNotNull(rates);
        assertEquals(3, rates.size());

        // Validate the contents of the first rate
        MortgageRate rate = rates.get(0);
        assertEquals(10, rate.maturityPeriod());
        assertEquals(new BigDecimal("3.5"), rate.interestRate());
        assertNotNull(rate.lastUpdate());
    }

    @Test
    void testGetInterestRateForExistingPeriod() {
        BigDecimal rate = mortgageRateFetcher.getInterestRate(20);
        assertEquals(new BigDecimal("4.0"), rate);
    }

    @Test
    void testGetInterestRateForNonExistingPeriod() {
        BigDecimal rate = mortgageRateFetcher.getInterestRate(40);
        assertEquals(BigDecimal.TEN, rate); 
    }
}

