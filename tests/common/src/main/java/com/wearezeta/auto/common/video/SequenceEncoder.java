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
import org.jcodec.scale.AWTUtil;
import org.jcodec.scale.ColorUtil;
import org.jcodec.scale.Transform;

/**
 * SequenceEncoder come from org.jcodec.api.awt.SequenceEncoder,
 * I copy it and update it to retrieve the channel size
 */
public class SequenceEncoder {
    private SeekableByteChannel ch;
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
     * Create one video frame from BufferedImage
     *
     * @param image
     * @return the rendered video frame
     * @throws IOException
     */
    public Picture createFrameFromSingleImage(BufferedImage image) throws IOException {
        Picture pic = AWTUtil.fromBufferedImage(image);
        Picture renderedFrame = Picture.create(pic.getWidth(), pic.getHeight(), this.encoder.getSupportedColorSpaces()[0]);
        this.transform.transform(pic, renderedFrame);
        return renderedFrame;
    }

    /**
     * Please note, it doesn't support to add different size picuture into Picture.
     * Such as first you add the picture 10 * 10, second time you cannot add the picture 100* 100
     * You need to resize all pictures into same size
     *
     * @param picture
     * @return current channel size
     * @throws IOException
     */
    public long addFrameToVideo(Picture picture) throws IOException {
        // each time it needs to init SPS and PPS in encoder.encodeFrame, so cannot extract it
        this._out.clear();
        ByteBuffer frameByteBuffer = this.encoder.encodeFrame(picture, this._out);

        this.spsList.clear();
        this.ppsList.clear();
        H264Utils.wipePS(frameByteBuffer, this.spsList, this.ppsList);
        H264Utils.encodeMOVPacket(frameByteBuffer);
        this.outTrack.addFrame(new MP4Packet(frameByteBuffer, (long) this.frameNo, 25L, 1L, (long) this.frameNo, true, null, (long) this.frameNo, 0));
        ++this.frameNo;
        return this.ch.size();
    }

    /**
     * Flush the data within channel into output file
     * Should be called when you add all your frames
     *
     * @throws IOException
     */
    public void finish() throws IOException {
        this.outTrack.addSampleEntry(H264Utils.createMOVSampleEntry(this.spsList, this.ppsList, 4));
        this.muxer.writeHeader();
        NIOUtils.closeQuietly(this.ch);
    }

}
