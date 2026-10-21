package com.donicolas.librarymanagementsystem.exception;

public class DuplicateIsbnException extends RuntimeException{
    public DuplicateIsbnException(String menssage) {
        super(menssage);
    }
}
