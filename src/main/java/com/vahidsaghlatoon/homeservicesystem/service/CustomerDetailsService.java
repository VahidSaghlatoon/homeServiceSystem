package com.vahidsaghlatoon.homeservicesystem.service;

import com.vahidsaghlatoon.homeservicesystem.model.Customer;
import com.vahidsaghlatoon.homeservicesystem.model.CustomerDetails;
import com.vahidsaghlatoon.homeservicesystem.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomerDetailsService implements UserDetailsService {
    private final CustomerRepository customerRepository ;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email);
        if(customer!=null){
            return new CustomerDetails(customer);
        }
        throw new UsernameNotFoundException("Could not find customer with email: " + email);
    }
}
