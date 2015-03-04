package com.wearezeta.common.process;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.concurrent.TimeoutException;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.log.ZetaLogger;

public class AsyncProcess {
	private static final int IS_RUNNING_CHECK_INTERVAL = 100; // milliseconds
	private static final String STDOUT_LOG_PREFIX = "STDOUT: ";
	private static final String STDERR_LOG_PREFIX = "STDERR: ";

	private static final Logger log = ZetaLogger.getLog(AsyncProcess.class
			.getSimpleName());

	private String[] cmd;
	private boolean shouldLogStdOut;
	private boolean shouldLogStdErr;
	private Process process = null;
	private Thread stdOutMonitor = null;
	private Thread stdErrMonitor = null;

	public AsyncProcess(String[] cmd, boolean shouldLogStdOut,
			boolean shouldLogStdErr) {
		this.cmd = cmd;
		this.shouldLogStdOut = shouldLogStdOut;
		this.shouldLogStdErr = shouldLogStdErr;
	}

	private Thread createListenerThread(final BufferedReader reader,
			final String logPrefix) {
		return new Thread() {
			@Override
			public void run() {
				do {
					try {
						log.debug(logPrefix + reader.readLine());
					} catch (IOException e) {
						break;
					}
				} while (!this.isInterrupted());
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
	}

	public synchronized void start() throws IOException {
		process = Runtime.getRuntime().exec(cmd);

		final BufferedReader stdout = new BufferedReader(new InputStreamReader(
				process.getInputStream()));
		stdOutMonitor = createListenerThread(stdout, STDOUT_LOG_PREFIX);
		if (this.shouldLogStdOut) {
			stdOutMonitor.start();
		}
		final BufferedReader stderr = new BufferedReader(new InputStreamReader(
				process.getErrorStream()));
		stdErrMonitor = createListenerThread(stderr, STDERR_LOG_PREFIX);
		if (this.shouldLogStdErr) {
			stdErrMonitor.start();
		}
	}

	public boolean isRunning() {
		try {
			process.exitValue();
			return false;
		} catch (IllegalThreadStateException e) {
			return true;
		}
	}

	public synchronized void stop(String signal, int[] pids,
			long timeoutMilliseconds) throws Exception {
		try {
			if (this.isRunning()) {
				if (signal == null) {
					process.destroy();
				} else {
					for (final int pid : pids) {
						final String killCmd = String.format("kill -%s %s",
								signal, pid);
						log.debug("Executing: " + killCmd);
						Runtime.getRuntime().exec(
								new String[] { "/bin/bash", "-c", killCmd });
					}
				}
			}
			long milliSecondsElapsed = 0;
			while (this.isRunning()) {
				Thread.sleep(IS_RUNNING_CHECK_INTERVAL);
				milliSecondsElapsed += IS_RUNNING_CHECK_INTERVAL;
				if (milliSecondsElapsed >= timeoutMilliseconds) {
					throw new TimeoutException(
							String.format(
									"The application %s has not been stopped after %s milliseconds timeout",
									Arrays.toString(this.cmd),
									timeoutMilliseconds));
				}
			}
			log.debug(String
					.format("The application %s has been successfully stopped after %s millisecond(s)",
							Arrays.toString(this.cmd), milliSecondsElapsed));
		} finally {
			if (shouldLogStdOut && stdOutMonitor.isAlive()) {
				stdOutMonitor.interrupt();
			}
			if (shouldLogStdErr && stdErrMonitor.isAlive()) {
				stdErrMonitor.interrupt();
			}
		}
	}

	public void stop(long timeoutMilliseconds) throws Exception {
		this.stop(null, null, timeoutMilliseconds);
	}

	/**
	 * http://www.golesny.de/p/code/javagetpid
	 * 
	 * @return process id
	 * @throws Exception
	 */
	public int getPid() throws Exception {
		if (process.getClass().getName().equals("java.lang.UNIXProcess")) {
			Field f = process.getClass().getDeclaredField("pid");
			f.setAccessible(true);
			return f.getInt(process);
		} else {
			throw new UnsupportedOperationException(
					"getPid implementation is not available for non-Unix systems");
		}
	}
}
