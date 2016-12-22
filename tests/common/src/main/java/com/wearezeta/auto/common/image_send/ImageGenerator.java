package com.wearezeta.auto.common.image_send;

import javax.imageio.ImageIO;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ImageGenerator {

    public static BufferedImage generateJellyBeanImage(String text) throws IOException {
        return generate(text, ImageStyle.JELLYBEANS);
    }

    public static BufferedImage generateDarkulaImage(String text) throws IOException {
        return generate(text, ImageStyle.DARKULA);
    }

    public static BufferedImage generate(String text, ImageStyle style) throws IOException {
        int w = 720, h = 1280;

        BufferedImage bufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = bufferedImage.createGraphics();
        drawBackground(graphics2D, text, w, h, style);
        Font font = new Font("TimesRoman", Font.BOLD, 120);
        graphics2D.setPaint(Color.WHITE);
        graphics2D.setFont(font);
        drawTextCenter(graphics2D, text, w, h / 2);

        drawFooter(w, h, graphics2D);
        return bufferedImage;
    }

    public static File getTestPictureFile(String text) throws IOException {
        BufferedImage image = generateJellyBeanImage(text);
        File file = new File("tmp.png");
        ImageIO.write(image, "PNG", file);
        return file;
    }

    private static void drawBackground(Graphics2D graphics2D, String text, int w, int h, ImageStyle style) {
        graphics2D.setPaint(Color.DARK_GRAY);
        graphics2D.fillRect(0, 0, w, h);
        try {
            //md5 is always 32 bytes length
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            String sizes = new HexBinaryAdapter().marshal(md5.digest(text.getBytes()));
            String xPoints = new HexBinaryAdapter().marshal(md5.digest((text + text).getBytes()));
            String yPoints = new HexBinaryAdapter().marshal(md5.digest((text + text + text).getBytes()));
            for (int i = 0; i < sizes.length(); i++) {
                int size = (Integer.parseInt(((Character) sizes.charAt(i)).toString(), 16) + 1) * style.size;
                int x = (2 * w) / 16 * (Integer.parseInt(((Character) xPoints.charAt(i)).toString(), 16));
                int y = (2 * h) / 16 * (Integer.parseInt(((Character) yPoints.charAt(i)).toString(), 16));

                graphics2D.setColor(getIterationColor(i, style));
                graphics2D.setStroke(new BasicStroke(style.stroke));
                graphics2D.drawOval(x - h, y - h, size, size);
            }


        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static Color getIterationColor(int i, ImageStyle style) {
        int maxColour = 0xffffff;
        int ithColor = maxColour / 38;
        int intColour = ithColor * (i + 1);
        @SuppressWarnings("NumericOverflow")
        int alpha = ((int) (0xff * style.opacity)) << 24;

        return new Color(intColour + alpha, true);
    }

    private static void drawFooter(int w, int h, Graphics2D graphics2D) {
        Font oldFont = graphics2D.getFont();
        Font font;
        int lineHeight;

        font = new Font("TimesRoman", Font.BOLD | Font.ITALIC, 100);
        graphics2D.setFont(font);
        lineHeight = getLineHeight(graphics2D);
        drawTextCenter(graphics2D, "Wire", w, h - 3 * lineHeight);
        font = new Font("TimesRoman", Font.ITALIC, 70);
        graphics2D.setFont(font);
        lineHeight = getLineHeight(graphics2D);
        drawTextCenter(graphics2D, "testing image", w, h - 2 * lineHeight);
        graphics2D.setFont(oldFont);
    }

    private static void drawTextCenter(Graphics2D graphics2D, String text, int w, int y) {
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        int stringWidth = fontMetrics.stringWidth(text);
        int stringHeight = fontMetrics.getAscent();
        graphics2D.drawString(text, (w - stringWidth) / 2, y + stringHeight / 4);
    }

    private static int getLineHeight(Graphics2D graphics2D) {
        FontMetrics fontMetrics = graphics2D.getFontMetrics();
        return fontMetrics.getAscent();
    }

    enum ImageStyle {
        DARKULA(new ImageProfile(160, 8, 50)),
        JELLYBEANS(new ImageProfile(360, 300, 40));

        private int size;
        private int stroke;
        private float opacity;

        ImageStyle(ImageProfile imageProfile) {
            this.size = imageProfile.size;
            this.stroke = imageProfile.stroke;
            this.opacity = (float) imageProfile.opacity / 100;
        }
    }

    private static class ImageProfile {
        int size;
        int stroke;
        int opacity;

        public ImageProfile(int size, int stroke, int opacity) {
            this.size = size;
            this.stroke = stroke;
            this.opacity = opacity;
        }
    }


}
