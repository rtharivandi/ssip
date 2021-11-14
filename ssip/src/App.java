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
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class App extends JFrame {

    private final static int WIDTH = 1920;
    private final static int HEIGHT = 1080;

    private static ArrayList<String> images = new ArrayList<>();
    private static int currentSlide = 0;

    private static JLabel slidesLabel = new JLabel();
    private static ArrayList<Icon> icons = new ArrayList<>();

    public App() {
        slidesLabel.setVerticalAlignment(JLabel.CENTER);
        slidesLabel.setHorizontalAlignment(JLabel.CENTER);
        slidesLabel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new BorderLayout());
        add(slidesLabel, BorderLayout.CENTER);
        slidesLabel.addMouseListener(new ClickListener());
    }

    public void nextImage() {
        currentSlide = (currentSlide + 1) % icons.size();
        slidesLabel.setIcon(icons.get(currentSlide));
    }

    public static void main(String[] args) throws Exception {

        while (true) {
            System.out.println("Please enter a command!");
            System.out.println("p : Play the slideshow.\na : Add a new file.\nc : Clear the queue");

            char command = new Scanner(System.in).next().charAt(0);

            if (command == 'a') {
                System.out.println("Please enter the path of the pic.");
                String path = new Scanner(System.in).next();

                File file = new File(path);

                try {
                    if(file.isDirectory()) {
                        insertFilesFolder(file);
                    } else {
                        //TODO:Do the resizing thing with single files as well!
                        insertFileToQueue(file);
                    }
                    System.out.println("File(s) succesfully added.\n");
                } catch (IOException e) {
                    System.out.println("Error. Please try again.\n");
                }

            } else if (command == 'p') {
                if (icons.isEmpty()) {
                    System.out.println("No files in queue, please add some.");
                } else {
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
                }
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
        System.out.println("Loading...");
        for (File file : Objects.requireNonNull(folder.listFiles())) {
            if (!file.isDirectory() && ImageIO.read(file) != null) {
                insertFileToQueue(file);
            }
        }
    }

    public static void insertFileToQueue (File file) {
        icons.add(new ImageIcon(new ImageIcon(file.getPath()).getImage().getScaledInstance(WIDTH, HEIGHT, Image.SCALE_DEFAULT)));
    }

    public class ClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            nextImage();
        }
    }

}


