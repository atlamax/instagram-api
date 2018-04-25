package com.mobcrush.instagram.service;

import com.mobcrush.instagram.domain.MobcamInstagram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AuthenticateService {
    private static Logger LOGGER = LoggerFactory.getLogger(AuthenticateService.class);


    public MobcamInstagram login (String username, String password) {
        MobcamInstagram instagram = null;
        try {
            instagram = new MobcamInstagram(username,password);
            instagram.setup();
            instagram.login();
            LOGGER.info("Login success");
        } catch (IOException ex) {
            LOGGER.error("Error occurred", ex);
        }
        return instagram;
    }

    public MobcamInstagram loginByToken(String csrftoken, String uuid, String sessionIdCookie) {
        return new MobcamInstagram(csrftoken, uuid, sessionIdCookie);
    }
}
