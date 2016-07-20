package compress.png;

import java.nio.ByteBuffer;
import java.util.zip.CRC32;

public class PngChunk {
    public static final byte[] SIGNATURE = new byte[]{(byte) -119, (byte) 80, (byte) 78, (byte) 71, (byte) 13, (byte) 10,
            (byte) 26, (byte) 10};
    public static final byte[] IHDR = new byte[]{(byte) 73, (byte) 72, (byte) 68, (byte) 82};
    public static final byte[] PLTE = new byte[]{(byte) 80, (byte) 76, (byte) 84, (byte) 69};
    public static final byte[] tRNS = new byte[]{(byte) 116, (byte) 82, (byte) 78, (byte) 83};
    public static final byte[] IDAT = new byte[]{(byte) 73, (byte) 68, (byte) 65, (byte) 84};
    public static final byte[] IEND = new byte[]{(byte) 73, (byte) 69, (byte) 78, (byte) 68};
    private final byte[] length;
    private final byte[] name;
    private final byte[] data;

    public PngChunk(byte[] length, byte[] name, byte[] data) {
        this.length = length;
        this.name = name;
        this.data = data;
    }

    public static PngChunk createHeaderChunk(int width, int height, byte bitDepth, byte colorType, byte compression, byte
            filter, byte interlace) {
        ByteBuffer buff = ByteBuffer.allocate(13);
        buff.putInt(width);
        buff.putInt(height);
        buff.put(bitDepth);
        buff.put(colorType);
        buff.put(compression);
        buff.put(filter);
        buff.put(interlace);
        byte[] data = buff.array();
        return new PngChunk(intToBytes(13), IHDR, data);
    }

    public static PngChunk createPaleteChunk(byte[] palBytes) {
        return new PngChunk(intToBytes(palBytes.length), PLTE, palBytes);
    }

    public static PngChunk createTrnsChunk(byte[] trnsBytes) {
        return new PngChunk(intToBytes(trnsBytes.length), tRNS, trnsBytes);
    }

    public static PngChunk createDataChunk(byte[] zLibBytes) {
        return new PngChunk(intToBytes(zLibBytes.length), IDAT, zLibBytes);
    }

    public static PngChunk createEndChunk() {
        return new PngChunk(intToBytes(0), IEND, new byte[0]);
    }

    public byte[] getCRCValue() {
        CRC32 crc32 = new CRC32();
        crc32.update(this.name);
        crc32.update(this.data);
        byte[] temp = longToBytes(crc32.getValue());
        return new byte[]{temp[4], temp[5], temp[6], temp[7]};
    }

    public static byte[] intToBytes(int value) {
        return new byte[]{(byte) (value >> 24), (byte) (value >> 16), (byte) (value >> 8), (byte) value};
    }

    private static byte[] longToBytes(long value) {
        return new byte[]{(byte) ((int) (value >> 56)), (byte) ((int) (value >> 48)), (byte) ((int) (value >> 40)), (byte) (
                (int) (value >> 32)), (byte) ((int) (value >> 24)), (byte) ((int) (value >> 16)), (byte) ((int) (value >> 8))
                , (byte) ((int) value)};
    }

    public byte[] getLength() {
        return this.length;
    }

    public byte[] getName() {
        return this.name;
    }

    public byte[] getData() {
        return this.data;
    }
}