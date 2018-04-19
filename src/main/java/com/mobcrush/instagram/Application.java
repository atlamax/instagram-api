package com.mobcrush.instagram;

import com.mobcrush.instagram.service.AuthenticateService;
import org.brunocvcunha.instagram4j.Instagram4j;

public class Application {

    public static void main(String[] args) {
        Instagram4j instagram = new AuthenticateService().login("test", "test");
    }
}
