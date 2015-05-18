package com.wearezeta.auto.android.common.reporter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;

public class LogcatListener extends Thread {

	private static Logger log = ZetaLogger.getLog(LogcatListener.class
			.getSimpleName());

	String output = "";

	Process process = null;

	boolean isRunning = false;

	public LogcatListener() {

	}

	public void startListeningLogcat() {
		String adbCommand = AndroidCommonUtils.ADB_PREFIX + "adb -d logcat *:W";
		try {
			process = Runtime.getRuntime().exec(
					new String[] { "/bin/bash", "-c", adbCommand });
		} catch (IOException e) {
			log.debug(e.getMessage());
		}
		log.debug("Started process " + process.isAlive());

		isRunning = true;

		log.debug(process);

		this.start();
	}

	public void stopListeningLogcat() {
		isRunning = false;
	}

	public void run() {
		InputStream stream = null;
		InputStreamReader isReader = null;
		BufferedReader bufferedReader = null;
		try {
			stream = process.getInputStream();
			isReader = new InputStreamReader(stream);
			bufferedReader = new BufferedReader(isReader);
			while (process != null && isRunning) {
				String s;

				while ((s = bufferedReader.readLine()) != null && isRunning) {
					output += s + "\n";
				}
			}
		} catch (IOException e) {
			log.error(e.getMessage());
		} finally {
			try {
				if (bufferedReader != null)
					bufferedReader.close();
				if (isReader != null)
					isReader.close();
				if (stream != null)
					stream.close();
			} catch (IOException e) {
			}
		}

	}

	public String getOutput() {
		return output;
	}
}
