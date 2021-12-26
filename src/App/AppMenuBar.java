package App;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AppMenuBar extends JMenuBar {

//    @Override
//    protected void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        Graphics2D gd = (Graphics2D) g;
//        gd.setColor(Color.gray);
//        gd.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
//    }

    public AppMenuBar(App app) {
        JMenu file = new JMenu("File");
        JMenuItem addFiles = new JMenuItem("Add file(s)");
        JMenuItem clearFiles = new JMenuItem("Clear files");

        addFiles.addActionListener(e -> {
            app.selectFiles();
            if(app.imagesNotEmpty())
                app.start();
        });

        clearFiles.addActionListener(e -> app.clearFiles());

        file.add(addFiles);
        file.add(clearFiles);
        add(file);

        //We should implement these ones with JMenuItem
        JMenuItem play = new JMenu("Play");
        play.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (app.getSlideshowTimer().isTimerDown()) {
                    try {
                        app.startTimer();
                    } catch (NullPointerException nullPointerException) {
                        System.err.println("Please add a file!");
                    }
                } else {
                    app.stopTimer();
                }
            }
        });

        add(play);

        JMenuItem random = new JMenu("Random");
        random.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                app.randomize();
            }
        });

        add(random);

        JMenuItem shuffle = new JMenu("Shuffle");
        shuffle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                app.switchShuffle();
            }
        });

        add(shuffle);


    }
}
