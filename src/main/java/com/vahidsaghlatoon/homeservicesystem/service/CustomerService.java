package com.vahidsaghlatoon.homeservicesystem.service;

import com.vahidsaghlatoon.homeservicesystem.exception.NotExistException;
import com.vahidsaghlatoon.homeservicesystem.model.Customer;
import com.vahidsaghlatoon.homeservicesystem.model.UserState;
import com.vahidsaghlatoon.homeservicesystem.repository.CustomerRepository;
import com.vahidsaghlatoon.homeservicesystem.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerService {

    public static final int CUSTOMER_PER_PAGE = 4;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public Customer saveOrUpdate(Customer theCustomer) {
        boolean isUpdatingCustomer = (theCustomer.getId() != null);
        if (isUpdatingCustomer) {
            Customer existedCustomer = customerRepository.findById(theCustomer.getId()).get();

            if (theCustomer.getPassword().isEmpty()) {
                theCustomer.setPassword(existedCustomer.getPassword());
            } else {
                encodePassword(theCustomer);
            }
        } else {
            encodePassword(theCustomer);
            String randomCode = RandomString.make(64);
            theCustomer.setVerificationCode(randomCode);
            System.out.println(randomCode);
        }
        return customerRepository.save(theCustomer);
    }

    @Transactional
    public boolean verify (String verificationCode){
        Customer customer = customerRepository.findByVerificationCode(verificationCode);
        if (customer == null || customer.isEnabled()){
            return false ;
        }else
        {
            customer.setUserState(UserState.AWAITING_APPROVAL);
            customer.setEnabled(true);
            customer.setVerificationCode(null);
//            customerRepository.enable(customer.getId());
            saveOrUpdate(customer);
            return true ;
        }
    }

    @Transactional
    public Customer findByVerificationCode(String code){
        return customerRepository.findByVerificationCode(code);
    }
@Transactional
    public Customer updateAccount(Customer customerInForm) {
        Customer existedCustomer = customerRepository.findById(customerInForm.getId()).get();
        if (!customerInForm.getPassword().isEmpty()) {
            existedCustomer.setPassword(customerInForm.getPassword());
            encodePassword(existedCustomer);
        }
        if (customerInForm.getPhoto() != null) {
            existedCustomer.setPhoto(customerInForm.getPhoto());
        }
        existedCustomer.setFirstName(customerInForm.getFirstName());
        existedCustomer.setLastName(customerInForm.getLastName());

        return customerRepository.save(existedCustomer);
    }

    private void encodePassword(Customer customer) {
        String encodePassword = passwordEncoder.encode(customer.getPassword());
        customer.setPassword(encodePassword);
    }

    @Transactional
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Transactional
    public Customer findById(Long theId) {
        return customerRepository.findById(theId)
                .orElseThrow(() -> new NotExistException("Did not find customer by id" + theId));
    }

    @Transactional
    public Customer deleteById(Long theId) {
        Customer theCustomer;
        if ((theCustomer = findById(theId)) == null)
            throw new NotExistException("Did not find customer by id" + theId);
        customerRepository.deleteById(theId);
        return theCustomer;
    }

    @Transactional
    public boolean isEmailUnique(Long id, String theEmail) {
        Customer theCustomer = customerRepository.findByEmail(theEmail.trim());
        if (theCustomer == null) {
            return true;
        }
        if (id != null) {
            return theEmail.equals(theCustomer.getEmail());
        }
        return false;
    }
    @Transactional
    public boolean isNationalCodeUnique(Long id, String theNationalCode) {
        Customer theCustomer = customerRepository.findByNationalCode(theNationalCode.trim());
        if (theCustomer == null) {
            return true;
        }
        if (id != null) {
            return theNationalCode.equals(theCustomer.getNationalCode());
        }
        return false;
    }

    @Transactional
    public void updateCustomerEnableStatus(Long id, boolean enabled) {
        customerRepository.updateEnableStatus(id, enabled);
    }

    @Transactional
    public Page<Customer> findAllByPage(int pageNumber, String sortField, String sortDir, String keyword) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNumber - 1, CUSTOMER_PER_PAGE, sort);
        if (keyword != null) {
            return customerRepository.findAllByKeyword(keyword, pageable);
        }
        return customerRepository.findAll(pageable);
    }

    @Transactional
    public Customer findByEmail(String email) {
        return customerRepository.findByEmail(email);
    }
}
