package compress.png;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.DataBufferUShort;
import java.awt.image.DirectColorModel;
import java.awt.image.IndexColorModel;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.zip.Deflater;

public class PngEncoder {
    private boolean compress;

    public PngEncoder() {
    }

    public void write(BufferedImage image, OutputStream outputStream) throws IOException {
        if(this.compress) {
            this.compress8Bit(image, outputStream);
        } else {
            this.compressNormal(image, outputStream);
        }

    }

    public boolean isCompressed() {
        return this.compress;
    }

    public void setCompressed(boolean compress) {
        this.compress = compress;
    }

    private void compressNormal(BufferedImage image, OutputStream outputStream) throws IOException {
        int bh = image.getHeight();
        int bw = image.getWidth();
        ColorModel colorModel = image.getColorModel();
        boolean hasAlpha = colorModel.hasAlpha();
        int pLen = colorModel.getPixelSize();
        int nComp = colorModel.getNumComponents();
        boolean isIndexed = colorModel instanceof IndexColorModel;
        int bitDepth = calculateBitDepth(pLen, nComp);
        int colType;
        if(isIndexed) {
            colType = 3;
            nComp = 1;
        } else if(nComp < 3) {
            colType = hasAlpha?4:0;
        } else if(bitDepth < 8) {
            colType = hasAlpha?4:0;
        } else {
            colType = hasAlpha?6:2;
        }

        outputStream.write(PngChunk.SIGNATURE);
        PngChunk chunk = PngChunk.createHeaderChunk(bw, bh, (byte)bitDepth, (byte)colType, (byte)0, (byte)0, (byte)0);
        outputStream.write(chunk.getLength());
        outputStream.write(chunk.getName());
        outputStream.write(chunk.getData());
        outputStream.write(chunk.getCRCValue());
        byte[] pixels;
        if(isIndexed && bitDepth != 8) {
            pixels = getIndexedPaletteData(image);
        } else {
            pixels = getPixelData(image, bitDepth, nComp, bw, bh);
        }

        if(isIndexed) {
            IndexColorModel indexModel = (IndexColorModel)colorModel;
            int indexModelMapSize = indexModel.getMapSize();
            int[] rgbs = new int[indexModelMapSize];
            indexModel.getRGBs(rgbs);
            if(bitDepth == 8) {
                indexModelMapSize = reduceIndexMap(indexModelMapSize, rgbs, pixels);
            }

            ByteBuffer bb = ByteBuffer.allocate(indexModelMapSize * 3);

            int i;
            for(int trnsBytes = 0; trnsBytes < indexModelMapSize; ++trnsBytes) {
                i = rgbs[trnsBytes];
                bb.put(new byte[]{(byte)(i >> 16), (byte)(i >> 8), (byte)i});
            }

            chunk = PngChunk.createPaleteChunk(bb.array());
            outputStream.write(chunk.getLength());
            outputStream.write(chunk.getName());
            outputStream.write(chunk.getData());
            outputStream.write(chunk.getCRCValue());
            if(indexModel.getNumComponents() == 4) {
                byte[] var20 = new byte[indexModelMapSize];

                for(i = 0; i < indexModelMapSize; ++i) {
                    var20[i] = (byte)(rgbs[i] >> 24);
                }

                chunk = PngChunk.createTrnsChunk(var20);
                outputStream.write(chunk.getLength());
                outputStream.write(chunk.getName());
                outputStream.write(chunk.getData());
                outputStream.write(chunk.getCRCValue());
            }
        }

        pixels = this.getDeflatedData(pixels);
        chunk = PngChunk.createDataChunk(pixels);
        outputStream.write(chunk.getLength());
        outputStream.write(chunk.getName());
        outputStream.write(chunk.getData());
        outputStream.write(chunk.getCRCValue());
        chunk = PngChunk.createEndChunk();
        outputStream.write(chunk.getLength());
        outputStream.write(chunk.getName());
        outputStream.write(chunk.getData());
        outputStream.write(chunk.getCRCValue());
    }

    private static int reduceIndexMap(int indexModelMapSize, int[] rgbs, byte[] pixels) {
        int numColors = 0;
        byte[] indexMap = new byte[indexModelMapSize];
        LinkedHashMap colors = new LinkedHashMap();

        int colorSet;
        int temp;
        for(colorSet = 0; colorSet < indexModelMapSize; ++colorSet) {
            temp = rgbs[colorSet];
            if(!colors.containsKey(temp)) {
                indexMap[colorSet] = (byte)numColors;
                colors.put(temp, numColors);
                ++numColors;
            } else {
                indexMap[colorSet] = (byte)((Integer)colors.get(temp)).intValue();
            }
        }

        if(numColors < indexModelMapSize) {
            for(colorSet = 0; colorSet < pixels.length; ++colorSet) {
                pixels[colorSet] = indexMap[pixels[colorSet] & 255];
            }

            Set var10 = colors.keySet();
            temp = 0;

            int c;
            for(Iterator var8 = var10.iterator(); var8.hasNext(); rgbs[temp++] = c) {
                c = ((Integer)var8.next());
            }
        }

        return numColors;
    }

    private static boolean isAlphaUsed(byte[] trnsBytes) {
        int var2 = trnsBytes.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            byte trn = trnsBytes[var3];
            if(trn != -1) {
                return true;
            }
        }

        return false;
    }

    private void compress8Bit(BufferedImage image, OutputStream outputStream) throws IOException {
        int bh;
        int bw;
        int dim;
        byte[] pixels;
        byte[] trnsBytes;
        int[][] argb;
        int[][] rgb;
        int type = image.getType();
        bh = image.getHeight();
        bw = image.getWidth();
        dim = bh * bw;
        trnsBytes = null;
        argb = null;
        rgb = null;
        int p = 0;
        int[] intPixels;
        int[] tempArr;
        int r;
        int g;
        int b;
        int colorPalette;
        int indexedPixels;
        label153:
        switch(type) {
            case 1:
                intPixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
                rgb = new int[bh][bw];
                colorPalette = 0;

                while(true) {
                    if(colorPalette >= bh) {
                        break label153;
                    }

                    tempArr = rgb[colorPalette];

                    for(indexedPixels = 0; indexedPixels < bw; ++indexedPixels) {
                        tempArr[indexedPixels] = intPixels[p++];
                    }

                    ++colorPalette;
                }
            case 2:
                intPixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
                argb = new int[bh][bw];
                colorPalette = 0;

                while(true) {
                    if(colorPalette >= bh) {
                        break label153;
                    }

                    tempArr = argb[colorPalette];

                    for(indexedPixels = 0; indexedPixels < bw; ++indexedPixels) {
                        tempArr[indexedPixels] = intPixels[p++];
                    }

                    ++colorPalette;
                }
            case 3:
            default:
                this.compressNormal(image, outputStream);
                return;
            case 4:
                intPixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
                rgb = new int[bh][bw];
                colorPalette = 0;

                while(true) {
                    if(colorPalette >= bh) {
                        break label153;
                    }

                    tempArr = rgb[colorPalette];

                    for(indexedPixels = 0; indexedPixels < bw; ++indexedPixels) {
                        int val = intPixels[p++];
                        b = val >> 16 & 255;
                        g = val >> 8 & 255;
                        r = val & 255;
                        tempArr[indexedPixels] = r << 16 | g << 8 | b;
                    }

                    ++colorPalette;
                }
            case 5:
                pixels = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
                rgb = new int[bh][bw];
                colorPalette = 0;

                while(true) {
                    if(colorPalette >= bh) {
                        break label153;
                    }

                    tempArr = rgb[colorPalette];

                    for(indexedPixels = 0; indexedPixels < bw; ++indexedPixels) {
                        b = pixels[p++] & 255;
                        g = pixels[p++] & 255;
                        r = pixels[p++] & 255;
                        tempArr[indexedPixels] = r << 16 | g << 8 | b;
                    }

                    ++colorPalette;
                }
            case 6:
                pixels = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();
                argb = new int[bh][bw];

                for(colorPalette = 0; colorPalette < bh; ++colorPalette) {
                    tempArr = argb[colorPalette];

                    for(indexedPixels = 0; indexedPixels < bw; ++indexedPixels) {
                        int a = pixels[p++] & 255;
                        b = pixels[p++] & 255;
                        g = pixels[p++] & 255;
                        r = pixels[p++] & 255;
                        tempArr[indexedPixels] = a << 24 | r << 16 | g << 8 | b;
                    }
                }
        }

        byte[] var30 = new byte[dim + bh];
        byte[] bitDepth;
        Object[] colType;
        int i;
        int j;
        byte[] var27;
        int var31;
        int var33;
        if(argb != null) {
            colType = getIndexedMap(argb);
            if(colType != null) {
                bitDepth = (byte[])colType[0];
                var27 = (byte[])colType[1];
                trnsBytes = (byte[])colType[2];
                if(!isAlphaUsed(trnsBytes)) {
                    trnsBytes = null;
                }
            } else {
                Quant32 chunk = new Quant32();
                Object[] z = chunk.getPalette(argb);
                var27 = (byte[])z[0];
                trnsBytes = (byte[])z[1];
                bitDepth = D4.process(var27, trnsBytes, argb, bh, bw);
                if(!isAlphaUsed(trnsBytes)) {
                    trnsBytes = null;
                }
            }

            var31 = 0;
            var33 = 0;

            for(i = 0; i < bh; ++i) {
                var30[var33++] = 0;

                for(j = 0; j < bw; ++j) {
                    var30[var33++] = bitDepth[var31++];
                }
            }
        } else {
            colType = getIndexedMap(rgb);
            if(colType != null) {
                bitDepth = (byte[])colType[0];
                var27 = (byte[])colType[1];
            } else {
                Quant24 var32 = new Quant24();
                var27 = var32.getPalette(rgb);
                bitDepth = D3.process(var27, rgb, bh, bw);
            }

            var31 = 0;
            var33 = 0;

            for(i = 0; i < bh; ++i) {
                var30[var33++] = 0;

                for(j = 0; j < bw; ++j) {
                    var30[var33++] = bitDepth[var31++];
                }
            }
        }

        byte var29 = 8;
        byte var28 = 3;
        outputStream.write(PngChunk.SIGNATURE);
        PngChunk var34 = PngChunk.createHeaderChunk(bw, bh, var29, var28, (byte)0, (byte)0, (byte)0);
        outputStream.write(var34.getLength());
        outputStream.write(var34.getName());
        outputStream.write(var34.getData());
        outputStream.write(var34.getCRCValue());
        pixels = this.getDeflatedData(var30);
        var34 = PngChunk.createPaleteChunk(var27);
        outputStream.write(var34.getLength());
        outputStream.write(var34.getName());
        outputStream.write(var34.getData());
        outputStream.write(var34.getCRCValue());
        if(trnsBytes != null) {
            var34 = PngChunk.createTrnsChunk(trnsBytes);
            outputStream.write(var34.getLength());
            outputStream.write(var34.getName());
            outputStream.write(var34.getData());
            outputStream.write(var34.getCRCValue());
        }

        var34 = PngChunk.createDataChunk(pixels);
        outputStream.write(var34.getLength());
        outputStream.write(var34.getName());
        outputStream.write(var34.getData());
        outputStream.write(var34.getCRCValue());
        var34 = PngChunk.createEndChunk();
        outputStream.write(var34.getLength());
        outputStream.write(var34.getName());
        outputStream.write(var34.getData());
        outputStream.write(var34.getCRCValue());
    }

    private static Object[] getIndexedMap(int[][] pixel) {
        int h = pixel.length;
        int w = pixel[0].length;
        int[] colors = new int[256];
        int c = 0;
        int p = 0;
        int t = 0;
        byte[] indexedBytes = new byte[h * w];
        HashMap map = new HashMap();

        int i;
        for(int palette = 0; palette < h; ++palette) {
            int[] temp = pixel[palette];

            for(int trns = 0; trns < w; ++trns) {
                i = temp[trns];
                Integer val = (Integer)map.get(i);
                if(val == null) {
                    if(c > 255) {
                        return null;
                    }

                    map.put(i, c);
                    colors[c] = i;
                    indexedBytes[p++] = (byte)c;
                    ++c;
                } else {
                    indexedBytes[p++] = (byte)val.intValue();
                }
            }
        }

        byte[] var14 = new byte[c * 3];
        byte[] var15 = new byte[c];
        p = 0;

        for(i = 0; i < c; ++i) {
            int var16 = colors[i];
            var15[t++] = (byte)(var16 >> 24 & 255);
            var14[p++] = (byte)(var16 >> 16 & 255);
            var14[p++] = (byte)(var16 >> 8 & 255);
            var14[p++] = (byte)(var16 & 255);
        }

        return new Object[]{indexedBytes, var14, var15};
    }

    private static byte[] getIndexedPaletteData(BufferedImage buff) throws IOException {
        byte[] pixels = ((DataBufferByte)buff.getRaster().getDataBuffer()).getData();
        int ih = buff.getHeight();
        int len = pixels.length / ih;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        int k = 0;

        for(int i = 0; i < ih; ++i) {
            bos.write(0);
            byte[] temp = new byte[len];
            System.arraycopy(pixels, k, temp, 0, len);
            bos.write(temp);
            k += len;
        }

        bos.close();
        return bos.toByteArray();
    }

    private static byte[] getPixelData(BufferedImage buff, int bitDepth, int nComp, int bw, int bh) throws IOException {
        ColorModel model = buff.getColorModel();
        ByteBuffer bos16;
        int i;
        int var36;
        int var37;
        switch(bitDepth) {
            case 1:
            case 2:
            case 4:
                byte[] pixels = ((DataBufferByte)buff.getRaster().getDataBuffer()).getData();
                int multi = bitDepth == 1?8:(bitDepth == 2?4:2);
                PngBitReader reader = new PngBitReader(pixels, true);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                BitWriter writer = new BitWriter(bos);
                int cc2 = 0;
                int iter = pixels.length * multi;

                for(int var34 = 0; var34 < iter; ++var34) {
                    if(cc2 == 0) {
                        writer.writeByte((byte)0);
                    }

                    writer.writeBits(reader.getPositive(bitDepth), bitDepth);
                    ++cc2;
                    if(cc2 == bw) {
                        cc2 = 0;
                    }
                }

                writer.end();
                bos.flush();
                bos.close();
                return bos.toByteArray();
            case 8:
                DataBuffer dataBuff = buff.getRaster().getDataBuffer();
                byte[] p;
                switch(dataBuff.getDataType()) {
                    case 0:
                        byte[] pixels8 = ((DataBufferByte)buff.getRaster().getDataBuffer()).getData();
                        int pLen = pixels8.length;
                        int shortPixels = 0;
                        bos16 = ByteBuffer.allocate(bw * bh * nComp + bh);
                        switch(buff.getType()) {
                            case 5:
                                for(var36 = 0; var36 < pLen; var36 += nComp) {
                                    if(shortPixels == 0) {
                                        bos16.put((byte)0);
                                    }

                                    p = new byte[]{pixels8[var36 + 2], pixels8[var36 + 1], pixels8[var36]};
                                    bos16.put(p);
                                    ++shortPixels;
                                    if(shortPixels == bw) {
                                        shortPixels = 0;
                                    }
                                }

                                return bos16.array();
                            case 6:
                            case 7:
                                for(var36 = 0; var36 < pLen; var36 += nComp) {
                                    if(shortPixels == 0) {
                                        bos16.put((byte)0);
                                    }

                                    p = new byte[]{pixels8[var36 + 3], pixels8[var36 + 2], pixels8[var36 + 1], pixels8[var36]};
                                    bos16.put(p);
                                    ++shortPixels;
                                    if(shortPixels == bw) {
                                        shortPixels = 0;
                                    }
                                }

                                return bos16.array();
                            default:
                                var36 = 0;

                                for(; var36 < pLen; var36 += nComp) {
                                    if(shortPixels == 0) {
                                        bos16.put((byte)0);
                                    }

                                    for(var37 = 0; var37 < nComp; ++var37) {
                                        bos16.put(pixels8[var36 + var37]);
                                    }

                                    ++shortPixels;
                                    if(shortPixels == bw) {
                                        shortPixels = 0;
                                    }
                                }

                                return bos16.array();
                        }
                    case 3:
                        int[] scol = ((DataBufferInt)buff.getRaster().getDataBuffer()).getData();
                        i = 0;
                        int p1 = 0;
                        int val;
                        int out;
                        int clm;
                        if(buff.getType() != 2 && buff.getType() != 3) {
                            if(buff.getType() == 1) {
                                p = new byte[bw * bh * 3 + bh];

                                for(out = 0; out < bh; ++out) {
                                    p[i++] = 0;

                                    for(clm = 0; clm < bw; ++clm) {
                                        val = scol[p1++];
                                        p[i++] = (byte)(val >> 16);
                                        p[i++] = (byte)(val >> 8);
                                        p[i++] = (byte)val;
                                    }
                                }
                            } else if(buff.getType() == 4) {
                                p = new byte[bw * bh * 3 + bh];

                                for(out = 0; out < bh; ++out) {
                                    p[i++] = 0;

                                    for(clm = 0; clm < bw; ++clm) {
                                        val = scol[p1++];
                                        p[i++] = (byte)val;
                                        p[i++] = (byte)(val >> 8);
                                        p[i++] = (byte)(val >> 16);
                                    }
                                }
                            } else {
                                if(!(model instanceof DirectColorModel)) {
                                    ByteBuffer var39 = ByteBuffer.allocate(bw * bh * nComp + bh);
                                    clm = 0;
                                    int var41 = scol.length;

                                    for(int var27 = 0; var27 < var41; ++var27) {
                                        int var42 = scol[var27];
                                        if(clm == 0) {
                                            var39.put((byte)0);
                                        }

                                        byte[] t = PngChunk.intToBytes(var42);
                                        switch(nComp) {
                                            case 1:
                                                var39.put(t[3]);
                                                break;
                                            case 2:
                                                var39.put(new byte[]{t[2], t[3]});
                                                break;
                                            case 3:
                                                var39.put(new byte[]{t[1], t[2], t[3]});
                                                break;
                                            case 4:
                                                var39.put(new byte[]{t[1], t[2], t[3], t[0]});
                                        }

                                        ++clm;
                                        if(clm == bw) {
                                            clm = 0;
                                        }
                                    }

                                    return var39.array();
                                }

                                DirectColorModel var38 = (DirectColorModel)model;
                                long var40 = (long)getMaskValue(var38.getRedMask());
                                long gMask = (long)getMaskValue(var38.getGreenMask());
                                long i1 = (long)getMaskValue(var38.getBlueMask());
                                long aMask = (long)getMaskValue(var38.getAlphaMask());
                                p = new byte[bw * bh * 4 + bh];

                                for(int i2 = 0; i2 < bh; ++i2) {
                                    p[i++] = 0;

                                    for(int j = 0; j < bw; ++j) {
                                        val = scol[p1++];
                                        p[i++] = (byte)(val >> (int)var40);
                                        p[i++] = (byte)(val >> (int)gMask);
                                        p[i++] = (byte)(val >> (int)i1);
                                        p[i++] = (byte)(val >> (int)aMask);
                                    }
                                }
                            }
                        } else {
                            p = new byte[bw * bh * 4 + bh];

                            for(out = 0; out < bh; ++out) {
                                p[i++] = 0;

                                for(clm = 0; clm < bw; ++clm) {
                                    val = scol[p1++];
                                    p[i++] = (byte)(val >> 16);
                                    p[i++] = (byte)(val >> 8);
                                    p[i++] = (byte)val;
                                    p[i++] = (byte)(val >> 24);
                                }
                            }
                        }

                        return p;
                }
            case 16:
                short[] var35 = ((DataBufferUShort)buff.getRaster().getDataBuffer()).getData();
                bos16 = ByteBuffer.allocate(var35.length * 2 + bh);
                var36 = 0;
                var37 = 0;

                for(; var37 < var35.length; var37 += nComp) {
                    if(var36 == 0) {
                        bos16.put((byte)0);
                    }

                    for(i = 0; i < nComp; ++i) {
                        bos16.putShort(var35[var37 + i]);
                    }

                    ++var36;
                    if(var36 == bw) {
                        var36 = 0;
                    }
                }

                return bos16.array();
            default:
                return null;
        }
    }

    private static int getMaskValue(int mask) {
        switch(mask) {
            case 255:
                return 0;
            case 65280:
                return 8;
            case 16711680:
                return 16;
            default:
                return 24;
        }
    }

    private static int calculateBitDepth(int pixelBits, int nComp) {
        if(pixelBits < 8) {
            return pixelBits;
        } else {
            int c = pixelBits / nComp;
            return c != 8 && c != 16?8:c;
        }
    }

    private byte[] getDeflatedData(byte[] pixels) throws IOException {
        Deflater deflater;
        if(this.compress) {
            deflater = new Deflater(9);
        } else {
            deflater = new Deflater(1);
        }

        deflater.setInput(pixels);
        int min = Math.min(pixels.length / 2, 4096);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(min);
        deflater.finish();
        byte[] buffer = new byte[min];

        while(!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }

        deflater.end();
        outputStream.close();
        return outputStream.toByteArray();
    }
}
