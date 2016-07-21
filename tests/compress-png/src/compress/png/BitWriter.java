package compress.png;

import java.io.IOException;
import java.io.OutputStream;

public class BitWriter {
    private int bitCount;
    private int pointer;
    private final OutputStream stream;

    public BitWriter(OutputStream stream) {
        this.stream = stream;
    }

    public void end() {
        while (this.bitCount > 0) {
            this.pointer <<= 1;
            ++this.bitCount;
            if (this.bitCount == 8) {
                try {
                    this.stream.write(this.pointer);
                } catch (IOException var2) {
                    var2.printStackTrace();
                }
                this.pointer = 0;
                this.bitCount = 0;
            }
        }
    }

    public void writeBits(int bits, int num) {
        if (num >= 0 && num <= 32) {
            while (num > 0) {
                int cbit = Math.min(num, 8 - this.bitCount);
                this.pointer = this.pointer << cbit | bits >>> num - cbit & (1 << cbit) - 1;
                this.bitCount += cbit;
                num -= cbit;
                if (this.bitCount == 8) {
                    try {
                        this.stream.write(this.pointer);
                    } catch (IOException var5) {
                        var5.printStackTrace();
                    }
                    this.pointer = 0;
                    this.bitCount = 0;
                }
            }
        } else {
            throw new IllegalArgumentException("Number of bits is out of range");
        }
    }

    public void writeByte(byte nextByte) {
        if (this.bitCount == 0) {
            try {
                this.stream.write(nextByte);
            } catch (IOException var3) {
                var3.printStackTrace();
            }
        } else {
            this.writeBits(nextByte, 8);
        }
    }

    public void writeBytes(byte[] bytes) {
        if (this.bitCount == 0) {
            try {
                this.stream.write(bytes);
            } catch (IOException var6) {
                var6.printStackTrace();
            }
        } else {
            byte[] e = bytes;
            int var3 = bytes.length;
            for (int var4 = 0; var4 < var3; ++var4) {
                byte b = e[var4];
                this.writeByte(b);
            }
        }
    }
}