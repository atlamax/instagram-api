package com.mobcrush.instagram.service;

import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.job.FFmpegJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

import static org.apache.commons.lang3.Validate.notNull;

public class FFmpegRunnerService {

    private static Logger LOGGER = LoggerFactory.getLogger(FFmpegRunnerService.class);

    private static final String COMPLEX_FILTER_BASE = "[0:v]crop=w=iw:h=ih/2[top];[1:v]crop=w=iw:h=ih/2[bottom];[top][bottom]vstack[vid]";
    private static final String STEREO_AUDIO_COMPLEX_FILTER = ";amerge,pan=stereo|c0<c0|c1<c1";
    private static final String MONO_AUDIO_COMPLEX_FILTER = ";pan=stereo|c0=c0|c1=c0[aout]";
    private static final String[] MONO_AUDIO_MAP_PARAMETERS = new String[] {"-map", "[aout]"};

    public static void run(@Nonnull String videoFilePath, @Nonnull String streamUploadUrl) {
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

            /*
            '-rtbufsize 256M -re -i %s
             -acodec libmp3lame
              -ar 44100
               -b:a 128k
                -pix_fmt yuv420p
                 -profile:v baseline
                  -s 720x1280
                   -bufsize 6000k
                    -vb 400k
                     -maxrate 1500k
                      -deinterlace
                       -vcodec libx264
                        -preset veryfast
                         -g 30
                          -r 30
                           -f flv
                            %s'
             */

            FFmpegJob job = new FFmpegExecutor(ffmpeg, ffprobe).createJob(builder);
            new Thread(() -> job.run()).start();
        } catch (Exception e) {
            LOGGER.error("Error occur during running FFMpeg", e);
        }
    }
}
