package org.akua25258;

import com.sun.jna.Native;
import com.sun.jna.win32.StdCallLibrary;
import oshi.SystemInfo;
import oshi.hardware.PowerSource;

import java.net.URI;
import java.awt.Desktop;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Main
{
    private static final SystemInfo systemInfo = new SystemInfo();

    private static final Config Config = new Config();
    private static final Gui Gui = new Gui();

    public static final JWindow window = new JWindow();
    private static final JLabel label = new JLabel("Loading...");
    public static Timer timer;

    private static final int wid = 300;
    private static final int hei = 18;

    private static int time = 0;

    public interface KeyMonitorUser32 extends StdCallLibrary
    {
        KeyMonitorUser32 INSTANCE = Native.load("user32", KeyMonitorUser32.class);
        short GetAsyncKeyState(int vKey);
    }

    public static Config getConfig() { return Config; }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> {
            window.setSize(wid, hei);
            setTextPosition();
            window.setBackground(new Color(0, 0, 0, 0));

            JPanel panel = new JPanel(new BorderLayout());
            panel.setOpaque(false);

            label.setForeground(Color.DARK_GRAY);
            label.setFont(new Font("Meiryo", Font.PLAIN, 18));
            label.setVerticalAlignment(SwingConstants.CENTER);
            panel.add(label, BorderLayout.CENTER);

            window.add(panel);
            window.setVisible(true);
            window.setAlwaysOnTop(true);

            setupSystemTray();

            timer = new Timer(1000, e -> {
                if (getConfig().isTimerRunning()) time++;
                if (!window.isVisible()) return;

                refresh();
            });
            timer.setInitialDelay(0);
            timer.start();
            startKeyMonitor(timer);
        });
        // Gui.open();
        // デフォルトでメニューを開くとかの設定も実装したいな
    }

    public static void setTextPosition()
    {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Rectangle bounds = ge.getMaximumWindowBounds();
        final int pos = Config.getTextPosition();
        int x = 0;
        int y = 0;
        if (pos == 1 || pos == 4) label.setHorizontalAlignment(SwingConstants.LEFT);
        else if (pos == 2 || pos == 5) { x = (bounds.width - wid) / 2; label.setHorizontalAlignment(SwingConstants.CENTER); }
        else if (pos == 3 || pos == 6) { x = bounds.width - wid; label.setHorizontalAlignment(SwingConstants.RIGHT); }
        if (pos >= 4 && pos <= 6) y = bounds.y + bounds.height - hei;
        window.setLocation(x, y);
    }

    public static void refresh()
    {
        if (isCharging()) {
            if (getBattery() >= 70) label.setForeground(Color.YELLOW);
            else label.setForeground(Color.GREEN);
        } else if (getBattery() >= 20) label.setForeground(Color.WHITE);
        else label.setForeground(Color.RED);

        label.setText(getText());
        window.repaint();
    }

    public static short getBattery()
    {
        double totalCurrent = 0;
        double totalMax = 0;
        for (PowerSource ps : systemInfo.getHardware().getPowerSources())
        {
            ps.updateAttributes();
            totalCurrent += ps.getCurrentCapacity();
            totalMax += ps.getMaxCapacity();
        }
        return (short) ((totalMax > 0) ? (totalCurrent / totalMax) * 100.0 : 0);
    }

    public static boolean isCharging()
    {
        for (PowerSource ps : systemInfo.getHardware().getPowerSources())
        {
            ps.updateAttributes();
            if (ps.isCharging()) return true;
        }
        return false;
    }

    public static void resetTIme() { time = 0; }

    public static String getTime()
    {
        if (!getConfig().isTimerRunning()) return "";
        int m = time / 60;
        int s = time % 60;
        return " | " + m + ":" + (s < 10 ? "0" + s : s);
    }

    public static String getFps()
    {
        if (!getConfig().isOnFps()) return "";
        return " | " + Fps.getFps() + " FPS";
    }

    public static String getText() { return getBattery() + "%" + getTime() + getFps(); }

    private static void openWebpage()
    {
        try { if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) Desktop.getDesktop().browse(new URI("https://github.com/akua25258/WinlessUI")); }
        catch (Exception e) { e.printStackTrace(); }
    }

    private static void setupSystemTray()
    {
        if (!SystemTray.isSupported()) return;
        SystemTray tray = SystemTray.getSystemTray();

        Image image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.GREEN);
        g2.fillOval(2, 2, 12, 12);
        g2.dispose();

        PopupMenu popup = new PopupMenu();

        MenuItem toggleItem = new MenuItem("表示の切り替え");
        toggleItem.addActionListener(e -> window.setVisible(!window.isVisible()));
        popup.add(toggleItem);

        popup.addSeparator();

        MenuItem guiItem = new MenuItem("設定画面を開く");
        guiItem.addActionListener(e -> Gui.open() );
        popup.add(guiItem);

        popup.addSeparator();

        MenuItem webItem = new MenuItem("GitHubページを開く");
        webItem.addActionListener(e -> openWebpage());
        popup.add(webItem);

        popup.addSeparator();

        MenuItem exitItem = new MenuItem("システムを終了");
        exitItem.addActionListener(e -> System.exit(0));
        popup.add(exitItem);

        TrayIcon trayIcon = new TrayIcon(image, "WinlessUI", popup);
        trayIcon.setImageAutoSize(true);

        try { tray.add(trayIcon); }
        catch (AWTException e) { e.printStackTrace(); }
    }

    private static void startKeyMonitor(Timer timer) {
        Thread monitorThread = new Thread(() -> {
            final int SHIFT_KEY = 0xA1;
            boolean wasPressed = false;

            while (true)
            {
                try {
                    Thread.sleep(50);

                    if (!Config.isTimerRunning())
                    {
                        wasPressed = false;
                        continue;
                    }

                    boolean isPressed = (KeyMonitorUser32.INSTANCE.GetAsyncKeyState(SHIFT_KEY) & 0x8000) != 0;
                    if (isPressed && !wasPressed)
                    {
                        SwingUtilities.invokeLater(() -> {
                            time = 0;
                            refresh();
                            timer.setInitialDelay(1000);
                            timer.restart();
                        });
                        new Sound("/timer.wav");

                    }
                    wasPressed = isPressed;
                } catch (InterruptedException e) { break; }
            }
        });
        monitorThread.setDaemon(true);
        monitorThread.start();
    }
}