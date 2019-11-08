package com.example.tracker.service;

import com.example.tracker.dao.IUserDao;
import com.example.tracker.dao.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Collection;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    IUserDao userService;

    private final static Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Connection conn = userService.connect();
        User user = userService.getUserByName(conn, username);
        if (user != null) {
            String password = user.getPassword();
            logger.info("Password: " + password);

            Collection<GrantedAuthority> authorities= new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            org.springframework.security.core.userdetails.User securedUser
                    = new org.springframework.security.core.userdetails.User(
                        username, "{noop}" + password, true, true,
                    true, true, authorities);
            return securedUser;
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
