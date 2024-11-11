package com.microservice.accounts.expectionhandling;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFound extends RuntimeException {

    // {Customer, MobileNUmber, Value
    public ResourceNotFound(String Name, String Field, String Value) {
        super(String.format("%s does not have the %s: %s", Name, Field, Value));
    }

}
