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

    private static final JLabel imageLabel = new JLabel();
    private static final ArrayList<File> images = new ArrayList<>();

    private ScheduledExecutorService scheduledFuture;

    private String lastPath;
    private boolean randomize = false;

    //Constructor
    public App() {
        //Setting up the JLabel for the images
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        imageLabel.setHorizontalTextPosition(JLabel.CENTER);
        imageLabel.setVerticalTextPosition(JLabel.BOTTOM);
        imageLabel.setIconTextGap(-45);
        setLayout(new BorderLayout());
        add(imageLabel, BorderLayout.CENTER);

        imageLabel.addMouseListener(new ClickListener());

        //Important for keyListener
        imageLabel.setFocusable(true);
        imageLabel.requestFocusInWindow();
        imageLabel.addKeyListener(new KeyPressListener());

        setJMenuBar(new AppMenuBar(this));
    }

    private void prevImage() {
        if (images.size() != 0) {
            if (currentSlide == 0)
                currentSlide = images.size() - 1;
            else
                currentSlide = (currentSlide - 1) % images.size();
            update();
        }
    }

    private void nextImage() {
        if (images.size() != 0) {
            currentSlide = (currentSlide + 1) % images.size();
            update();
        }
    }

    void update() {
        ImageIcon imageIcon;
        File path;

        if (!randomize) {
            path = images.get(currentSlide);
        } else {
            path = images.get(new Random().nextInt(images.size()));
        }
        imageIcon = new ImageIcon(path.getAbsolutePath());
        //This part is such a mess, find a way to clean it up
        //For some reason, gifs just do not appear when using getScaledInstance
        if(path.getPath().endsWith(".gif")) {
            imageLabel.setIcon(imageIcon);
        } else {
            Dimension dim = getFittingSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
            imageLabel.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(dim.width, dim.height, Image.SCALE_AREA_AVERAGING)));
            imageLabel.setText(path.getPath());
        }
    }

    //Method works 90% of the time, there are still some pictures that do not fit the frame
    private Dimension getFittingSize(int width, int height) {
        //IsWider tells us if the picture is landcape or portrait
        double aspectRatio = (double)width/height;
        boolean isLandscape = aspectRatio > 1;

        //In case it is landscape, the width has to be the same as the getWidth
        //In case it is portrait, the height has to be the same as the getHeigth
        //and then we make resize the other parameter accordingly

        if (isLandscape) {
            double div = (double) height/width;
            return new Dimension(getWidth(), (int)(getWidth()*div));
        } else {
            double div = (double) width/height;
            return new Dimension((int)(getHeight()*div), getHeight()-5);
        }
    }

    public void createAndShowGUI() {
        SwingUtilities.invokeLater(() -> {
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
        imageLabel.setText(chooser.getSelectedFile().getPath() + " added!");

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
        imageLabel.setIcon(null);
        imageLabel.setText("Images cleared!");
    }

    boolean isScheduledFutureNull() {
        return scheduledFuture == null;
    }

    boolean imagesNotEmpty() {
        return !images.isEmpty();
    }

    void startTimer() {
        //Creates a thread pool that can schedule commands to run after a given delay, or to execute periodically.
        scheduledFuture = Executors.newScheduledThreadPool(1);
        long timerTime = 2500;
        scheduledFuture.scheduleAtFixedRate(this::nextImage, timerTime, timerTime, TimeUnit.MILLISECONDS);
    }

    void stopTimer() {
        scheduledFuture.shutdown();
    }

    void randomize() {
        randomize = !randomize;
    }

    boolean isTimerDown() {
        return scheduledFuture.isShutdown();
    }

}





