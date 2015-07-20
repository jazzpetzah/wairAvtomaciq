package com.wearezeta.auto.android.common.reporter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.wearezeta.auto.android.common.AndroidCommonUtils;
import com.wearezeta.auto.common.CommonUtils;
import com.wearezeta.auto.common.log.ZetaLogger;
import com.wearezeta.auto.common.misc.ClientDeviceInfo;

class ExecutionData {
	private String runDate = new Date().toString();
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

	public String getRunDate() {
		return runDate;
	}

	public void setRunDate(String runDate) {
		this.runDate = runDate;
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

public class AndroidPerformanceHelpers {

	private static final Logger log = ZetaLogger
			.getLog(AndroidPerformanceHelpers.class.getSimpleName());

	public static String REPORT_DATA_PATH;

	private static String[] CHART_TITLES = new String[] {
			"Application Startup Time", "Sign In Time",
			"First Conversation Loading Time",
			"Conversation Loading Median Time" };

	public static final String API_KEY = "d99071df5c72636eb5aba15117d050c7";
	private static LinkedHashMap<String, String> WIDGETS = new LinkedHashMap<String, String>();
	static {
		WIDGETS.put(CHART_TITLES[0] + " (Wifi)",
				"152307-6fa497fb-3841-4681-a1d7-a3678537eb89");
		WIDGETS.put(CHART_TITLES[1] + " (Wifi)",
				"152307-4cac0493-16cc-404b-acab-0c2104771ac8");
		WIDGETS.put(CHART_TITLES[2] + " (Wifi)",
				"152307-9b5145d9-58bc-44dd-8801-9801fee143a3");
		WIDGETS.put(CHART_TITLES[3] + " (Wifi)",
				"152307-8df9a10b-5932-47a3-8459-3a96dd59b4bf");

		WIDGETS.put(CHART_TITLES[0] + " (LTE)",
				"152307-669b4d33-df05-4efb-9657-b60729c2cd31");
		WIDGETS.put(CHART_TITLES[1] + " (LTE)",
				"152307-afde00b0-2129-423d-8808-fe4cf672d123");
		WIDGETS.put(CHART_TITLES[2] + " (LTE)",
				"152307-8bc26c2a-3f3c-4a9e-ae03-3bb091b74b33");
		WIDGETS.put(CHART_TITLES[3] + " (LTE)",
				"152307-44e0fc4e-5a37-4d8d-8aca-8c5ad06eccda");
	}

	public static ExecutionData data = new ExecutionData();

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
		int result = 0;
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
		int result = 0;
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
		String resultsPath = AndroidPerformanceHelpers.REPORT_DATA_PATH;
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
	private static final String ANDROID_PERFORMANCE_RESULTS_FILE = "android_performance_runs.csv";

	private static void copyReportAsLatest(String filePath) throws Exception {
		String resultsPath = AndroidPerformanceHelpers.REPORT_DATA_PATH;
		resultsPath = resultsPath + File.separator + LATEST_REPORT_NAME;
		try {
			File f = new File(resultsPath);
			if (f.exists())
				f.delete();
			FileUtils.copyFile(new File(filePath), f);
		} catch (Exception ex) {
			log.error("Failed to create result file.\n" + ex.getMessage());
			throw (ex);
		}
	}

	private static final String[] RESULTS_FILE_HEADER = { "runDate",
			"buildNumber", "deviceModel", "deviceOSVersion", "networkType",
			"numberOfUsers", "startupTime", "signInTime",
			"firstConversationLoadingTime", "conversationLoadingMedianTime" };

	public static void storeRunResultsToCSV() {
		try {
			String filePath = createResultFile();
			CSVWriter writer = new CSVWriter(new FileWriter(filePath),
					CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
			String[] entries = RESULTS_FILE_HEADER;
			writer.writeNext(entries);

			String[] values = { data.getRunDate(), data.getBuildNumber(),
					data.getDeviceModel(), data.getDeviceOSVersion(),
					data.getNetworkType(),
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

	private static List<String[]> readAllResults(String resultsPath)
			throws IOException {
		List<String[]> savedData = new ArrayList<>();
		File f = new File(resultsPath);
		if (!f.exists()) {
			f.getParentFile().mkdirs();
			f.createNewFile();
		} else {
			CSVReader reader = null;
			try {
				reader = new CSVReader(new FileReader(resultsPath));
				String[] nextLine;
				while ((nextLine = reader.readNext()) != null) {
					savedData.add(nextLine);
				}
			} finally {
				if (reader != null)
					reader.close();
			}
		}
		return savedData;
	}

	private static List<String[]> readLatestResult() throws IOException {
		List<String[]> savedData = new ArrayList<>();
		String resultsPath = String.format("%s%s%s",
				AndroidPerformanceHelpers.REPORT_DATA_PATH,
				File.separator, LATEST_REPORT_NAME);
		File f = new File(resultsPath);
		if (!f.exists()) {
			f.getParentFile().mkdirs();
			f.createNewFile();
		} else {
			CSVReader reader = null;
			try {
				reader = new CSVReader(new FileReader(resultsPath));
				String[] nextLine;
				while ((nextLine = reader.readNext()) != null) {
					savedData.add(nextLine);
				}
			} finally {
				if (reader != null)
					reader.close();
			}
		}
		return savedData;
	}

	public static void mergeLastResultIntoList() throws IOException {
		String resultsPath = String.format("%s%s%s",
				AndroidPerformanceHelpers.REPORT_DATA_PATH,
				File.separator, ANDROID_PERFORMANCE_RESULTS_FILE);
		List<String[]> savedData = readAllResults(resultsPath);
		List<String[]> newData = readLatestResult();
		newData.remove(0);
		CSVWriter writer = null;
		try {
			writer = new CSVWriter(new FileWriter(resultsPath),
					CSVWriter.DEFAULT_SEPARATOR, CSVWriter.NO_QUOTE_CHARACTER);
			if (savedData.size() > 0) {
				writer.writeAll(savedData);
			} else {
				writer.writeNext(RESULTS_FILE_HEADER);
			}
			writer.writeAll(newData);
			writer.flush();
		} catch (Exception ex) {
			log.error("Failed to generate summary report string.\n"
					+ ex.getMessage());
			throw (ex);
		} finally {
			writer.close();
		}
	}

	private static void buildChartFor(List<String[]> rawData, String title,
			String network) throws Exception {
		TreeMap<String, String> users10Values = new TreeMap<String, String>();
		TreeMap<String, String> users300Values = new TreeMap<String, String>();
		TreeMap<String, String> users600Values = new TreeMap<String, String>();
		TreeMap<String, String> users1000Values = new TreeMap<String, String>();

		int index = -1;
		if (title.equals(CHART_TITLES[0])) {
			index = ReporterConstants.Csv.APPLICATION_STARTUP_TIME_COLUMN;
		} else if (title.equals(CHART_TITLES[1])) {
			index = ReporterConstants.Csv.SIGN_IN_TIME_COLUMN;
		} else if (title.equals(CHART_TITLES[2])) {
			index = ReporterConstants.Csv.FIRST_CONVERSATION_LOADING_COLUMN;
		} else if (title.equals(CHART_TITLES[3])) {
			index = ReporterConstants.Csv.CONVERSATION_LOADING_MEDIAN_COLUMN;
		}
		if (index < 0) {
			throw new Exception("Incorrect chart title specified.");
		}

		for (String[] raw : rawData) {
			if (!raw[ReporterConstants.Csv.NETWORK_TYPE_COLUMN].equals(network))
				continue;
			String build = raw[ReporterConstants.Csv.BUILD_NUMBER_COLUMN];
			String users = raw[ReporterConstants.Csv.NUMBER_OF_USERS_COLUMN];
			String time = raw[index];

			switch (users) {
			case "10":
				users10Values.put(build, time);
				break;
			case "300":
				users300Values.put(build, time);
				break;
			case "600":
				users600Values.put(build, time);
				break;
			case "1000":
				users1000Values.put(build, time);
				break;
			default:
				throw new Exception(
						"Unsupported number of users (supported - 10/300/600/1000)");
			}
		}

		ArrayList<String> builds = new ArrayList<String>();
		for (String build : users10Values.keySet()) {
			builds.add("\\\"" + build + "\\\"");
		}

		String titleValue = String.format("%s (%s)", title, network);

		String curlFormat = String
				.format("curl https://push.geckoboard.com/v1/send/%s "
						+ "-d '{"
						+ "\"api_key\":\"%s\","
						+ "\"data\":{"
						+ "\"highchart\": "
						+ "\"{chart:{type:\\\"column\\\", style: {color: \\\"#b9bbbb\\\"},"
						+ "renderTo:\\\"container\\\",backgroundColor:\\\"transparent\\\","
						+ "lineColor:\\\"rgba(35,37,38,100)\\\",plotShadow: false,},"
						+ "credits:{enabled:false},title:{style: {color: \\\"#b9bbbb\\\"},"
						+ "text:\\\"%s\\\"},"
						+ "xAxis:{categories:[%s]},"
						+ "yAxis:{title:{style: {color: \\\"#b9bbbb\\\"}, text:\\\"Time (ms)\\\"}},"
						+ "legend:{itemStyle: {color: \\\"#b9bbbb\\\"}, layout:\\\"vertical\\\","
						+ "align:\\\"right\\\",verticalAlign:\\\"middle\\\",borderWidth:0},"
						+ "series:[{color:\\\"#108ec5\\\",name:\\\"10 users\\\",data:[%s]},"
						+ "{color:\\\"#52b238\\\",name:\\\"300 users\\\",data:[%s]},"
						+ "{color:\\\"#ee5728\\\",name:\\\"600 users\\\",data:[%s]},"
						+ "{color:\\\"#fefe22\\\",name:\\\"1000 users\\\",data:[%s]}]}\"}}'",
						WIDGETS.get(titleValue),
						API_KEY,
						titleValue,
						Arrays.toString(builds.toArray(new String[0]))
								.replace("[", "").replace("]", ""),
						Arrays.toString(
								users10Values.values().toArray(new String[0]))
								.replace("[", "").replace("]", ""),
						Arrays.toString(
								users300Values.values().toArray(new String[0]))
								.replace("[", "").replace("]", ""),
						Arrays.toString(
								users600Values.values().toArray(new String[0]))
								.replace("[", "").replace("]", ""),
						Arrays.toString(
								users1000Values.values().toArray(new String[0]))
								.replace("[", "").replace("]", ""));

		CommonUtils
				.executeOsXCommand(new String[] { "bash", "-c", curlFormat });
	}

	public static void buildGeckoJSONRequest() throws Exception {
		String resultsPath = String.format("%s%s%s",
				AndroidPerformanceHelpers.REPORT_DATA_PATH,
				File.separator, ANDROID_PERFORMANCE_RESULTS_FILE);
		List<String[]> results = readAllResults(resultsPath);
		results.remove(0);

		buildChartFor(results, CHART_TITLES[0], "Wifi");
		buildChartFor(results, CHART_TITLES[1], "Wifi");
		buildChartFor(results, CHART_TITLES[2], "Wifi");
		buildChartFor(results, CHART_TITLES[3], "Wifi");

		buildChartFor(results, CHART_TITLES[0], "LTE");
		buildChartFor(results, CHART_TITLES[1], "LTE");
		buildChartFor(results, CHART_TITLES[2], "LTE");
		buildChartFor(results, CHART_TITLES[2], "LTE");
	}
}