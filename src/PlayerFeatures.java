import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.IOException;
import java.net.URL;


/**
 * Created by Jude on 12/7/2017.
 */
public class PlayerFeatures {
    public URL songURL;
    public URL currentSelectedSongURL;
    public static Mixer mixer;
    public static Clip clip;
    public AudioInputStream audioStream;

    PlayerFeatures() {
        Mixer.Info[] mixInfos = AudioSystem.getMixerInfo();
        mixer = AudioSystem.getMixer(mixInfos[0]);
        DataLine.Info dataInfo = new DataLine.Info(Clip.class, null);
        try {
            clip = (Clip) mixer.getLine(dataInfo);
        } catch (LineUnavailableException lue) {
            lue.printStackTrace();
        }


    }

    boolean playSong(URL songURL, JList<String> playList, Boolean isPaused,URL currentSelectedSongURL) {
        System.out.println(songURL + " "+currentSelectedSongURL);
        if(isPaused && songURL.equals(currentSelectedSongURL))
           return resumeSong(clip);
        else{

            try {
                audioStream = AudioSystem.getAudioInputStream(songURL);
                if(!clip.isOpen()) {
                    clip.open(audioStream);
                }
                else{
                    clip.stop();
                    clip.flush();
                    clip.close();
                    clip.open(audioStream);
                }
                clip.start();
            }
            catch(IOException IoEvent){
                IoEvent.printStackTrace();
            }
            catch(UnsupportedAudioFileException uafException){
                uafException.printStackTrace();
            }
            catch (LineUnavailableException luException){
                luException.printStackTrace();
            }

        }
        return false;
    }
    boolean pauseSong(Clip clip){
        clip.stop();
        return true;

    }

    boolean resumeSong(Clip clip){
        System.out.println("In resume song");
        clip.start();
        return false;

    }

}

