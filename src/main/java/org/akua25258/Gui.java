package org.akua25258;

import javax.swing.*;
import java.awt.*;

import static org.akua25258.Main.getConfig;
import static org.akua25258.Main.setTextPosition;

public class Gui
{
    private final JFrame frame = new JFrame("WinlessUI - Setting Menu");
    final int wid;
    final int hei;

    public Gui()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        wid = (int) (screenSize.width * 0.65);
        hei = (int) (screenSize.height * 0.7);

        frame.setSize(wid, hei);
        frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        java.net.URL iconUrl = getClass().getResource("/icon.jpg");
        if (iconUrl != null)
        {
            ImageIcon icon = new ImageIcon(iconUrl);
            frame.setIconImage(icon.getImage());
        }

        JPanel panel = new JPanel(null);
        panel.setBackground(new Color(0, 0, 0));

        panel.add(MText("テキストの位置", 5));
        panel.add(MButton(1, "左上", wid / 2 - 200, 50));
        panel.add(MButton(2, "中上", wid / 2, 50));
        panel.add(MButton(3, "右上", wid / 2 + 200, 50));
        panel.add(MButton(4, "左下", wid / 2 - 200, 125));
        panel.add(MButton(5, "中下", wid / 2, 125));
        panel.add(MButton(6, "右下", wid / 2 + 200, 125));

        panel.add(MText("Created by akua25258", hei - 75));
        frame.add(panel);
    }

    public void open()
    {
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        new Sound("/menu.wav");
    }

    private JLabel MText(String text, int y)
    {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Meiryo", Font.PLAIN, 20));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setBounds(0, y, wid, 50);
        return label;
    }

    private JButton MButton(int id, String text, int x, int y)
    {
        JButton button = new JButton(text);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Meiryo", Font.BOLD, 18));
        button.setBackground(new Color(140, 140, 140));
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setBounds(x - 75, y, 150, 50);
        button.addActionListener(e -> setting(id));
        return button;
    }

    private void setting(int id)
    {
        new Sound("/click.wav");
        if (id >= 1 && id <= 6) {
            getConfig().setTextPosition(id);
            setTextPosition();
        }
    }
}
