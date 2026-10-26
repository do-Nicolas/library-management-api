package com.donicolas.librarymanagementsystem.exception;

public class QuantityBelowActiveLoansException extends RuntimeException {
    public QuantityBelowActiveLoansException(String message) {
        super(message);
    }
}
