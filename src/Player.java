/**
 * This is a program, that will mostly automate the musical chairs process, all the user needs to do is select the song
 * and play the song initially, and whenever the song is paused.
 * Created by Jude Simonis
 * Project Started 12/6/2017
 *
 */

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.prefs.Preferences;
import static java.lang.Thread.sleep;

public class Player{
  private static  String[] songs;
  private DefaultListModel model = new DefaultListModel();
  private JList<String> playList;
  private static JButton pause;
  private PlayerFeatures features;
  private boolean Ispaused;
  private static JMenuItem chooseDirectory;
  private static JFileChooser fileExplorer;
  private Preferences preferences;
  private static String ID;
  private static File directory;
  private int min = 10;
  private int max = 27;
  private int randomnum;
  private Thread game;
    /**
     * Constructor, Primary purpose is to create the player window, if its the first time the user is running the program
     * it will ask the user to select the music directory. It will also generate the initial playlist.
     */
  private  Player(){
        JFrame frame;
        JPanel panelTop;
        JPanel panelBottom;
        JButton play;
        JMenuBar menuBar;
        JMenu menu;
        JScrollPane scrollPane;
        /* Retrieving the node that the preferred directory will be saved into*/
        preferences = Preferences.userRoot().node(this.getClass().getName());
        ID = "Preferred Directory";
        preferences.get(ID,"");
        /*Instantiating the playerFeatures class, this class contains the code for the music player's various actions*/
        features = new PlayerFeatures();
        Ispaused = false; // No music is playing so nothing is paused.
       /*Getting the info for a clip type dataLine*/
        DataLine.Info dataInfo = new DataLine.Info(Clip.class,null);
        /*Creating our file explorer*/
        fileExplorer = new JFileChooser();
        fileExplorer.setDialogTitle("Select Music Folder");
        fileExplorer.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        /*If this is the first time the software is running request the user to find their music directory*/
        if(preferences.get(ID,"").equals("")){
            int returnVal = fileExplorer.showOpenDialog(chooseDirectory);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                directory = fileExplorer.getCurrentDirectory();
                preferences.put(ID,directory.toString()); // This now becomes the default
            }
        }
        /*If the if statement didn't go off, then we've already got a directory that we can work with*/
        directory = new File(preferences.get(ID,""));
        List<String> list = makePlayList(directory);
        /*If the list size is 0 then then open the file explorer again for the user to find their music*/
        while (list.size() == 0){
            int returnVal = fileExplorer.showOpenDialog(chooseDirectory);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                directory = fileExplorer.getCurrentDirectory();
                preferences.put(ID,directory.toString());
            }
            list = makePlayList(directory);
        }
        int i;
        /* now that we have our list, we need an array of strings so we will convert it*/
        songs = new String[list.size()];
        songs = list.toArray(songs);
        /*Add the songs to the model, one by one I am using a model as it makes editing the play list a lot easier*/
        for(i = 0; i < list.size(); i++)
            addItems(songs[i],i);
        /*create a Jlist using the model*/
        playList = new JList(model);
        /*Configurations for the playList componenet, and adding a listener that listens to mouse actions*/
        playList.addMouseListener(new listMouseListener());
        playList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        playList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        playList.setVisibleRowCount(-1);
        frame = new JFrame("Musical Chairs");
        panelTop = new JPanel(); //Making two, Panels to give me more control of where I place my components
        panelBottom = new JPanel();
        panelBottom.setLayout(new BoxLayout(panelBottom,BoxLayout.X_AXIS)); // We want a boxLayout for the bottom Panel.
        play = new JButton();
        play.addActionListener(new playListener()); //Listener for the button
       // pause = new JButton();
        //pause.addActionListener(new pauseListener());
        scrollPane = new JScrollPane(playList); //We need a scrollPane which is what and we attach the play list to it
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panelTop.add(scrollPane);
        menuBar = new JMenuBar();
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);
        chooseDirectory = new JMenuItem("Select Music Folder",KeyEvent.VK_S);
        chooseDirectory.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_1,ActionEvent.ALT_MASK)); //ALT+1 to access
        chooseDirectory.addActionListener(new chooseDirectoryListener());
        menu.add(chooseDirectory);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);
        /*Setting the play button an image requires a try and and a catch*/
        try{
            Image img = ImageIO.read(getClass().getResource("Play24.gif"));
           // Image pauseImg = ImageIO.read(getClass().getResource("Pause16.gif"));
            play.setIcon(new ImageIcon(img));
          //  pause.setIcon(new ImageIcon(pauseImg));
            Dimension buttonDimensions = new Dimension(25,26);
            play.setPreferredSize(buttonDimensions);
            //pause.setPreferredSize(buttonDimensions);
        }
        catch(IllegalArgumentException ex){
            System.out.println("No input exist");
            ex.printStackTrace();
        }
        catch(IOException ioe){
            System.out.println("Error while reading");
            ioe.printStackTrace();
        }
        panelBottom.add(play);
        //panelBottom.add(pause);
        frame.getContentPane().add(BorderLayout.WEST,panelTop); //Top panel goes on west side, bottom goes on south side
        frame.getContentPane().add(BorderLayout.SOUTH,panelBottom);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,250);
        frame.setVisible(true);
        game = new Thread(new Game()); //the thread reference we will use for the game.
    }

    /**This function creates an Array as a list we do this by accessiong the method list in the File class.
     * we create a quick anonymous FilenameFilter and override it's accept method so that it will return only the files
     * in the directory that end with the .wav file format.
     *
     * @param directory the directory that the music files are in
     * @return a list of strings, where the strings are the names of the music files.
     */
    private List<String> makePlayList(File directory){
        List<String> list = Arrays.asList(directory.list(
                new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".wav");
                    }
                }
        ));
        return list;
    }

    /**Adds a song to the model
     *
     * @param song the name of the song file as a string
     * @param index the index of the model that we will be adding the song to
     */
    private void addItems(String song,int index){
        model.add(index,song);
    }

    /*clears the model*/
    private  void clearItems(){
        model.clear();
    }
 /*   class pauseListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event) {
            if(features.clip.isActive())
                Ispaused = features.pauseSong(features.clip);


        }
    }*/

    /**
     * What follows now are a bunch of inner classes, they implement various interfaces we use these classes as listeners
     * They work together to determine how the user is starting the song, they also change the value of Ispaused
     * to make sure that every listener will use the correct boolean value so the song will pause and play correctly
     */
    class listMouseListener implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent event) {
            /*Using string concatenation to get the full directory that includes the wav file*/
            String newDirectory = directory + "\\"+playList.getSelectedValue();
            File songDirectory = new File(newDirectory);
            try {
                features.songURL = songDirectory.toURI().toURL();
            }
            catch(MalformedURLException mue){
                System.out.println("Something's wrong with the URL");
            }
            if(event.getClickCount() == 2){
                //generate random num for the game you'll see this through out the rest of the program
                randomnum = ThreadLocalRandom.current().nextInt(min,max+1);
                Ispaused = false; // I don't want double clicking the list to resume a paused song.
                Ispaused = features.playSong(features.songURL,Ispaused,features.currentSelectedSongURL);
                game.run();
            }
            if(event.getClickCount() == 1){ //This part here is most likely redundant once I am clear why I'll probably change it
                features.currentSelectedSongURL = features.songURL;
            }
        }

        // Unneeded but necessary implementations
        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }
    }

    class playListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event) {
            randomnum = ThreadLocalRandom.current().nextInt(min,max+1);
            /*If the song is paused, then we will play the song but the playSong function should branch into a resume function*/
            if(Ispaused) {
                    randomnum = ThreadLocalRandom.current().nextInt(min,max+1);
                    Ispaused = features.playSong(features.songURL,Ispaused, features.currentSelectedSongURL);
                    game.run();
            }
            /*Else we will just play the song, and play Song should just play the song*/
            else{
                randomnum = ThreadLocalRandom.current().nextInt(min,max+1);
                Ispaused = features.playSong(features.songURL,Ispaused,features.currentSelectedSongURL);
                game.run();
            }
        }
    }

    /**
     * The game, using the thread.run command starts the game which uses a random number and sleeps the thread
     * when the sleep is over it wil pause the song, the  main problem with this approach currently is that sleep, will
     * sleep the player to making it unable to be interacted with.
     */
    class Game implements Runnable {
        public void run(){
            Game();
        }

        void Game(){
            long use = randomnum *1000;
            try {
                sleep(use);
                Ispaused =  features.pauseSong(features.clip );
            }
            catch (InterruptedException Iexception){
                Iexception.printStackTrace();
            }
        }
    }

    /**
     * When the user hits Choose music Directory, it will open the file explorer, once they select a directory
     * that directory will be set as the preference, and the playlist will be updated by editing the model that the play
     * list uses.
     */
    class chooseDirectoryListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            int returnVal = fileExplorer.showOpenDialog(chooseDirectory);
            if(returnVal == JFileChooser.APPROVE_OPTION){
                System.out.println(fileExplorer.getCurrentDirectory().toString() + "test");
                directory = fileExplorer.getCurrentDirectory();
                preferences.put(ID,directory.toString());
                List<String> list = makePlayList(directory);
                songs = list.toArray(songs);
                clearItems();
                for(int i = 0; i < list.size(); i++)
                    addItems(songs[i],i);
            }
        }
    }

    public static void main(String[] args){
        Player playerWindow = new Player();
    }
}
