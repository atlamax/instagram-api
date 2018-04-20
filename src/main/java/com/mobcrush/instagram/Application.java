package com.mobcrush.instagram;

import com.mobcrush.instagram.request.CreateLiveRequest;
import com.mobcrush.instagram.request.CreateLiveResult;
import com.mobcrush.instagram.request.StartLiveRequest;
import com.mobcrush.instagram.request.StartLiveResult;
import com.mobcrush.instagram.request.payload.CreateLivePayload;
import com.mobcrush.instagram.request.payload.StartLivePayload;
import com.mobcrush.instagram.service.AuthenticateService;
import com.mobcrush.instagram.service.FFmpegRunnerService;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.LoggerFactory;

import java.net.URI;


public class Application {

    private static org.slf4j.Logger LOG = LoggerFactory.getLogger(Application.class);

    @Option(name = "-u", aliases = "--user", usage = "Instagram username")
    private String user;

    @Option(name = "-p", aliases = "--password", usage = "Instagram password")
    private String password;

    @Option(name = "-t", aliases = "--token", usage = "Instagram token")
    private String token;

    @Option(name = "-f", aliases = "--file", usage = "Path to video file")
    private String videoFile;

    @Option(name = "-s", aliases = "--stream", usage = "RTMP stream URL")
    private String streamUrl;

    public static void main(String[] args) {
        new Application().doMain(args);
    }

    public void doMain(String[] args) {
        parseArgs(args);

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
            if (streamUrl != null) {
                FFmpegRunnerService.run(streamUrl, uri.toString());
            } else {
                FFmpegRunnerService.run(videoFile, uri.toString());
            }

        } catch (Exception ex) {
            LOG.error("Something went wrong: ", ex);
        }
    }

    private void parseArgs(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            // parse the arguments.
            parser.parseArgument(args);
            if ( (StringUtils.isEmpty(user) || StringUtils.isEmpty(password)) && StringUtils.isEmpty(token))
                throw new CmdLineException(parser, "You should set username/password or token ");

            if (StringUtils.isEmpty(videoFile) && StringUtils.isEmpty(streamUrl) )
                throw new CmdLineException(parser, "You should set at least one of parameters -  path to video file or RTMP stream URL ");

        } catch( CmdLineException e ) {
            System.err.println(e.getMessage());
            System.err.println("available arguments...");
            // print the list of available options
            parser.printUsage(System.err);
            System.err.println();
            System.exit(1);
        }

    }


}
