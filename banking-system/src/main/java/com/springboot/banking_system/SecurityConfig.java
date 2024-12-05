package com.springboot.banking_system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.springboot.banking_system.service.UserSecurityService;


@Configuration
public class SecurityConfig {

    @Autowired
    private UserSecurityService userSecurityService;

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET, "/api/token").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/sign-up").permitAll()
                        .requestMatchers(HttpMethod.POST, "/auth/login").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/hello").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/executive/hello").hasAuthority("EXECUTIVE")
                        .requestMatchers(HttpMethod.POST, "/customer/register").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/customer/get/details").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/customer/accounts/get").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/customer/account/add/{cid}").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/customer/details/update").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/customer/transaction-history/get/{aid}").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, " /customer/account/get/{aid}").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, " /customer/account").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.POST, " /customer/account/transaction/transfer").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/customer/account/transaction/deposit").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/customer/account/transaction/withdraw").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/customer/loan-details/get/{aid}").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.GET, "/customer/card-details/get/{aid}").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/customer/account/loan-apply/{aid}").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/customer/account/card-apply/{aid}").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/customer/investment/FD/{aid}").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/customer/investment/mutual-fund/{aid}").hasAuthority("CUSTOMER")
                        .requestMatchers(HttpMethod.POST, "/customer/investment/bonds/{aid}").hasAuthority("CUSTOMER")
//                        .requestMatchers("/customer/**").hasAuthority("CUSTOMER")
                       
                        .anyRequest().permitAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    @Bean
    BCryptPasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userSecurityService);
        authenticationProvider.setPasswordEncoder(getEncoder());
        return authenticationProvider;
    }
}