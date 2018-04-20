package com.mobcrush.instagram;

import com.mobcrush.instagram.request.*;
import com.mobcrush.instagram.request.payload.CreateLivePayload;
import com.mobcrush.instagram.request.payload.StartLivePayload;
import com.mobcrush.instagram.service.*;
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

            //Instagram4j myInstagram = new AuthenticateService().loginByToken(instagram);


            LiveBroadcastService liveBroadcastService = new LiveBroadcastService(instagram);
            CreateLiveResult live = liveBroadcastService.start();
            if (live == null) {
                return;
            }

            Thread ffmpegThread;
            if (streamUrl != null) {
                ffmpegThread = FFmpegRunnerService.run(streamUrl, live.getUploadUrl());
            } else {
                ffmpegThread = FFmpegRunnerService.run(videoFile, new URIBuilder(live.getUploadUrl())
                        .setScheme("rtmp")
                        .setPort(80)
                        .build().toString());
            }

            if (ffmpegThread == null) {
                LOG.warn("FFmpeg thread is null. Going to exit");
                return;
            }

            BroadcastDataService broadcastDataService = new BroadcastDataService(instagram);
            LiveHeartbeatService liveHeartbeatService = new LiveHeartbeatService(instagram);
            do {
                CommentsResponse comments = broadcastDataService.getComments(live.getBroadcastId());
                if (comments != null) {
                    LOG.info("Get comments: {}", comments.getCount());
                }

                liveHeartbeatService.perform(live.getBroadcastId());

                LikeCountResponse likes = broadcastDataService.getLikes(live.getBroadcastId());
                if (likes != null) {
                    LOG.info("Get likes: {}", likes.getCount());
                }

            } while (ffmpegThread.isAlive());

            liveBroadcastService.end(live.getBroadcastId());

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
