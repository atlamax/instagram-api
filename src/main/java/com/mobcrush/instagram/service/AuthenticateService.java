package com.mobcrush.instagram.service;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class AuthenticateService {
    private static Logger LOGGER = LoggerFactory.getLogger(AuthenticateService.class);

    private static String API_BASE_URL = "https://i.instagram.com/api/v1/";
    private static String AUTH_URL = "accounts/login/";


    private static CloseableHttpClient httpClient = buildHttpClient();


    public void login (String username, String password) {
        // Perform a full relogin if necessary.
        // Full relogin




        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            LOGGER.error("You must provide a username and password to login().");
            return;
        }

        HttpPost request = new HttpPost(API_BASE_URL + AUTH_URL);
        CloseableHttpResponse response;

        request.addHeader(HttpHeaders.USER_AGENT, "Instagram 27.0.0.7.97 Android (23/6.0.1; 640dpi; 1440x2560; ZTE; ZTE A2017U; ailsa_ii; qcom; en_US)");
        request.addHeader(HttpHeaders.CONNECTION, "Keep-Alive");
        request.addHeader("X-FB-HTTP-Engine", "Liger");
        request.addHeader(HttpHeaders.ACCEPT, "*/*");
        request.addHeader(HttpHeaders.ACCEPT_ENCODING, "gzip,deflate");
        request.addHeader(HttpHeaders.ACCEPT_LANGUAGE, "en-US");
        request.addHeader(HttpHeaders.HOST, "i.instagram.com");
        request.addHeader("X-IG-App-ID", "567067343352427");
        request.addHeader("X-IG-Capabilities", "3brTBw==");
        request.addHeader("X-IG-Connection-Type", "WIFI");
        request.addHeader("X-IG-Connection-Speed", "1137kbps");
        request.addHeader("X-IG-Bandwidth-Speed-KBPS", "-1.000");
        request.addHeader("X-IG-Bandwidth-TotalBytes-B", "0");
        request.addHeader("X-IG-Bandwidth-TotalTime-MS", "0");
        request.addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8");

        try {
            request.setEntity(new UrlEncodedFormEntity(getAuthRequestBodyParams(username, password)));
            response = httpClient.execute(request);
            String requestBody = IOUtils.toString(response.getEntity().getContent(), Charset.forName("UTF-8"));
            LOGGER.info("Response body from Instagram: {}", requestBody);
        } catch (Exception e) {
            LOGGER.error("Error occurred", e);
            return;
        }

        switch (response.getStatusLine().getStatusCode()) {
            case 200: LOGGER.info("Auth success"); break;
            case 429: LOGGER.error("Throttled by Instagram because of too many API requests."); break;
            case 431: LOGGER.error("Throttled by Instagram because of too many API requests."); break;
            default: LOGGER.info("Access is denied");
        }

    }

    private List<NameValuePair> getAuthRequestBodyParams(String username, String password) {
        List<NameValuePair> authRequestBodyParams = new ArrayList<>();
        authRequestBodyParams.add(new BasicNameValuePair("phone_id", "062d1b67-07bc-4346-93e5-33d769c00ca9"));
        authRequestBodyParams.add(new BasicNameValuePair("_csrftoken", "null"));
        authRequestBodyParams.add(new BasicNameValuePair("username", username));
        authRequestBodyParams.add(new BasicNameValuePair("password", password));
        authRequestBodyParams.add(new BasicNameValuePair("login_attempt_count", "0"));
        authRequestBodyParams.add(new BasicNameValuePair("device_id", "android-e66aa0f453e75ce6"));
        authRequestBodyParams.add(new BasicNameValuePair("adid", "725b19c1-8bfa-4c78-bc73-106a6d3a7e5a"));
        authRequestBodyParams.add(new BasicNameValuePair("guid", "129a384a-014e-4d56-837f-66ab63fd3035"));

        return authRequestBodyParams;
    }

    private static CloseableHttpClient buildHttpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setAuthenticationEnabled(false)
                .build();

        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

}
