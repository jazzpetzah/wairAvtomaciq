package com.wearezeta.auto.common.calling;

import java.io.*;
import java.util.concurrent.CountDownLatch;

import javax.sound.sampled.*;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;

public class AudioPlayer {

	private static final Logger log = ZetaLogger.getLog(AudioPlayer.class
			.getSimpleName());

	private String inputFileName;

	public AudioPlayer(String filename) {
		this.inputFileName = filename;
	}

	public void playAudio() throws IOException {
		AudioInputStream audioInputStream = null;
		try {
			audioInputStream = AudioSystem.getAudioInputStream(new File(
					inputFileName).getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			final CountDownLatch clipDone = new CountDownLatch(1);
			clip.addLineListener(new LineListener() {
				@Override
				public void update(LineEvent event) {
					if (event.getType() == LineEvent.Type.STOP) {
						event.getLine().close();
						clipDone.countDown();
					}
				}
			});
			clip.start();
			clipDone.await();
		} catch (Exception ex) {
			log.debug("Error while playing audiofile " + inputFileName + ".\n"
					+ ex.getMessage());
		} finally {
			audioInputStream.close();
		}
	}
}