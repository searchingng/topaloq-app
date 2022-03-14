package com.company.topaloq.exceptions;

public class ItemNotFoundException extends RuntimeException{
    public ItemNotFoundException(String message) {
        super(message);
    }

    public ItemNotFoundException() {
    }
}
