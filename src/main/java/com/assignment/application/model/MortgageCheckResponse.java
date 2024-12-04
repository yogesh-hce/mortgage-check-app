package com.assignment.application.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MortgageCheckResponse {

    private boolean feasible;
    private double monthlyCost;
}
