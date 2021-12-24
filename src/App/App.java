package App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.*;

public class App extends JFrame {

    private final static int WIDTH = 1080;
    private final static int HEIGHT = 1080;

    private static int currentSlide = 0;

    private static final JLabel appLabel = new JLabel();
    private static final ArrayList<File> images = new ArrayList<>();

    private ScheduledExecutorService scheduledFuture;
    private long timerTime = 2500;

    private String lastPath;
    private boolean randomize = false;

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
    //TODO: Probably implement the random method in another method and not just an extention of this method
    void update() {
        if (!randomize)
            appLabel.setIcon(new ImageIcon(images.get(currentSlide).getAbsolutePath()));
        else {
            int random = new Random().nextInt(images.size() - 1);
            appLabel.setIcon(new ImageIcon(images.get(random).getAbsolutePath()));
        }
    }

    public void createAndShowGUI() {
        SwingUtilities.invokeLater(() -> {
//            try {
//                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
//                    | UnsupportedLookAndFeelException ex) {
//                ex.printStackTrace();
//            }
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            pack();
            setLocationRelativeTo(null);
            setVisible(true);
        });
    }

    private void insertFiles(File file) throws IOException {
        if (file.isDirectory()) {
            images.addAll(Arrays.asList(Objects.requireNonNull(file.listFiles())));
            images.removeIf(App::isNotImage);
        } else if (file.isFile() && !isNotImage(file)) {
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

    void selectFiles() {
//        System.out.println("Please choose the path of the files.");
        JFileChooser chooser;
        if (lastPath == null) {
            chooser = new ImageFileChooser(false);
        } else {
            chooser = new ImageFileChooser(false, lastPath);

        }

        int returnVal = chooser.showOpenDialog(null);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] files = chooser.getSelectedFiles();
            for (File file : files) {
                try {
                    insertFiles(file);
                    System.out.println("File '" + file.getAbsolutePath() + "' succesfully added.\n");
                } catch (IOException e) {
                    System.out.println("Error. Please try again.\n");
                }
            }
        }

        if (chooser.getSelectedFile() != null)
            lastPath = chooser.getSelectedFile().getParent();

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

    void clearFiles() {
        images.clear();
    }

    boolean isScheduledFutureNull() {
        return scheduledFuture == null;
    }

    boolean imagesEmpty() {
        return !images.isEmpty();
    }

    void startTimer() {
        //Creates a thread pool that can schedule commands to run after a given delay, or to execute periodically.
        scheduledFuture = Executors.newScheduledThreadPool(1);
        scheduledFuture.scheduleAtFixedRate(this::nextImage, timerTime, timerTime, TimeUnit.MILLISECONDS);
    }

    void stopTimer() {
        scheduledFuture.shutdown();
    }

    void randomize() {
        randomize = !randomize;
    }



//    void setTimer(int seconds) {
//        if (startWithTimer && !scheduledFuture.isShutdown())
//            scheduledFuture.shutdown();
//        else {
//            timerTime = seconds;
//            System.out.println("Timer succesfully set to " + timerTime + " seconds");
//        }
//    }

//        public void start () {
//            boolean isRunning = true;
//            while (isRunning) {
//                System.out.println("Please enter a command!");
//                System.out.println("""
//                        --------------------------------------------
//                        p : Play the slideshow.
//                        a : Add a new file.
//                        c : Clear the queue
//                        e: End the program
//                        t: Set/Turn off a scheduled slideshow + (time in seconds)
//                        --------------------------------------------""");
//
//                String[] commands = new Scanner(System.in).nextLine().split(" ");
//                switch (commands[0]) {
//                    case "a" -> selectFiles();
//                    case "p" -> createAndShowGUI();
//                    case "c" -> images.clear();
//                    case "e" -> {
//                        if (scheduledFuture != null)
//                            scheduledFuture.shutdown();
//                        isRunning = false;
//                    }
//                    case "t" -> {
//                        if (startWithTimer && !scheduledFuture.isShutdown())
//                            scheduledFuture.shutdown();
//                        else if (commands.length < 2)
//                            System.out.println("Enter the time in seconds!");
//                        else {
//                            String timer = commands[1];
//                            if (timer.matches("^[+-]?\\d+$")) {
//                                timerTime = Integer.parseInt(timer);
//                                System.out.println("Timer succesfully set to " + timerTime + " seconds");
//                                startWithTimer = true;
//                            } else
//                                System.out.println("Please enter a valid time in seconds.");
//                        }
//                    }
//                    default -> System.out.println("Please enter a valid command!");
//                }
//            }
//        }

}





