package com.microservice.accounts.service;

import com.microservice.accounts.dto.CustomerDto;


public interface IAccountService {

    /**
     *
     * @param customerDto
     */
    void createAccount(CustomerDto customerDto);

    /**
     *
     * @param mobileNumber
     * @return
     */
    CustomerDto fetchCustomer(String mobileNumber);

    /**
     *
     * @param customerDto
     * @return
     */
    boolean updateCustomer(CustomerDto customerDto);

    boolean deleteAccount(String mobileNumber);
}
