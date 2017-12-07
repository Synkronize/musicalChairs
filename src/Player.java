import javafx.collections.ListChangeListener;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Jude on 12/6/2017.
 */
public class Player{
  static  String[] songs;
  public  JList<String> playList;
  static  JFrame frame;
  static  JPanel panelTop;
  static  JPanel panelBottom;
  static JButton play;
  static JButton pause;
  static JScrollPane scrollPane;
  public PlayerFeatures features;
  boolean Ispaused;
  boolean pauseIt;




    Player(){
        features = new PlayerFeatures();
        pauseIt = false;
        Ispaused = false;
        DataLine.Info dataInfo = new DataLine.Info(Clip.class,null);
        //get resource grabs things, with get resource we can grab things that are sitting in side the class folder.
        File directory = new File("C:\\Users\\Jude\\Documents\\musicalChairs\\src");
        List<String> list = Arrays.asList(directory.list(
                new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                            return name.endsWith(".wav");
                        }
                }
            ));
        songs = new String[list.size()];
        songs = list.toArray(songs);
        playList = new JList(songs);
        playList.addMouseListener(new listMouseListener());
        playList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        playList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        playList.setVisibleRowCount(-1);
        frame = new JFrame();
        panelTop = new JPanel();
        panelBottom = new JPanel();
        panelBottom.setLayout(new BoxLayout(panelBottom,BoxLayout.X_AXIS));
        play = new JButton();
        play.addActionListener(new playListener());
        pause = new JButton();
        pause.addActionListener(new pauseListener());
        scrollPane = new JScrollPane(playList);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        panelTop.add(scrollPane);

        try{
            Image img = ImageIO.read(getClass().getResource("Play24.gif"));
            Image pauseImg = ImageIO.read(getClass().getResource("Pause16.gif"));
            play.setIcon(new ImageIcon(img));
            pause.setIcon(new ImageIcon(pauseImg));
            Dimension buttonDimensions = new Dimension(25,26);
            play.setPreferredSize(buttonDimensions);
            pause.setPreferredSize(buttonDimensions);


        }catch(IllegalArgumentException ex){
            System.out.println("No input exist");
            ex.printStackTrace();
        }catch(IOException ioe){
            System.out.println("Error while reading");
            ioe.printStackTrace();
        }


        features.songURL = Player.class.getResource(songs[0]);
        panelBottom.add(play);
        panelBottom.add(pause);
        frame.getContentPane().add(BorderLayout.WEST,panelTop);
        frame.getContentPane().add(BorderLayout.SOUTH,panelBottom);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,250);
        frame.setVisible(true);
    }
    class listListener implements ListSelectionListener{
        @Override
        public void valueChanged(ListSelectionEvent event) {
            if(event.getValueIsAdjusting())
                features.songURL = Player.class.getResource(playList.getSelectedValue());
        }
    }
    class pauseListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event) {
            Ispaused = features.playSong(features.songURL,playList,true,Ispaused);


        }
    }
    class listMouseListener implements MouseListener{
        @Override
        public void mouseClicked(MouseEvent event) {
            if(event.getClickCount() == 2){

                if(Ispaused && (features.songURL.equals(Player.class.getResource(playList.getSelectedValue())))) {
                    features.playSong(features.songURL, playList, false, Ispaused);
                    Ispaused = false;
                }
                else {
                    features.songURL = Player.class.getResource(playList.getSelectedValue());
                    features.playSong(features.songURL, playList, false,Ispaused );
                }
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            features.songURL = Player.class.getResource(playList.getSelectedValue());
            System.out.println(playList.getSelectedValue());
        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }
    }
    class playListener implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent event) {

            if(Ispaused && (features.songURL.equals(Player.class.getResource(playList.getSelectedValue())))) {
                Ispaused = features.playSong(features.songURL, playList, false, Ispaused);

            }

            else {
                System.out.println("we in here");
                System.out.println(playList.getSelectedValue());
                Ispaused = false;
                features.playSong(features.songURL, playList, false, Ispaused);
            }
        }
    }



    public static void main(String[] args){
        Player playerWindow = new Player();


    }

}
