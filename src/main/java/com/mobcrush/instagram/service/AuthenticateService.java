package com.mobcrush.instagram.service;

import org.apache.http.client.CookieStore;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Date;
import java.util.Optional;

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

    public MyInstagram loginByToken (Instagram4j instagram4j) {
//        String token, String uuid
        Instagram4j instagram = null;


//        Optional[[version: 0][name: csrftoken][value: obKH6USZpMKpr8G4VykGta6Ts1xQeZUB][domain: i.instagram.com][path: /][expiry: Fri Apr 19 16:10:29 MSK 2019]]
//        try {

            //instagram.setup();
            //instagram.login();
//            LOGGER.info("Login success");
//        } catch (IOException ex) {
//            LOGGER.error("Error occurred", ex);
//        }
        return new MyInstagram(instagram4j);
    }


}
