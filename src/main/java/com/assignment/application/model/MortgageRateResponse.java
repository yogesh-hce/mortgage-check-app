package com.assignment.application.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MortgageRateResponse {

    private List<MortgageRateItem> rates;

    @Data
    @AllArgsConstructor
    public static class MortgageRateItem {
        private int maturityPeriodInYears;
        private String interestRate; 
        private String lastUpdate;  
    }
}
