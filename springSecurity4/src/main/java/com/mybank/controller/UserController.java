package com.mybank.controller;

import com.mybank.model.Customer;
import com.mybank.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompromisedPasswordChecker compromisedPasswordChecker;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Customer customer){
        try{
            CompromisedPasswordDecision compromisedPasswordDecision =
                    compromisedPasswordChecker.check(customer.getPwd());

            if (compromisedPasswordDecision.isCompromised()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("The provided password is compromised, please provide a strong password");
            }
            String hashPassword = passwordEncoder.encode(customer.getPwd());
            customer.setPwd(hashPassword);
            Customer saveCustomer = customerRepository.save(customer);

            if(saveCustomer.getId() > 0){
                return ResponseEntity.status(HttpStatus.CREATED).body("User details registered");
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User registration failed!");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An exception occurred: " + e);
        }
    }
}
