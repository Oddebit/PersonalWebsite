package be.od;

import java.awt.*;
import java.time.Instant;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;

public class TextPrinter {

    private final TileHandler handler;

    private final String text;
    private final Color color;

    private final int x;
    private final int y;


    public TextPrinter(String text, int x, int y, Color color, TileHandler handler) {
        this.handler = handler;
        this.text = text;
        this.color = color;
        this.x = x;
        this.y = y;
    }

    public void print(double timing) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                char[] textArray = text.toCharArray();
                for (int i = 0; i < text.length(); i++) {
                    handler.getTiles()[x + i][y].set(textArray[i], color);
                }
            }
        };
        new Timer().schedule(task, (long) (timing * 1000));
    }

    public void rndPrint(double timing, double timer) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Random random = new Random();
                char[] textArray = text.toCharArray();
                for (int i = 0; i < text.length(); i++) {
                    handler.getTiles()[x + i][y].set(textArray[i], color, random.nextInt((int) (timer * 1000)));
                }
            }
        };
        new Timer().schedule(task, (long) (timing * 1000));
    }

    public void delete(double timing) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < text.length(); i++) {
                    handler.getTiles()[x + i][y].reset();
                }
            }
        };
        new Timer().schedule(task, (long) (timing * 1000));
    }

    public void rndDelete(double timing, double timer) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Random random = new Random();
                for (int i = 0; i < text.length(); i++) {
                    handler.getTiles()[x + i][y].reset(random.nextInt((int) (timer * 1000)));
                }
            }
        };
        new Timer().schedule(task, (long) (timing * 1000));
    }

    public void flash(double timing, double timer, double frequency, Color...colors) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Instant stop = Instant.now().plusMillis((long) ((timing + timer) * 1000));
                Instant lastTime = Instant.now();
                AtomicInteger color = new AtomicInteger(0);
                while (stop.isAfter(Instant.now())) {

                    if(lastTime.isBefore(Instant.now().plusMillis((long) (frequency * 1000)))) {
                        for (int i = 0; i < text.length(); i++) {
                            handler.getTiles()[x + i][y].setColor(colors[color.get()]);
                        }
                        color.getAndIncrement();
                        if (color.get() >= colors.length) color.set(0);
                        lastTime = Instant.now();
                    }
                }
                for (int i = 0; i < text.length(); i++) {
                    handler.getTiles()[x + i][y].reset(100);
                }
            }
        };
        new Timer().schedule(task, (long) (timing * 1000));
    }
}
