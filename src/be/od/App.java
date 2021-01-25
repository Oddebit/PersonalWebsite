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
        new Window(1366, 800, "Audric \"OD\" Onockx", this);
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
        TextPrinter text1 = new TextPrinter("Bienvenue dans la matrice...", 60, 57, Color.WHITE, handler);
        text1.rndPrint(5, 6);
        text1.rndDelete(15, 2);

        TextPrinter text2 = new TextPrinter("Mon nom est ", 60, 57, Color.WHITE, handler);
        text2.rndPrint(19, 4);
        text2.rndDelete(30, 2);

        TextPrinter audric = new TextPrinter("Audric", 72, 57, Color.RED, handler);
        audric.print(23);
        audric.delete(42);
        audric.flash(32, 6, 1.5, Color.BLACK, Color.RED);

        TextPrinter text3 = new TextPrinter("et ceci est mon univers.", 79, 57, Color.WHITE, handler);
        text3.rndPrint(24, 2);
        text3.rndDelete(32, 2);
    }

    public static int clamp(int value, int min, int max) {
        if (value < min) return min;
        if (value > max) return max;
        return value;
    }
}
