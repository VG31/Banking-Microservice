package com.microservice.accounts.service.impl;

import com.microservice.accounts.constants.AccountConstants;
import com.microservice.accounts.dto.AccountsDto;
import com.microservice.accounts.dto.CustomerDto;
import com.microservice.accounts.entity.Accounts;
import com.microservice.accounts.entity.Customer;
import com.microservice.accounts.expectionhandling.CustomerAlreadyPresentException;
import com.microservice.accounts.expectionhandling.ResourceNotFound;
import com.microservice.accounts.mapper.AccountMapper;
import com.microservice.accounts.mapper.CustomerMapper;
import com.microservice.accounts.repository.AccountsRepo;
import com.microservice.accounts.repository.CustomerRepo;
import com.microservice.accounts.service.IAccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements IAccountService {

    private AccountsRepo accountsRepo;
    private CustomerRepo customerRepo;

    /**
     * @param customerDto
     */
    @Override
    public void createAccount(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto, new Customer());
        Optional<Customer> optionalCustomer = customerRepo.findByMobileNumber(customerDto.getMobileNumber());

        if(optionalCustomer.isPresent()){
            throw new CustomerAlreadyPresentException("Customer Already Registered " + " " +  customerDto.getMobileNumber());
        }

        customer.setCreatedAt(LocalDateTime.now());
        customer.setCreatedBy("VivekG");
        Customer savedCustomer=customerRepo.save(customer);
        accountsRepo.save(createAccount(savedCustomer));

    }

    private Accounts createAccount(Customer customer) {
        Accounts newAccounts = new Accounts();
        newAccounts.setCustomerId(customer.getCustomerId());
        long randomAccNumber = 1000000000L + new Random().nextInt(900000000);

        newAccounts.setAccountNumber(randomAccNumber);
        newAccounts.setAccountType(AccountConstants.SAVINGS);
        newAccounts.setBranchAddress(AccountConstants.ADDRESS);
        newAccounts.setCreatedAt(LocalDateTime.now());
        newAccounts.setCreatedBy("Vivek G");

        return newAccounts;
    }

    @Override
    public CustomerDto fetchCustomer(String mobileNumber) {

        Customer customer = customerRepo.findByMobileNumber(mobileNumber).orElseThrow(
                ()-> new ResourceNotFound("Customer", "MobileNumber", mobileNumber)
        );

        Accounts accounts = accountsRepo.findByCustomerId(customer.getCustomerId()).orElseThrow(
                ()-> new ResourceNotFound("Accounts", "CustomerID", customer.getCustomerId().toString())
        );

        CustomerDto customerDto = CustomerMapper.mapToCustomerDto(customer, new CustomerDto());
        customerDto.setAccountsdto(AccountMapper.mapToAccountsDto(accounts, new AccountsDto()));

        return customerDto;
    }

    @Override
    public boolean updateCustomer(CustomerDto customerDto) {
        boolean result = false;
        AccountsDto accountsDto = customerDto.getAccountsdto();

        if (accountsDto != null) {
            Accounts accounts = accountsRepo.findById(accountsDto.getAccountNumber()).orElseThrow(
                    () -> new ResourceNotFound("Accounts", "Account Number", accountsDto.getAccountNumber().toString())
            );

            AccountMapper.mapToAccounts(accountsDto, accounts);        // save the updated account
            accountsRepo.save(accounts);

            Long customerId = accounts.getCustomerId();
            Customer customer = customerRepo.findById(customerId).orElseThrow(
                    () -> new ResourceNotFound("Customer", "CustomerID", customerId.toString())
            );

            CustomerMapper.mapToCustomer(customerDto, customer);       // save the updated customer
            customerRepo.save(customer);

            result=true;
        }

        return result;
    }

    @Override
    public boolean deleteAccount(String mobileNumber) {
        Customer customer = customerRepo.findByMobileNumber(mobileNumber).orElseThrow(
                () -> new ResourceNotFound("Customer", "MobileNumber", mobileNumber)
        );

        accountsRepo.deleteByCustomerId(customer.getCustomerId());
        customerRepo.deleteById(customer.getCustomerId());
        return true;
    }


}
