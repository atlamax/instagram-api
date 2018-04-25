package com.mobcrush.instagram.service;

import com.mobcrush.instagram.domain.MobcamInstagram;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramSearchUsernameRequest;
import org.brunocvcunha.instagram4j.requests.payload.InstagramSearchUsernameResult;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.core.Is.is;

public class AuthenticateServiceTest {

    private static String USERNAME = "ivan.mobile";
    private static String PASSWORD = "ivan.mobile84";

    @Test
    public void testLoginByToken() {
        boolean isThrownException = false;
        try {
            Instagram4j instagram4j = new AuthenticateService().login(USERNAME, PASSWORD);
            MobcamInstagram instagram = new AuthenticateService().loginByToken(instagram4j.getCookieStore().getCookies()
                        .get(0).getValue(), instagram4j.getUuid(), instagram4j.getCookieStore().getCookies().get(7).getValue());

            InstagramSearchUsernameResult userResult = instagram.sendRequest(new InstagramSearchUsernameRequest("github"));
            Assert.assertThat(userResult.getStatus(), is("ok"));
        } catch (Exception expected) {
            isThrownException = true;
        }
        Assert.assertFalse(isThrownException);
    }
}
