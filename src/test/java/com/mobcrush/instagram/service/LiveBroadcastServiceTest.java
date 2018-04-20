package com.mobcrush.instagram.service;

import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class LiveBroadcastServiceTest {

    @Test
    public void testRequestBody() throws Exception {

        HttpPost request = new HttpPost("some_url");
        request.setEntity(new UrlEncodedFormEntity(getLiveRequestBodyParams("some_uuid", "some_token")));
        System.out.println(
                IOUtils.readLines(request.getEntity().getContent(), "UTF-8")
        );
    }


    private List<NameValuePair> getLiveRequestBodyParams(String uuid, String token) {
        List<NameValuePair> result = new ArrayList<>();
        result.add(new BasicNameValuePair("_uuid", uuid));
        result.add(new BasicNameValuePair("_csrftoken", token));
        result.add(new BasicNameValuePair("should_send_notifications", "1"));

        return result;
    }
}