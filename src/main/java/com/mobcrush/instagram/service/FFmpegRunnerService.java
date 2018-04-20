package com.mobcrush.instagram.service;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static org.apache.commons.lang3.Validate.notNull;

public class FFmpegRunnerService {

    private static Logger LOGGER = LoggerFactory.getLogger(FFmpegRunnerService.class);

    @Nullable
    public static Thread run(@Nonnull String videoFilePath, @Nonnull String streamUploadUrl) {
        notNull(videoFilePath, "Path to video file must not be null");
        notNull(streamUploadUrl, "Stream URL must not be null");

        LOGGER.info("Going to start FFmpeg for file {} and URL {}", videoFilePath, streamUploadUrl);

        try {
            FFmpeg ffmpeg = new FFmpeg();
            FFprobe ffprobe = new FFprobe();
            FFmpegBuilder builder = new FFmpegBuilder()
                    .setInput(videoFilePath)
                    .addExtraArgs("-rtbufsize", "256M")
                    .addExtraArgs("-re")
                    .setVerbosity(FFmpegBuilder.Verbosity.VERBOSE)
                    .addOutput(streamUploadUrl)
                        .addExtraArgs("-acodec", "libmp3lame")
                        .addExtraArgs("-ar", "44100")
                        .addExtraArgs("-b:a", "128k")
                        .addExtraArgs("-pix_fmt", "yuv420p")
                        .addExtraArgs("-profile:v", "baseline")
                        .addExtraArgs("-s", "720x1280")
                        .addExtraArgs("-bufsize", "6000k")
                        .addExtraArgs("-vb", "400k")
                        .addExtraArgs("-maxrate", "1500k")
                        .addExtraArgs("-deinterlace")
                        .addExtraArgs("-vcodec", "libx264")
                        .addExtraArgs("-preset", "veryfast")
                        .addExtraArgs("-g", "30")
                        .addExtraArgs("-r", "30")
                        .addExtraArgs("-f", "flv")
                        .done();

            FFmpegJob job = new FFmpegExecutor(ffmpeg, ffprobe)
                    .createJob(builder);
            Thread thread = new Thread(() -> job.run());
            thread.start();

            return thread;
        } catch (Exception e) {
            LOGGER.error("Error occur during running FFmpeg", e);
            return null;
        }
    }
}
