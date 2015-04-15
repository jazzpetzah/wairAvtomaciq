package com.wearezeta.auto.common.calling;

import java.io.*;

import javax.sound.sampled.*;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;

class AudioRecorder {

	private static final Logger log = ZetaLogger.getLog(AudioRecorder.class
			.getSimpleName());

	private AudioFormat audioFormat;
	private TargetDataLine targetDataLine;
	private String outputFile;

	public AudioRecorder(String outputFile) {
		this.outputFile = outputFile;
	}

	public void captureAudio() {
		try {
			audioFormat = getAudioFormat();
			DataLine.Info dataLineInfo = new DataLine.Info(
					TargetDataLine.class, audioFormat);
			targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);

			new CaptureThread().start();
		} catch (Exception e) {
			log.debug(e);
		}
	}

	public void stopCapture() {
		targetDataLine.stop();
		targetDataLine.close();
	}

	private AudioFormat getAudioFormat() {
		float sampleRate = 16000.0F;
		int sampleSizeInBits = 16;
		int channels = 1;
		boolean signed = true;
		boolean bigEndian = false;
		return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed,
				bigEndian);
	}

	class CaptureThread extends Thread {
		public void run() {
			AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

			File audioFile = new File(outputFile);

			try {
				targetDataLine.open(audioFormat);
				targetDataLine.start();
				AudioSystem.write(new AudioInputStream(targetDataLine),
						fileType, audioFile);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}