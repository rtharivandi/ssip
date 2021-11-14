import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class App extends JFrame {

    private static ArrayList<String> images = new ArrayList<>();
    private static int currentSlide = -1;

    private static JLabel slidesLabel = new JLabel();
    private static ArrayList<Icon> icons = new ArrayList<>();

    public App() {
        slidesLabel.setVerticalAlignment(JLabel.CENTER);
        slidesLabel.setHorizontalAlignment(JLabel.CENTER);
        slidesLabel.setPreferredSize(new Dimension(1920, 1080));
        setLayout(new BorderLayout());
        add(slidesLabel, BorderLayout.CENTER);
        slidesLabel.addMouseListener(new ClickListener());

//        try {
//            Thread.sleep(1500);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        nextImage();
    }

    public void nextImage() {
        currentSlide = (currentSlide + 1) % icons.size();
        slidesLabel.setIcon(icons.get(currentSlide));
    }

    public static void main(String[] args) throws Exception {

        while (true) {
            System.out.println("Please enter a command!");
            System.out.println("p : Play the slideshow.\na : Add a new file.");
            System.out.println("c : Clear the queue");

            // Scanner scanner = new Scanner(System.in);
            char command = new Scanner(System.in).next().charAt(0);

            if (command == 'a') {
                System.out.println("Please enter the path of the pic.");
                String path = new Scanner(System.in).next();

                File readFile = new File(path);

                try {
                    if(readFile.isDirectory()) {
                        System.out.println("Folder added!");
                        insertFilesFolder(readFile);
                    } else {
                        //TODO:Do the resizing thing with single files as well!
                        icons.add(new ImageIcon(ImageIO.read(new File(path))));
                    }
                    System.out.println("File succesfully added.");
                } catch (IOException e) {
                    System.out.println("File unsuccessfully added\nPlease try again.");
                }


            } else if (command == 'p') {
            
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                                | UnsupportedLookAndFeelException ex) {
                            ex.printStackTrace();
                        }

                        App app = new App();
                        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        app.pack();
                        app.setLocationRelativeTo(null);
                        app.setVisible(true);
                    }
                });
            } else if (command == 'c') {
                images.clear();
            } else if (command == 'e') {
                break;
            } else {
                System.out.println("Please enter a valid command.\n");
            }
        }
    }

    public static void insertFilesFolder (File folder) throws IOException {
        for (File file : folder.listFiles()) {
            if (!file.isDirectory() && ImageIO.read(file) != null) {

                BufferedImage bufferedImage = null;

                try {
                    bufferedImage = ImageIO.read(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //TODO:Deal with non 1920x1080 images
                //TODO: Find a way to stretch the JLabel to the size of the current pic!

                Image resizedImage = bufferedImage.getScaledInstance(1920, 1080, Image.SCALE_SMOOTH);
                ImageIcon imageIcon = new ImageIcon(resizedImage);
                icons.add(imageIcon);
            }
        }
    }


    public class ClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            nextImage();
        }
    }

}


