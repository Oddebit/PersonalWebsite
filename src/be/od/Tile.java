package be.od;

import java.awt.*;
import java.time.Instant;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Tile {

    private final Random random = new Random();

    private final int x;
    private final int y;

    private final Color initialColor;
    private Color currentColor;
    private Color wantedColor;

    private char currentChar;
    private char wantedChar;

    private boolean rndChar;
    private boolean rndColor;

    public Tile(int x, int y, Color color) {
        this.x = x;
        this.y = y;

        this.initialColor = color;
        this.currentColor = color;
        this.wantedColor = color;

        this.wantedChar = (char) 1;

        this.rndChar = true;
        this.rndColor = true;
    }

    public void tick() {
        if (rndChar) {
            tickChar();
        } else {
            this.currentChar = wantedChar;
        }

        if (rndColor) {
            this.currentColor = initialColor;
        } else {
            this.currentColor = wantedColor;
        }
    }

    private void tickShade() {
        int incr = random.nextBoolean() ? 1 : -1;
        int shade = App.clamp(currentColor.getGreen() + incr, 0, 255);
        currentColor = new Color(0, shade, 0);

    }

    private void tickChar() {
        for (int i = 0; i < 6; i++) {
            if (random.nextBoolean()) return;
        }
        currentChar = (char) (33 + random.nextInt(89));
    }

    public void render(Graphics graphics) {
        graphics.setColor(currentColor);
        int side = TileHandler.getTileSide();
        graphics.drawString(String.valueOf(currentChar), x, y + side);
//        graphics.fillRect(x, y, TileHandler.getTileSide(), TileHandler.getTileSide());
    }

    public void set(char wantedChar, Color color) {
        this.wantedColor = color;
        this.wantedChar = wantedChar;

        rndChar = false;
        rndColor = false;
    }


    public void set(char wantedChar, Color color, int timer) {
        this.wantedColor = color;
        this.wantedChar = wantedChar;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                rndChar = false;
                rndColor = false;
            }
        };
        new Timer().schedule(task, timer);
    }

    public void setColor(Color color, int timer) {
        this.wantedColor = color;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                rndColor = false;
            }
        };
        new Timer().schedule(task, timer);
    }

    public void setColor(Color color) {
        setColor(color, 0);
    }

    public void setChar(char wantedChar, int timer) {
        this.wantedChar = wantedChar;
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                rndChar = false;
            }
        };
        new Timer().schedule(task, timer);
    }

    public void reset(int timer) {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                rndChar = true;
                rndColor = true;
            }
        };
        new Timer().schedule(task, random.nextInt(timer));
    }

    public void reset() {
        rndChar = true;
        rndColor = true;
    }
}
