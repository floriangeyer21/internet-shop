package com.internet.shop.security;

import com.internet.shop.exceptions.AuthenticationException;
import com.internet.shop.lib.Inject;
import com.internet.shop.lib.Service;
import com.internet.shop.model.User;
import com.internet.shop.service.UserService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {
    @Inject
    private UserService userService;

    @Override
    public User login(String login, String password) throws AuthenticationException {
        if (userService.findByLogin(login).isPresent()
                && userService.findByLogin(login).get().getPassword().equals(password)) {
            User userFromDB = userService.findByLogin(login).get();
            return userFromDB;
        }
        throw new AuthenticationException("Incorrect users name or password");
    }
}
