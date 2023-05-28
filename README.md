# graal sampled sound attempt

This repo shows my attempt at getting graal native image to produce tones.

As of now it does not work. Based on the maven config from the [Fortune demo on graalvm website](https://github.com/graalvm/graalvm-demos/blob/ba9607e6278d993c5154b5e232c20899a291c678/fortune-demo/fortune/pom.xml#L63)

Repro steps:
* Build with non graalvm to get a working jar
```
mvn clean package
```
* Run the image with graal tracer agent to pick up JNI config and confirm tone plays, mixers are found, etc.
```
mvn -Pnative -Dagent exec:exec@java-agent
```
* Now build a native image with that config
```
mvn -Pnative -Dagent package
```
* Run it natively with sound.properties path
```
./target/graal-sound-sampled-bug -Djavax.sound.config.file=$JAVA_HOME/conf/sound.properties
```

Output on my M1 laptop natively:
```
Iterator has next: true
Found: com.sun.media.sound.DirectAudioDeviceProvider
Found: com.sun.media.sound.PortMixerProvider
Exception in thread "main" java.lang.IllegalArgumentException: No line matching interface javax.sound.sampled.SourceDataLine supporting format PCM_SIGNED 8000.0 Hz, 8 bit, mono, 1 bytes/frame is supported.
	at java.desktop@19.0.2/javax.sound.sampled.AudioSystem.getLine(AudioSystem.java:423)
	at java.desktop@19.0.2/javax.sound.sampled.AudioSystem.getSourceDataLine(AudioSystem.java:532)
	at com.willpaul.App.tone(App.java:33)
	at com.willpaul.App.tone(App.java:20)
	at com.willpaul.App.main(App.java:68)
```

vs. jar:
```
Port MacBook Air Microphone, version Unknown Version
Port MacBook Air Speakers, version Unknown Version
Default Audio Device, version Unknown Version
MacBook Air Microphone, version Unknown Version
MacBook Air Speakers, version Unknown Version
Iterator has next: true
Found: com.sun.media.sound.DirectAudioDeviceProvider
Found: com.sun.media.sound.PortMixerProvider
```

And we hear a tone.

Note we can find the providers by directly using the ServiceLoader, but when that gets wrapped by sampled.sound packages it doesn't work, even with the JNI config from the direct access.
