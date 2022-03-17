package App;

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

