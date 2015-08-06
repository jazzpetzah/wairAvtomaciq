package com.wearezeta.auto.ios.reporter;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.common.process.AsyncProcess;

public final class IOSLogListener {
	private static final Logger log = ZetaLogger.getLog(IOSLogListener.class
			.getSimpleName());

	private static IOSLogListener instance = null;

	public static synchronized IOSLogListener getInstance() {
		if (instance == null) {
			instance = new IOSLogListener();
		}
		return instance;
	}

	private IOSLogListener() {
	}

	public boolean isRunning() {
		if (listener == null) {
			return false;
		}
		return listener.isRunning();
	}

	private AsyncProcess listener;

	public void start() throws Exception {
		if (this.isRunning()) {
			log.warn("Restarting IOS log listener...");
			this.stop();
		}
		String syslogCmd = "idevicesyslog";

		final String[] cmd = new String[] { "/bin/bash", "-c", syslogCmd };
		listener = new AsyncProcess(cmd, true, true);
		listener.start();
	}

	public void stop() throws Exception {
		if (!this.isRunning()) {
			throw new IllegalStateException(
					"The listener has to be started first");
		}
		this.listener.stop(2, new int[] { this.listener.getPid() + 1,
				this.listener.getPid() }, 5000);
	}

	public String getStdOut() {
		if (this.listener == null) {
			throw new IllegalStateException(
					"The listener has to be started first");
		}
		return this.listener.getStdout();
	}

	public String getStdErr() {
		if (this.listener == null) {
			throw new IllegalStateException(
					"The listener has to be started first");
		}
		return this.listener.getStderr();
	}

	public static void writeDeviceLogsToConsole(final IOSLogListener listener)
			throws Exception {
		log.debug("\n\n\n=== CAPTURED STDERR LOGS ===\n");
		System.out.println(listener.getStdErr().trim());
		log.debug("\n=== END OF CAPTURED STDERR LOGS ===\n\n\n");
	}

	public static void forceStopAll() {
		if (instance != null && instance.isRunning()) {
			try {
				instance.stop();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
