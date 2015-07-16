package com.wearezeta.auto.android.common.reporter;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.opencsv.CSVWriter;
import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;

class ExecutionData {
	private String buildNumber;
	private String deviceModel;
	private String deviceOSVersion;
	private String networkType;
	private int numberOfUsers;
	private double startupTime;
	private double signInTime;
	private double firstConversationLoadingTime;
	private double conversationLoadingMedianTime;

	public String getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(String buildNumber) {
		this.buildNumber = buildNumber;
	}

	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	public int getNumberOfUsers() {
		return numberOfUsers;
	}

	public void setNumberOfUsers(int numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
	}

	public double getStartupTime() {
		return startupTime;
	}

	public void setStartupTime(double startupTime) {
		this.startupTime = startupTime;
	}

	public double getSignInTime() {
		return signInTime;
	}

	public void setSignInTime(double signInTime) {
		this.signInTime = signInTime;
	}

	public double getFirstConversationLoadingTime() {
		return firstConversationLoadingTime;
	}

	public void setFirstConversationLoadingTime(
			double firstConversationLoadingTime) {
		this.firstConversationLoadingTime = firstConversationLoadingTime;
	}

	public double getConversationLoadingMedianTime() {
		return conversationLoadingMedianTime;
	}

	public void setConversationLoadingMedianTime(
			double conversationLoadingMedianTime) {
		this.conversationLoadingMedianTime = conversationLoadingMedianTime;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public String getDeviceOSVersion() {
		return deviceOSVersion;
	}

	public void setDeviceOSVersion(String deviceOSVersion) {
		this.deviceOSVersion = deviceOSVersion;
	}

	public String toString() {
		return String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s", buildNumber,
				deviceModel, deviceOSVersion, networkType, numberOfUsers,
				startupTime, signInTime, firstConversationLoadingTime,
				conversationLoadingMedianTime);
	}
}

public class AndroidPerformanceReportGenerator {

	private static final Logger log = ZetaLogger
			.getLog(AndroidPerformanceReportGenerator.class.getSimpleName());

	public static String REPORT_DATA_PATH;

	public static ExecutionData data = new ExecutionData();

	static {
		try {
			REPORT_DATA_PATH = AndroidCommonUtils
					.getRxLogResultsPathFromConfig(AndroidPerformanceReportGenerator.class);
		} catch (Exception e) {
			REPORT_DATA_PATH = "";
			e.printStackTrace();
		}
	}

	private static LinkedHashMap<String, Double> readTimeMeasurementsData(
			String applicationLog) {
		LinkedHashMap<String, Double> times = new LinkedHashMap<String, Double>();
		times.put(ReporterConstants.Values.APPLICATION_STARTUP_TIME,
				(double) readApplicationStartupTime(applicationLog));
		times.put(ReporterConstants.Values.LOGIN_TIME,
				(double) readSuccessfulLoginTime(applicationLog));
		times.put(ReporterConstants.Values.CONV_PAGE_LOADING_TIME,
				readConversationPageLoadingTime(applicationLog));
		times.put(ReporterConstants.Values.CONV_LOADING_MEDIAN_TIME,
				calculateConversationPageLoadingMedianTime(applicationLog));
		return times;
	}

	private static int readApplicationStartupTime(String output) {
		int result = -1;
		Pattern pattern = Pattern
				.compile(ReporterConstants.Log.APP_LAUNCH_TIME_REGEX);
		Matcher matcher = pattern.matcher(output);
		while (matcher.find()) {
			try {
				result = (int) (Long.parseLong(matcher.group(1)) / 1000000);
			} catch (NumberFormatException e) {
			}
		}
		return result;
	}

	private static int readSuccessfulLoginTime(String output) {
		int result = -1;
		Pattern pattern = Pattern
				.compile(ReporterConstants.Log.LOGIN_SUCCESS_REGEX);
		Matcher matcher = pattern.matcher(output);
		while (matcher.find()) {
			try {
				result = (int) (Long.parseLong(matcher.group(1)) / 1000000);
			} catch (NumberFormatException e) {
			}
		}
		return result;
	}

	private static double readConversationPageLoadingTime(String output) {
		double result = 0;
		Pattern pattern = Pattern
				.compile(ReporterConstants.Log.CONVERSATION_PAGE_VISIBLE_REGEX);
		Matcher matcher = pattern.matcher(output);
		while (matcher.find()) {
			try {
				result = (int) (Long.parseLong(matcher.group(1)) / 1000000);
				log.debug("First conversation loading time " + result);
				break;
			} catch (NumberFormatException e) {
			}
		}
		return result;
	}

	private static double calculateConversationPageLoadingMedianTime(
			String output) {
		ArrayList<Double> results = new ArrayList<Double>();
		Pattern pattern = Pattern
				.compile(ReporterConstants.Log.CONVERSATION_PAGE_VISIBLE_REGEX);
		Matcher matcher = pattern.matcher(output);
		while (matcher.find()) {
			try {
				results.add((double) ((int) (Long.parseLong(matcher.group(1)) / 1000000)));
			} catch (NumberFormatException e) {
			}
		}
		return calculateMedian(results);
	}

	private static double calculateMedian(List<Double> listValues) {
		Double[] values = new Double[listValues.size()];
		listValues.toArray(values);
		Arrays.sort(values);
		double median = 0d;
		if (!listValues.isEmpty()) {
			if (values.length % 2 == 0) {
				median = ((double) values[values.length / 2] + (double) values[values.length / 2 - 1]) / 2;
			} else {
				median = (double) values[values.length / 2];
			}
		}
		return median;
	}

	private static ExecutionData fillBuildAndDeviceData(ExecutionData data) {
		data.setBuildNumber("Unknown");
		data.setNetworkType("Unknown");
		data.setDeviceModel("Unknown");
		data.setDeviceOSVersion("Unknown");
		try {
			data.setBuildNumber(AndroidCommonUtils.readClientVersionFromAdb());
			ClientDeviceInfo deviceInfo = AndroidCommonUtils.readDeviceInfo();
			log.debug("Execution device info: " + deviceInfo.toString());
			try {
				data.setNetworkType(deviceInfo.isWifiEnabled() ? "Wifi"
						: deviceInfo.getGsmNetworkType());
			} catch (NullPointerException e) {
			}
			try {
				data.setDeviceModel(deviceInfo.getDeviceName());
			} catch (NullPointerException e) {
			}
			try {
				data.setDeviceOSVersion(deviceInfo.getOperatingSystemBuild());
			} catch (NullPointerException e) {
			}
		} catch (Exception e) {
		}
		return data;
	}

	public static ExecutionData collectExecutionData(int usersCount,
			String appLog) {
		// set number of users
		data.setNumberOfUsers(usersCount);

		data = fillBuildAndDeviceData(data);

		// calculate time consumption
		LinkedHashMap<String, Double> timeConsumption = readTimeMeasurementsData(appLog);

		for (Map.Entry<String, Double> time : timeConsumption.entrySet()) {
			switch (time.getKey()) {
			case ReporterConstants.Values.APPLICATION_STARTUP_TIME:
				data.setStartupTime(time.getValue());
				break;
			case ReporterConstants.Values.LOGIN_TIME:
				data.setSignInTime(time.getValue());
				break;
			case ReporterConstants.Values.CONV_PAGE_LOADING_TIME:
				data.setFirstConversationLoadingTime(time.getValue());
				break;
			case ReporterConstants.Values.CONV_LOADING_MEDIAN_TIME:
				data.setConversationLoadingMedianTime(time.getValue());
				break;
			default:
				break;
			}
		}
		return data;
	}

	private static String createResultFile() throws Exception {
		String resultsPath = AndroidPerformanceReportGenerator.REPORT_DATA_PATH;
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
				.format(Calendar.getInstance().getTime());
		resultsPath = resultsPath + File.separator + timeStamp + ".csv";
		log.debug("Created file " + resultsPath);
		try {
			File f = new File(resultsPath);
			f.getParentFile().mkdirs();
			f.createNewFile();
		} catch (Exception ex) {
			log.error("Failed to create result file.\n" + ex.getMessage());
			throw (ex);
		}
		return resultsPath;
	}
	
	
	private static final String LATEST_REPORT_NAME = "latest_android_performance.csv";
	private static void copyReportAsLatest(String filePath) throws Exception {
		String resultsPath = AndroidPerformanceReportGenerator.REPORT_DATA_PATH;
		resultsPath = resultsPath + File.separator + LATEST_REPORT_NAME;
		try {
			File f = new File(resultsPath);
			if (f.exists()) f.delete();
			FileUtils.copyFile(new File(filePath), f);
		} catch (Exception ex) {
			log.error("Failed to create result file.\n" + ex.getMessage());
			throw (ex);
		}
	}
	
	public static void storeRunResultsToCSV() {
		try {
			String filePath = createResultFile();
			CSVWriter writer = new CSVWriter(new FileWriter(filePath),
					CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
			String[] entries = { "buildNumber", "deviceModel",
					"deviceOSVersion", "networkType", "numberOfUsers",
					"startupTime", "signInTime",
					"firstConversationLoadingTime",
					"conversationLoadingMedianTime" };
			writer.writeNext(entries);

			String[] values = { data.getBuildNumber(), data.getDeviceModel(),
					data.getDeviceOSVersion(), data.getNetworkType(),
					Integer.toString(data.getNumberOfUsers()),
					Double.toString(data.getStartupTime()),
					Double.toString(data.getSignInTime()),
					Double.toString(data.getFirstConversationLoadingTime()),
					Double.toString(data.getConversationLoadingMedianTime()) };
			writer.writeNext(values);
			writer.flush();
			writer.close();

			copyReportAsLatest(filePath);
		} catch (Exception e) {
			log.debug(e);
		}
	}
}