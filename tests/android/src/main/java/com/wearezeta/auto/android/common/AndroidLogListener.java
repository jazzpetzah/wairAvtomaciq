package com.wearezeta.auto.android.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.common.process.AsyncProcess;

public final class AndroidLogListener {
	private static final Logger log = ZetaLogger
			.getLog(AndroidLogListener.class.getSimpleName());

	public static final String ADB_PREFIX = "";

	private static final List<String> stdoutIncludePatterns = new ArrayList<>();
	static {
		stdoutIncludePatterns.addAll(Arrays.asList(new String[] { "E/" }));
	}

	public static enum ListenerType {
		DEFAULT(null), PERF("LoadTimeLoggerController");

		private final String tags;

		private ListenerType(String tags) {
			this.tags = tags;
		}

		public String getTags() {
			return this.tags;
		}
	}

	private static Map<ListenerType, AndroidLogListener> instances = new HashMap<>();

	public static synchronized AndroidLogListener getInstance(
			ListenerType listenerType) {
		if (instances.isEmpty()) {
			for (ListenerType type : ListenerType.values()) {
				instances.put(type, new AndroidLogListener(type.getTags()));
			}
		}
		return instances.get(listenerType);
	}

	private String tags;

	private AndroidLogListener(String tags) {
		this.tags = tags;
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
			log.warn("Restarting Android log listener...");
			this.stop();
		}
		String adbCmd;
		if (this.tags == null) {
			adbCmd = ADB_PREFIX + "adb logcat -v time";
		} else {
			adbCmd = ADB_PREFIX
					+ String.format("adb logcat -v time -s %s", this.tags);
		}
		final String[] cmd = new String[] { "/bin/bash", "-c", adbCmd };
		listener = new AsyncProcess(cmd, (this.tags != null),
				(this.tags != null));
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

	public static void writeDeviceLogsToConsole(
			final AndroidLogListener listener) throws Exception {
		log.debug("=== CAPTURED STDERR LOGS ===\n");
		System.out.println(listener.getStdErr().trim());
		log.debug("=== END OF CAPTURED STDERR LOGS ===\n\n\n");

		log.debug("=== CAPTURED STDOUT LOGS ===\n");
		for (String line : listener.getStdOut().trim().split("\n")) {
			for (String incPatt : stdoutIncludePatterns) {
				if (line.contains(incPatt)) {
					System.out.println(line);
				}
			}
		}
		log.debug("=== END OF CAPTURED STDOUT LOGS ===\n\n\n");
	}

	public static void forceStopAll() {
		if (!instances.isEmpty()) {
			for (AndroidLogListener listener : instances.values()) {
				if (listener.isRunning()) {
					try {
						listener.stop();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
}
