package compress.png;

public class D4 {
    public final int a;
    public final int r;
    public final int g;
    public final int b;
    public final int argb;

    public D4(int a, int r, int g, int b) {
        this.a = a;
        this.r = r;
        this.g = g;
        this.b = b;
        this.argb = a << 24 | r << 16 | g << 8 | b;
    }

    private static int diff(int a, int r, int g, int b, D4 pal) {
        int Adiff = a - pal.a;
        int Rdiff = r - pal.r;
        int Gdiff = g - pal.g;
        int Bdiff = b - pal.b;
        return Adiff * Adiff + Rdiff * Rdiff + Gdiff * Gdiff + Bdiff * Bdiff;
    }

    public static int[] findClosest(int argb, D4[] palette) {
        D4 closest = palette[0];
        int a = argb >> 24 & 255;
        int r = argb >> 16 & 255;
        int g = argb >> 8 & 255;
        int b = argb & 255;
        int cDiff = diff(a, r, g, b, closest);
        int found = 0;

        for(int i = 1; i < 256; ++i) {
            D4 n = palette[i];
            int nDiff = diff(a, r, g, b, n);
            if(nDiff < cDiff) {
                closest = n;
                found = i;
                cDiff = nDiff;
            }
        }

        return new int[]{closest.argb, found};
    }

    public static byte[] process(byte[] colorPalette, byte[] trns, int[][] image, int h, int w) {
        int p = 0;
        D4[] palette = new D4[256];

        int y;
        int x;
        int argb;
        for(int indexedPixels = 0; indexedPixels < 256; ++indexedPixels) {
            y = trns[indexedPixels] & 255;
            x = colorPalette[p++] & 255;
            argb = colorPalette[p++] & 255;
            int obj = colorPalette[p++] & 255;
            palette[indexedPixels] = new D4(y, x, argb, obj);
        }

        byte[] var26 = new byte[h * w];
        p = 0;

        for(y = 0; y < h; ++y) {
            for(x = 0; x < w; ++x) {
                argb = image[y][x];
                int[] var27 = findClosest(argb, palette);
                int nextArgb = var27[0];
                var26[p++] = (byte)var27[1];
                int a = argb >> 24 & 255;
                int r = argb >> 16 & 255;
                int g = argb >> 8 & 255;
                int b = argb & 255;
                int na = nextArgb >> 24 & 255;
                int nr = nextArgb >> 16 & 255;
                int ng = nextArgb >> 8 & 255;
                int nb = nextArgb & 255;
                int errA = a - na;
                int errR = r - nr;
                int errG = g - ng;
                int errB = b - nb;
                int update;
                if(x + 1 < w) {
                    update = applyFloyd(image[y][x + 1], errA, errR, errG, errB, 7);
                    image[y][x + 1] = update;
                    if(y + 1 < h) {
                        update = applyFloyd(image[y + 1][x + 1], errA, errR, errG, errB, 1);
                        image[y + 1][x + 1] = update;
                    }
                }

                if(y + 1 < h) {
                    update = applyFloyd(image[y + 1][x], errA, errR, errG, errB, 5);
                    image[y + 1][x] = update;
                    if(x - 1 >= 0) {
                        update = applyFloyd(image[y + 1][x - 1], errA, errR, errG, errB, 3);
                        image[y + 1][x - 1] = update;
                    }
                }
            }
        }

        return var26;
    }

    private static int applyFloyd(int argb, int errA, int errR, int errG, int errB, int mul) {
        int a = argb >> 24 & 255;
        int r = argb >> 16 & 255;
        int g = argb >> 8 & 255;
        int b = argb & 255;
        a += errA * mul / 16;
        r += errR * mul / 16;
        g += errG * mul / 16;
        b += errB * mul / 16;
        if(a < 0) {
            a = 0;
        } else if(a > 255) {
            a = 255;
        }

        if(r < 0) {
            r = 0;
        } else if(r > 255) {
            r = 255;
        }

        if(g < 0) {
            g = 0;
        } else if(g > 255) {
            g = 255;
        }

        if(b < 0) {
            b = 0;
        } else if(b > 255) {
            b = 255;
        }

        return a << 24 | r << 16 | g << 8 | b;
    }
}
