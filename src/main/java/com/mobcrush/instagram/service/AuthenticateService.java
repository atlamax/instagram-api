package com.mobcrush.instagram.service;

import org.brunocvcunha.instagram4j.Instagram4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AuthenticateService {
    private static Logger LOGGER = LoggerFactory.getLogger(AuthenticateService.class);


    public Instagram4j login (String username, String password) {
        Instagram4j instagram = null;
        try {
            instagram = Instagram4j.builder().username(username).password(password).build();
            instagram.setup();
            instagram.login();
            LOGGER.info("Login success");
        } catch (IOException ex) {
            LOGGER.error("Error occurred", ex);
        }
        return instagram;
    }
}
