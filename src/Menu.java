import App.App;
import App.SlideshowTimer;

import javax.swing.*;

public class Menu {
    
    public static void main(String[] args) {
        App app = new App();
        app.setTitle("SSIP Pre-Alpha");
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            app.createAndShowGUI();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
