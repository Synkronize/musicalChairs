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
    public static Mixer mixer;
    public static Clip clip;
    final int PAUSE = 1;
    PlayerFeatures(){
        Mixer.Info[] mixInfos = AudioSystem.getMixerInfo();
        mixer = AudioSystem.getMixer(mixInfos[0]);
        DataLine.Info dataInfo = new DataLine.Info(Clip.class,null);
        try{
            clip = (Clip)mixer.getLine(dataInfo);
        }catch(LineUnavailableException lue){
            lue.printStackTrace();
        }


    }

    Boolean playSong(URL songURL, JList<String> playList, Boolean pause,Boolean IsPaused) {
      if (pause){
          if(clip.isRunning()) {
              clip.stop();
              return pause;
          }
      }
      if(IsPaused){
          if(songURL)
          System.out.println(playList.getSelectedValue());
          System.out.println("how");
          clip.start();
          return false;
        }
      else {
              clip.stop();
              clip.flush();
              clip.close();
              songURL = Player.class.getResource(playList.getSelectedValue());


          try {
              AudioInputStream audioStream = AudioSystem.getAudioInputStream(songURL);
              if(!clip.isOpen()) {
                  clip.open(audioStream);
              }
              clip.start();
          } catch (LineUnavailableException lue) {
              lue.printStackTrace();
          } catch (UnsupportedAudioFileException uafe) {
              uafe.printStackTrace();
          } catch (IOException ioe) {
              ioe.printStackTrace();
          }
      }
      return  pause;
  }

}
