package com.example.tracker.server.security;

import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

/*
    @Value("${security.enable-csrf}")
    private boolean csrfEnabled;
*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
//        if (!csrfEnabled) {
            http.csrf().disable();
//        }
    }
}

