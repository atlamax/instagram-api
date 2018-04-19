package com.mobcrush.instagram;

import com.mobcrush.instagram.request.CreateLiveRequest;
import com.mobcrush.instagram.request.CreateLiveResult;
import com.mobcrush.instagram.request.StartLiveRequest;
import com.mobcrush.instagram.request.StartLiveResult;
import com.mobcrush.instagram.request.payload.CreateLivePayload;
import com.mobcrush.instagram.request.payload.StartLivePayload;
import com.mobcrush.instagram.service.AuthenticateService;
import com.mobcrush.instagram.service.FFmpegRunnerService;
import org.apache.http.client.utils.URIBuilder;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.util.Arrays;
import java.util.Optional;


public class Application {

    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(Application.class);

    private static final String USER_PARAMETER_NAME = "user";
    private static final String PASSWORD_PARAMETER_NAME = "password";
    private static final String FILE_PARAMETER_NAME = "file";

    public static void main(String[] args) {
        String user = parseParameter(USER_PARAMETER_NAME, args);
        String password = parseParameter(PASSWORD_PARAMETER_NAME, args);
        String videoFile = parseParameter(FILE_PARAMETER_NAME, args);

        try {
            Instagram4j instagram = new AuthenticateService().login(user, password);

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
            FFmpegRunnerService.run(videoFile, uri.toString());
        } catch (Exception ex) {
            LOG.error("Something went wrong: ", ex);
        }

    }

    private static String parseParameter(String name, String[] args) {
        Optional<String> optional = Arrays.stream(args)
                .filter(arg ->
                        arg.startsWith("--" + name)
                )
                .map(arg -> {
                    String[] split = arg.split("=");
                    if (split.length == 2) {
                        return split[1];
                    }
                    throw new IllegalArgumentException("Cannot find value for parameter '" + name + "'");
                }).findFirst();

        return optional.get();
    }
}
