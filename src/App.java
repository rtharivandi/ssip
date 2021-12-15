import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.concurrent.*;

public class App extends JFrame {

    private final static int WIDTH = 1920;
    private final static int HEIGHT = 1080;

    private static int currentSlide = 0;

    private static final JLabel appLabel = new JLabel();
    private static final ArrayList<File> images = new ArrayList<>();

    //Constructor
    public App() {
        appLabel.setVerticalAlignment(JLabel.CENTER);
        appLabel.setHorizontalAlignment(JLabel.CENTER);
        appLabel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new BorderLayout());
        add(appLabel, BorderLayout.CENTER);
        appLabel.addMouseListener(new ClickListener());
        //Important for keyListener
        appLabel.setFocusable(true);
        appLabel.requestFocusInWindow();
        appLabel.addKeyListener(new KeyPressListener());
    }

    private void prevImage() {
        if(currentSlide == 0)
            currentSlide = images.size()-1;
        else
            currentSlide = (currentSlide - 1) % images.size();
        update();
    }


    private void nextImage() {
        currentSlide = (currentSlide + 1) % images.size();
        update();
    }

    //TODO: Fit the image to the windows
    private void update() {
        File file = images.get(currentSlide);
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            Dimension scaled = getScaledDimension(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()), getWindowDimension());
            appLabel.setIcon(new ImageIcon(getScaledImage(bufferedImage, scaled.width, scaled.height)));
//            appLabel.setIcon(new ImageIcon(bufferedImage));
        } catch (IOException e) {
            System.err.println("File error, please try again!");
        }
    }

    private void startWithTimer(long s) {
        //Creates a thread pool that can schedule commands to run after a given delay, or to execute periodically.
        ScheduledExecutorService scheduledFuture = Executors.newScheduledThreadPool(1);
        final ScheduledFuture<?> timer = scheduledFuture.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                nextImage();
            }
        }, s, s, TimeUnit.SECONDS);
    }

    public static BufferedImage getScaledImage(BufferedImage image, int width, int height) throws IOException {
        int imageWidth  = image.getWidth();
        int imageHeight = image.getHeight();

        double scaleX = (double)width/imageWidth;
        double scaleY = (double)height/imageHeight;
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp bilinearScaleOp = new AffineTransformOp(scaleTransform, AffineTransformOp.TYPE_BILINEAR);

        return bilinearScaleOp.filter(
                image,
                new BufferedImage(width, height, image.getType()));
    }

    private void startSlideshow() {
        if (images.isEmpty()) {
            System.out.println("No files in queue, please add some.");
        } else {
            SwingUtilities.invokeLater(() -> {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                        | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }
                setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                pack();
                setLocationRelativeTo(null);
                setVisible(true);
                update();
                startWithTimer(5);
            });
        }
    }

    private static void insertFilesFolder(File folder) throws IOException {
        images.addAll(Arrays.asList(Objects.requireNonNull(folder.listFiles())));
        images.removeIf(App::isNotImage);
    }

    private static boolean isNotImage(File file) {
        try {
            return !Files.probeContentType(file.toPath()).split("/")[0].equals("image");
        } catch (IOException | NullPointerException e) {
            return false;
        }
    }

    private static void selectFiles() {
        System.out.println("Please choose the path of the files.");
        JFileChooser chooser = new ImageFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            try {
                insertFilesFolder(file);
                System.out.println("File " + chooser.getSelectedFile().getAbsolutePath() + " succesfully added.\n");
            } catch (IOException e) {
                System.out.println("Error. Please try again.\n");
            }
        }
    }

    private class ClickListener extends MouseAdapter {
        @Override
        public void mouseClicked(MouseEvent e) {
            nextImage();
        }
    }

    private class KeyPressListener extends KeyAdapter {
        @Override
        public void keyTyped(KeyEvent e) {
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            switch (keyCode) {
                case KeyEvent.VK_LEFT -> prevImage();
                case KeyEvent.VK_RIGHT -> nextImage();
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

    private Dimension getWindowDimension() {
        return new Dimension(WIDTH, HEIGHT);
    }

    private Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {
        int original_width = imgSize.width;
        int original_height = imgSize.height;
        int bound_width = boundary.width;
        int bound_height = boundary.height;
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }

    public void start() throws Exception {
        while (true) {
            System.out.println("Please enter a command!");
            System.out.println("p : Play the slideshow.\na : Add a new file.\nc : Clear the queue\ne: End the program");

            char command = new Scanner(System.in).next().charAt(0);

            if (command == 'a') {
                selectFiles();
            } else if (command == 'p') {
                startSlideshow();
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




