package com.vahidsaghlatoon.homeservicesystem.config;

import com.vahidsaghlatoon.homeservicesystem.service.CustomerDetailsService;
import com.vahidsaghlatoon.homeservicesystem.service.EmployeeDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Order(1)
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final EmployeeDetailsService employeeDetailsService;
//    private final CustomerDetailsService customerDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(employeeDetailsService);
//        authProvider.setUserDetailsService(customerDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/register/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/employees/**").hasAuthority("مدیر")
                .antMatchers("/mainServices/**").hasAuthority("مدیر")
                .antMatchers("/customers/**").hasAuthority("مدیر")
                .anyRequest()
//                .permitAll();
        //
                .authenticated()
                .and()
                .formLogin()
                .usernameParameter("email")
                .loginPage("/login")
                .permitAll()
                .and()
                .logout().permitAll()
                .and().rememberMe().key("Abcdghgjgkkjgjhklhkl_6546546546")
                .tokenValiditySeconds(7 * 24 * 60 * 60);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()

                .antMatchers("/images/**", "/js/**", "/webjars/**");
    }
}
