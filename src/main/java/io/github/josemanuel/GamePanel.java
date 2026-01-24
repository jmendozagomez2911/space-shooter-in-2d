package io.github.josemanuel;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public final class GamePanel extends JPanel implements KeyListener, Runnable {
    private static final int INTERNAL_W = 320;
    private static final int INTERNAL_H = 180;
    private static final int SCALE = 4;

    private static final Color GREEN = new Color(0, 255, 64);
    private static final Color GREEN_DIM = new Color(0, 140, 32);
    private static final Color BG = new Color(0, 8, 0);

    private final BufferedImage backBuffer = new BufferedImage(INTERNAL_W, INTERNAL_H, BufferedImage.TYPE_INT_RGB);

    private final boolean[] keys = new boolean[256];

    private Thread loopThread;
    private volatile boolean running;

    private long lastShotNs;
    private long nextEnemySpawnNs;

    private int score;
    private int lives;
    private boolean gameOver;

    private final Player player = new Player();
    private final List<Bullet> bullets = new ArrayList<>();
    private final List<Enemy> enemies = new ArrayList<>();

    private float crtPhase;

    public GamePanel() {
        setPreferredSize(new Dimension(INTERNAL_W * SCALE, INTERNAL_H * SCALE));
        setFocusable(true);
        addKeyListener(this);
        resetGame();
    }

    public void start() {
        if (running) {
            return;
        }
        running = true;
        loopThread = new Thread(this, "game-loop");
        loopThread.setDaemon(true);
        loopThread.start();
        requestFocusInWindow();
    }

    @Override
    public void run() {
        final double dt = 1.0 / 60.0;
        long previous = System.nanoTime();
        double accumulator = 0.0;

        while (running) {
            long now = System.nanoTime();
            long frameNs = now - previous;
            previous = now;
            accumulator += frameNs / 1_000_000_000.0;

            while (accumulator >= dt) {
                update(dt, now);
                accumulator -= dt;
            }

            repaint();
            Toolkit.getDefaultToolkit().sync();

            try {
                Thread.sleep(2);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void resetGame() {
        score = 0;
        lives = 3;
        gameOver = false;

        bullets.clear();
        enemies.clear();

        player.x = INTERNAL_W / 2.0;
        player.y = INTERNAL_H - 24;
        player.vx = 0;

        long now = System.nanoTime();
        lastShotNs = now;
        nextEnemySpawnNs = now + 600_000_000L;
    }

    private void update(double dt, long nowNs) {
        crtPhase += (float) dt * 1.2f;

        if (gameOver) {
            if (isPressed(KeyEvent.VK_ENTER)) {
                resetGame();
            }
            return;
        }

        double ax = 0.0;
        if (isPressed(KeyEvent.VK_LEFT) || isPressed(KeyEvent.VK_A)) {
            ax -= 1.0;
        }
        if (isPressed(KeyEvent.VK_RIGHT) || isPressed(KeyEvent.VK_D)) {
            ax += 1.0;
        }

        player.vx = ax * 140.0;
        player.x += player.vx * dt;
        player.x = clamp(player.x, 10, INTERNAL_W - 10);

        boolean wantsShot = isPressed(KeyEvent.VK_SPACE) || isPressed(KeyEvent.VK_Z);
        if (wantsShot && nowNs - lastShotNs >= 140_000_000L) {
            bullets.add(new Bullet(player.x, player.y - 8));
            lastShotNs = nowNs;
        }

        if (nowNs >= nextEnemySpawnNs) {
            spawnEnemy();
            long base = 520_000_000L;
            long min = 180_000_000L;
            long ramp = Math.min(score / 20, 10);
            long interval = Math.max(min, base - ramp * 30_000_000L);
            nextEnemySpawnNs = nowNs + interval;
        }

        for (Bullet b : bullets) {
            b.y -= b.speed * dt;
        }
        bullets.removeIf(b -> b.y < -10);

        for (Enemy e : enemies) {
            e.y += e.speed * dt;
            e.x += Math.sin((e.phase += dt * 2.2) * 1.3) * dt * 24.0;
        }

        Iterator<Enemy> enemyIt = enemies.iterator();
        while (enemyIt.hasNext()) {
            Enemy e = enemyIt.next();
            if (e.y > INTERNAL_H + 12) {
                enemyIt.remove();
                lives--;
                if (lives <= 0) {
                    gameOver = true;
                }
            }
        }

        handleCollisions();
    }

    private void handleCollisions() {
        Iterator<Bullet> bIt = bullets.iterator();
        while (bIt.hasNext()) {
            Bullet b = bIt.next();
            Iterator<Enemy> eIt = enemies.iterator();
            boolean hit = false;
            while (eIt.hasNext()) {
                Enemy e = eIt.next();
                if (Math.abs(b.x - e.x) <= 6 && Math.abs(b.y - e.y) <= 6) {
                    eIt.remove();
                    hit = true;
                    score += 10;
                    break;
                }
            }
            if (hit) {
                bIt.remove();
            }
        }

        for (Enemy e : enemies) {
            if (Math.abs(player.x - e.x) <= 10 && Math.abs(player.y - e.y) <= 10) {
                lives = 0;
                gameOver = true;
                break;
            }
        }
    }

    private void spawnEnemy() {
        double x = ThreadLocalRandom.current().nextDouble(12, INTERNAL_W - 12);
        double y = -10;
        double speed = ThreadLocalRandom.current().nextDouble(26, 60);
        enemies.add(new Enemy(x, y, speed));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D bg = backBuffer.createGraphics();
        try {
            bg.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            bg.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);

            bg.setColor(BG);
            bg.fillRect(0, 0, INTERNAL_W, INTERNAL_H);

            renderStars(bg);
            renderEntities(bg);
            renderHud(bg);
            renderCrtOverlay(bg);
        } finally {
            bg.dispose();
        }

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
            g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);

            AffineTransform at = AffineTransform.getScaleInstance(SCALE, SCALE);
            g2.drawImage(backBuffer, at, null);
        } finally {
            g2.dispose();
        }
    }

    private void renderStars(Graphics2D g) {
        g.setColor(GREEN_DIM);
        int seed = (int) (crtPhase * 60);
        for (int i = 0; i < 70; i++) {
            int x = (i * 37 + seed * 13) % INTERNAL_W;
            int y = (i * 61 + seed * 23) % INTERNAL_H;
            if ((i + seed) % 9 == 0) {
                g.fillRect(x, y, 2, 2);
            } else {
                g.fillRect(x, y, 1, 1);
            }
        }
    }

    private void renderEntities(Graphics2D g) {
        g.setColor(GREEN);

        int px = (int) Math.round(player.x);
        int py = (int) Math.round(player.y);
        g.drawLine(px, py - 8, px - 7, py + 7);
        g.drawLine(px, py - 8, px + 7, py + 7);
        g.drawLine(px - 7, py + 7, px + 7, py + 7);
        g.drawLine(px, py - 2, px, py + 8);

        for (Bullet b : bullets) {
            int x = (int) Math.round(b.x);
            int y = (int) Math.round(b.y);
            g.drawLine(x, y - 3, x, y + 3);
        }

        for (Enemy e : enemies) {
            int x = (int) Math.round(e.x);
            int y = (int) Math.round(e.y);
            g.drawRect(x - 6, y - 6, 12, 12);
            g.drawLine(x - 6, y, x + 6, y);
            g.drawLine(x, y - 6, x, y + 6);
        }
    }

    private void renderHud(Graphics2D g) {
        g.setColor(GREEN);
        g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 10));
        g.drawString("SCORE " + score, 8, 14);
        g.drawString("LIVES " + lives, 8, 28);

        if (gameOver) {
            String msg = "GAME OVER";
            String msg2 = "PRESS ENTER";
            int w1 = g.getFontMetrics().stringWidth(msg);
            int w2 = g.getFontMetrics().stringWidth(msg2);
            g.drawString(msg, (INTERNAL_W - w1) / 2, INTERNAL_H / 2 - 4);
            g.drawString(msg2, (INTERNAL_W - w2) / 2, INTERNAL_H / 2 + 10);
        } else {
            g.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 9));
            g.drawString("ARROWS/A,D MOVE  SPACE SHOOT", 8, INTERNAL_H - 8);
        }
    }

    private void renderCrtOverlay(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 70));
        for (int y = 0; y < INTERNAL_H; y += 2) {
            g.drawLine(0, y, INTERNAL_W, y);
        }

        g.setColor(new Color(0, 255, 64, 18));
        int bandY = (int) ((Math.sin(crtPhase) * 0.5 + 0.5) * (INTERNAL_H - 1));
        g.drawLine(0, bandY, INTERNAL_W, bandY);
        g.drawLine(0, Math.min(INTERNAL_H - 1, bandY + 1), INTERNAL_W, Math.min(INTERNAL_H - 1, bandY + 1));

        g.setColor(new Color(0, 0, 0, 90));
        g.drawRect(0, 0, INTERNAL_W - 1, INTERNAL_H - 1);
    }

    private boolean isPressed(int keyCode) {
        if (keyCode < 0 || keyCode >= keys.length) {
            return false;
        }
        return keys[keyCode];
    }

    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code >= 0 && code < keys.length) {
            keys[code] = true;
        }

        if (code == KeyEvent.VK_ESCAPE) {
            running = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code >= 0 && code < keys.length) {
            keys[code] = false;
        }
    }

    private static final class Player {
        double x;
        double y;
        double vx;
    }

    private static final class Bullet {
        final double x;
        double y;
        final double speed = 220.0;

        Bullet(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private static final class Enemy {
        double x;
        double y;
        final double speed;
        double phase;

        Enemy(double x, double y, double speed) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.phase = ThreadLocalRandom.current().nextDouble(0, Math.PI * 2);
        }
    }
}
