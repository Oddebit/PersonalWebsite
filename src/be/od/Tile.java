package be.od;

import java.awt.*;
import java.time.Instant;
import java.util.Random;

public class Tile {

    private final Random random = new Random();

    private final int x;
    private final int y;

    private Color color;
    private char currentChar;

    private char wantedChar;
    private Instant showTime;

    public Tile(int x, int y, Color color) {
        this.x = x;
        this.y = y;

        this.color = color;

        this.wantedChar = (char) 1;
        this.showTime = Instant.MAX;
    }

    public void tick() {
        if(showTime.isAfter(Instant.now())) {
            //tickShade();
            tickChar();
        } else {
            writeWantedChar();
        }
    }

    private void tickShade() {
        int incr = random.nextBoolean() ? 1 : - 1;
        int shade = App.clamp(color.getGreen() + incr, 0, 255);
        color = new Color(0, shade, 0);

    }

    private void tickChar() {
        for (int i = 0; i < 6; i++) {
            if(random.nextBoolean()) return;
        }
        currentChar = (char) (33 + random.nextInt(89));
    }

    public void render(Graphics graphics) {
        graphics.setColor(color);
        int side = TileHandler.getTileSide();
        graphics.drawString(String.valueOf(currentChar), x + side, y + side);
//        graphics.fillRect(x, y, TileHandler.getTileSide(), TileHandler.getTileSide());
    }

    public void setWantedChar(char wantedChar, int timer) {
        if (wantedChar == ' ') return;
        this.wantedChar = wantedChar;
        this.showTime = Instant.now().plusMillis(timer);
    }

    public void writeWantedChar() {
        this.currentChar = wantedChar;
        this.color = Color.WHITE;
    }
}
