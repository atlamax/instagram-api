package com.mobcrush.instagram;

import com.mobcrush.instagram.service.AuthenticateService;

public class Application {

    public static void main(String[] args) {
        new AuthenticateService().login("test", "test");
    }
}
