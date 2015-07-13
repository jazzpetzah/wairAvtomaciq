package com.wearezeta.auto.android.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;

public final class AndroidLoggingUtils {
	private static final Logger log = ZetaLogger
			.getLog(AndroidLoggingUtils.class.getSimpleName());

	public static final String ADB_PREFIX = "";

	public AndroidLoggingUtils() {
		// TODO Auto-generated constructor stub
	}

	private static final List<Pattern> patternsToExcludeFromLog = new ArrayList<Pattern>();
	static {
		patternsToExcludeFromLog.add(Pattern.compile("/resourcetype",
				Pattern.CASE_INSENSITIVE));
		patternsToExcludeFromLog.add(Pattern.compile("/selendroid",
				Pattern.CASE_INSENSITIVE));
	}

	private static final ExecutorService logsCollector = Executors
			.newFixedThreadPool(3);
	private static final int LOG_READ_TIMEOUT_SECONDS = 15;

	/**
	 * 
	 * @param testStartedTimestamp
	 *            should be taken as new Date().getTime(). This is Unix time
	 *            stamp
	 * @throws Exception
	 */
	public static void writeDeviceLogsToConsole(long testStartedTimestamp)
			throws Exception {
		final long testFinsihedTimestamp = new Date().getTime();
		final String[] cmd = new String[] { "/bin/bash", "-c",
				ADB_PREFIX + "adb logcat -d -v time" };
		final Future<String> logOutput = logsCollector
				.submit(new Callable<String>() {
					public String call() {
						try {
							return CommonUtils.executeOsXCommandWithOutput(cmd);
						} catch (Exception e) {
							log.error(e.getMessage());
							// Throwables.propagate(e);
							return "";
						}
					}
				});
		final String[] allLogLines = logOutput.get(LOG_READ_TIMEOUT_SECONDS,
				TimeUnit.SECONDS).split("\n");
		final SimpleDateFormat sdf = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss.SSS");
		final Pattern pattern = Pattern
				.compile("(\\d{2}\\-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}\\.\\d{3})");
		log.debug("\n\n\n===CAPTURED DEVICE LOGS===\n");
		for (String logLine : allLogLines) {
			final Matcher matcher = pattern.matcher(logLine);
			long logLineTimestamp = -1;
			if (matcher.find()) {
				try {
					logLineTimestamp = sdf.parse(
							Integer.toString(Calendar.getInstance().get(
									Calendar.YEAR))
									+ "-" + matcher.group(1)).getTime();
				} catch (ParseException e) {
					continue;
				}
			} else {
				continue;
			}
			if (logLineTimestamp > testStartedTimestamp
					&& logLineTimestamp < testFinsihedTimestamp) {
				boolean shouldIncludeLineToLog = true;
				for (final Pattern excludePattern : patternsToExcludeFromLog) {
					final Matcher excludeMatcher = excludePattern
							.matcher(logLine);
					if (excludeMatcher.find()) {
						shouldIncludeLineToLog = false;
						break;
					}
				}
				if (shouldIncludeLineToLog) {
					System.out.println(logLine.trim());
				}
			}
		}
		log.debug("\n===END OF CAPTURED DEVICE LOGS===\n\n\n");
	}

}
