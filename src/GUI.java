import com.sun.tools.javac.Main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class GUI implements ActionListener {

    private JFrame frame;
    private JPanel panel;
    private JLabel label;
    public int cookies = 0;

    public GUI(){
        loadCookies();

        frame = new JFrame();
        panel = new JPanel();

        JToolBar toolBar = new JToolBar("Application");

        JButton button = new JButton("GET A COOKIE!");
        label = new JLabel();
        updateLabel();

        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(this);

        panel.setBorder(BorderFactory.createEmptyBorder(30, 10, 30, 10));

        frame.setResizable(false);
        panel.setLayout(new GridLayout(0, 1));

        JPopupMenu menu = new JPopupMenu("Menu");

        JMenuItem Reset_Cookies = new JMenuItem(new MyAction());
        menu.add(Reset_Cookies);

        frame.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1){
                    menu.show(frame , e.getX(), e.getY());
                }
            }
        });

        frame.add(menu);

        panel.add(button);
        panel.add(label);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Get Some Cookies!");
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args){
        new GUI();

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        cookies += 1;

        save();

        updateLabel();
    }

    void save(){
        try (OutputStream output = new FileOutputStream("./src/.properties")) {
            Properties prop = new Properties();
            prop.setProperty("cookies.count", Integer.toString(cookies));
            prop.store(output, null);
        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void loadCookies(){
        try (InputStream input = Main.class.getClassLoader().getResourceAsStream(".properties")) {
            Properties prop = new Properties();
            if (input == null) {
                System.out.println("Sorry, unable to find .properties");
                return;
            }
            prop.load(input);
            cookies = Integer.parseInt(prop.getProperty("cookies.count"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void updateLabel() {
        String s = "YOU HAVE " + cookies + " COOKIES!";
        String html = String.format("<html><div style=\"width:%dpx; text-align:center;\">%s</div></html>", 200, s);
        label.setText(html);
    }

    public class MyAction extends AbstractAction {
        public MyAction() {
            super("Reset Cookies");
        }

        public void actionPerformed(ActionEvent e) {
            cookies = 0;
            updateLabel();
            save();
        }
    }
}