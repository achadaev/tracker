package com.example.tracker.server.service;

import com.example.tracker.server.dao.IUserDAO;
import com.example.tracker.shared.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    IUserDAO iUserDAO;

    private final static Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = iUserDAO.getUserByName(username);
        if (user != null) {
            String password = user.getPassword();
            logger.info("Username: " + user.getLogin());
            logger.info("Password: " + password);
            logger.info("Registration date: " + new SimpleDateFormat("yyyy-MM-dd").format(user.getRegDate()));

            Collection<GrantedAuthority> authorities= new ArrayList<GrantedAuthority>();
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            org.springframework.security.core.userdetails.User securedUser
                    = new org.springframework.security.core.userdetails.User(
                        username, password, true, true,
                    true, true, authorities);
            return securedUser;
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }
}
