package be.od;

import java.awt.*;
import java.time.Instant;
import java.util.Random;

public class TileHandler {

    private final int tileSide = 24;
    private final int tilesOnWidth;
    private final int tilesOnHeight;

    private final Tile[][] tiles;
    private Instant lastTime;

    public TileHandler(App app) {
        tilesOnWidth = 2 * app.getWidth()/tileSide;
        tilesOnHeight = 2 * app.getHeight()/tileSide;

        tiles = new Tile[tilesOnWidth][tilesOnHeight];

        for (int x = 0; x < tilesOnWidth; x++) {
            for (int y = 0; y < tilesOnHeight; y++) {
                tiles[x][y] = new Tile(x * tileSide, y * tileSide);
            }
        }

        lastTime = Instant.now();
    }

    public void tick() {
        if (lastTime.plusMillis(100).isAfter(Instant.now())) return;

        lastTime = Instant.now();
        for (int x = 0; x < tilesOnWidth; x++) {
            for (int y = 0; y < tilesOnHeight; y++) {
                tiles[x][y].tick();
            }
        }
    }

    public void render(Graphics graphics) {
        for (int x = 0; x < tilesOnWidth; x++) {
            for (int y = 0; y < tilesOnHeight; y++) {
                tiles[x][y].render(graphics);
            }
        }
    }

    public void printText(String text, int x, int y, int timing, int timer) {
        Thread textPrinter = new TextPrinter(text, x, y, timing, timer, this);
        textPrinter.start();
    }

    public void printText(String text, int x, int y, int timer) {
        Random random = new Random();
        char[] textArray = text.toCharArray();
        for (int i = 0; i < text.length(); i++) {
            tiles[x + i][y].setWantedChar(textArray[i], random.nextInt(timer));
        }
    }

    public int getTileSide() {
        return tileSide;
    }


}
