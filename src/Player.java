import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;

/**
 * Created by Jude on 12/6/2017.
 */
public class Player{
    Player(){
        JFrame frame = new JFrame();
        JPanel pane = new JPanel();
        JButton play = new JButton();
        JButton pause = new JButton();
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



        play.setSize(1,10);
        frame.getContentPane().add(pane);
        pane.add(play);
        pane.add(pause);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300,300);
        frame.setVisible(true);
    }
    public static void main(String[] args){
        Player playerWindow = new Player();
    }

}
