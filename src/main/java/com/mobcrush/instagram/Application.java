package com.mobcrush.instagram;

import com.mobcrush.instagram.domain.CreateLiveResponse;
import com.mobcrush.instagram.request.CreateLiveRequest;
import com.mobcrush.instagram.request.CreateLiveResult;
import com.mobcrush.instagram.request.StartLiveRequest;
import com.mobcrush.instagram.request.StartLiveResult;
import com.mobcrush.instagram.request.payload.CreateLivePayload;
import com.mobcrush.instagram.request.payload.StartLivePayload;
import com.mobcrush.instagram.service.AuthenticateService;
import com.mobcrush.instagram.service.FFmpegRunnerService;
import com.mobcrush.instagram.service.InstagramBroadcastingService;
import org.apache.http.client.utils.URIBuilder;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.slf4j.LoggerFactory;

import java.net.URI;


public class Application {
    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(Application.class);
    private static String TEST_USERNAME = "ivan.mobile";
    private static String TEST_PASSWORD = "ivan.mobile84";

    public static void main(String[] args) {
        try {
            Instagram4j instagram = new AuthenticateService().login(TEST_USERNAME, TEST_PASSWORD);

            CreateLivePayload payload = new CreateLivePayload();
            payload.set_uuid(instagram.getUuid());
            payload.set_csrftoken(instagram.getOrFetchCsrf());
            payload.setPreview_height("1184");
            payload.setPreview_width("720");
            payload.setBroadcast_message("");
            payload.setBroadcast_type("RTMP");
            payload.setInternal_only("0");
            CreateLiveRequest request = new CreateLiveRequest(payload);
            CreateLiveResult response = instagram.sendRequest(request);

            StartLivePayload startLivePayload = new StartLivePayload();
            startLivePayload.set_uuid(instagram.getUuid());
            startLivePayload.set_csrftoken(instagram.getOrFetchCsrf());
            startLivePayload.setShould_send_notifications("1");
            StartLiveRequest startLiveRequest = new StartLiveRequest(startLivePayload, response.getBroadcastId());
            StartLiveResult startLiveResult = instagram.sendRequest(startLiveRequest);

            URI uri =  new URIBuilder(response.getUploadUrl())
                    .setScheme("rtmp")
                    .setPort(80)
                    .build();
            FFmpegRunnerService.run("c:\\Downloads\\sample.mp4", uri.toString());
        } catch (Exception ex) {
            LOG.error("Something went wrong: ", ex);
        }

    }
}
