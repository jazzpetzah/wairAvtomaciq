package compress.png;

import java.util.BitSet;

public class PngBitReader {
    private int p;
    private final int totalBitLen;
    private BitSet bitset;
    private byte[] data;
    private final boolean hasSmallBits;

    public PngBitReader(byte[] data, boolean hasSmallBits) {
        this.hasSmallBits = hasSmallBits;
        this.totalBitLen = data.length * 8;
        if(this.hasSmallBits) {
            this.bitset = new BitSet(this.totalBitLen);
            int c = 0;
            int dLen = data.length;

            for(int i = 0; i < dLen; ++i) {
                byte b = data[i];

                for(int j = 7; j >= 0; --j) {
                    boolean isOn = (b >> j & 1) == 1;
                    this.bitset.set(c, isOn);
                    ++c;
                }
            }
        } else {
            this.data = data;
        }

    }

    private int readBits(int lenToRead) {
        int retVal = 0;
        int i;
        if(this.hasSmallBits) {
            BitSet len = this.bitset.get(this.p, this.p + lenToRead);

            for(i = 0; i < lenToRead; ++i) {
                if(len.get(i)) {
                    retVal = retVal << 1 | 1;
                } else {
                    retVal <<= 1;
                }
            }

            this.p += lenToRead;
        } else {
            int var5 = lenToRead / 8;

            for(i = 0; i < var5; ++i) {
                retVal <<= 8;
                retVal |= this.data[this.p / 8] & 255;
                this.p += 8;
            }
        }

        return retVal;
    }

    public int getPositive(int bitLen) {
        return this.readBits(bitLen);
    }

    public int getPointer() {
        return this.p;
    }

    public int getTotalBitLen() {
        return this.totalBitLen;
    }
}
