import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
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

    private final static int WIDTH = 1080;
    private final static int HEIGHT = 1080;

    private static int currentSlide = 0;

    private static final JLabel appLabel = new JLabel();
    private static final ArrayList<File> images = new ArrayList<>();

    private ScheduledExecutorService scheduledFuture;
    private long timerTime = 3;
    private boolean startWithTimer = false;

    //Constructor
    public App() {
        appLabel.setVerticalAlignment(JLabel.CENTER);
        appLabel.setHorizontalAlignment(JLabel.CENTER);
        appLabel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setLayout(new BorderLayout());
        add(appLabel, BorderLayout.CENTER);

        setJMenuBar(new AppMenuBar(this));

        appLabel.addMouseListener(new ClickListener());

        //Important for keyListener
        appLabel.setFocusable(true);
        appLabel.requestFocusInWindow();
        appLabel.addKeyListener(new KeyPressListener());
    }

    private void prevImage() {
        if (currentSlide == 0)
            currentSlide = images.size() - 1;
        else
            currentSlide = (currentSlide - 1) % images.size();
        update();
    }


    private void nextImage() {
        currentSlide = (currentSlide + 1) % images.size();
        update();
    }

    //TODO: Fit the image to the windows
    void update() {
        File file = images.get(currentSlide);
        try {
            BufferedImage bufferedImage = ImageIO.read(file);
            //Dimension scaled = getScaledDimension(new Dimension(bufferedImage.getWidth(), bufferedImage.getHeight()), getWindowDimension());
            appLabel.setIcon(new ImageIcon(bufferedImage));
//            appLabel.setIcon(new ImageIcon(bufferedImage));
        } catch (IOException e) {
            System.err.println("File error, please try again!");
        }
    }

    void startTimer() {
        //Creates a thread pool that can schedule commands to run after a given delay, or to execute periodically.
        scheduledFuture = Executors.newScheduledThreadPool(1);
        final ScheduledFuture<?> timer = scheduledFuture.scheduleAtFixedRate(this::nextImage, timerTime, timerTime, TimeUnit.SECONDS);
    }

    void stopTimer() {
        scheduledFuture.shutdown();
    }

    boolean isTimerActive() {
        return (scheduledFuture != null);
    }

    void startSlideshow() {
//        if (images.isEmpty()) {
//            System.out.println("No files in queue, please add some.");
//        } else {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                    | UnsupportedLookAndFeelException ex) {
                ex.printStackTrace();
            }
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
//                update();
//                if (startWithTimer)
//                    startTimer();
        });
//        }
    }

    private static void insertFilesFolder(File file) throws IOException {
        if (file.isDirectory()) {
            images.addAll(Arrays.asList(Objects.requireNonNull(file.listFiles())));
            images.removeIf(App::isNotImage);
        } else if (file.isFile() && isNotImage(file)) {
            images.add(file);
        }
    }

    private static boolean isNotImage(File file) {
        try {
            return !Files.probeContentType(file.toPath()).split("/")[0].equals("image");
        } catch (IOException | NullPointerException e) {
            return false;
        }
    }

    static void selectFiles() {
        System.out.println("Please choose the path of the files.");
        JFileChooser chooser = new ImageFileChooser();
        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] files = chooser.getSelectedFiles();
            for (File file : files) {
                try {
                    insertFilesFolder(file);
                    System.out.println("File '" + file.getAbsolutePath() + "' succesfully added.\n");
                } catch (IOException e) {
                    System.out.println("Error. Please try again.\n");
                }
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

    static void clearFiles() {
        images.clear();
    }

    void setTimer(int seconds) {
        if (startWithTimer && !scheduledFuture.isShutdown())
            scheduledFuture.shutdown();
        else {
            timerTime = seconds;
            System.out.println("Timer succesfully set to " + timerTime + " seconds");
        }
    }

        public void start () {
            boolean isRunning = true;
            while (isRunning) {
                System.out.println("Please enter a command!");
                System.out.println("""
                        --------------------------------------------
                        p : Play the slideshow.
                        a : Add a new file.
                        c : Clear the queue
                        e: End the program
                        t: Set/Turn off a scheduled slideshow + (time in seconds)
                        --------------------------------------------""");

                String[] commands = new Scanner(System.in).nextLine().split(" ");
                switch (commands[0]) {
                    case "a" -> selectFiles();
                    case "p" -> startSlideshow();
                    case "c" -> images.clear();
                    case "e" -> {
                        if (scheduledFuture != null)
                            scheduledFuture.shutdown();
                        isRunning = false;
                    }
                    case "t" -> {
                        if (startWithTimer && !scheduledFuture.isShutdown())
                            scheduledFuture.shutdown();
                        else if (commands.length < 2)
                            System.out.println("Enter the time in seconds!");
                        else {
                            String timer = commands[1];
                            if (timer.matches("^[+-]?\\d+$")) {
                                timerTime = Integer.parseInt(timer);
                                System.out.println("Timer succesfully set to " + timerTime + " seconds");
                                startWithTimer = true;
                            } else
                                System.out.println("Please enter a valid time in seconds.");
                        }
                    }
                    default -> System.out.println("Please enter a valid command!");
                }
            }
        }

    }





