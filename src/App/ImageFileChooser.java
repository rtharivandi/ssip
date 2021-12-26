package App;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.FileFilter;

public class ImageFileChooser extends  JFileChooser {

    private String lastPath;


    public ImageFileChooser(boolean hideFiles) {
        addChoosableFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
        setAcceptAllFileFilterUsed(false);
        setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        setPreferredSize(new Dimension(1000, 800));
        setMultiSelectionEnabled(true);
        setFileHidingEnabled(hideFiles);
    }

    public ImageFileChooser(boolean hideFiles, String s) {
        super(s);
        addChoosableFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
        setAcceptAllFileFilterUsed(false);
        setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        setPreferredSize(new Dimension(1000, 800));
        setMultiSelectionEnabled(true);
        setFileHidingEnabled(hideFiles);
    }

}

