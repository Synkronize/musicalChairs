import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Jude on 12/6/2017.
 */
public class Player{
    Player(){

            //get resource grabs things, with get resource we can grab things that are sitting in side the class folder.
            File directory = new File("C:\\Users\\Jude\\Documents\\javaSoundPractice\\src");
            List<String> list = Arrays.asList(directory.list(
                    new FilenameFilter() {
                        @Override
                        public boolean accept(File dir, String name) {
                            return name.endsWith(".wav");
                        }
                    }
            ));
        String[] songs = new String[list.size()];
        songs = list.toArray(songs);
        JList<String> playList = new JList(songs);
        playList.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        playList.setLayoutOrientation(JList.HORIZONTAL_WRAP);
        playList.setVisibleRowCount(-1);
        JFrame frame = new JFrame();
        JPanel panelTop = new JPanel();
        JPanel panelBottom = new JPanel();
        panelBottom.setLayout(new BoxLayout(panelBottom,BoxLayout.X_AXIS));
        JButton play = new JButton();
        JButton pause = new JButton();
        JScrollPane scrollPane = new JScrollPane(playList);
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



        panelBottom.add(play);
        panelBottom.add(pause);
        frame.getContentPane().add(BorderLayout.WEST,panelTop);
        frame.getContentPane().add(BorderLayout.SOUTH,panelBottom);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,250);
        frame.setVisible(true);
    }
    public static void main(String[] args){
        Player playerWindow = new Player();
    }

}
