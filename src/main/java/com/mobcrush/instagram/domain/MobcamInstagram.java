package com.mobcrush.instagram.domain;

import org.apache.http.client.CookieStore;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.brunocvcunha.instagram4j.Instagram4j;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

public class MobcamInstagram extends Instagram4j {

    public MobcamInstagram(String csrftoken, String uuid, String sessionId) {
        super("", "");
        BasicClientCookie csrfTokenCcookie  = new BasicClientCookie("csrftoken", csrftoken);
        csrfTokenCcookie.setDomain("i.instagram.com");
        csrfTokenCcookie.setPath("/");
        csrfTokenCcookie.setExpiryDate(Date.from(LocalDate.now().plusMonths(1).atStartOfDay().toInstant(ZoneOffset.UTC)));

        BasicClientCookie sessionIdCcookie  = new BasicClientCookie("sessionid", sessionId);
        sessionIdCcookie.setDomain("i.instagram.com");
        sessionIdCcookie.setPath("/");
        sessionIdCcookie.setExpiryDate(Date.from(LocalDate.now().plusMonths(2).atStartOfDay().toInstant(ZoneOffset.UTC)));

        CookieStore cookieStore = new BasicCookieStore();
        cookieStore.addCookie(csrfTokenCcookie);
        cookieStore.addCookie(sessionIdCcookie);
        this.cookieStore = cookieStore;
        this.client = new DefaultHttpClient();
        this.client.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        this.client.setCookieStore(cookieStore);
        this.isLoggedIn =true;
        this.uuid = uuid;
    }

    public MobcamInstagram(String username, String password) {
        super(username, password);
    }
}
