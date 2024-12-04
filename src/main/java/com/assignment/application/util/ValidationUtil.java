package com.assignment.application.util;

import java.math.BigDecimal;

import com.assignment.application.exception.MortgageServiceException;

public class ValidationUtil {
	
	private ValidationUtil() {}

    public static void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new MortgageServiceException(fieldName + " cannot be null.");
        }
    }

    public static void validateGreaterThanZero(BigDecimal value, String fieldName) {
        validateNotNull(value, fieldName);
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new MortgageServiceException(fieldName + " must be greater than zero.");
        }
    }

    public static void validateGreaterThanZero(int value, String fieldName) {
        if (value <= 0) {
            throw new MortgageServiceException(fieldName + " must be greater than zero.");
        }
    }
}

