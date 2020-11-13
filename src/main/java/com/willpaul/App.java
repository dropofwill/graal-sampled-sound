package com.willpaul;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.Mixer.Info;

public class App {
    // c+p from https://stackoverflow.com/questions/23096533/how-to-play-a-sound-with-a-given-sample-rate-in-java
    public static float SAMPLE_RATE = 8000f;

    public static void tone(int hz, int msecs)
            throws LineUnavailableException {
            tone(hz, msecs, 1.0);
    }

    public static void tone(int hz, int msecs, double vol)
        throws LineUnavailableException {
        byte[] buf = new byte[1];
        AudioFormat af =
            new AudioFormat(
                    SAMPLE_RATE, // sampleRate
                    8,           // sampleSizeInBits
                    1,           // channels
                    true,        // signed
                    false);      // bigEndian
        SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
        sdl.open(af);
        sdl.start();
        for (int i=0; i < msecs*8; i++) {
            double angle = i / (SAMPLE_RATE / hz) * 2.0 * Math.PI;
            buf[0] = (byte)(Math.sin(angle) * 127.0 * vol);
            sdl.write(buf,0,1);
        }
        sdl.drain();
        sdl.stop();
        sdl.close();
    }

    public static void main( String[] args)
        throws InterruptedException, LineUnavailableException {

        Info[] mixerInfos = AudioSystem.getMixerInfo();
        for (Info info : mixerInfos) {
            System.out.println(info);
        }

        System.out.println(mixerInfos.length);
        if (mixerInfos.length == 0)
            throw new AssertionError("no mixers available");

        // To get the java home error
        // tone(1000,100);
        // Thread.sleep(1000);
    }
}
