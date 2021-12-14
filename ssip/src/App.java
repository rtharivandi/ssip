import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class App extends JFrame {

    private final static int WIDTH = 1920;
    private final static int HEIGHT = 1080;

    private static int currentSlide = 1;

    private static final JLabel slidesLabel = new JLabel();
    private static final ArrayList<File> images = new ArrayList<>();

    public App() {
        slidesLabel.setVerticalAlignment(JLabel.CENTER);
        slidesLabel.setHorizontalAlignment(JLabel.CENTER);
        slidesLabel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new BorderLayout());
        add(slidesLabel, BorderLayout.CENTER);
        slidesLabel.addMouseListener(new ClickListener());
    }

    public void nextImage() {
        currentSlide = (currentSlide + 1) % images.size();
        File file = images.get(currentSlide);
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            slidesLabel.setIcon(new ImageIcon(bufferedImage));
        } catch (IOException e) {
            System.err.println("File error, please try again!");
        }
    }

    public static void startSlideshow(App app) {
        if (images.isEmpty()) {
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

                    app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    app.pack();
                    app.setLocationRelativeTo(null);
                    app.setVisible(true);
                }
            });
        }
    }

    public static void insertFilesFolder(File folder) throws IOException {
        System.out.println("Loading...");
        images.addAll(Arrays.asList(Objects.requireNonNull(folder.listFiles())));
        images.removeIf(App::isNotImage);
    }

    public static boolean isNotImage (File file) {
        try {
            return !Files.probeContentType(file.toPath()).split("/")[0].equals("image");
        } catch (IOException | NullPointerException e) {
            return false;
        }
    }

    public static void selectFiles() {
        System.out.println("Please choose the path of the pic.");
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                insertFilesFolder(file);
                System.out.println("File(s) succesfully added.\n");
            } catch (IOException e) {
                System.out.println("Error. Please try again.\n");
            }
        }
    }

    public class ClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            nextImage();
        }
    }

    public static void main(String[] args) throws Exception {

        while (true) {
            System.out.println("Please enter a command!");
            System.out.println("p : Play the slideshow.\na : Add a new file.\nc : Clear the queue\ne: End the program");

            char command = new Scanner(System.in).next().charAt(0);

            if (command == 'a') {
                selectFiles();
            } else if (command == 'p') {
                App app = new App();
                startSlideshow(app);
            } else if (command == 'c') {
                images.clear();
            } else if (command == 'e') {
                break;
            } else {
                System.out.println("Please enter a valid command.\n");
            }
        }
    }

}




