package App;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AppMenuBar extends JMenuBar {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gd = (Graphics2D) g;
        gd.setColor(Color.gray);
        gd.fillRect(0,0,getWidth()-1,getHeight()-1);
    }

    public AppMenuBar(App app) {
        JMenu file = new JMenu("File");
        JMenuItem addFiles = new JMenuItem("Add file(s)");
        JMenuItem clearFiles = new JMenuItem("Clear files");

        addFiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                app.selectFiles();
                app.update();
            }
        });

        addFiles.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                App.clearFiles();
            }
        });
        file.add(addFiles);
        file.add(clearFiles);

        JMenu play = new JMenu("Play");
        play.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                app.startTimer();
            }

            @Override
            public void menuDeselected(MenuEvent e) {
                app.stopTimer();
            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });

        add(file);
        add(play);

        JMenu timer = new JMenu("Timer");

//        setPreferredSize(new Dimension(getWidth(), getHeight()));
    }
}
