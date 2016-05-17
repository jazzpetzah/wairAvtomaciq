package com.wearezeta.auto.common.video;


import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.jcodec.codecs.h264.H264Encoder;
import org.jcodec.codecs.h264.H264Utils;
import org.jcodec.common.NIOUtils;
import org.jcodec.common.SeekableByteChannel;
import org.jcodec.common.model.ColorSpace;
import org.jcodec.common.model.Picture;
import org.jcodec.containers.mp4.Brand;
import org.jcodec.containers.mp4.MP4Packet;
import org.jcodec.containers.mp4.TrackType;
import org.jcodec.containers.mp4.muxer.FramesMP4MuxerTrack;
import org.jcodec.containers.mp4.muxer.MP4Muxer;
import org.jcodec.scale.ColorUtil;
import org.jcodec.scale.Transform;

/**
 *  SequenceEncoder come from org.jcodec.api.awt.SequenceEncoder, but cannot use directly
 *  based on transform issues, I copy it and update it
 */
public class SequenceEncoder {
    private SeekableByteChannel ch;
    private Picture toEncode;
    private Transform transform;
    private H264Encoder encoder;
    private ArrayList<ByteBuffer> spsList;
    private ArrayList<ByteBuffer> ppsList;
    private FramesMP4MuxerTrack outTrack;
    private ByteBuffer _out;
    private int frameNo;
    private MP4Muxer muxer;

    public SequenceEncoder(File out) throws IOException {
        this.ch = NIOUtils.writableFileChannel(out);
        this.muxer = new MP4Muxer(this.ch, Brand.MP4);
        this.outTrack = this.muxer.addTrack(TrackType.VIDEO, 25);
        this._out = ByteBuffer.allocate(12441600);
        this.encoder = new H264Encoder();
        this.transform = ColorUtil.getTransform(ColorSpace.RGB, this.encoder.getSupportedColorSpaces()[0]);
        this.spsList = new ArrayList();
        this.ppsList = new ArrayList();
    }

    /**
     * Encode Image to Video
     *
     * @param image the image which will be encoded into video
     * @return the current channel size
     * @throws IOException
     */
    public long encodeImage(BufferedImage image) throws IOException {
        Picture pic = makeFrame(image, ColorSpace.YUV420J);
        if (this.toEncode == null) {
            this.toEncode = Picture.create(pic.getWidth(), pic.getHeight(), this.encoder.getSupportedColorSpaces()[0]);
        }

        this.transform.transform(pic, this.toEncode);
        this._out.clear();
        ByteBuffer result = this.encoder.encodeFrame(this.toEncode, this._out);

        this.spsList.clear();
        this.ppsList.clear();
        H264Utils.wipePS(result, this.spsList, this.ppsList);
        H264Utils.encodeMOVPacket(result);
        this.outTrack.addFrame(new MP4Packet(result, (long) this.frameNo, 25L, 100L, (long) this.frameNo, true, null, (long) this.frameNo, 0));
        this.frameNo++;
        return this.ch.size();
    }

    public void finish() throws IOException {
        this.outTrack.addSampleEntry(H264Utils.createMOVSampleEntry(this.spsList, this.ppsList, 4));
        this.muxer.writeHeader();
        NIOUtils.closeQuietly(this.ch);
    }

    private Picture makeFrame(BufferedImage bi, ColorSpace cs) {
        DataBuffer imgdata = bi.getRaster().getDataBuffer();
        int[] ypix = new int[imgdata.getSize()];
        int[] upix = new int[imgdata.getSize() >> 2];
        int[] vpix = new int[imgdata.getSize() >> 2];
        int ipx = 0, uvoff = 0;

        for (int h = 0; h < bi.getHeight(); h++) {
            for (int w = 0; w < bi.getWidth(); w++) {
                int elem = imgdata.getElem(ipx);
                int r = 0x0ff & (elem >>> 16);
                int g = 0x0ff & (elem >>> 8);
                int b = 0x0ff & elem;
                ypix[ipx] = ((66 * r + 129 * g + 25 * b) >> 8) + 16;
                if ((0 != w % 2) && (0 != h % 2)) {
                    upix[uvoff] = ((-38 * r + -74 * g + 112 * b) >> 8) + 128;
                    vpix[uvoff] = ((112 * r + -94 * g + -18 * b) >> 8) + 128;
                    uvoff++;
                }
                ipx++;
            }
        }
        int[][] pix = {ypix, upix, vpix, null};
        return new Picture(bi.getWidth(), bi.getHeight(), pix, cs);
    }


}
