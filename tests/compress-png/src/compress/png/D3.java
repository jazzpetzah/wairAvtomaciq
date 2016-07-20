package compress.png;

public class D3 {
    public final int r;
    public final int g;
    public final int b;
    public final int rgb;

    public D3(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.rgb = r << 16 | g << 8 | b;
    }

    private static int diff(int r, int g, int b, D3 pal) {
        int Rdiff = r - pal.r;
        int Gdiff = g - pal.g;
        int Bdiff = b - pal.b;
        return Rdiff * Rdiff + Gdiff * Gdiff + Bdiff * Bdiff;
    }

    public static int[] findClosest(int argb, D3[] palette) {
        D3 closest = palette[0];
        int r = argb >> 16 & 255;
        int g = argb >> 8 & 255;
        int b = argb & 255;
        int cDiff = diff(r, g, b, closest);
        int found = 0;

        for (int i = 1; i < 256; ++i) {
            D3 n = palette[i];
            int nDiff = diff(r, g, b, n);
            if (nDiff < cDiff) {
                closest = n;
                found = i;
                cDiff = nDiff;
            }
        }
        return new int[]{closest.rgb, found};
    }

    public static byte[] process(byte[] colorPalette, int[][] image, int h, int w) {
        int p = 0;
        D3[] palette = new D3[256];

        int y;
        int x;
        int argb;
        for (int indexedPixels = 0; indexedPixels < 256; ++indexedPixels) {
            y = colorPalette[p++] & 255;
            x = colorPalette[p++] & 255;
            argb = colorPalette[p++] & 255;
            palette[indexedPixels] = new D3(y, x, argb);
        }

        byte[] var22 = new byte[h * w];
        p = 0;

        for (y = 0; y < h; ++y) {
            for (x = 0; x < w; ++x) {
                argb = image[y][x];
                int[] obj = findClosest(argb, palette);
                int nextArgb = obj[0];
                var22[p++] = (byte) obj[1];
                int r = argb >> 16 & 255;
                int g = argb >> 8 & 255;
                int b = argb & 255;
                int nr = nextArgb >> 16 & 255;
                int ng = nextArgb >> 8 & 255;
                int nb = nextArgb & 255;
                int errR = r - nr;
                int errG = g - ng;
                int errB = b - nb;
                int update;
                if (x + 1 < w) {
                    update = applyFloyd(image[y][x + 1], errR, errG, errB, 7);
                    image[y][x + 1] = update;
                    if (y + 1 < h) {
                        update = applyFloyd(image[y + 1][x + 1], errR, errG, errB, 1);
                        image[y + 1][x + 1] = update;
                    }
                }

                if (y + 1 < h) {
                    update = applyFloyd(image[y + 1][x], errR, errG, errB, 5);
                    image[y + 1][x] = update;
                    if (x - 1 >= 0) {
                        update = applyFloyd(image[y + 1][x - 1], errR, errG, errB, 3);
                        image[y + 1][x - 1] = update;
                    }
                }
            }
        }
        return var22;
    }

    private static int applyFloyd(int argb, int errR, int errG, int errB, int mul) {
        int r = argb >> 16 & 255;
        int g = argb >> 8 & 255;
        int b = argb & 255;
        r += errR * mul / 16;
        g += errG * mul / 16;
        b += errB * mul / 16;
        if (r < 0) {
            r = 0;
        } else if (r > 255) {
            r = 255;
        }

        if (g < 0) {
            g = 0;
        } else if (g > 255) {
            g = 255;
        }

        if (b < 0) {
            b = 0;
        } else if (b > 255) {
            b = 255;
        }
        return r << 16 | g << 8 | b;
    }
}