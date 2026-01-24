package io.github.josemanuel;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public final class GameApp {
    public void start() {
        JFrame frame = new JFrame("Space Shooter (Retro Green)");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        GamePanel panel = new GamePanel();
        frame.setContentPane(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        panel.start();
    }

    public static void launch() {
        SwingUtilities.invokeLater(() -> new GameApp().start());
    }
}
