package com.mobcrush.instagram;

import com.mobcrush.instagram.service.AuthenticateService;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.slf4j.LoggerFactory;


public class Application {
    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        try {
            Instagram4j instagram = new AuthenticateService().login("test", "test");
        } catch (Exception ex) {
            LOG.error("Something went wrong: ", ex);
        }

    }
}
