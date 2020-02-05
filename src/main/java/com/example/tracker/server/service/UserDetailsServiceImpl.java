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

import static com.example.tracker.server.constant.DBConstants.DATE_PATTERN;
import static com.example.tracker.server.constant.ExceptionMessages.USER_NOT_FOUND_MESSAGE;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    IUserDAO iUserDAO;

    private final static Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = iUserDAO.getUserByName(username);
        if (user != null && user.getIsActive() == 1) {
            String password = user.getPassword();
            logger.info("Username: " + user.getLogin());
            logger.info("Password: " + password);
            logger.info("Registration date: " + new SimpleDateFormat(DATE_PATTERN).format(user.getRegDate()));

            Collection<GrantedAuthority> authorities= new ArrayList<GrantedAuthority>();
            if ("admin".equals(user.getRole())) {
                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
            } else {
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
            }
            org.springframework.security.core.userdetails.User securedUser
                    = new org.springframework.security.core.userdetails.User(
                        username, password, true, true,
                    true, true, authorities);
            return securedUser;
        } else {
            throw new UsernameNotFoundException(USER_NOT_FOUND_MESSAGE);
        }
    }
}
