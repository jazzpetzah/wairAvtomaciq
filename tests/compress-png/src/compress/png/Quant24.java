package compress.png;

public class Quant24 {
    private static final int IndexBits = 7;
    private static final int IndexBitsPlus = 8;
    private static final int DoubleIndexBits = 14;
    private static final int IndexCount = 129;
    private static final int TableLength = 2146689;
    private final long[] vwt = new long[2146689];
    private final long[] vmr = new long[2146689];
    private final long[] vmg = new long[2146689];
    private final long[] vmb = new long[2146689];
    private final double[] m2 = new double[2146689];
    private final byte[] tag = new byte[2146689];

    public Quant24() {
    }

    public byte[] getPalette(int[][] image) {
        short colorCount = 256;
        this.histogram(image);
        this.M3d();
        Quant24.Cube[] cube = new Quant24.Cube[colorCount];
        this.buildCube(cube, colorCount);
        byte[] palette = new byte[768];
        int z = 0;

        for (int k = 0; k < colorCount; ++k) {
            double weight = volume(cube[k], this.vwt);
            if (weight != 0.0D) {
                palette[z++] = (byte) ((int) (volume(cube[k], this.vmr) / weight));
                palette[z++] = (byte) ((int) (volume(cube[k], this.vmg) / weight));
                palette[z++] = (byte) ((int) (volume(cube[k], this.vmb) / weight));
            } else {
                z += 3;
            }
        }

        return palette;
    }

    public byte findMatch(int rgb) {
        byte mm = 1;
        int r = (rgb >> 16 & 255) >> mm;
        int g = (rgb >> 8 & 255) >> mm;
        int b = (rgb & 255) >> mm;
        int ind = indexify(r + 1, g + 1, b + 1);
        return this.tag[ind];
    }

    private static int indexify(int r, int g, int b) {
        return (r << 14) + (r << 8) + (g << 7) + r + g + b;
    }

    private static double volume(Quant24.Cube cube, long[] moment) {
        return (double) (moment[indexify(cube.R1, cube.G1, cube.B1)] - moment[indexify(cube.R1, cube.G1, cube.B0)] -
                moment[indexify(cube.R1, cube.G0, cube.B1)] + moment[indexify(cube.R1, cube.G0, cube.B0)] - moment[indexify
                (cube.R0, cube.G1, cube.B1)] + moment[indexify(cube.R0, cube.G1, cube.B0)] + moment[indexify(cube.R0, cube
                .G0, cube.B1)] - moment[indexify(cube.R0, cube.G0, cube.B0)]);
    }

    private static long base(Quant24.Cube cube, int direction, long[] moment) {
        switch (direction) {
            case 0:
                return -moment[indexify(cube.R1, cube.G1, cube.B0)] + moment[indexify(cube.R1, cube.G0, cube.B0)] +
                        moment[indexify(cube.R0, cube.G1, cube.B0)] - moment[indexify(cube.R0, cube.G0, cube.B0)];
            case 1:
                return -moment[indexify(cube.R1, cube.G0, cube.B1)] + moment[indexify(cube.R1, cube.G0, cube.B0)] +
                        moment[indexify(cube.R0, cube.G0, cube.B1)] - moment[indexify(cube.R0, cube.G0, cube.B0)];
            case 2:
                return -moment[indexify(cube.R0, cube.G1, cube.B1)] + moment[indexify(cube.R0, cube.G1, cube.B0)] +
                        moment[indexify(cube.R0, cube.G0, cube.B1)] - moment[indexify(cube.R0, cube.G0, cube.B0)];
            default:
                return 0L;
        }
    }

    private static long findTop(Quant24.Cube cube, int direction, int position, long[] moment) {
        switch (direction) {
            case 0:
                return moment[indexify(cube.R1, cube.G1, position)] - moment[indexify(cube.R1, cube.G0, position)] -
                        moment[indexify(cube.R0, cube.G1, position)] + moment[indexify(cube.R0, cube.G0, position)];
            case 1:
                return moment[indexify(cube.R1, position, cube.B1)] - moment[indexify(cube.R1, position, cube.B0)] -
                        moment[indexify(cube.R0, position, cube.B1)] + moment[indexify(cube.R0, position, cube.B0)];
            case 2:
                return moment[indexify(position, cube.G1, cube.B1)] - moment[indexify(position, cube.G1, cube.B0)] -
                        moment[indexify(position, cube.G0, cube.B1)] + moment[indexify(position, cube.G0, cube.B0)];
            default:
                return 0L;
        }
    }

    private void histogram(int[][] image) {
        byte mm = 1;
        int h = image.length;
        int w = image[0].length;

        for (int y = 0; y < h; ++y) {
            int[] temp = image[y];

            for (int x = 0; x < w; ++x) {
                int val = temp[x];
                int r = val >> 16 & 255;
                int g = val >> 8 & 255;
                int b = val & 255;
                int inr = r >> mm;
                int ing = g >> mm;
                int inb = b >> mm;
                int ind = indexify(inr + 1, ing + 1, inb + 1);
                ++this.vwt[ind];
                this.vmr[ind] += (long) r;
                this.vmg[ind] += (long) g;
                this.vmb[ind] += (long) b;
                this.m2[ind] += (double) (r * r + g * g + b * b);
            }
        }
    }

    private void M3d() {
        for (int r = 1; r < 129; ++r) {
            long[] area = new long[129];
            long[] areaR = new long[129];
            long[] areaG = new long[129];
            long[] areaB = new long[129];
            double[] areaTemp = new double[129];

            for (int g = 1; g < 129; ++g) {
                long line = 0L;
                long line_r = 0L;
                long line_g = 0L;
                long line_b = 0L;
                double line2 = 0.0D;

                for (int b = 1; b < 129; ++b) {
                    int ind1 = indexify(r, g, b);
                    line += this.vwt[ind1];
                    line_r += this.vmr[ind1];
                    line_g += this.vmg[ind1];
                    line_b += this.vmb[ind1];
                    line2 += this.m2[ind1];
                    area[b] += line;
                    areaR[b] += line_r;
                    areaG[b] += line_g;
                    areaB[b] += line_b;
                    areaTemp[b] += line2;
                    int ind2 = ind1 - indexify(1, 0, 0);
                    this.vwt[ind1] = this.vwt[ind2] + area[b];
                    this.vmr[ind1] = this.vmr[ind2] + areaR[b];
                    this.vmg[ind1] = this.vmg[ind2] + areaG[b];
                    this.vmb[ind1] = this.vmb[ind2] + areaB[b];
                    this.m2[ind1] = this.m2[ind2] + areaTemp[b];
                }
            }
        }
    }

    private double variance(Quant24.Cube cube) {
        double dr = volume(cube, this.vmr);
        double dg = volume(cube, this.vmg);
        double db = volume(cube, this.vmb);
        double xx = this.m2[indexify(cube.R1, cube.G1, cube.B1)] - this.m2[indexify(cube.R1, cube.G1, cube.B0)] - this
                .m2[indexify(cube.R1, cube.G0, cube.B1)] + this.m2[indexify(cube.R1, cube.G0, cube.B0)] - this.m2[indexify
                (cube.R0, cube.G1, cube.B1)] + this.m2[indexify(cube.R0, cube.G1, cube.B0)] + this.m2[indexify(cube.R0, cube
                .G0, cube.B1)] - this.m2[indexify(cube.R0, cube.G0, cube.B0)];
        return xx - (dr * dr + dg * dg + db * db) / volume(cube, this.vwt);
    }

    private Object[] maximize(Quant24.Cube cube, int direction, int first, int last, double whole_r, double whole_g, double
            whole_b, double whole_w) {
        long base_r = base(cube, direction, this.vmr);
        long base_g = base(cube, direction, this.vmg);
        long base_b = base(cube, direction, this.vmb);
        long base_w = base(cube, direction, this.vwt);
        double max = 0.0D;
        int cut = -1;

        for (int i = first; i < last; ++i) {
            double half_r = (double) (base_r + findTop(cube, direction, i, this.vmr));
            double half_g = (double) (base_g + findTop(cube, direction, i, this.vmg));
            double half_b = (double) (base_b + findTop(cube, direction, i, this.vmb));
            double half_w = (double) (base_w + findTop(cube, direction, i, this.vwt));
            if (half_w != 0.0D) {
                double temp = (half_r * half_r + half_g * half_g + half_b * half_b) / half_w;
                half_r = whole_r - half_r;
                half_g = whole_g - half_g;
                half_b = whole_b - half_b;
                half_w = whole_w - half_w;
                if (half_w != 0.0D) {
                    temp += (half_r * half_r + half_g * half_g + half_b * half_b) / half_w;
                    if (temp > max) {
                        max = temp;
                        cut = i;
                    }
                }
            }
        }
        return new Object[]{Integer.valueOf(cut), Double.valueOf(max)};
    }

    private boolean cut(Quant24.Cube set1, Quant24.Cube set2) {
        double whole_r = volume(set1, this.vmr);
        double whole_g = volume(set1, this.vmg);
        double whole_b = volume(set1, this.vmb);
        double whole_w = volume(set1, this.vwt);
        Object[] temp = this.maximize(set1, 2, set1.R0 + 1, set1.R1, whole_r, whole_g, whole_b, whole_w);
        int cutr = ((Integer) temp[0]).intValue();
        double maxr = ((Double) temp[1]).doubleValue();
        temp = this.maximize(set1, 1, set1.G0 + 1, set1.G1, whole_r, whole_g, whole_b, whole_w);
        int cutg = ((Integer) temp[0]).intValue();
        double maxg = ((Double) temp[1]).doubleValue();
        temp = this.maximize(set1, 0, set1.B0 + 1, set1.B1, whole_r, whole_g, whole_b, whole_w);
        int cutb = ((Integer) temp[0]).intValue();
        double maxb = ((Double) temp[1]).doubleValue();
        byte dir;
        if (maxr >= maxg && maxr >= maxb) {
            dir = 2;
            if (cutr < 0) {
                return false;
            }
        } else if (maxg >= maxr && maxg >= maxb) {
            dir = 1;
        } else {
            dir = 0;
        }

        set2.R1 = set1.R1;
        set2.G1 = set1.G1;
        set2.B1 = set1.B1;
        switch (dir) {
            case 0:
                set2.B0 = set1.B1 = cutb;
                set2.R0 = set1.R0;
                set2.G0 = set1.G0;
                break;
            case 1:
                set2.G0 = set1.G1 = cutg;
                set2.R0 = set1.R0;
                set2.B0 = set1.B0;
                break;
            case 2:
                set2.R0 = set1.R1 = cutr;
                set2.G0 = set1.G0;
                set2.B0 = set1.B0;
        }

        set1.Volume = (set1.R1 - set1.R0) * (set1.G1 - set1.G0) * (set1.B1 - set1.B0);
        set2.Volume = (set2.R1 - set2.R0) * (set2.G1 - set2.G0) * (set2.B1 - set2.B0);
        return true;
    }

    private void buildCube(Quant24.Cube[] cube, int colorCount) {
        double[] vv = new double[colorCount];

        int next;
        for (next = 0; next < colorCount; ++next) {
            cube[next] = new Quant24.Cube();
        }

        cube[0].R0 = cube[0].G0 = cube[0].B0 = 0;
        cube[0].R1 = cube[0].G1 = cube[0].B1 = 128;
        next = 0;

        for (int i = 1; i < colorCount; ++i) {
            if (this.cut(cube[next], cube[i])) {
                vv[next] = cube[next].Volume > 1 ? this.variance(cube[next]) : 0.0D;
                vv[i] = cube[i].Volume > 1 ? this.variance(cube[i]) : 0.0D;
            } else {
                vv[next] = 0.0D;
                --i;
            }

            next = 0;
            double temp = vv[0];

            for (int k = 1; k <= i; ++k) {
                if (vv[k] > temp) {
                    temp = vv[k];
                    next = k;
                }
            }

            if (temp <= 0.0D) {
                break;
            }
        }
    }

    private class Cube {
        int R0;
        int R1;
        int G0;
        int G1;
        int B0;
        int B1;
        int Volume;

        private Cube() {
        }
    }
}