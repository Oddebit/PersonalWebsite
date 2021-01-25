package be.od;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Stream;

public class App extends Canvas implements Runnable {

    private Thread thread;
    private TileHandler handler;
    private boolean running;

    public static void main(String[] args) {
        new App();
    }

    private App() {
        new Window(1366, 768, "Audric \"OD\" Onockx", this);
        this.handler = new TileHandler(this);
        start();
        printText();
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    private synchronized void stop() {
        try {
            thread.join();
            running = false;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60;
        double tickTime = 1_000_000_000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        while (running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / tickTime;
            lastTime = now;

            while (delta >= 1) {
                tick();
                delta--;
            }

            if (running) {
                render();
            }
            frames++;

            if (System.currentTimeMillis() - timer > 1_000) {
                timer += 1_000;
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
        stop();
    }

    private void tick() {
        handler.tick();
    }

    private void render() {
        BufferStrategy bufferStrategy = this.getBufferStrategy();
        if (bufferStrategy == null) {
            this.createBufferStrategy(3);
            return;
        }
        Graphics graphics = bufferStrategy.getDrawGraphics();

        graphics.setColor(Color.black);
        graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
        graphics.setFont(new Font(Font.MONOSPACED, Font.PLAIN, TileHandler.getTileSide()));
        handler.render(graphics);

        graphics.dispose();
        bufferStrategy.show();
    }

    public void printText() {
        handler.printText("Bienvenue dans la matrice...", 60, 57, 5, 8);
    }

    public static int clamp(int value, int min, int max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }
}
