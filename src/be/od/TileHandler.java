package be.od;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.Instant;
import java.util.Random;

public class TileHandler {

    private static final int tileSide = 12;
    private final int tilesOnWidth;
    private final int tilesOnHeight;

    private final Tile[][] tiles;
    private Instant lastTime;
    private final int updateFreq = 100;

    public TileHandler(App app) {
        tilesOnWidth = app.getWidth()/tileSide;
        tilesOnHeight = app.getHeight()/tileSide;

        tiles = new Tile[tilesOnWidth][tilesOnHeight];

        BufferedImage img = ThemePixer.getPixTheme(app);
        for (int x = 0; x < tilesOnWidth; x++) {
            for (int y = 0; y < tilesOnHeight; y++) {

                try {
                    Color color = new Color(img.getRGB(x * tileSide, y * tileSide));
                    tiles[x][y] = new Tile(x * tileSide, y * tileSide, color);
                } catch (IndexOutOfBoundsException e) {
                    tiles[x][y] = new Tile(x * tileSide, y * tileSide, Color.BLACK);
                }
            }
        }
        lastTime = Instant.now();
    }

    public void tick() {
        if (lastTime.plusMillis(updateFreq).isAfter(Instant.now())) return;

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

    public static int getTileSide() {
        return tileSide;
    }


}
