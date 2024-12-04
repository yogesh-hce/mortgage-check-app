package com.assignment.application.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class MortgageCheckRequest {

    private BigDecimal income;
    private int maturityPeriod;
    private BigDecimal loanValue;
    private BigDecimal homeValue;
}
