package com.assignment.application.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.assignment.application.model.MortgageCheckRequest;
import com.assignment.application.model.MortgageCheckResponse;
import com.assignment.application.model.MortgageRateResponse;
import com.assignment.application.service.MortgageService;

@WebMvcTest(MortgageController.class)
class MortgageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MortgageService mortgageService;

    @Test
    void testGetInterestRates() throws Exception {
        MortgageRateResponse.MortgageRateItem rateItem1 =
                new MortgageRateResponse.MortgageRateItem(15, "3.5%", "2024-12-01");
        MortgageRateResponse.MortgageRateItem rateItem2 =
                new MortgageRateResponse.MortgageRateItem(30, "4.0%", "2024-12-01");
        MortgageRateResponse rateResponse = new MortgageRateResponse(List.of(rateItem1, rateItem2));

        when(mortgageService.getInterestRates()).thenReturn(rateResponse);

        mockMvc.perform(get("/api/interest-rates")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rates[0].maturityPeriodInYears").value(15))
                .andExpect(jsonPath("$.rates[0].interestRate").value("3.5%"))
                .andExpect(jsonPath("$.rates[0].lastUpdate").value("2024-12-01"))
                .andExpect(jsonPath("$.rates[1].maturityPeriodInYears").value(30))
                .andExpect(jsonPath("$.rates[1].interestRate").value("4.0%"))
                .andExpect(jsonPath("$.rates[1].lastUpdate").value("2024-12-01"));
    }

    @Test
    void testCheckMortgage() throws Exception {
        MortgageCheckRequest mockRequest = new MortgageCheckRequest();
        mockRequest.setIncome(BigDecimal.valueOf(100000));
        mockRequest.setMaturityPeriod(30);
        mockRequest.setLoanValue(BigDecimal.valueOf(500000));
        mockRequest.setHomeValue(BigDecimal.valueOf(600000));

        MortgageCheckResponse mockResponse = new MortgageCheckResponse();
        mockResponse.setFeasible(true);
        mockResponse.setMonthlyCost(2000.0);

        when(mortgageService.checkMortgage(mockRequest)).thenReturn(mockResponse);

        mockMvc.perform(post("/api/mortgage-check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                                "income": 100000,
                                "maturityPeriod": 30,
                                "loanValue": 500000,
                                "homeValue": 600000
                            }
                            """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.feasible").value(true))
                .andExpect(jsonPath("$.monthlyCost").value(2000.0));
    }
}
