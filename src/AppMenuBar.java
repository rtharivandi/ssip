import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.awt.*;

public class AppMenuBar extends JMenuBar {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D gd = (Graphics2D) g;
        gd.setColor(Color.gray);
        gd.fillRect(0,0,getWidth()-1,getHeight()-1);
    }

    public AppMenuBar() {
        JMenu jMenu = new JMenu("Add file(s)");
        //JMenuItem addFiles = new JMenuItem("Add file(s)");
        jMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                App.selectFiles();
            }

            @Override
            public void menuDeselected(MenuEvent e) {

            }

            @Override
            public void menuCanceled(MenuEvent e) {

            }
        });
        //jMenu.add(addFiles);
        add(jMenu);

        JMenu timer = new JMenu("Timer");
//        setPreferredSize(new Dimension(getWidth(), getHeight()));
    }
}
