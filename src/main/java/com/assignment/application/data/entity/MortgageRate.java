package com.assignment.application.data.entity;

import java.math.BigDecimal;
import java.time.Instant;

public record MortgageRate(int maturityPeriod, BigDecimal interestRate, Instant lastUpdate) {}
