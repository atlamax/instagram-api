package com.mobcrush.instagram;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobcrush.instagram.domain.CommentsResponse;
import com.mobcrush.instagram.domain.CreateLiveResponse;
import com.mobcrush.instagram.domain.LikeCountResponse;
import com.mobcrush.instagram.domain.mobcrush.ChatInlineMessage;
import com.mobcrush.instagram.domain.mobcrush.ChatMessage;
import com.mobcrush.instagram.service.*;
import com.mobcrush.instagram.service.mediaserver.MediaServerService;
import com.mobcrush.instagram.service.mediaserver.red5.Red5Service;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;


public class Application {

    private static Logger LOG = LoggerFactory.getLogger(Application.class);

    private static final int PULLING_BROADCAST_DATA_PAUSE = 2000;

    @Option(
            name = "-u", aliases = "--user", usage = "Instagram username", metaVar = "String",
            depends = {"-p"}, forbids = {"-t", "-uuid", "-sessionid"}
    )
    private String user;

    @Option(
            name = "-p", aliases = "--password", usage = "Instagram password", metaVar = "String",
            depends = {"-u"}, forbids = {"-t", "-uuid", "-sessionid"}
    )
    private String password;

    @Option(
            name = "-t", aliases = "--token", usage = "Instagram token", metaVar = "String",
            depends = {"-uuid", "-sessionid"},
            forbids = {"-u", "-p"}
    )
    private String token;

    @Option(name = "-uuid", usage = "Instagram uuid", depends = {"-t", "-sessionid"}, forbids = {"-u", "-p"})
    private String uuid;

    @Option(name = "-sessionid", usage = "Instagram sessionId", depends = {"-uuid", "-t"}, forbids = {"-u", "-p"})
    private String sessionId;

    @Option(
            name = "-f", aliases = "--file", usage = "Path to video file", metaVar = "String",
            forbids = {"-s"}
    )
    private String videoFile;

    @Option(
            name = "-s", aliases = "--stream", usage = "RTMP stream URL", metaVar = "URL",
            forbids = {"-f"}
    )
    private String streamUrl;

    private boolean isLoginByToken = false;

    @Option(
            name = "--mobcrush-host", usage = "Mobcrush base URL", metaVar = "URL",
            depends = {"--mobcrush-chatroom-id", "--mobcrush-access-token"}
    )
    private String mobcrushHost;

    @Option(
            name = "--mobcrush-chatroom-id", usage = "Mobcrush chatroom ID", metaVar = "String",
            depends = {"--mobcrush-host", "--mobcrush-access-token"}
    )
    private String mobcrushChatroomId;

    @Option(
            name = "--mobcrush-access-token", usage = "Mobcrush access token", metaVar = "String",
            depends = {"--mobcrush-chatroom-id", "--mobcrush-host"}
    )
    private String mobcrushAccessToken;

    @Option(
            name = "--mediaserver-host", usage = "Media Server host name or IP address", metaVar = "String"
    )
    private String mediaServerHost;

    @Option(
            name = "--mediaserver-access-token", usage = "Media Server access token", metaVar = "String"
    )
    private String mediaServerAccessToken;

    public static void main(String[] args) {
        new Application().doMain(args);
    }

    private void doMain(String[] args) {
        parseArgs(args);

        try {
            Instagram4j instagram;
            if (isLoginByToken) {
                instagram = new AuthenticateService().loginByToken(token, uuid, sessionId);
            } else {
                instagram = new AuthenticateService().login(user, password);
            }

            LiveBroadcastService liveBroadcastService = new LiveBroadcastService(instagram);
            CreateLiveResponse live = liveBroadcastService.start();
            if (live == null) {
                return;
            }

            Thread ffmpegThread;
            if (streamUrl != null) {
                ffmpegThread = FFmpegRunnerService.run(streamUrl, live.getUploadUrl());
            } else {
                ffmpegThread = FFmpegRunnerService.run(videoFile, fixStreamingURL(live));
            }

            if (ffmpegThread == null) {
                LOG.warn("FFmpeg thread is null. Going to exit");
                return;
            }

            BroadcastDataService broadcastDataService = new BroadcastDataService(instagram);
            LiveHeartbeatService liveHeartbeatService = new LiveHeartbeatService(instagram);
            MediaServerService mediaServerService = new Red5Service(mediaServerHost, mediaServerAccessToken);
            MobcrushService mobcrushService = new MobcrushService(mobcrushChatroomId, mobcrushHost, mobcrushAccessToken);
            ObjectMapper objectMapper = new ObjectMapper();
            String streamName = parseStreamName(streamUrl);
            do {
                CommentsResponse comments = broadcastDataService.getComments(live.getBroadcastId());
                if (comments != null) {
                    LOG.info("Get comments: {}", comments.getCount());
                    comments.getComments().stream()
                            .map(comment -> {
                                ChatInlineMessage inlineMessage = ChatInlineMessage.builder()
                                        .text(comment.getText())
                                        .senderName(comment.getUser().getUsername())
                                        .profileImage(comment.getUser().getProfilePictureURL())
                                        .build();

                                ChatMessage chatMessage = new ChatMessage();
                                try {
                                    chatMessage.setMessage(
                                            objectMapper.writeValueAsString(inlineMessage)
                                    );
                                } catch (JsonProcessingException e) {
                                    LOG.error("Cannot convert message text for sending to Mobcrush");
                                }

                                return chatMessage;
                            })
                            .forEach(mobcrushService::publish);
                }

                liveHeartbeatService.perform(live.getBroadcastId());

                LikeCountResponse likes = broadcastDataService.getLikes(live.getBroadcastId());
                if (likes != null) {
                    LOG.info("Get likes: {}", likes.getCount());
                }

                Thread.sleep(PULLING_BROADCAST_DATA_PAUSE);

            } while (isStreamContinue(mediaServerService, streamName));

            LOG.info("Streaming is finish");
            liveBroadcastService.end(live.getBroadcastId());

        } catch (Exception ex) {
            LOG.error("Something went wrong: ", ex);
        } finally {
            System.exit(0);
        }

    }

    private String fixStreamingURL(CreateLiveResponse live) throws URISyntaxException {
        return new URIBuilder(live.getUploadUrl())
                .setScheme("rtmp")
                .setPort(80)
                .build()
                .toString();
    }

    private void parseArgs(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);

        try {
            // parse the arguments.
            parser.parseArgument(args);
            if ( (StringUtils.isEmpty(user) || StringUtils.isEmpty(password)) &&
                    (StringUtils.isEmpty(token) || StringUtils.isEmpty(uuid) || StringUtils.isEmpty(sessionId)) )
                throw new CmdLineException(parser, "You should set username/password or token/uuid/sessionid ");

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

        if ( StringUtils.isNoneEmpty(token, uuid, sessionId)) {
            isLoginByToken = true;
        }
    }

    private boolean isStreamContinue(MediaServerService mediaServerService, String streamUrl) {

        return mediaServerService.publish(streamUrl).isContinue();
    }

    private String parseStreamName(String streamUrl) {
        try {
            URI uri = new URI(streamUrl);
            String[] paths = uri.getPath().split("/");

            String result = paths[paths.length - 1];
            LOG.info("Successfully parse stream name '{}'", result);

            return result;
        } catch (URISyntaxException e) {
            LOG.error("Cannot parse stream URL");
            return null;
        }
    }
}
