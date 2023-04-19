package nl.rug.ai.oop.rpg.general;

import java.net.URL;

import javax.sound.sampled.*;

/**
 * Sound class: stores URL of sounds that can be accesed with an index
 * 
 * @author Matthijs
 * @author Niclas
 *         Based on: RyiSnow (https://www.youtube.com/c/RyiSnow)
 */
public class Sound {

    private Clip clip;
    private URL soundURL[] = new URL[100]; // store file paths of sound files

    public Sound() {
        String s = "/BBA/Sound/";
        soundURL[0] = getClass().getResource(s + "BlueBoyAdventure.wav");
        soundURL[1] = getClass().getResource(s + "coin.wav");
        soundURL[2] = getClass().getResource(s + "powerup.wav");
        soundURL[3] = getClass().getResource(s + "unlock.wav");
        soundURL[4] = getClass().getResource(s + "fanfare.wav");

        String s2 = "/Sound/";
        soundURL[41] = getClass().getResource(s2 + "Golden slaughterer.wav");
        soundURL[42] = getClass().getResource(s2 + "Prison strip.wav");
        soundURL[43] = getClass().getResource(s2 + "Worldend_dominator.wav");
        soundURL[44] = getClass().getResource(s2 + "Miragecoordinator.wav");
        soundURL[45] = getClass().getResource(s2 + "Resurrected replayer.wav");
        soundURL[46] = getClass().getResource(s2 + "Ruriair.wav");
        soundURL[47] = getClass().getResource(s2 + "Liberated liberater.wav");
        soundURL[48] = getClass().getResource(s2 + "The Executioner.wav");
        soundURL[49] = getClass().getResource(s2 + "lastendconductor_compressed.wav");

        String s3 = "/SE/";
        soundURL[5] = getClass().getResource(s3 + "Hi-tech-button-click-interface-623.wav");
        soundURL[6] = getClass().getResource(s3 + "umilse_028.wav");
        soundURL[7] = getClass().getResource(s3 + "umise_1053.wav");
        soundURL[8] = getClass().getResource(s3 + "umise_004.wav");
        soundURL[9] = getClass().getResource(s3 + "umile_005.wav");
        soundURL[10] = getClass().getResource(s3 + "umise_007.wav");
        soundURL[11] = getClass().getResource(s3 + "umise_009.wav");
        soundURL[12] = getClass().getResource(s3 + "umise_020.wav");
        soundURL[13] = getClass().getResource(s3 + "umise_036.wav");

        soundURL[14] = getClass().getResource(s3 + "umise_058.wav");
        soundURL[15] = getClass().getResource(s3 + "umise_1001.wav");
    }

    /**
     * Open audio file i
     * 
     * @author RyiSnow (https://www.youtube.com/c/RyiSnow)
     * @param i
     */
    public void setFile(int i) {
        try (AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i])) {
            clip = AudioSystem.getClip();
            clip.open(ais);
            ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(-25f);
        } catch (IllegalArgumentException | LineUnavailableException e) {
            System.out.println("Sound effects died :(\n restart game to fix");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Play sound file
     * 
     * @author RyiSnow (https://www.youtube.com/c/RyiSnow)
     */
    public void play() {
        clip.start();
    }

    /**
     * @author RyiSnow (https://www.youtube.com/c/RyiSnow)
     */
    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Stop sound
     * 
     * @author RyiSnow (https://www.youtube.com/c/RyiSnow)
     */
    public void stop() {
        clip.stop();
    }

}
