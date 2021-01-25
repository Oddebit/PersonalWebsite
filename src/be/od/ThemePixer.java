package be.od;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ThemePixer {

    private static final File theme = new File("res/theme.jpg");

    public static BufferedImage getPixTheme(Canvas canvas) {
        int pixel = TileHandler.getTileSide();
        BufferedImage img;

        try {
            img = ImageIO.read(theme);
            BufferedImage pixImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);

            for (int i = 0; i < img.getWidth(); i += pixel) {
                for (int j = 0; j < img.getHeight(); j += pixel) {

                    int shade = 0;

                    int width = Math.min(img.getWidth() - i, pixel);
                    int height = Math.min(img.getHeight() - j, pixel);

                    for (int k = 0; k < width; k++) {
                        for (int l = 0; l < height; l++) {
                            Color color = new Color(img.getRGB(i + k, j + l));
                            shade += color.getRed() + color.getGreen() + color.getBlue();
                        }
                    }

                    shade /= 5 * width * height;
                    shade = light(shade, 1.2);
                    shade = contrast(shade, 0);
                    Color grayColor = new Color(0, shade, 0);

                    for (int k = 0; k < width; k++) {
                        for (int l = 0; l < height; l++) {
                            pixImg.setRGB(i + k, j + l, grayColor.getRGB());
                        }
                    }
                }
            }

            BufferedImage scaledImg = new BufferedImage(canvas.getWidth(), canvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
            AffineTransform scaleInstance = AffineTransform.getScaleInstance((double)canvas.getWidth()/img.getWidth(), (double)canvas.getHeight()/img.getHeight());
            AffineTransformOp scaleOp = new AffineTransformOp(scaleInstance, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
            scaleOp.filter(pixImg, scaledImg);
            return scaledImg;

        } catch (IOException e) {
        }
        return null;
    }

    public static int contrast(int shade, int contrast) {

        if(shade < 255/2d) {
            double sqrtDelta = Math.sqrt(Math.pow(255, 2) - 4 * Math.pow(shade, 2));
            return (255 - sqrtDelta)/2 <= 255/2d ? (int)((255 - sqrtDelta)/2) : (int)((255 + sqrtDelta)/2);
        } else {
            double sqrtDelta = Math.sqrt(Math.pow(255, 2) - 4 * Math.pow(255 - shade, 2));
            return (255 - sqrtDelta) / 2 >= 255 / 2d ? (int) ((255 - sqrtDelta) / 2) : (int) ((255 + sqrtDelta) / 2);
        }
    }

    public static int light(int shade, double light) {
        return (int)(255 - (double)(255 - shade) / light);
    }
}
