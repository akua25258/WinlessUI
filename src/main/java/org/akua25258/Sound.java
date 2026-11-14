package org.akua25258;

import javax.sound.sampled.*;
import java.net.URL;

public class Sound
{
    public Sound(String resourcePath)
    {
        try {
            URL soundUrl = getClass().getResource(resourcePath);
            if (soundUrl == null) return;

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(soundUrl);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);

            clip.addLineListener(e -> { if (e.getType() == LineEvent.Type.STOP) clip.close(); });

            clip.start();
        } catch (Exception ex) { ex.printStackTrace(); }
    }
}
