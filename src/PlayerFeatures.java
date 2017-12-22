/**
 * This class was created 12/7/2017, it's job is to contain the features that the music player needs to operate
 * Written by Jude Simonis
 */
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

 class PlayerFeatures {
     URL songURL;
     URL currentSelectedSongURL;
     static Clip clip;

     /**
      * The constructor, It will get the information of the various mixers of the computer's audioSystem.
      * It will then obtain dataInformation about the a clip data line.
      * Then It will get a clip by asking the mixer to supply us with a clip data line.
      *
      */
    PlayerFeatures() {
        Mixer mixer;
        Mixer.Info[] mixInfos = AudioSystem.getMixerInfo();
        mixer = AudioSystem.getMixer(mixInfos[0]);
        DataLine.Info dataInfo = new DataLine.Info(Clip.class, null);
        try {
            clip = (Clip) mixer.getLine(dataInfo);
        } catch (LineUnavailableException lue) {
            lue.printStackTrace();
        }
    }

     /**
      *
      * @param songURL the url of the song file that will be played or paused
      * @param isPaused a boolean that represents whether the song is paused or not
      * @param currentSelectedSongURL the URL of the song that is currently highlighted in the Jlist (Might be redundant)
      * @return a boolean signifying if the clip is paused or not.
      */
    boolean playSong(URL songURL,Boolean isPaused,URL currentSelectedSongURL){
        //This if statement might be redundant but it works currently it will resume the song, if both conditions are fulfilled
        if(isPaused && songURL.equals(currentSelectedSongURL))
           return resumeSong(clip);
        else{
            /**
             * If we aren't resuming the song then we are playing a new song
             * we get an input stream which is the music file, and open a clip using that stream, making sure to
             * reset the clip if the clip already had a stream
             */
            AudioInputStream audioStream;
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

    private boolean resumeSong(Clip clip){
        clip.start();
        return false;

    }
}

