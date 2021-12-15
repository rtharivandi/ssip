import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FileFilter;

public class ImageFileChooser extends  JFileChooser{

    public ImageFileChooser() {
        addChoosableFileFilter(new FileNameExtensionFilter("Image files", ImageIO.getReaderFileSuffixes()));
        setAcceptAllFileFilterUsed(false);
    }

}
