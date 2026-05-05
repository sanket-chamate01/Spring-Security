package com.mybank.config;

import org.springframework.boot.security.autoconfigure.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity.authorizeHttpRequests((request) -> request
                .requestMatchers("/myAccount", "/myBalance", "/myCards", "/myLoans").authenticated()
                .requestMatchers("/notices", "/contact").permitAll());
        httpSecurity.formLogin(form -> form.disable()); // if only this then browser will pop-up a form for login
        httpSecurity.httpBasic(Customizer.withDefaults()); // if disable this as well along with formLogin then we'll get 403
        return httpSecurity.build();
    }

    // in memory users created with role based access
    // {noop} is no operation for password encoder so treat the password as plain text
    @Bean
    public UserDetailsService userDetailsService(){
        UserDetails user = User.withUsername("user").password("{noop}12345").authorities("read").build();
        UserDetails admin = User.withUsername("admin").password("{noop}12345").authorities("admin").build();
        return new InMemoryUserDetailsManager(user, admin);
    }
}
