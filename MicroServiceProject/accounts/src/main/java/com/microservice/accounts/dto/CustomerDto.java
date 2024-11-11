package com.microservice.accounts.dto;

import com.microservice.accounts.entity.Accounts;
import lombok.Data;

@Data
public class CustomerDto {

    private String name;

    private String email;

    private String mobileNumber;

    AccountsDto accountsdto;

}
