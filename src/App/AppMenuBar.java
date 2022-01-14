package App;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class AppMenuBar extends JMenuBar {

    public AppMenuBar(App app) {
        JMenu file = new JMenu("File");
        JMenuItem addFiles = new JMenuItem("Add file(s)");
        JMenuItem clearFiles = new JMenuItem("Clear files");

        addFiles.addActionListener(e -> {
            app.selectFiles();
            app.start();
        });
        clearFiles.addActionListener(e -> app.clearFiles());
        file.add(addFiles);
        file.add(clearFiles);
        add(file);

        JMenu play = new JMenu("Play");
        play.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!app.isTimerDown()) {
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

        JMenu random = new JMenu("Random");
        random.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                app.switchRandomize();
            }
        });
        add(random);

        JMenu shuffle = new JMenu("Shuffle");
        shuffle.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                app.switchShuffle();
            }
        });
        add(shuffle);

        JMenu loop = new JMenu("Loop");
        loop.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                app.switchLoop();
            }
        });
        add(loop);


    }
}
