package be.od;

import java.time.Instant;

public class TextPrinter extends Thread {

    private final TileHandler handler;

    private final String text;

    private final int x;
    private final int y;

    private Instant showTime;
    private final int timer;

    public TextPrinter(String text, int x, int y, double timing, double timer, TileHandler handler) {
        this.handler = handler;

        this.text = text;

        this.x = x;
        this.y = y;

        this.timer = (int) (timer * 1000);
        setShowTime(timing);
    }

    private void setShowTime(double timing) {
        this.showTime = Instant.now().plusMillis((long) (timing * 1000));
    }

    @Override
    public void run() {
        while (showTime.isAfter(Instant.now())) { }

        handler.printText(text, x, y, timer);

        try {
            join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
