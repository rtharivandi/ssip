package App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App extends JFrame {

    private final static int WIDTH = 1080;
    private final static int HEIGHT = 1080;

    protected static final JLabel imageLabel = new JLabel();
    private static final Shuffler shuffler = new Shuffler();

    private final ScheduledExecutorService slideshowTime = Executors.newScheduledThreadPool(1);
    private Future<?> future = null;

    private String lastPath;
    private boolean shuffle = false;

    private boolean timerIsRunning = false;

    //Constructor
    public App() {
        //Setting up the JLabel for the images
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        imageLabel.setHorizontalTextPosition(JLabel.CENTER);
        imageLabel.setVerticalTextPosition(JLabel.CENTER);
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
        try {
            File file = shuffle ? shuffler.getShuffledImagesPrev() : shuffler.getContinuousImagesPrev();
            update(file);
        } catch (IndexOutOfBoundsException | IllegalArgumentException | ArithmeticException e) {
            imageLabel.setText("No images to show.");
        }
    }

    private void nextImage() {
        try {
            File file = shuffle ? shuffler.getShuffledImagesNext() : shuffler.getContinuousImagesNext();
            update(file);
        } catch (IndexOutOfBoundsException | IllegalArgumentException | ArithmeticException e) {
            imageLabel.setText("No images to show.");
        }
    }

    void start() {
        if (shuffler.getNumberOfFolders() == 1)
            nextImage();
    }

    void update(File file) {
        ImageIcon imageIcon = new ImageIcon(file.getAbsolutePath());

        if (file.getPath().endsWith(".gif")) {
            imageLabel.setIcon(imageIcon);
        } else {
            Dimension dim = getFittingSize(imageIcon.getIconWidth(), imageIcon.getIconHeight());
            imageLabel.setIcon(new ImageIcon(imageIcon.getImage().getScaledInstance(dim.width, dim.height, Image.SCALE_SMOOTH)));
            imageLabel.setText(file.getPath());
        }
    }

    //Method works 90% of the time, there are still some pictures that do not fit the frame
    private Dimension getFittingSize(int width, int height) {
        double aspectRatio = (double) width / height;
        boolean isLandscape = aspectRatio > 1;

        if (isLandscape) {
            double div = (double) height / width;
            return new Dimension(getWidth(), (int) (getWidth() * div));
        } else {
            double div = (double) width / height;
            return new Dimension((int) (getHeight() * div), getHeight());
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
            shuffler.addNewFolder();
            shuffler.addImage(file);
        } else if (file.isFile()) {
            shuffler.addImage(file);
        }
    }

    void selectFiles() {
        JFileChooser chooser;
        if (lastPath == null)
            chooser = new ImageFileChooser(false);
        else
            chooser = new ImageFileChooser(false, lastPath);

        int returnVal = chooser.showOpenDialog(null);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File[] files = chooser.getSelectedFiles();
            for (File file : files) {
                try {
                    insertFiles(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        if (chooser.getSelectedFile() != null) {
            lastPath = chooser.getSelectedFile().getParent();
            imageLabel.setText(chooser.getSelectedFile().getPath() + " added!");
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

    void clearFiles() {
        String parent = shuffler.clearImages(shuffler.getCurrentFolder());
        if (parent != null){
            imageLabel.setIcon(null);
            imageLabel.setText(parent + " cleared!");
        } else {
            imageLabel.setText("No images to clear.");
        }
    }

    boolean imagesEmpty() {
        return shuffler.isEmpty();
    }

    void startTimer() {
        if (shuffler.isEmpty())
            imageLabel.setText("No images to play. Please insert images!");
        else {
            future = slideshowTime.scheduleAtFixedRate(this::nextImage, 2500, 2500, TimeUnit.MILLISECONDS);
            imageLabel.setText("Slideshow started!");
            timerIsRunning = true;
        }
    }

    void stopTimer() {
        future.cancel(true);
        imageLabel.setText("Slideshow paused!");
        timerIsRunning = false;
    }

    boolean isTimerDown() {
        return timerIsRunning;
    }

    void randomize() {
        boolean randomized = shuffler.randomize();
        String message = randomized ? "enabled" : "disabled";
        imageLabel.setText("Random slideshow is " + message);
    }

    void switchShuffle() {
        shuffle = !shuffle;
        String message = shuffle ? "enabled" : "disabled";
        imageLabel.setText("Shuffle is " + message);
    }

    void loop() {
        boolean loop = shuffler.loop();
        String message = loop ? "enabled" : "disabled";
        imageLabel.setText("Looped slideshow is " + message);
    }
}







