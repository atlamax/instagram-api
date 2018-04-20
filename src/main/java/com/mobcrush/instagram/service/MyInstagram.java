package com.mobcrush.instagram.service;

import org.apache.http.client.CookieStore;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.brunocvcunha.instagram4j.Instagram4j;

import java.util.Date;

public class MyInstagram extends Instagram4j {

    public MyInstagram(Instagram4j instagram4j) {

        super("", "");
        //CookieStore cookie = instagram4j.getCookieStore();

        BasicClientCookie cookie  = new BasicClientCookie("csrftoken", instagram4j.getCookieStore().getCookies()
                .get(0).getValue());
        cookie.setDomain(" i.instagram.com");
        cookie.setPath("/");
        cookie.setExpiryDate(new Date());


       // [name: csrftoken][value: bH7RuSV4Ln7f4RUwhdbT6wslw0fwp99z][domain: i.instagram.com][path: /][expiry: Fri Apr 19 18:45:17 MSK 2019],
       // [version: 0][name: ds_user][value: ivan.mobile][domain: i.instagram.com][path: /][expiry: Thu Jul 19 18:45:13 MSK 2018], [version: 0][name: ds_user_id][value: 7547064343][domain: i.instagram.com][path: /][expiry: Thu Jul 19 18:45:17 MSK 2018], [version: 0][name: igfl][value: ivan.mobile][domain: i.instagram.com][path: /][expiry: Sat Apr 21 18:45:15 MSK 2018], [version: 0][name: is_starred_enabled][value: yes][domain: i.instagram.com][path: /][expiry: Thu Apr 15 18:45:15 MSK 2038], [version: 0][name: mid][value: WtoLBwABAAGk-x-38uM8r7bU2Ndb][domain: i.instagram.com][path: /][expiry: Thu Apr 15 18:45:11 MSK 2038], [version: 0][name: rur][value: FRC][domain: i.instagram.com][path: /][expiry: null], [version: 0][name: sessionid][value: IGSC8beeb875debf7c53e458b523c88912354fe8bdf5f0e040d134b07d16c4351630%3AzBcDQbIN9MQ1ppcNhKxwD6sWNUoGTuN4%3A%7B%22_auth_user_id%22%3A7547064343%2C%22_auth_user_backend%22%3A%22accounts.backends.CaseInsensitiveModelBackend%22%2C%22_auth_user_hash%22%3A%22%22%2C%22_platform%22%3A1%2C%22_token_ver%22%3A2%2C%22_token%22%3A%227547064343%3ACPmq4EiWAnXjZBPXCHeBGIFku09ikpym%3A310ea703c2fecd48b9667e19a36fb521eb54f2a9f2ffc8d29812e6840a67460d%22%2C%22last_refreshed%22%3A1524239113.1457436085%7D][domain: i.instagram.com][path: /][expiry: Thu Jul 19 18:45:13 MSK 2018], [version: 0][name: urlgen][value: "{\"time\": 1524239113\054 \"178.124.173.22\": 6697}:1f9YDt:aax5EbihTEKS6H_wv8qFkTMJTxo"][domain: i.instagram.com][path: /][expiry: null]]

        CookieStore cookieStore = new BasicCookieStore();
        cookieStore.addCookie(cookie);
        this.cookieStore = cookieStore;
        this.client = new DefaultHttpClient();
        this.client.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.BROWSER_COMPATIBILITY);
        this.client.setCookieStore(cookieStore);
        this.isLoggedIn =true;
        this.uuid = instagram4j.getUuid();
        //this.rankToken = instagram4j.getRankToken();
        //this.deviceId = instagram4j.getDeviceId();
        //this.userId = instagram4j.getUserId();

    }
}
