import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList; 



public class App {

    public App() {
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                
                //Setting the frame of the program
                JFrame frame = new JFrame("SSIP");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                
                //Creating the buffered image
                BufferedImage img = null;

                try {
                    //Trying to get the fill the BufferedImage object with an actual image
                    //Which we need to show error just in case that it's not found
                    img = ImageIO.read(getClass().getResource("fubuki.JPG"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ImageIcon imageIcon = new ImageIcon(img);
                JLabel label = new JLabel();
                label.setIcon(imageIcon);

                frame.getContentPane().add(label, BorderLayout.CENTER);
                frame.pack();

                //Centering the frame
                frame.setLocationRelativeTo(null);

                frame.setVisible(true);

            }
        });
    }

    public static void main(String[] args) throws Exception {
       
        private ArrayList<String> folders = new ArrayList();
        
        if (args[0].equals("a")) {
            //System.out.println("Entering the .");
            folders.add(args[1]);
        } 

        if (args[0].equals("p")) {
            new App();
        }


    }
}
