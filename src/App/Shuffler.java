package App;

<<<<<<< HEAD
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;

public class Shuffler extends  JFileChooser {

    private String lastPath;


    public Shuffler(boolean hideFiles) {
        addChoosableFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
        setAcceptAllFileFilterUsed(false);
        setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        setPreferredSize(new Dimension(1000, 800));
        setMultiSelectionEnabled(true);
        setFileHidingEnabled(hideFiles);
    }

    public Shuffler(boolean hideFiles, String s) {
        super(s);
        addChoosableFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
        setAcceptAllFileFilterUsed(false);
        setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        setPreferredSize(new Dimension(1000, 800));
        setMultiSelectionEnabled(true);
        setFileHidingEnabled(hideFiles);
    }

}

=======
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Shuffler {
    private final ArrayList<File> images = new ArrayList<>();
    private int current;

    public Shuffler() {
        current = 0;
    }

    public File getImage(int index) {
        return images.get(index);
    }

    public void addImage(File file) {
        if (file.isDirectory())
            images.addAll(Arrays.asList(Objects.requireNonNull(file.listFiles())));
        else
            images.add(file);
        images.removeIf(this::isNotImage);
    }

    public void clearImages() {
        images.clear();
    }

    private boolean isNotImage(File file) {
        try {
            return !Files.probeContentType(file.toPath()).split("/")[0].equals("image");
        } catch (IOException | NullPointerException e) {
            return false;
        }
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getSize() {
        return images.size();
    }

    public boolean isEmpty() {
        return images.isEmpty();
    }

    public String getParentFile() {
        return images.get(0).getParent();
    }
}
>>>>>>> 241f15d5d72429cd8e0089bb319141a8b3d44d35
