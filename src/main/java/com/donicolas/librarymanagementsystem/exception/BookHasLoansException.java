package com.donicolas.librarymanagementsystem.exception;

public class BookHasLoansException extends RuntimeException{
    public BookHasLoansException(String message){
        super(message);
    }
}
