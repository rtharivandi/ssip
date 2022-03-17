package App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class App extends JFrame {

    private final static int WIDTH = 1080;
    private final static int HEIGHT = 1080;

    protected static final JLabel imageLabel = new JLabel();

    private final ScheduledExecutorService slideshowTime = Executors.newScheduledThreadPool(1);
    private final ArrayList<Shuffler> folders = new ArrayList<>();
    private Future<?> future = null;

    private String lastPath;
    private boolean shuffle = false;

    private boolean timerIsRunning = false;

    public App() {
        //Setting up the JLabel for the images
        imageLabel.setVerticalAlignment(JLabel.CENTER);
        imageLabel.setHorizontalAlignment(JLabel.CENTER);
        imageLabel.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        imageLabel.setHorizontalTextPosition(JLabel.CENTER);
        imageLabel.setVerticalTextPosition(JLabel.BOTTOM);
        imageLabel.setIconTextGap(-45);
        imageLabel.setForeground(Color.WHITE);

        setLayout(new BorderLayout());
        add(imageLabel, BorderLayout.CENTER);
        getContentPane().setBackground(Color.BLACK);

        imageLabel.addMouseListener(new ClickListener());

        //Important for keyListener
        imageLabel.setFocusable(true);
        imageLabel.requestFocusInWindow();
        imageLabel.addKeyListener(new KeyPressListener());

        setJMenuBar(new AppMenuBar(this));
    }

    private void prevImage() {
        try {
            String file = shuffle ? getShuffledImagesPrev() : getContinuousImagesPrev();
            update(file);
        } catch (IndexOutOfBoundsException | IllegalArgumentException | ArithmeticException e) {
            imageLabel.setText("No images to show.");
        }
    }

    private void nextImage() {
        try {
            String file = shuffle ? getShuffledImagesNext() : getContinuousImagesNext();
            update(file);
        } catch (IndexOutOfBoundsException | IllegalArgumentException | ArithmeticException e) {
            imageLabel.setText("No images to show.");
        }
    }

    void start() {
        if (imageLabel.getIcon() == null)
            nextImage();
    }

    void update(String file) {
        imageLabel.setIcon(getFittingSize(new ImageIcon(file), getWidth(), getHeight()));
        imageLabel.setText(file);
    }

    //Method works 90% of the time, there are still some pictures that do not fit the frame
    //Moral of the story, REDUCE THE AMOUNT OF FLOATING POINT NUMBER CALCULATIONS AND ENCAPSULATION
    private ImageIcon getFittingSize(ImageIcon imageIcon, int width, int height) {
        int newWidth = imageIcon.getIconWidth();
        int newHeight = imageIcon.getIconHeight();

        if (newWidth > width) {
            newWidth = width;
            newHeight = (newWidth * imageIcon.getIconHeight()) / imageIcon.getIconWidth();
        } else {
            newHeight = height;
            newWidth = (newHeight * imageIcon.getIconWidth()) / imageIcon.getIconHeight();
        }
        return new ImageIcon(imageIcon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_DEFAULT));
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
            addNewFolder();
            addImage(file);
        } else if (file.isFile()) {
            addImage(file);
        }
    }

    void selectFiles() {
        JFileChooser chooser;

        if (lastPath == null)
            chooser = new JFileChooser();
        else
            chooser = new JFileChooser(lastPath);

        //Sets the default values for the JFileChooser
        chooser.setFileHidingEnabled(false);
        chooser.setPreferredSize(new Dimension(720,720));
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);


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
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT : prevImage(); break;
                case KeyEvent.VK_RIGHT : nextImage(); break;
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
        }
    }

    void clearFiles() {
        String parent = clearImages();
        if (parent != null) {
            imageLabel.setIcon(null);
            imageLabel.setText(parent + " cleared!");
        } else {
            imageLabel.setText("No images to clear.");
        }
    }

    boolean imagesEmpty() {
        return isEmpty();
    }

    void startTimer() {
        if (isEmpty())
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

    private int currentFolder;
    private boolean randomize = false;
    private boolean loop = false;

    boolean isTimerDown() {
        return timerIsRunning;
    }

    void switchRandomize() {
        randomize = !randomize;
        String message = randomize ? "enabled" : "disabled";
        imageLabel.setText("Random slideshow is " + message);
    }

    void switchShuffle() {
        shuffle = !shuffle;
        String message = shuffle ? "enabled" : "disabled";
        imageLabel.setText("Shuffle is " + message);
    }

    void switchLoop() {
        loop = !loop;
        String message = loop ? "enabled" : "disabled";
        imageLabel.setText("Looped slideshow is " + message);
    }

    public String getShuffledImagesNext() {
        return getFolderShuffleInfoShuffled(true);
    }

    public String getShuffledImagesPrev() {
        return getFolderShuffleInfoShuffled(false);
    }

    private String getFolderShuffleInfoShuffled(boolean next) {
        int foldersSize = folders.size();
        currentFolder = randomize ? new Random().nextInt(foldersSize) : (next ? (currentFolder + 1) % foldersSize : currentFolder - 1);
        if (currentFolder == -1)
            currentFolder = foldersSize - 1;

        Shuffler temp = folders.get(currentFolder);
        int tempSize = temp.getSize();
        int imageIndex = randomize ? new Random().nextInt(tempSize) : (next ? (temp.getCurrent() + 1) % tempSize : temp.getCurrent() - 1);
        if (imageIndex == -1)
            imageIndex = tempSize - 1;
        temp.setCurrent(imageIndex);
        return temp.getImage(temp.getCurrent()).getAbsolutePath();
    }

    public String getContinuousImagesNext() {
        return getFolderShuffleInfoContinuous(true);
    }

    public String getContinuousImagesPrev() {
        return getFolderShuffleInfoContinuous(false);
    }

    private String getFolderShuffleInfoContinuous(boolean next) {
        currentFolder = randomize ? new Random().nextInt(folders.size()) : currentFolder;
        Shuffler temp = folders.get(currentFolder);
        int size = temp.getSize();

        int imageIndex = randomize ? (new Random().nextInt(size)) : (next ? (temp.getCurrent() + 1) % size : temp.getCurrent() - 1);
        temp.setCurrent(imageIndex == -1 ? size - 1 : imageIndex);

        if (!loop)
            currentFolder = next ? (imageIndex == size - 1 ? (currentFolder + 1) % folders.size() : currentFolder) : (currentFolder == 0 ? folders.size() - 1 : currentFolder - 1);

        return temp.getImage(temp.getCurrent()).getAbsolutePath();
    }

    public void addImage(File file) {
        if (folders.size() == 0)
            folders.add(new Shuffler());
        folders.get(folders.size() - 1).addImage(file);
    }

    public void addNewFolder() {
        folders.add(new Shuffler());
    }

    public String clearImages() {
        if (!folders.isEmpty()) {
            String parent = folders.get(currentFolder).getParentFile();
            folders.remove(currentFolder);
            return parent;
        }
        return null;
    }

    public boolean isEmpty() {
        return folders.isEmpty();
    }

}







